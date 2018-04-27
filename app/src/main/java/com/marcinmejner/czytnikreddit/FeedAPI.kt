package com.marcinmejner.czytnikreddit

import com.marcinmejner.czytnikreddit.model.Feed
import retrofit2.Call
import retrofit2.http.GET



interface FeedAPI {

    @get:GET("androiddev/.rss")
    val getFeed: Call<Feed>

}

