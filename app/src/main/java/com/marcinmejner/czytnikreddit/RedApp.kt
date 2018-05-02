package com.marcinmejner.czytnikreddit

import android.app.Application
import com.marcinmejner.czytnikreddit.di.DaggerNetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkModule
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RedApp: Application() {

    companion object {
        lateinit var component: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule(BASE_URL, SimpleXmlConverterFactory.create()))
                .build()
    }

}