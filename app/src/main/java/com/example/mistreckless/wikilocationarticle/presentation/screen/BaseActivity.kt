package com.example.mistreckless.wikilocationarticle.presentation.screen

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.arellomobile.mvp.MvpView
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by mistreckless on 26.10.17.
 */

abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity(), BaseView{
    @Inject
    lateinit var presenter: P
//    @Inject
//    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

//    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        attachView()
    }


    override fun onDestroy() {
        detachView()
        super.onDestroy()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun detachView()
    protected abstract fun attachView()
}

interface BaseView : MvpView