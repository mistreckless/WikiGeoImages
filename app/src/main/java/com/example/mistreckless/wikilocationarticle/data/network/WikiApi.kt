package com.example.mistreckless.wikilocationarticle.data.network

import com.example.mistreckless.wikilocationarticle.data.dto.ArticleResponse
import com.example.mistreckless.wikilocationarticle.data.dto.ImageResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by mistreckless on 26.10.17.
 */

interface WikiApi {


    @GET("w/api.php")
    fun getGeoArticles(@Query(encoded = true, value = "gscoord") latlon: String,
                       @Query("action") action: String = "query",
                       @Query("list") list: String = "geosearch",
                       @Query("gsradius") radius: Int = 10000,
                       @Query("gslimit") limit: Int = 50,
                       @Query("format") format: String = "json"): Single<ArticleResponse>

    @GET("w/api.php")
    fun getImages(@Query("pageids") pageIds: String,
                  @QueryMap cont : Map<String,String>?,
                  @Query("gimlimit") limit: Int=50,
                  @Query("action") action: String="query",
                  @Query("prop") prop: String="images",
                  @Query("generator") generator : String="images",
                  @Query("format") format: String="json") : Single<ImageResponse>
}