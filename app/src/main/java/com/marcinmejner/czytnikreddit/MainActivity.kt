package com.marcinmejner.czytnikreddit

import android.app.PendingIntent.getActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory




class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val feedAPI = retrofit.create(FeedAPI::class.java)

        val call = feedAPI.getFeed

        call.enqueue(object : Callback<Feed> {
            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                Log.d(TAG, "onResponse: feed: " + response.body()?.entrys)
                Log.d(TAG, "onResponse: feed: " + response.body()?.toString())
                Log.d(TAG, "onResponse: Server Response: " + response.toString())
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.message)
                Toast.makeText(this@MainActivity, "An Error Occured", Toast.LENGTH_SHORT).show()

            }
        })
    }

    companion object {


        private val TAG = "MainActivity"

        private val BASE_URL = "https://www.reddit.com/r/"
    }
}






//    private val TAG = "MainActivity"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//
//        val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build()
//
//        val feedApi = retrofit.create<FeedAPI>(FeedAPI::class.java!!)
//
//        val call = feedApi.getFeed()
//
//
//
//
////        call.enqueue(object : Callback<Feed> {
////            override fun onResponse(call: Call<Feed>, response: retrofit2.Response<Feed>) {
////                Log.d(TAG, "onResponse: ${response.body()?.entrys}")
////                Log.d(TAG, "onResponse: ${response.body()?.title}")
////                Log.d(TAG, "onResponse: SERVER RESPONSE ${response.toString()}")
////
////            }
////
////            override fun onFailure(call: Call<Feed>, t: Throwable) {
////                Log.d(TAG, "onFailure: unable to rtrive RSS ${t.localizedMessage}")
////            }
////        })
//
//
//
//
//
//
//        call.enqueue(object : Callback<Feed> {
//            override fun onResponse(call: Call<Feed>?, response: Response<Feed>?) {
//                Log.d(TAG, "onResponse: ${response?.body()?.entrys}")
//                Log.d(TAG, "onResponse: ${response?.body()?.title}")
//                Log.d(TAG, "onResponse: SERVER RESPONSE ${response.toString()}")
//            }
//
//            override fun onFailure(call: Call<Feed>?, t: Throwable?) {
//                Log.d(TAG, "onFailure: unable to rtrive RSS ${t?.localizedMessage}")
//            }
//
//
//        })
//
//
//
//
//
//
//
//
//
//    }

