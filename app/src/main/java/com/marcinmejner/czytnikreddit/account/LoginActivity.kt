package com.marcinmejner.czytnikreddit.account


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.marcinmejner.czytnikreddit.R
import com.marcinmejner.czytnikreddit.api.FeedAPI

import com.marcinmejner.czytnikreddit.di.DaggerNetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkModule
import com.marcinmejner.czytnikreddit.model.Feed
import com.marcinmejner.czytnikreddit.model.Post
import com.marcinmejner.czytnikreddit.model.entry.Entry
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.marcinmejner.czytnikreddit.utils.ExtractXML
import com.marcinmejner.czytnikreddit.utils.LOGIN_URL
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    @Inject
    lateinit var feedAPI: FeedAPI

    //widgets
    lateinit var progressBar: ProgressBar
    var username: String? = ""
    var password: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        progressBar = loginRequestLoadingProgressBar.apply {
            visibility = View.GONE
        }

        loginBtnSignup.setOnClickListener {
            Log.d(TAG, "onCreate: proba logowania")
            username = loginInputName.text.toString()
            password = loginInputPassword.text.toString()

            if (username!!.isNotBlank() && password!!.isNotBlank()) {
                progressBar.visibility = View.VISIBLE
                //fun to sign in
                login(username, password)
            } else {
                Toast.makeText(this, "Please fill username and password", Toast.LENGTH_LONG).show()
            }
        }




    }


    fun login(username: String?, password: String?) {

        val loginComponent = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule(LOGIN_URL, GsonConverterFactory.create()))
                .build()
        loginComponent.inject(this)

        val headerMap = HashMap<String, String>().apply {
            put("Content-Type", "application/json")
        }

        val call = feedAPI.signIn(headerMap, username!!, username!!, password!!, "json")
        call.enqueue(object : Callback<CheckLogin> {
            override fun onResponse(call: Call<CheckLogin>?, response: Response<CheckLogin>?) {
                Log.d(TAG, "onResponse: feed ${response?.body()?.toString()}")
                Log.d(TAG, "onResponse: Server Response ${response.toString()}")
            }

            override fun onFailure(call: Call<CheckLogin>?, t: Throwable?) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t?.message)
                Toast.makeText(this@LoginActivity, "An Error Occured", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
