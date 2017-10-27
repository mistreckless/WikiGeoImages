package com.example.mistreckless.wikilocationarticle.data.repository

import com.example.mistreckless.wikilocationarticle.data.location.RxLocation
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by mistreckless on 27.10.17.
 */

interface LocationRepository{

    fun getLastKnownLocation() : Single<Pair<Double,Double>>
}

class LocationRepositoryImpl(private val rxLocation: RxLocation) : LocationRepository{
    override fun getLastKnownLocation(): Single<Pair<Double, Double>> {
        return rxLocation.getLastKnownLocation()
                .subscribeOn(Schedulers.io())
    }
}