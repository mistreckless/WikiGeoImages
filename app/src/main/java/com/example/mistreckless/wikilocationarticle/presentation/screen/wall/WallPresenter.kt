package com.example.mistreckless.wikilocationarticle.presentation.screen.wall

import android.util.Log
import com.example.mistreckless.wikilocationarticle.domain.entity.*
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import com.example.mistreckless.wikilocationarticle.presentation.screen.main.MainActivityRouter
import io.reactivex.Observable

/**
 * Created by mistreckless on 27.10.17.
 */


class WallPresenter(private val wallInteractor: WallInteractor) : BasePresenter<WallView, MainActivityRouter>() {
    override fun onViewFirstAttached() {
        getView()?.requestLocationPermission()
        getView()?.initUi()
    }

    override fun onViewRestored() {
        getView()?.initUi()
    }

    companion object {
        const val TAG = "WallPresenter"
    }

    fun controlList(observeScroll: Observable<Int>) {
        viewDisposable.add(wallInteractor.controlList(observeScroll)
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
                        is StatePartImagesLoaded -> getView()?.showItems(state.images)
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
}