package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.api.FeedAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class FeedModule {

    @Provides
    @NetworkScope
    fun provideRedditApi(retrofi: Retrofit): FeedAPI = retrofi.create(FeedAPI::class.java)
}