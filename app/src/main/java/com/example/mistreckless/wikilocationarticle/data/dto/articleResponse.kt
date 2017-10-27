package com.example.mistreckless.wikilocationarticle.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by mistreckless on 27.10.17.
 */
data class ArticleResponse(val query: Query=Query())

data class Query(@SerializedName("geosearch") val items : List<GeoSearchItem> = listOf())

data class GeoSearchItem(@SerializedName("pageid") val pageId : Long =-1)