package com.example.mistreckless.wikilocationarticle.presentation.screen

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by mistreckless on 26.10.17.
 */

abstract class BaseActivity<P : BasePresenter<out BaseView>> : AppCompatActivity(), BaseView{
    @Inject
    lateinit var presenterProvider: Provider<P>

    val mvpDelegate by lazy { MvpDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    override fun onStart() {
        super.onStart()
        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onDetach()
    }

    override fun onDestroy() {
        mvpDelegate.onDestroyView()
        if (isFinishing) mvpDelegate.onDestroy()
        super.onDestroy()
    }

    override fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int
}

interface BaseView : MvpView{

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message : String)
}