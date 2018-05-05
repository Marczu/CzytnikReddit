package com.marcinmejner.czytnikreddit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.marcinmejner.czytnikreddit.R.id.*
import com.marcinmejner.czytnikreddit.adapters.CustomListAdapter
import com.marcinmejner.czytnikreddit.api.FeedAPI
import com.marcinmejner.czytnikreddit.comments.CommentsActivity
import com.marcinmejner.czytnikreddit.account.LoginActivity
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.model.Post
import com.marcinmejner.czytnikreddit.model.entry.Entry
import com.marcinmejner.czytnikreddit.utils.ExtractXML
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

//    @Inject
//    lateinit var retrofi: Retrofit

    @Inject
    lateinit var feedAPI: FeedAPI

    //vars
    lateinit var posts: ArrayList<Post>
    var currentFeed: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RedApp.component.inject(this)

        setupToolbar()

        btnRefreshFeed.setOnClickListener {
            var feedName = etFeedName.text.toString()
            if (feedName.isNotEmpty()) {
                currentFeed = feedName
                retrofitSetup()
            } else {
                retrofitSetup()
                val customListAdapter = CustomListAdapter(this@MainActivity, R.layout.card_layout_main, posts)
                listView.adapter = customListAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    fun setupToolbar(){
        toolbar_main.apply {
            setSupportActionBar(this)

            setOnMenuItemClickListener {item ->
                when (item.itemId) {
                    R.id.navLogin -> Intent(this@MainActivity, LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                    else -> Log.d(TAG, "setupToolbar: error")
                }
                true
            }
        }
    }

    fun retrofitSetup() {

        val call = feedAPI.getFeed(currentFeed!!)
        call.enqueue(object : Callback<Feed> {
            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {

                val entrys: List<Entry>? = response.body()?.entrys
                Log.d(TAG, "onResponse: entrysss: " + response.body()?.entrys)

                posts = ArrayList()
                for (i in 0 until entrys!!.size) {
                    val extractXml1 = ExtractXML(entrys[i].content!!, "<a href=")
                    var postContent: ArrayList<String> = extractXml1.start()
                    var extractXml2 = ExtractXML(entrys[i].content!!, "<img src=")

                    try {
                        postContent.add(extractXml2.start()[0])
                    } catch (e: KotlinNullPointerException) {
                        Log.d(TAG, "onResponse: NullPointerException (thumbnail) : ${e.localizedMessage}")
                        postContent.add("")
                    } catch (e: IndexOutOfBoundsException) {
                        postContent.add("tunicniema")
                        Log.d(TAG, "onResponse: IndexOutOfBoundsException (thumbnail) : ${e.localizedMessage}")

                    }
                    val lastPosition = postContent.size - 1
                    posts.add(Post(
                            entrys[i].title!!,
                            entrys[i].author?.name!!.replace("/u/", ""),
                            entrys[i].updated!!,
                            postContent[0],
                            postContent[lastPosition],
                            entrys[i].id!!
                    ))
                    Log.d(TAG, "onResponse: tralalala : $postContent")
                }

                for (j in 0 until posts.size) {
                    Log.d(TAG, "onResponse dane z XMLa: \n" +
                            "PostURL : ${posts[j].postURL} \n" +
                            "ThumbnailUrl : ${posts[j].thumbnailURL} \n" +
                            "Title : ${posts[j].title} \n" +
                            "Author : ${posts[j].author} \n" +
                            "Date Updated : ${posts[j].date_updated} \n" +
                            "Post ID : ${posts[j].id} " )
                }

                val customListAdapter = CustomListAdapter(this@MainActivity, R.layout.card_layout_main, posts)
                listView.adapter = customListAdapter

                /*Wysyłamy dane z kliknietego posta do CommentsActivity*/
                listView.setOnItemClickListener { adapterView, view, i, l ->
                    Log.d(TAG, "onResponse: clicked on ${posts[i].author}")
                    Intent(this@MainActivity, CommentsActivity::class.java).apply {
                        putExtra(getString(R.string.post_url), posts[i].postURL)
                        putExtra(getString(R.string.thumbnail_Url), posts[i].thumbnailURL)
                        putExtra(getString(R.string.title), posts[i].title)
                        putExtra(getString(R.string.author), posts[i].author)
                        putExtra(getString(R.string.date_updated), posts[i].date_updated)
                        putExtra(getString(R.string.id), posts[i].id)

                        startActivity(this)
                    }
                }
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.message)
                Toast.makeText(this@MainActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





