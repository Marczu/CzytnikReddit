package com.marcinmejner.czytnikreddit.api

import com.marcinmejner.czytnikreddit.model.Feed
import retrofit2.Call
import com.marcinmejner.czytnikreddit.account.CheckLogin
import retrofit2.http.*


interface FeedAPI {

    @GET("{feed_name}/.rss")
    fun getFeed(@Path("feed_name") feed_name: String): Call<Feed>




    @POST("{user}")
    fun signIn(
            @HeaderMap headers: Map<String, String>,
            @Path("user") username: String,
            @Query("user") user: String,
            @Query("passwd") password: String,
            @Query("api_type") type: String
    ): Call<CheckLogin>


}

