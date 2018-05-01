package com.marcinmejner.czytnikreddit.WebView

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.marcinmejner.czytnikreddit.R

class WebViewActivity: AppCompatActivity() {
    private val TAG = "WebViewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_layout)

        Log.d(TAG, "onCreate: started")


    }
}