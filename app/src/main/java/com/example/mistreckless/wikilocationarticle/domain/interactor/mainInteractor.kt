package com.example.mistreckless.wikilocationarticle.domain.interactor

import android.util.Log
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import java.net.UnknownHostException

/**
 * Created by mistreckless on 27.10.17.
 */

interface WallInteractor {

    fun controlList(observeScroll: Observable<Int>): Observable<FetchImagesState>
}

class WallInteractorImpl(private val wikiRepository: WikiRepository, private val locationRepository: LocationRepository) : WallInteractor {

    private var nextImage: Map<String, String>? = mapOf()
    private var requestPartIndex = 0

    override fun controlList(observeScroll: Observable<Int>): Observable<FetchImagesState> {
        val initialObservable: Observable<FetchImagesState> = Observable.just(StateInit())
        var isArticlesFetched = false
        return Observable.merge(initialObservable, wikiRepository.listenNetworkState()
                .flatMap {
                    when (it) {
                        true -> if (!isArticlesFetched) fetchArticles() else Observable.just(StateConnectionRemainded())
                        false -> Observable.just(StateError(ErrorType.NETWORK_CONNECTION_ERROR))
                    }
                }
                .flatMap { state ->
                    when (state) {
                        is StateArticlesLoaded -> {
                            isArticlesFetched = true
                            paginControl(observeScroll, state.articles.map { it.pageId }.toTypedArray(), MAX_PAGE_IDS_PER_REQUEST)
                        }
                        else -> Observable.just(state)
                    }
                }
                .onErrorReturn {
                    when (it) {
                        is UnknownHostException -> StateError(ErrorType.NETWORK_CONNECTION_ERROR)
                        else -> StateError(ErrorType.RESPONSE_ERROR)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    private fun fetchArticles(): Observable<FetchImagesState> {
        return locationRepository.getLastKnownLocation()
                .flatMap { wikiRepository.getNearestArticles(it.first, it.second) }
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { articles -> StateArticlesLoaded(articles.filter { it.pageId >= 0 }) }

    }

    private fun paginControl(observeScroll: Observable<Int>, pageIds: Array<Long>, maxPageIdsPerRequest: Int): Observable<FetchImagesState> {
        val requestParts = calculateRequestsPart(pageIds, maxPageIdsPerRequest)
        return Observable.combineLatest(wikiRepository.listenNetworkState(), observeScroll.distinctUntilChanged(), BiFunction { isConnected: Boolean, _: Int -> isConnected })
                .flatMapSingle { isConnected ->
                    Log.e("next", nextImage.toString() + "requestPartIndex " + requestPartIndex + " countArr " + requestParts.lastIndex)
                    if (!isConnected) Single.just(StateError(ErrorType.NETWORK_CONNECTION_ERROR))
                    else when (nextImage) {
                        null -> {
                            if (requestPartIndex == requestParts.lastIndex) Single.just(StateDone())
                            else {
                                requestPartIndex++
                                fetchImages(requestParts[requestPartIndex], emptyMap())
                            }
                        }
                        else -> fetchImages(requestParts[requestPartIndex], nextImage!!)
                    }
                }
    }


    private fun fetchImages(pageIds: Array<Long>, next: Map<String, String>): Single<FetchImagesState> {
        return wikiRepository.getPageImages(pageIds, next)
                .doOnSuccess { this.nextImage = it.second }
                .map { StatePartImagesLoaded(it.first) }
    }

    private fun calculateRequestsPart(pageIds: Array<Long>, maxSize: Int): Array<Array<Long>> {
        val requestPartsSize = ((pageIds.size / maxSize) + if (pageIds.size % maxSize > 0) 1 else 0)
        return Array(requestPartsSize, { pageIds.copyOfRange(it * maxSize, if (((it * maxSize) + maxSize) > pageIds.size) pageIds.size else ((it * maxSize) + maxSize)) })
    }

    companion object {
        const val MAX_PAGE_IDS_PER_REQUEST = 50
    }

}