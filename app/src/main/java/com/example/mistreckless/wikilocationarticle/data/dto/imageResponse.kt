package com.example.mistreckless.wikilocationarticle.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mistreckless on 27.10.17.
 */


data class ImageResponse(@Expose @SerializedName("continue")val cont : Continue= Continue(),
                         @Expose @SerializedName("query") val query: ImageQuery= ImageQuery())

data class Continue(@Expose @SerializedName("imcontinue")val imcontinue : String="")

data class ImageQuery(@Expose @SerializedName("pages")val pages : Pages= Pages())

data class Pages(@Expose @SerializedName("page") val page: Page=Page())

data class Page(@Expose @SerializedName("images")val items : List<ImageItem> = listOf())

data class ImageItem(@Expose @SerializedName("title")val title : String="")