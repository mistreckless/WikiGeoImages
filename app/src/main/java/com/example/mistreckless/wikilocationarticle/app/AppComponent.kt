package com.example.mistreckless.wikilocationarticle.app

import android.app.Application
import com.example.mistreckless.wikilocationarticle.data.LocationModule
import com.example.mistreckless.wikilocationarticle.data.NetworkModule
import com.example.mistreckless.wikilocationarticle.data.RepositoryModule
import com.example.mistreckless.wikilocationarticle.domain.InteractorModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by mistreckless on 26.10.17.
 */

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        AppModule::class,
        InteractorModule::class,
        RepositoryModule::class,
        NetworkModule::class,
        LocationModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application) : Builder
        fun interactorModule(interactorModule: InteractorModule) : Builder
        fun repositoryModule(repositoryModule: RepositoryModule) : Builder
        fun networkModule(networkModule: NetworkModule) : Builder
        fun locationModule(locationModule: LocationModule) : Builder

        fun build() : AppComponent
    }

    fun inject(app: App)
}