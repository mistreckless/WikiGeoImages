package com.example.mistreckless.wikilocationarticle.domain

import com.example.mistreckless.wikilocationarticle.data.repository.LocationRepository
import com.example.mistreckless.wikilocationarticle.data.repository.WikiRepository
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractorImpl
import dagger.Module
import dagger.Provides

/**
 * Created by mistreckless on 26.10.17.
 */

@Module
class InteractorModule{

    @Provides
    fun provideMainInteractor(wikiRepository: WikiRepository,locationRepository: LocationRepository) : WallInteractor = WallInteractorImpl(wikiRepository, locationRepository)
}

