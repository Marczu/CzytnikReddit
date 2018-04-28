package com.marcinmejner.czytnikreddit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.marcinmejner.czytnikreddit.Adapters.CustomListAdapter
import com.marcinmejner.czytnikreddit.R.id.listView
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.model.Post
import com.marcinmejner.czytnikreddit.model.entry.Entry
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.marcinmejner.czytnikreddit.utils.ExtractXML
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //vars
    lateinit var posts: ArrayList<Post>
    var currentFeed: String? = ""



    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitSetup{complete ->
            if(complete){
                val customListAdapter = CustomListAdapter(this@MainActivity, R.layout.card_layout_main, posts)
                listView.adapter = customListAdapter
            }
        }

        btnRefreshFeed.setOnClickListener {
            var feedName = etFeedName.text.toString()
            if(feedName.isNotEmpty()){
                currentFeed = feedName
                retrofitSetup{complete ->
                    if(complete){
                        val customListAdapter = CustomListAdapter(this@MainActivity, R.layout.card_layout_main, posts)
                        listView.adapter = customListAdapter
                    }
                }
            }else{
                retrofitSetup{complete ->
                    if(complete){
                        val customListAdapter = CustomListAdapter(this@MainActivity, R.layout.card_layout_main, posts)
                        listView.adapter = customListAdapter
                    }
                }
            }
        }


    }

    fun retrofitSetup(complete: (Boolean) -> Unit){
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val feedAPI = retrofit.create(FeedAPI::class.java)
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
                    val lastPosition = postContent.size -1
                    posts.add(Post(
                            entrys[i].title!!,
                            entrys[i].author!!.name!!.replace("/u/", ""),
                            entrys[i].updated!!,
                            postContent[0],
                            postContent[lastPosition]
                    ))
                    Log.d(TAG, "onResponse: tralalala : $postContent")

                }
                for(j in 0 until posts.size){
                    Log.d(TAG, "onResponse dane z XMLa: \n" +
                            "PostURL : ${posts[j].postURL} \n" +
                            "ThumbnailUrl : ${posts[j].thumbnailURL} \n" +
                            "Title : ${posts[j].title} \n" +
                            "Author : ${posts[j].author} \n" +
                            "Date Updated : ${posts[j].date_updated}")
                }
                complete(true)
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.message)
                Toast.makeText(this@MainActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
                complete(false)
            }
        })
    }

}





