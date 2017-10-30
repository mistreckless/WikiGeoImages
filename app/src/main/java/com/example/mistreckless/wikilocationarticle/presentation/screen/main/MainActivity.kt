package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import com.example.mistreckless.wikilocationarticle.R
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseActivity
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseRouter
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import com.example.mistreckless.wikilocationarticle.presentation.screen.wall.Wall

class MainActivity : BaseActivity<MainActivityPresenter>(),MainActivityView,MainActivityRouter {

    override fun getLayoutId()=R.layout.activity_main


    override fun navigateToWall() {
        supportFragmentManager.beginTransaction().add(R.id.container,Wall()).commitAllowingStateLoss()
    }

}

interface MainActivityView : BaseView

interface MainActivityRouter : BaseRouter {
    fun navigateToWall()
}
