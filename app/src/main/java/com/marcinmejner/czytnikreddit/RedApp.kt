package com.marcinmejner.czytnikreddit

import android.app.Application
import com.marcinmejner.czytnikreddit.di.*
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RedApp: Application() {

    companion object {
        lateinit var component: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerNetworkComponent.builder()
                .networkModule(com.marcinmejner.czytnikreddit.di.NetworkModule(BASE_URL, SimpleXmlConverterFactory.create()))
                .sharedPreferencesModule(com.marcinmejner.czytnikreddit.di.SharedPreferencesModule(this))
                .build()
    }

}