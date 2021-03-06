package com.example.mistreckless.wikilocationarticle.data.location

import android.annotation.SuppressLint
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Single

/**
 * Created by mistreckless on 27.10.17.
 */

interface RxLocation {

    fun getLastKnownLocation(): Single<Pair<Double, Double>>
}

class RxLocationImpl(private val fusedLocationProviderClient: FusedLocationProviderClient) : RxLocation {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Single<Pair<Double, Double>> {
        return Single.create { e ->

            fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener {
                        if (!e.isDisposed) {
                            if (it != null)
                                e.onSuccess(Pair(it.latitude, it.longitude))
                            else {
                                e.onError(LocationException("null location"))
                            }
                        }
                    }
                    .addOnFailureListener {
                        if (!e.isDisposed)
                            e.onError(LocationException(it.message?:""))
                    }
        }
    }


}