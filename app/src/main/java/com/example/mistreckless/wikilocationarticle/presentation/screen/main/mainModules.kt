package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import com.example.mistreckless.wikilocationarticle.app.PerActivity
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.presenterHolder
import dagger.Module
import dagger.Provides

/**
 * Created by mistreckless on 26.10.17.
 */

@PerActivity
@Module
class MainActivityModule {

    @PerActivity
    @Provides
    fun provideMainActivityPresenter(wallInteractor: WallInteractor): MainActivityPresenter {
        if (presenterHolder.contains(MainActivityPresenter.TAG))
            return presenterHolder[MainActivityPresenter.TAG] as MainActivityPresenter
        val presenter = MainActivityPresenter(wallInteractor)
        presenterHolder.put(MainActivityPresenter.TAG, presenter)
        return presenter
    }
}

//@Module
//abstract class MainActivityFragmentProvider{
//
////    @PerFragment
////    @ContributesAndroidInjector(modules = arrayOf(WallModule::class))
////    abstract fun provideWall() : Wall
//}