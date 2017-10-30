package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import io.reactivex.Observable

/**
 * Created by mistreckless on 26.10.17.
 */

@InjectViewState
class MainActivityPresenter(private val wallInteractor: WallInteractor) : BasePresenter<MainActivityView>() {
    override fun onFirstViewAttach() {
        viewState.initUi()
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