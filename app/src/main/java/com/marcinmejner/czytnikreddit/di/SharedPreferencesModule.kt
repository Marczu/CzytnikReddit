package com.marcinmejner.czytnikreddit.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@Module
class SharedPreferencesModule(val context: Context) {

    @Provides
    @NetworkScope
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @NetworkScope
    @NotNull
    fun provideSharedPreferencesEditor(preferences: SharedPreferences): SharedPreferences.Editor {
        return preferences.edit()
    }

}