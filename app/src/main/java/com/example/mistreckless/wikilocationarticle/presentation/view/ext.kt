package com.example.mistreckless.wikilocationarticle.presentation.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

/**
 * Created by @mistreckless on 29.10.2017. !
 */

fun RecyclerView.observeScroll(initialValue: Int): Observable<Int> = Observable.create { e ->
    if (!e.isDisposed)
        e.onNext(initialValue)
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!e.isDisposed) {
                val position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val needToEmit = layoutManager.itemCount != 0 && position * 100 / layoutManager.itemCount > 70
                if (needToEmit)
                    e.onNext(layoutManager.itemCount)
            }
        }
    }
    this.addOnScrollListener(listener)
    e.setDisposable(Disposables.fromAction { this.removeOnScrollListener(listener) })
}