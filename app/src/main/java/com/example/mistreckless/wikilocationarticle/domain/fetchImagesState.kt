package com.example.mistreckless.wikilocationarticle.domain

import com.example.mistreckless.wikilocationarticle.domain.entity.Image

/**
 * Created by mistreckless on 27.10.17.
 */


sealed class FetchImagesState

class StateInit : FetchImagesState()

class StatePageIdsLoaded(val pageIds: Array<Long>) : FetchImagesState()

data class StateError(val errorType : ErrorType, val message : String="") : FetchImagesState()

data class StatePartImagesLoaded(val images : List<Image>) : FetchImagesState()

class StateDone : FetchImagesState()

class StateConnectionRemained : FetchImagesState()

enum class ErrorType {
    NETWORK_CONNECTION_ERROR, RESPONSE_ERROR, LOCATION_ERROR
}