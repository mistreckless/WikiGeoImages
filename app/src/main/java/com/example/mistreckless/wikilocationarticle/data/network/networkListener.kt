package com.example.mistreckless.wikilocationarticle.data.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposables


/**
 * Created by @mistreckless on 30.10.2017. !
 */

interface NetworkConnectionListener {
    fun listen(): Observable<Boolean>
}

class NetworkConnectionListenerImpl(private val context: Context) : NetworkConnectionListener {
    override fun listen(): Observable<Boolean> {
        return Observable.create { e ->
            val receiver = object : NetworkChangeReceiver() {
                override fun onConnectionStateChanged(isConnected: Boolean) {
                    if (!e.isDisposed)
                        e.onNext(isConnected)
                }
            }
            context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            e.setDisposable(Disposables.fromAction {
                Log.e("networkRec","UNREGISTERED")
                context.unregisterReceiver(receiver) })
        }
    }

}

abstract class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        isNetworkAvailable(context)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        onConnectionStateChanged(connectivity?.activeNetworkInfo != null)
        return false
    }

    abstract fun onConnectionStateChanged(isConnected: Boolean)

}
