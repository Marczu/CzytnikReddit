package com.marcinmejner.czytnikreddit

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.marcinmejner.czytnikreddit.api.FeedAPI
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.activity_comments.*
import retrofit2.Call
import javax.inject.Inject

class CommentsActivity : AppCompatActivity() {
    private val TAG = "CommentsActivity"

    companion object {
        var postURL: String = ""
        var postThumbnailUrl: String = ""
        var stringPostTitle: String = ""
        var stringPostAuthor: String = ""
        var stringPostUpdated: String = ""
        var defaultImage: Int = 0
    }

    var currentFeed: String = ""

    @Inject
    lateinit var feedAPI: FeedAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        Log.d(TAG, "onCreate: ")

        RedApp.component.inject(this)

        initPost()

        val call = feedAPI.getFeed(currentFeed)

    }

    fun initPost() {
        postURL = intent.getStringExtra(getString(R.string.post_url))
        postThumbnailUrl = intent.getStringExtra(getString(R.string.thumbnail_Url))
        stringPostTitle = intent.getStringExtra(getString(R.string.title))
        stringPostAuthor = intent.getStringExtra(getString(R.string.author))
        stringPostUpdated = intent.getStringExtra(getString(R.string.date_updated))

        postTitle.text = intent.getStringExtra(getString(R.string.title))
        postAuthor.text = intent.getStringExtra(getString(R.string.author))
        postUpdated.text = intent.getStringExtra(getString(R.string.date_updated))

        setupImageLoader()
        displayImage(postThumbnailUrl, postThumbnail, postLoadingProgressBar)

        val splitUrl = postURL.split(BASE_URL)

        try {
            currentFeed = splitUrl[1]
            Log.d(TAG, "initPost: splitted String: $currentFeed")
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.d(TAG, "initPost: ArrayIndexOutOfBoundsException ${e.message}")
        }

    }

    fun displayImage(imageUrl: String, imageView: ImageView, progressBar: ProgressBar) {
        val imageLoader = ImageLoader.getInstance()

        val defaultImage = this@CommentsActivity.resources.getIdentifier("@drawable/reddit_default", null, this@CommentsActivity.packageName)

        val options = DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build()

        imageLoader.displayImage(imageUrl, imageView, options, object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String, view: View) {
                progressBar.visibility = View.VISIBLE
            }

            override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                progressBar.visibility = View.GONE
            }

            override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                progressBar.visibility = View.GONE
            }

            override fun onLoadingCancelled(imageUri: String, view: View) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun setupImageLoader() {
        // UIL Setup
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(FadeInBitmapDisplayer(300)).build()

        val config = ImageLoaderConfiguration.Builder(
                this@CommentsActivity)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build()

        ImageLoader.getInstance().init(config)

        defaultImage = this@CommentsActivity.resources.getIdentifier("@drawable/reddit_default", null, this@CommentsActivity.packageName)
    }

}
