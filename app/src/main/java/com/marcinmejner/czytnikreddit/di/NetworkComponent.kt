package com.marcinmejner.czytnikreddit.di

import com.marcinmejner.czytnikreddit.CommentsActivity
import com.marcinmejner.czytnikreddit.MainActivity
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

}