package com.example.mistreckless.wikilocationarticle.app

import android.app.Activity
import android.app.Application
import com.example.mistreckless.wikilocationarticle.data.LocationModule
import com.example.mistreckless.wikilocationarticle.data.NetworkModule
import com.example.mistreckless.wikilocationarticle.data.RepositoryModule
import com.example.mistreckless.wikilocationarticle.domain.InteractorModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by mistreckless on 26.10.17.
 */

class App : Application(), HasActivityInjector{
    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .interactorModule(InteractorModule())
                .repositoryModule(RepositoryModule())
                .networkModule(NetworkModule())
                .locationModule(LocationModule())
                .build()
                .inject(this)
    }
}