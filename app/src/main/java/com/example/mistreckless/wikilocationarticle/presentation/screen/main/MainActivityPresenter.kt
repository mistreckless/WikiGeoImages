package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.example.mistreckless.wikilocationarticle.app.PerActivity
import com.example.mistreckless.wikilocationarticle.domain.*
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import com.example.mistreckless.wikilocationarticle.presentation.view.ImageAdapterWrapper
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

    fun locationPermissionsResult(granted: Boolean) {
        if (granted) viewState.initUi(INITIAL_SCROLL_VALUE)
        else viewState.requestLocationPermission()
    }

    fun controlList(observeScroll: Observable<Int>, imageAdapterWrapper: ImageAdapterWrapper, limit : Int) {
        viewDisposable.add(wallInteractor.controlList(observeScroll, imageAdapterWrapper, INITIAL_SCROLL_VALUE, limit)
                .subscribe({ state ->
                    when (state) {
                        is StateInit -> {
                        }
                        is StatePageIdsLoaded -> {
                        }
                        is StatePartImagesLoaded -> {
                            viewState.showItems(state.images)
                        }
                        is StateConnectionRemained -> {
                            viewState.showToast("connection changed")
                        }
                        is StateDone -> {
                            viewState.showToast("done")
                        }

                        is StateError -> {
                            when (state.errorType) {
                                ErrorType.RESPONSE_ERROR -> {
                                    viewState.showToast("response err " + state.message)
                                }
                                ErrorType.NETWORK_CONNECTION_ERROR -> {
                                    viewState.showToast("network err " + state.message)
                                }
                                ErrorType.LOCATION_ERROR -> {
                                    viewState.showToast("location err " + state.message)
                                }
                            }
                        }
                    }

                }, {
                    Log.e(TAG, it.message, it)
                }))
    }

    companion object {
        const val TAG = "MainActivityPresenter"
        const val INITIAL_SCROLL_VALUE = 0
    }
}