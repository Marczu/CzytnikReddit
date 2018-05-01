package com.marcinmejner.czytnikreddit.WebView

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.marcinmejner.czytnikreddit.R
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity: AppCompatActivity() {
    private val TAG = "WebViewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        Log.d(TAG, "onCreate: started")

        webviewLoadingProgressBar.visibility = View.VISIBLE
        webcviewProgressText.visibility = View.VISIBLE

        initWebview()


    }

    fun initWebview(){
        val url = intent.getStringExtra(getString(R.string.url))
        webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    webviewLoadingProgressBar.visibility = View.GONE
                    webcviewProgressText.visibility = View.GONE
                }
            }
            loadUrl(url)
        }
    }
}