package com.marcinmejner.czytnikreddit.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule(var url: String, val factory: Converter.Factory) {

    @Provides
    @NetworkScope
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(factory)
                .build()
    }


}
