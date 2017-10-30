package com.example.mistreckless.wikilocationarticle.app

import android.app.Application
import android.content.Context
import com.example.mistreckless.wikilocationarticle.presentation.screen.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

/**
 * Created by mistreckless on 26.10.17.
 */

@Singleton
@Module
class AppModule{

    @Singleton
    @Provides
    fun provideContext(application: Application) : Context = application

    @Singleton
    @Provides
    fun provideFusedLocationClient(context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
}

@PerActivity
@Module
abstract class ActivityBuilder{

    @PerActivity
    @ContributesAndroidInjector
    abstract fun provideMainActivity() : MainActivity

}