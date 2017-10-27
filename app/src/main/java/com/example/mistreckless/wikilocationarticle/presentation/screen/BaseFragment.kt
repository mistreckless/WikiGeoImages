package com.example.mistreckless.wikilocationarticle.presentation.screen

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mistreckless.wikilocationarticle.presentation.BasePresenter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by mistreckless on 27.10.17.
 */

abstract class BaseFragment<P : BasePresenter<*,*>> : Fragment(),BaseView{

    @Inject
    lateinit var presenter : P


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        presenter.attachRouter(getRouter())
        retainInstance=true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayoutId(),container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter.attachView(this)

        if (savedInstanceState==null) presenter.onViewFirstAttached()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onViewRestored()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onDetach() {
        presenter.detachRouter()
        super.onDetach()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getRouter() : BaseRouter
}