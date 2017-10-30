package com.example.mistreckless.wikilocationarticle.presentation

import com.arellomobile.mvp.MvpPresenter
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by mistreckless on 26.10.17.
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : BaseView> : MvpPresenter<V>() {
    protected val viewDisposable by lazy { CompositeDisposable() }

    override fun detachView(view: V) {
        viewDisposable.clear()
        super.detachView(view)
    }

}

val presenterHolder: MutableMap<String, BasePresenter<*>> = HashMap()