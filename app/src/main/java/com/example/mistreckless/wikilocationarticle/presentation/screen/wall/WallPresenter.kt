package com.example.mistreckless.wikilocationarticle.presentation.screen.wall

import android.util.Log
import com.example.mistreckless.wikilocationarticle.domain.entity.StateInit
import com.example.mistreckless.wikilocationarticle.domain.entity.StatePartImagesLoaded
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import com.example.mistreckless.wikilocationarticle.presentation.screen.main.MainActivityRouter

/**
 * Created by mistreckless on 27.10.17.
 */


class WallPresenter(private val wallInteractor: WallInteractor) : BasePresenter<WallView,MainActivityRouter>(){
    override fun onViewFirstAttached() {

        wallInteractor.fetchImages()
                .subscribe({state->
                    when(state){
                        is StateInit->getView()?.initUi()
                        is StatePartImagesLoaded->getView()?.showItems(state.images)
                    }
                },{
                    Log.e(TAG,it.message,it)
                })
    }

    override fun onViewRestored() {
        getView()?.initUi()
    }

    companion object {
        const val TAG="WallPresenter"
    }
}