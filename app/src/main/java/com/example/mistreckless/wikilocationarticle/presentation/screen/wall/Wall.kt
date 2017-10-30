package com.example.mistreckless.wikilocationarticle.presentation.screen.wall

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.example.mistreckless.wikilocationarticle.R
import com.example.mistreckless.wikilocationarticle.domain.entity.Image
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseFragment
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import com.example.mistreckless.wikilocationarticle.presentation.screen.main.MainActivityRouter
import com.example.mistreckless.wikilocationarticle.presentation.view.observeScroll
import kotlinx.android.synthetic.main.fragment_wall.*

/**
 * Created by mistreckless on 27.10.17.
 */

class Wall : BaseFragment<WallPresenter>(),WallView{
    override fun getRouter(): MainActivityRouter {
      return  activity as MainActivityRouter
    }

    override fun getLayoutId()= R.layout.fragment_wall

    private val adapter : ImageAdapter by lazy { ImageAdapter() }

    override fun initUi() {
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=adapter
        presenter.controlList(recyclerView.observeScroll())
    }

    override fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        5)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showItems(items: List<Image>) {
        adapter.items.addAll(items)
        adapter.notifyItemRangeInserted(adapter.itemCount-items.size,adapter.itemCount)
    }

}


interface WallView : BaseView{
    fun initUi()
    fun requestLocationPermission()
    fun showItems(items : List<Image>)
}