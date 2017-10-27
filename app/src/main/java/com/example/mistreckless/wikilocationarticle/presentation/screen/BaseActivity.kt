package com.example.mistreckless.wikilocationarticle.presentation.screen

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by mistreckless on 26.10.17.
 */

abstract class BaseActivity<P : BasePresenter<*,*>> : AppCompatActivity(), BaseView,BaseRouter,HasSupportFragmentInjector {
    @Inject
    lateinit var presenter: P
    @Inject
    lateinit var fragmentInjector : DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        presenter.attachRouter(this)
        presenter.attachView(this)
        if (savedInstanceState==null) presenter.onViewFirstAttached()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.onViewRestored()
    }

    override fun onDestroy() {
        presenter.detachView()
        presenter.detachRouter()
        super.onDestroy()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int
}

interface BaseView

interface BaseRouter