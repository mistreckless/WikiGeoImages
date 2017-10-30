package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.mistreckless.wikilocationarticle.R
import com.example.mistreckless.wikilocationarticle.domain.entity.Image
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseActivity
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import com.example.mistreckless.wikilocationarticle.presentation.view.observeScroll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView {

    private val adapter by lazy { ImageAdapter() }

    override fun initUi() {
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=adapter
        presenter.controlList(recyclerView.observeScroll(), ImageWrapper(adapter))
    }

    override fun requestLocationPermission() {

    }

    override fun showItems(items: List<Image>) {
        adapter.items.addAll(items)
        adapter.notifyItemRangeInserted(adapter.itemCount-items.size,adapter.itemCount)

    }

    override fun detachView() = presenter.detachView(this)

    override fun attachView() = presenter.attachView(this)

    override fun getLayoutId() = R.layout.activity_main


}

interface MainActivityView : BaseView {
    @StateStrategyType(AddToEndStrategy::class)
    fun initUi()

    @StateStrategyType(SkipStrategy::class)
    fun requestLocationPermission()

    @StateStrategyType(value = AddToEndStrategy::class)
    fun showItems(items: List<Image>)
}


