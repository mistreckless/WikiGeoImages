package com.example.mistreckless.wikilocationarticle.presentation.screen.wall

import com.example.mistreckless.wikilocationarticle.app.PerFragment
import com.example.mistreckless.wikilocationarticle.domain.interactor.WallInteractor
import com.example.mistreckless.wikilocationarticle.presentation.presenterHolder
import dagger.Module
import dagger.Provides

/**
 * Created by mistreckless on 27.10.17.
 */


@PerFragment
@Module
class WallModule{

    @PerFragment
    @Provides
    fun provideWallPresenter(wallInteractor: WallInteractor) : WallPresenter{
        if (presenterHolder.contains(WallPresenter.TAG))
            return presenterHolder[WallPresenter.TAG] as WallPresenter
        val presenter = WallPresenter(wallInteractor)
        presenterHolder.put(WallPresenter.TAG,presenter)
        return presenter
    }
}