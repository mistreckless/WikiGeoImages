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
    private var pageIds: Array<Long> = arrayOf()

    override fun controlList(observeScroll: Observable<Int>, imageWrapper: ImageWrapper): Observable<FetchImagesState> {
        val initialObservable: Observable<FetchImagesState> = Observable.just(StateInit())
        val pageIdsObservable by lazy { if (pageIds.isEmpty()) fetchArticles() else Observable.just(StatePageIdsLoaded(pageIds)) }
        val paginObservable by lazy {   paginControl(observeScroll,imageWrapper,pageIds, MAX_PAGE_IDS_PER_REQUEST)}

        return Observable.merge(initialObservable,pageIdsObservable)
                .flatMap { if (it is StatePageIdsLoaded) paginObservable else Observable.just(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchArticles(): Observable<FetchImagesState> {
        var isCalled=false
        return wikiRepository.listenNetworkState()
                .flatMapSingle {
                    if (!isCalled)
                    locationRepository.getLastKnownLocation()
                            .flatMap { wikiRepository.getNearestArticles(it.first, it.second) }
                            .onErrorReturn { handleFetchImageError(it) }
                            .doOnSuccess {
                                if (it is StatePageIdsLoaded) {
                                    this.pageIds = it.pageIds
                                    isCalled=true
                                }
                            }
                    else Single.just(StateConnectionRemained())
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun paginControl(observeScroll: Observable<Int>, imageWrapper: ImageWrapper, pageIds: Array<Long>, maxPageIdsPerRequest: Int): Observable<FetchImagesState> {
        val requestParts = calculateRequestsPart(pageIds, maxPageIdsPerRequest)
        val skipValue: Long = if (imageWrapper.getItemCount() == 0) 0 else 1
        return Observable.combineLatest(wikiRepository.listenNetworkState(), observeScroll.skip(skipValue).distinctUntilChanged(), BiFunction { isConnected: Boolean, _: Int -> isConnected })
                .flatMapSingle {
                    val lastImage = imageWrapper.getLastImage()
                    Log.e("lastImage", lastImage.toString() + "requestPartIndex " + requestPartIndex + " countArr " + requestParts.lastIndex)

                    if (lastImage == null) fetchImages(requestParts[requestPartIndex], emptyMap())
                    else nextImagesControl(lastImage, requestParts)
                }
                .onErrorReturn { handleFetchImageError(it) }
    }


    private fun fetchImages(pageIds: Array<Long>, next: Map<String, String>): Single<FetchImagesState> {
        return wikiRepository.getPageImages(pageIds, next)
                .onErrorReturn { handleFetchImageError(it) }
    }

    private fun checkPageIdsAndState(pageIds: Array<Long>,isConnected : Boolean): Observable<FetchImagesState> {
        return if (pageIds.isEmpty()) fetchArticles() else Observable.just<FetchImagesState>( if (isConnected)StatePageIdsLoaded(pageIds) else StateConnectionRemained())
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
    }


    private fun calculateRequestsPart(pageIds: Array<Long>, maxPageIdsPerRequest: Int): Array<Array<Long>> {
        val requestPartsSize = ((pageIds.size / maxPageIdsPerRequest) + if (pageIds.size % maxPageIdsPerRequest > 0) 1 else 0)
        return Array(requestPartsSize, { pageIds.copyOfRange(it * maxPageIdsPerRequest, if (((it * maxPageIdsPerRequest) + maxPageIdsPerRequest) > pageIds.size) pageIds.size else ((it * maxPageIdsPerRequest) + maxPageIdsPerRequest)) })
    }

    private fun handleFetchImageError(err: Throwable): FetchImagesState = when (err) {
        is IOException -> StateError(ErrorType.NETWORK_CONNECTION_ERROR)
        else -> StateError(ErrorType.RESPONSE_ERROR)
    }
    companion object {
        const val MAX_PAGE_IDS_PER_REQUEST = 50
    }

}