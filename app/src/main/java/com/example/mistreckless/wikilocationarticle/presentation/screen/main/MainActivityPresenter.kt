package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter

/**
 * Created by mistreckless on 26.10.17.
 */
class MainActivityPresenter : BasePresenter<MainActivityView, MainActivityRouter>() {
    override fun onViewFirstAttached() {
        getRouter()?.navigateToWall()
    }


    companion object {
        const val TAG = "MainActivityPresenter"
    }

}