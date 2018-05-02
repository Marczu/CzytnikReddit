package com.marcinmejner.czytnikreddit.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.marcinmejner.czytnikreddit.R
import com.marcinmejner.czytnikreddit.RedApp
import com.marcinmejner.czytnikreddit.api.FeedAPI
import com.marcinmejner.czytnikreddit.di.DaggerNetworkComponent
import com.marcinmejner.czytnikreddit.di.NetworkModule
import com.marcinmejner.czytnikreddit.utils.BASE_URL
import com.marcinmejner.czytnikreddit.utils.LOGIN_URL
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    @Inject
    lateinit var feedAPI: FeedAPI

    //widgets
    val progressBar = loginRequestLoadingProgressBar
    val userName = loginInputName
    val password = loginInputPassword

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        val comp = DaggerNetworkComponent.builder()
//                .networkModule(NetworkModule(LOGIN_URL, GsonConverterFactory.create()))
//                .build()
//
//        comp.inject(this)

        progressBar.visibility = View.GONE

        loginBtnSignup.setOnClickListener {
            Log.d(TAG, "onCreate: proba logowania")
            val username = userName.text.toString()
            val password = password.text.toString()

            if (!username.isBlank() && !password.isBlank()) {
                progressBar.visibility = View.VISIBLE
                //fun to sign in
            } else {
                Toast.makeText(this, "Please fill username and password", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(username: String, password: String) {

//        val call

    }
}
