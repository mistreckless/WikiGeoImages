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


//    override fun requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                presenter.fetch()
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                        5)
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//        else presenter.fetch()
//    }




}

interface MainActivityView : BaseView

interface MainActivityRouter : BaseRouter {
    fun navigateToWall()
}
