package com.example.mistreckless.wikilocationarticle.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by mistreckless on 27.10.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ArticleResponse(val query: Query=Query(),val error : Error?=null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Query(@JsonProperty("geosearch") val items : List<GeoSearchItem> = listOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoSearchItem(@JsonProperty("pageid") val pageId : Long =-1)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(val code : String="", val info : String="")