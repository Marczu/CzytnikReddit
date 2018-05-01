package com.marcinmejner.czytnikreddit.comments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.marcinmejner.czytnikreddit.R
import com.marcinmejner.czytnikreddit.R.id.*
import com.marcinmejner.czytnikreddit.RedApp
import com.marcinmejner.czytnikreddit.WebView.WebViewActivity
import com.marcinmejner.czytnikreddit.adapters.CommentsListAdapter
import com.marcinmejner.czytnikreddit.api.FeedAPI
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.marcinmejner.czytnikreddit.utils.ExtractXML
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.comments_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    var progressbar: ProgressBar? = null



    //vars
    var currentFeed: String = ""
    var comments: ArrayList<Comment> = ArrayList()

    @Inject
    lateinit var feedAPI: FeedAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        Log.d(TAG, "onCreate: ")

        progressbar = findViewById(R.id.commentsProgressBar)
        progressbar?.visibility = View.VISIBLE
        commentsProgressBar.visibility = View.VISIBLE
        progressText.visibility = View.VISIBLE

        RedApp.component.inject(this)

        initPost()
        retrofitInit()


    }

    fun retrofitInit() {
        val call = feedAPI.getFeed(currentFeed)

        call.enqueue(object : Callback<Feed> {
            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                val entrys = response.body()?.entrys
                for (i in 0 until entrys!!.size) {
                    Log.d(TAG, "onResponse: ${entrys[i]} ")
                    val extract = ExtractXML(entrys[i].content!!, "<div class=\"md\"><p>", "</p>" )
                    Log.d(TAG, "onResponse: ${extract.start()}")
                    val commentDetails = extract.start()

                    try{
                        comments.add(Comment(
                                commentDetails[0],
                                entrys[i].author?.name!!,
                                entrys[i].updated!!,
                                entrys[i].id!!))
                    }catch (e: IndexOutOfBoundsException){
                        comments.add(Comment(
                                "Error reading comment",
                                "None",
                                "None",
                                "None"))

                        Log.d(TAG, "onResponse: ArrayIndexOutOfBoundsException ${e.message} ")
                    }catch (e: NullPointerException){
                        comments.add(Comment(
                                commentDetails[0],
                                "None",
                                entrys[i].updated!!,
                                entrys[i].id!!))

                        Log.d(TAG, "onResponse: NullPointerException : ${e.message}")

                    }
                }
                val adapter = CommentsListAdapter(this@CommentsActivity, R.layout.comments_layout, comments)
                commentsListView.adapter = adapter

                progressbar?.visibility = View.INVISIBLE
                progressText.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.message)
                Toast.makeText(this@CommentsActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
            }
        })
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

        postThumbnail.setOnClickListener {
            Log.d(TAG, "initPost: opening url in webview : $postURL")
            Intent(this@CommentsActivity, WebViewActivity::class.java).apply {
                putExtra("url", postURL)
                startActivity(this)
            }
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
