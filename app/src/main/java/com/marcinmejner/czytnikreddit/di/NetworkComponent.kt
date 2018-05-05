package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.comments.CommentsActivity
import com.marcinmejner.czytnikreddit.MainActivity
import com.marcinmejner.czytnikreddit.account.LoginActivity
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@NetworkScope
@Component(modules = arrayOf(
        FeedModule::class,
        NetworkModule::class,
        SharedPreferencesModule::class)
)
interface NetworkComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(commentsActivity: CommentsActivity)

    fun inject(loginActivity: LoginActivity)

    fun getRetrofit(): Retrofit
}