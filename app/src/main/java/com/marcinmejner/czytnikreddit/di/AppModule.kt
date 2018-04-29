package com.marcinmejner.czytnikreddit.di

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.marcinmejner.czytnikreddit.MainActivity
import com.marcinmejner.czytnikreddit.RedApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * @author juancho.
 */
@Module
class AppModule(val app: MainActivity) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication() : AppCompatActivity = app

}
