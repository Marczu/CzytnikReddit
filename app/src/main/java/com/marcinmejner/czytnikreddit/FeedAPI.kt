package com.marcinmejner.czytnikreddit

import com.marcinmejner.czytnikreddit.model.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface FeedAPI {

    @GET("{feed_name}/.rss")
    fun getFeed(@Path("feed_name") feed_name: String): Call<Feed>

}

