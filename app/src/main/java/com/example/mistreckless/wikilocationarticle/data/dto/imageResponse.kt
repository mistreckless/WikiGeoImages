package com.example.mistreckless.wikilocationarticle.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by mistreckless on 27.10.17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageResponse(@JsonProperty("continue") val cont : Map<String,String>? = null,
                         @JsonProperty("query") val query: ImageQuery = ImageQuery())

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageQuery(@JsonProperty("pages") val pages: Map<String, ImageItem> = mapOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageItem(@JsonProperty("title") val title: String = "")