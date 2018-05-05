package com.marcinmejner.czytnikreddit.comments

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import com.marcinmejner.czytnikreddit.R
import com.marcinmejner.czytnikreddit.RedApp
import com.marcinmejner.czytnikreddit.WebView.WebViewActivity
import com.marcinmejner.czytnikreddit.account.LoginActivity
import com.marcinmejner.czytnikreddit.adapters.CommentsListAdapter
import com.marcinmejner.czytnikreddit.api.FeedAPI
import com.marcinmejner.czytnikreddit.di.DaggerNetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkModule
import com.marcinmejner.czytnikreddit.di.SharedPreferencesModule
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.marcinmejner.czytnikreddit.utils.COMMENT_URL
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
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
        var postID: String = ""
    }

    var progressbar: ProgressBar? = null



    //vars
    var currentFeed: String = ""
    var comments: ArrayList<Comment> = ArrayList()
    var modhash: String? = ""
    var cookie: String? = ""
    var username: String? = ""

    //Dagger
    @Inject
    lateinit var feedAPI: FeedAPI
    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        Log.d(TAG, "onCreate: ")
        RedApp.component.inject(this)

        setupToolbar()
        getSessionParams()

        progressbar = findViewById(R.id.commentsProgressBar)
        progressbar?.visibility = View.VISIBLE
        commentsProgressBar.visibility = View.VISIBLE
        progressText.visibility = View.VISIBLE

        initPost()
        retrofitInit()
    }

    override fun onPostResume() {
        super.onPostResume()
        getSessionParams()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }


    fun setupToolbar(){

        toolbar_main.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            setNavigationOnClickListener {
                finish()
            }
            setOnMenuItemClickListener {item ->
                when (item.itemId) {
                    R.id.navLogin -> Intent(this@CommentsActivity, LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                    else -> Log.d(TAG, "setupToolbar: error")
                }
                true
            }
        }
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

                    try {
                        comments.add(com.marcinmejner.czytnikreddit.comments.Comment(
                                commentDetails[0],
                                entrys[i].author?.name?: "None",
                                entrys[i].updated!!,
                                entrys[i].id!!
                        ))

                    }catch (e: IndexOutOfBoundsException){
                        comments.add(com.marcinmejner.czytnikreddit.comments.Comment(
                                "Error reading comment",
                                "None",
                                "None",
                                "None"))

                        Log.d(TAG, "onResponse: ArrayIndexOutOfBoundsException ${e.message} ")
                    }
                }
                val adapter = CommentsListAdapter(this@CommentsActivity, R.layout.comments_layout, comments)
                commentsListView.adapter = adapter

                //Klikniecie na komentarz powoduje włączenie alert dialogu
                commentsListView.setOnItemClickListener { adapterView, view, i, l ->
                    getUserComment(this@CommentsActivity.comments[i].id)
                }

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
        postID = intent.getStringExtra(getString(R.string.id))

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

        btnPostReply.setOnClickListener {
            Log.d(TAG, "initPost: clicked reply")
            getUserComment(postID)
        }

        postThumbnail.setOnClickListener {
            Log.d(TAG, "initPost: opening url in webview : $postURL")
            Intent(this@CommentsActivity, WebViewActivity::class.java).apply {
                putExtra(getString(R.string.url), postURL)
                startActivity(this)
            }
        }
    }

    /*Wyświetlamy dialog ktory pozwala dodać komentarz*/
    fun getUserComment(post_id: String){
        val dialog = Dialog(this@CommentsActivity).apply {
            title = "Dialog"
            setContentView(R.layout.comment_input_dialog)
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.7).toInt()
            window.setLayout(width, height)
            show()
        }

        val btnPostComment = dialog.findViewById<Button>(R.id.dialogPostCommentBtn)
        val comment = dialog.findViewById<EditText>(R.id.dialogComment)

        btnPostComment.setOnClickListener {
            Log.d(TAG, "getUserComment:  attempting to post comment")

            //Post comment retrofit stuff
            val commentsRetrofit = DaggerNetworkComponent.builder()
                    .networkModule(NetworkModule(COMMENT_URL, GsonConverterFactory.create()))
                    .sharedPreferencesModule(SharedPreferencesModule(this))
                    .build()

            val retrofit = commentsRetrofit.getRetrofit()
            val feedAPI2 = retrofit.create(FeedAPI::class.java)

            var headerMap = HashMap<String, String>()
            headerMap.put("User-Agent", username!!)
            headerMap.put("X-Modhash", modhash!!)
            headerMap.put("cookie", "reddit_session=" + cookie)

            Log.d(TAG, "btnPostComment:  \n" +
                    "username: " + username + "\n" +
                    "modhash: " + modhash + "\n" +
                    "cookie: " + cookie + "\n" +
                    "wydruk z hashmapy ${headerMap["User-Agent"]} + ${headerMap["X-Modhash"]}"
            )

            var com = comment.text.toString()

            val call = feedAPI2.submitComment(headerMap, "comment", post_id, com)
            call.enqueue(object : Callback<CheckComment>{

                override fun onResponse(call: Call<CheckComment>, response: Response<CheckComment>?) {
                    Log.d(TAG, "onResponse: feed ${response?.body()?.toString()}")
                    Log.d(TAG, "onResponse: Server Response ${response.toString()}")
                    Log.d(TAG, "onResponse: succesful =  ${response?.body()?.success!!}")

                    Log.d(TAG, "onResponse: post_id to: $post_id")

                    val postSuccess = response?.body()?.success

                    if(postSuccess.equals("true")){
                        Toast.makeText(this@CommentsActivity, "Comment added", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        Log.d(TAG, "onResponse: adding ")
                    }else{
                        Toast.makeText(this@CommentsActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "ERROR")
                    }
                }

                override fun onFailure(call: Call<CheckComment>?, t: Throwable?) {
                    Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t?.message)
                    Toast.makeText(this@CommentsActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
                }
            })
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

    /*Odbieramy dane zapisane w pamięci, z sesji logowania*/
    fun getSessionParams(){
        username = prefs.getString(getString(R.string.sesion_username), "")
        modhash = prefs.getString(getString(R.string.sesion_modhash), "")
        cookie = prefs.getString(getString(R.string.session_cookie), "")

        Log.d(TAG, "getSessionParams: retreaving sesion vars: \n" +
                "username = $username \n" +
                "modhash = $modhash \n" +
                "cookie = $cookie")
    }

}
