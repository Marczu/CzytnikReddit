package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.comments.CommentsActivity
import com.marcinmejner.czytnikreddit.MainActivity
import com.marcinmejner.czytnikreddit.account.LoginActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        FeedModule::class,
        NetworkModule::class)
)
interface NetworkComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(commentsActivity: CommentsActivity)

//    fun inject(loginActivity: LoginActivity)

}