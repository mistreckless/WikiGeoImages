package com.example.mistreckless.wikilocationarticle.presentation

import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseRouter
import com.example.mistreckless.wikilocationarticle.presentation.screen.BaseView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by mistreckless on 26.10.17.
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<out V : BaseView, out R : BaseRouter> {
    private var view : V?=null
    private var router : R?=null
    protected val viewDisposable by lazy { CompositeDisposable() }

    fun attachView(view : BaseView){
        this.view=view as V
    }

    fun detachView(){
        viewDisposable.clear()
        view = null
    }

    protected fun getView()=view

    fun attachRouter(router: BaseRouter){
        this.router=router as R
    }

    fun detachRouter(){
        router=null
    }

    fun getRouter()=router

    abstract fun onViewFirstAttached()
    open fun onViewRestored() {}
}

val presenterHolder : MutableMap<String,BasePresenter<*,*>> = HashMap()