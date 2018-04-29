package com.marcinmejner.czytnikreddit

import android.app.Application
import com.marcinmejner.czytnikreddit.di.DaggerNetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkModule

class RedApp: Application() {

    companion object {
        lateinit var component: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerNetworkComponent.builder()
                .build()
    }

}