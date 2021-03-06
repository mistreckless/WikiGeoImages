package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.mistreckless.wikilocationarticle.R
import com.example.mistreckless.wikilocationarticle.domain.entity.Image
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseActivity
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import com.example.mistreckless.wikilocationarticle.presentation.view.ImageAdapter
import com.example.mistreckless.wikilocationarticle.presentation.view.ImageAdapterWrapper
import com.example.mistreckless.wikilocationarticle.presentation.view.observeScroll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView {

    @ProvidePresenter
    fun providePresenter(): MainActivityPresenter = presenterProvider.get()

    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    private val adapter by lazy { ImageAdapter() }

    override fun initUi(initialScrollValue: Int) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        presenter.controlList(recyclerView.observeScroll(initialScrollValue), ImageAdapterWrapper(adapter),50)
    }

    override fun requestLocationPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    ACCESS_COARSE_LOCATION_CODE)

        } else presenter.locationPermissionsResult(true)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_COARSE_LOCATION_CODE -> presenter.locationPermissionsResult(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun showItems(items: List<Image>) {
        adapter.items.addAll(items)
        adapter.notifyItemRangeInserted(adapter.itemCount - items.size, adapter.itemCount)
    }

    override fun getLayoutId() = R.layout.activity_main


    companion object {
        const val ACCESS_COARSE_LOCATION_CODE = 0
    }
}

interface MainActivityView : BaseView {
    @StateStrategyType(AddToEndStrategy::class)
    fun initUi(initialScrollValue: Int)

    @StateStrategyType(SkipStrategy::class)
    fun requestLocationPermission()

    @StateStrategyType(value = AddToEndStrategy::class)
    fun showItems(items: List<Image>)

}


