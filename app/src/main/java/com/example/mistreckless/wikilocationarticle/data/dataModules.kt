package com.example.mistreckless.wikilocationarticle.data

import com.example.mistreckless.wikilocationarticle.data.location.RxLocation
import com.example.mistreckless.wikilocationarticle.data.location.RxLocationImpl
import com.example.mistreckless.wikilocationarticle.data.network.WikiApi
import com.example.mistreckless.wikilocationarticle.data.network.WikiImagesConverterFactory
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepositoryImpl
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
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
    fun provideWikiRepository(@Named("defaultApi")wikiApi: WikiApi, @Named("imagesApi") imagesWikiApi : WikiApi): WikiRepository = WikiRepositoryImpl(wikiApi,imagesWikiApi)

}

@Singleton
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("defaultApi")
    fun provideWikiApi(): WikiApi {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/")
                .build()
                .create(WikiApi::class.java)
    }

    @Singleton
    @Provides
    @Named("imagesApi")
    fun provideImagesWikiApi() : WikiApi{
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level= HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(WikiImagesConverterFactory())
                .baseUrl("https://en.wikipedia.org/")
                .build()
                .create(WikiApi::class.java)
    }
}

@Singleton
@Module
class LocationModule{

    @Singleton
    @Provides
    fun provideRxLocation(fusedLocationProviderClient: FusedLocationProviderClient) : RxLocation = RxLocationImpl(fusedLocationProviderClient)
}