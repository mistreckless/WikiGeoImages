package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.example.mistreckless.wikilocationarticle.app.PerActivity
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by mistreckless on 26.10.17.
 */
@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(private val wallInteractor: WallInteractor) : BasePresenter<MainActivityView>() {
    override fun onFirstViewAttach() {
        viewState.requestLocationPermission()
    }

    fun locationPermissionsResult(granted : Boolean){
        if (granted) viewState.initUi()
        else viewState.requestLocationPermission()
    }

    fun controlList(observeScroll: Observable<Int>, imageWrapper: ImageWrapper) {
        viewDisposable.add(wallInteractor.controlList(observeScroll,imageWrapper)
                .subscribe({ state ->
                    when (state) {
                        is StateError -> when (state.errorType) {
                            ErrorType.RESPONSE_ERROR -> {
                                Log.e(TAG,"respError")
                            }
                            ErrorType.NETWORK_CONNECTION_ERROR -> {
                                Log.e(TAG,"networkEror")
                            }
                        }
                        is StatePartImagesLoaded -> viewState.showItems(state.images)
                        is StateDone -> {
                            viewState.showImageCount()
                            Log.e(TAG, "stateDone")
                        }
                        is StateInit -> {
                            Log.e(TAG, "stateInit")
                        }
                    }
                }, {
                    Log.e(TAG, it.message, it)
                }))
    }

    companion object {
        const val TAG = "MainActivityPresenter"
    }
}