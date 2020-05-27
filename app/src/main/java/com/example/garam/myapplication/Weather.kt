package com.example.garam.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class Weather : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        val webView = findViewById<WebView>(R.id.weatherView)
        webView.webViewClient = WebViewClient()
        var mWebsetting = webView.settings
        mWebsetting.javaScriptEnabled = true
        mWebsetting.setSupportMultipleWindows(false)
        mWebsetting.javaScriptCanOpenWindowsAutomatically = false
        mWebsetting.useWideViewPort = true
        mWebsetting.loadWithOverviewMode = true
        mWebsetting.setSupportZoom(true)
        mWebsetting.builtInZoomControls = true
        mWebsetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mWebsetting.cacheMode = WebSettings.LOAD_DEFAULT
        webView.loadUrl("https://www.weather.go.kr/w/weather/today.do")
    }
}
