package com.example.mistreckless.wikilocationarticle.domain.interactor

import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by mistreckless on 27.10.17.
 */

interface WallInteractor {

    fun fetchImages(): Observable<FetchImagesState>
}

class WallInteractorImpl(private val wikiRepository: WikiRepository, private val locationRepository: LocationRepository) : WallInteractor {
    override fun fetchImages(): Observable<FetchImagesState> {
        val initialObservable = Observable.just(StateInit())
        return Observable.merge(initialObservable, fetchArticles().toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { state ->
                    when (state) {
                        is StateInit -> Observable.just(StateInit())
                        is StateArticlesLoaded -> fetchImages(state.articles)
                        else -> Observable.just(StateError("unexpected state " + state.toString()))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchArticles(): Single<FetchImagesState> {
        return locationRepository.getLastKnownLocation()
                .flatMap { wikiRepository.getNearestArticles(it.first, it.second) }
                .map { articles -> StateArticlesLoaded(articles.filter { it.pageId >= 0 }) }
    }

    private fun fetchImages(articles: List<Article>): Observable<FetchImagesState> {
        if (articles.isEmpty()) return Observable.just(StateError("articles is empty"))
        return Observable.fromIterable(articles)
                .flatMapSingle { wikiRepository.getPageImages(it.pageId) }
                .map { StatePartImagesLoaded(it) }
    }
}