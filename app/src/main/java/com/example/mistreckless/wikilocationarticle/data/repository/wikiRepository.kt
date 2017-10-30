package com.example.mistreckless.wikilocationarticle.data.repository

import com.example.mistreckless.wikilocationarticle.data.network.NetworkConnectionListener
import com.example.mistreckless.wikilocationarticle.data.network.WikiApi
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by mistreckless on 26.10.17.
 */

interface WikiRepository {

    fun getNearestArticles(lat: Double, lon: Double): Single<FetchImagesState>

    fun getPageImages(pageIds: Array<Long>, next: Map<String,String>?): Single<Pair<List<Image>,Map<String,String>?>>
    fun listenNetworkState(): Observable<Boolean>

}

class WikiRepositoryImpl(private val wikiApi: WikiApi,private val networkConnectionListener: NetworkConnectionListener) : WikiRepository {
    override fun getPageImages(pageIds: Array<Long>, next: Map<String,String>?): Single<Pair<List<Image>,Map<String,String>?>> {
        val idsLine = StringBuilder().apply {
            pageIds.take(pageIds.size-1).map {
                append(it)
                append("|")
            }
        }
        return wikiApi.getImages(idsLine.toString(), next)
                .map {res-> Pair(res.query.pages.values.map { Image(it.title,res.cont) }.toList(), res.cont) }
                .subscribeOn(Schedulers.io())
    }

    override fun getNearestArticles(lat: Double, lon: Double): Single<FetchImagesState> {
        val pipeLine = lat.toString() + "|" + lon.toString()
        return wikiApi.getGeoArticles(pipeLine)
                .map { response ->
                    if (response.error==null)
                    StateArticlesLoaded(response.query.items.filter { it.pageId>=0 }.map { Article(it.pageId) })
                    else StateError(ErrorType.RESPONSE_ERROR,response.error.toString())
                }
                .subscribeOn(Schedulers.io())
    }

    override fun listenNetworkState(): Observable<Boolean> = networkConnectionListener.listen()
}