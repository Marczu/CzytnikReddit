package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.account.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedPreferencesModule::class])
interface SharedPreferencesComponent {

    fun inject (loginActivity: LoginActivity)

}