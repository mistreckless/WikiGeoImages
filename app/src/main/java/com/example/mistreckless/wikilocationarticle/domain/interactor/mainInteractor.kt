package com.example.mistreckless.wikilocationarticle.domain.interactor

import android.util.Log
import com.example.mistreckless.wikilocationarticle.data.location.LocationException
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.domain.*
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import com.example.mistreckless.wikilocationarticle.presentation.view.ImageAdapterWrapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.IOException

/**
 * Created by mistreckless on 27.10.17.
 */

interface WallInteractor {

    fun controlList(observeScroll: Observable<Int>, imageAdapterWrapper: ImageAdapterWrapper, initialScrollValue: Int, limitPageIds: Int): Observable<FetchImagesState>
}

class WallInteractorImpl(private val wikiRepository: WikiRepository, private val locationRepository: LocationRepository) : WallInteractor {

    private var requestPartIndex = 0
    private var pageIds: Array<Long> = arrayOf()

    override fun controlList(observeScroll: Observable<Int>, imageAdapterWrapper: ImageAdapterWrapper, initialScrollValue: Int, limitPageIds: Int): Observable<FetchImagesState> {
        val skipCount = (if (pageIds.isEmpty()) 0 else 1).toLong()

        val initialObservable by lazy { Observable.just(StateInit()) }
        val pageIdsObservable by lazy { if (pageIds.isEmpty()) controlPageIds(limitPageIds) else Observable.just(StatePageIdsLoaded(pageIds)) }
        val paginObservable by lazy { paginControl(observeScroll, skipCount, initialScrollValue, imageAdapterWrapper, calculateRequestsPart(pageIds, wikiRepository.getMaxPageIdsPerRequest())) }

        return Observable.merge(initialObservable, pageIdsObservable)
                .flatMap { if (it is StatePageIdsLoaded) paginObservable else Observable.just(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }


    private fun controlPageIds(limit: Int): Observable<FetchImagesState> {
        return wikiRepository.listenNetworkState()
                .flatMapSingle {
                    if (pageIds.isEmpty()) fetchPageIds(limit)
                    else Single.just(StateConnectionRemained())
                }
                .doOnNext { if (it is StatePageIdsLoaded) this.pageIds = it.pageIds }
                .observeOn(AndroidSchedulers.mainThread())
    }


    private fun fetchPageIds(limit: Int): Single<FetchImagesState> {
        return locationRepository.getLastKnownLocation()
                .flatMap { wikiRepository.getNearestArticles(it.first, it.second, limit) }
                .onErrorReturn { handleFetchImageError(it) }
    }

    private fun paginControl(observeScroll: Observable<Int>, skipCount: Long, initialScrollValue: Int, imageAdapterWrapper: ImageAdapterWrapper, requestParts: Array<Array<Long>>): Observable<FetchImagesState> {
        var initialValue = initialScrollValue

        return wikiRepository.listenNetworkState()
                .filter { it }
                .flatMap { observeScroll }
                .map { if (it == initialScrollValue) initialValue else it }
                .skip(skipCount)
                .distinctUntilChanged()
                .flatMapSingle {
                    val lastImage = imageAdapterWrapper.getLastImage()

                    if (lastImage == null) fetchImages(requestParts[requestPartIndex], emptyMap())
                    else nextImagesControl(lastImage, requestParts)
                }
                .doOnNext { if (it is StateError) initialValue++ }
                .onErrorReturn { handleFetchImageError(it) }
    }


    private fun fetchImages(pageIds: Array<Long>, next: Map<String, String>): Single<FetchImagesState> {
        return wikiRepository.getPageImages(pageIds, next)
                .onErrorReturn { handleFetchImageError(it) }
    }


    private fun nextImagesControl(lastImage: Image, requestParts: Array<Array<Long>>): Single<FetchImagesState> {
        val next = lastImage.next

        return when {
            next != null -> fetchImages(requestParts[requestPartIndex], next)
            requestPartIndex < requestParts.lastIndex -> {
                requestPartIndex++
                fetchImages(requestParts[requestPartIndex], emptyMap())
            }
            else -> Single.just<FetchImagesState>(StateDone())
        }
    }


    private fun calculateRequestsPart(pageIds: Array<Long>, maxPageIdsPerRequest: Int): Array<Array<Long>> {
        val requestPartsSize = ((pageIds.size / maxPageIdsPerRequest) + if (pageIds.size % maxPageIdsPerRequest > 0) 1 else 0)
        return Array(requestPartsSize, { pageIds.copyOfRange(it * maxPageIdsPerRequest, if (((it * maxPageIdsPerRequest) + maxPageIdsPerRequest) > pageIds.size) pageIds.size else ((it * maxPageIdsPerRequest) + maxPageIdsPerRequest)) })
    }

    private fun handleFetchImageError(err: Throwable): FetchImagesState = StateError(when (err) {
        is IOException -> ErrorType.NETWORK_CONNECTION_ERROR
        is LocationException -> ErrorType.LOCATION_ERROR
        else -> ErrorType.RESPONSE_ERROR
    }, err.message ?: "")


}