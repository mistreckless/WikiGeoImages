package com.example.mistreckless.wikilocationarticle.data

import android.content.Context
import com.example.mistreckless.wikilocationarticle.BuildConfig
import com.example.mistreckless.wikilocationarticle.data.location.RxLocation
import com.example.mistreckless.wikilocationarticle.data.location.RxLocationImpl
import com.example.mistreckless.wikilocationarticle.data.network.NetworkConnectionListener
import com.example.mistreckless.wikilocationarticle.data.network.NetworkConnectionListenerImpl
import com.example.mistreckless.wikilocationarticle.data.network.WikiApi
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepositoryImpl
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepositoryImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by mistreckless on 26.10.17.
 */

@Singleton
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLocationRepository(rxLocation: RxLocation): LocationRepository = LocationRepositoryImpl(rxLocation)

    @Singleton
    @Provides
    fun provideWikiRepository(wikiApi: WikiApi,networkConnectionListener: NetworkConnectionListener): WikiRepository = WikiRepositoryImpl(wikiApi,networkConnectionListener)

}

@Singleton
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideWikiApi(): WikiApi {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(3, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .baseUrl("https://en.wikipedia.org/")
                .build()
                .create(WikiApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkConnectionListener(context: Context) : NetworkConnectionListener = NetworkConnectionListenerImpl(context)

}

@Singleton
@Module
class LocationModule{

    @Singleton
    @Provides
    fun provideRxLocation(fusedLocationProviderClient: FusedLocationProviderClient) : RxLocation = RxLocationImpl(fusedLocationProviderClient)
}