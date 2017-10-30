package com.example.mistreckless.wikilocationarticle.domain.interactor

import android.util.Log
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import com.example.mistreckless.wikilocationarticle.presentation.screen.main.ImageWrapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import java.io.IOException

/**
 * Created by mistreckless on 27.10.17.
 */

interface WallInteractor {

    fun controlList(observeScroll: Observable<Int>, imageWrapper: ImageWrapper): Observable<FetchImagesState>
}

class WallInteractorImpl(private val wikiRepository: WikiRepository, private val locationRepository: LocationRepository) : WallInteractor {

    private var requestPartIndex = 0
    private var articles: List<Article> = listOf()

    override fun controlList(observeScroll: Observable<Int>, imageWrapper: ImageWrapper): Observable<FetchImagesState> {
        val initialObservable: Observable<FetchImagesState> = Observable.just(StateInit())
        var isArticlesFetched = false

        return Observable.merge(initialObservable, wikiRepository.listenNetworkState()
                .flatMap { checkConnectionAndArticles(it, articles, isArticlesFetched) }
                .doOnNext {
                    if (it is StateArticlesLoaded) {
                        isArticlesFetched = true
                        this.articles = it.articles
                    }
                }
                .flatMap { state ->
                    when (state) {
                        is StateArticlesLoaded -> paginControl(observeScroll, imageWrapper, state.articles.map { it.pageId }.toTypedArray(), MAX_PAGE_IDS_PER_REQUEST)
                        else -> Observable.just(state)
                    }
                }
                .onErrorReturn { handleError(it) })
                .onErrorReturn { handleError(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchArticles(): Observable<FetchImagesState> {
        return locationRepository.getLastKnownLocation()
                .flatMap { wikiRepository.getNearestArticles(it.first, it.second) }
                .onErrorReturn { handleError(it) }
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun paginControl(observeScroll: Observable<Int>, imageWrapper: ImageWrapper, pageIds: Array<Long>, maxPageIdsPerRequest: Int): Observable<FetchImagesState> {
        val requestParts = calculateRequestsPart(pageIds, maxPageIdsPerRequest)
        val skipValue : Long = if (imageWrapper.getItemCount()==0) 0 else 1
        return Observable.combineLatest(wikiRepository.listenNetworkState(), observeScroll.skip(skipValue).distinctUntilChanged(), BiFunction { isConnected: Boolean, _: Int -> isConnected })
                .flatMapSingle { isConnected ->
                    val lastImage = imageWrapper.getLastImage()
                    Log.e("lastImage", lastImage.toString() + "requestPartIndex " + requestPartIndex + " countArr " + requestParts.lastIndex)

                    if (!isConnected) Single.just(StateError(ErrorType.NETWORK_CONNECTION_ERROR))
                    else if (lastImage == null) fetchImages(requestParts[requestPartIndex], emptyMap())
                    else nextImagesControl(lastImage, requestParts)

                }
                .onErrorReturn { handleError(it) }
    }


    private fun fetchImages(pageIds: Array<Long>, next: Map<String, String>): Single<FetchImagesState> {
        return wikiRepository.getPageImages(pageIds, next)
                .map<FetchImagesState> { StatePartImagesLoaded(it.first) }
                .onErrorReturn { handleError(it) }
    }

    private fun checkConnectionAndArticles(isConnected: Boolean, articles: List<Article>, isArticlesFetched: Boolean): Observable<FetchImagesState> {
        return when (isConnected) {
            true -> when (isArticlesFetched) {
                true -> Observable.just<FetchImagesState>(StateConnectionRemainded())
                false -> if (articles.isEmpty()) fetchArticles() else Observable.just<FetchImagesState>(StateArticlesLoaded(articles))
            }
            false -> Observable.just(StateError(ErrorType.NETWORK_CONNECTION_ERROR))
        }
    }

    private fun nextImagesControl(lastImage: Image, requestParts: Array<Array<Long>>): Single<FetchImagesState> {
        val next = lastImage.next
        return if (next == null && requestPartIndex == requestParts.lastIndex) {
            Single.just(StateDone())
        } else if (next != null) {
            fetchImages(requestParts[requestPartIndex], next)
        } else {
            requestPartIndex++
            fetchImages(requestParts[requestPartIndex], emptyMap())
        }
                .onErrorReturn { handleError(it) }
    }


    private fun calculateRequestsPart(pageIds: Array<Long>, maxSize: Int): Array<Array<Long>> {
        val requestPartsSize = ((pageIds.size / maxSize) + if (pageIds.size % maxSize > 0) 1 else 0)
        return Array(requestPartsSize, { pageIds.copyOfRange(it * maxSize, if (((it * maxSize) + maxSize) > pageIds.size) pageIds.size else ((it * maxSize) + maxSize)) })
    }

    private fun handleError(err: Throwable): FetchImagesState = when (err) {
        is IOException -> StateError(ErrorType.NETWORK_CONNECTION_ERROR)
        else -> StateError(ErrorType.RESPONSE_ERROR)
    }


    companion object {
        const val MAX_PAGE_IDS_PER_REQUEST = 50
    }

}