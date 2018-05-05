package com.marcinmejner.czytnikreddit.api

import com.marcinmejner.czytnikreddit.model.Feed
import retrofit2.Call
import com.marcinmejner.czytnikreddit.account.CheckLogin
import com.marcinmejner.czytnikreddit.comments.CheckComment
import retrofit2.http.*
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.net.URLEncoder


interface FeedAPI {

    @GET("{feed_name}/.rss")
    fun getFeed(@Path("feed_name") feed_name: String): Call<Feed>

    //Logowanie
    @POST("{user}")
    fun signIn(
            @HeaderMap headers: Map<String, String>,
            @Path("user") username: String,
            @Query("user") user: String,
            @Query("passwd") password: String,
            @Query("api_type") type: String
    ): Call<CheckLogin>

    val url: String
        get() = URLEncoder.encode("amp;text", "UTF-8")

    @POST("{comment}")
    fun submitComment(
            @HeaderMap headers: Map<String, String>,
            @Path("comment") comment: String,
            @Query("parent") parent: String,
            @Query("amp;text", encoded = true) text: String
    ): Call<CheckComment>



}

