package com.example.mistreckless.wikilocationarticle.data.repository

import android.util.Log
import com.example.mistreckless.wikilocationarticle.data.network.WikiApi
import com.example.mistreckless.wikilocationarticle.domain.entity.Article
import com.example.mistreckless.wikilocationarticle.domain.entity.Image
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

/**
 * Created by mistreckless on 26.10.17.
 */

interface WikiRepository {

    fun getNearestArticles(lat: Double, lon: Double): Single<List<Article>>

    fun getPageImages(pageId : Long) : Single<List<Image>>

}

class WikiRepositoryImpl(private val wikiApi: WikiApi, private val wikiApiForImages : WikiApi) : WikiRepository {
    override fun getPageImages(pageId: Long): Single<List<Image>> {
        return wikiApiForImages.getImages(pageId)
                .map {
                    Log.e("huita",it.toString())
                    it.query.pages.page.items.map { Image(it.title) }.toTypedArray().toList()
                }
                .subscribeOn(Schedulers.io())
    }

    override fun getNearestArticles(lat: Double, lon: Double): Single<List<Article>> {
        val pipeLine = lat.toString() + "|" + lon.toString()
        return wikiApi.getGeoArticles(pipeLine)
                .map { response -> response.query.items.map { Article(it.pageId) } }
                .subscribeOn(Schedulers.io())
    }

}