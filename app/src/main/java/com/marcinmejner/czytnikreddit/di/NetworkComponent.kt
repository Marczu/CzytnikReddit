package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(

        NetworkModule::class)
)
interface NetworkComponent {

    fun inject(mainActivity: MainActivity)

}