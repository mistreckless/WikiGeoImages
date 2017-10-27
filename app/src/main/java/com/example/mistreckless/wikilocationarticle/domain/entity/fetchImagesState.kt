package com.example.mistreckless.wikilocationarticle.domain.entity

/**
 * Created by mistreckless on 27.10.17.
 */


sealed class FetchImagesState

class StateInit() : FetchImagesState()

class StateArticlesLoaded(val articles : List<Article>) : FetchImagesState()

data class StateError(val message : String) : FetchImagesState()

data class StatePartImagesLoaded(val images : List<Image>) : FetchImagesState()

class StateDone() : FetchImagesState()