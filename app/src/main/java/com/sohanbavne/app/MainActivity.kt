package com.sohanbavne.app

import android.app.Activity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.os.Build
import android.view.View
import android.view.Window

class MainActivity : Activity() {

    private lateinit var webView: WebView

    private val homeUrl =
        "https://sohanbavne2026.blogspot.com/"

    private val allowedHost =
        "sohanbavne2026.blogspot.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    // Android 12+ में सिस्टम Splash Screen इस्तेमाल होगी।
} else {
    window.setBackgroundDrawableResource(
        R.drawable.splash_background
    )
}
        webView = WebView(this)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            mixedContentMode =
                android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW

            if (android.os.Build.VERSION.SDK_INT >= 26) {
                safeBrowsingEnabled = true
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                return handleUrl(request.url)
            }

            @Suppress("DEPRECATION")
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                return handleUrl(Uri.parse(url))
            }
        }

        webView.webChromeClient = WebChromeClient()

        setContentView(webView)

        if (savedInstanceState == null) {
            webView.loadUrl(homeUrl)
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    private fun handleUrl(uri: Uri): Boolean {

        if (uri.scheme != "https") {
            return true
        }

        if (uri.host == allowedHost) {
            return false
        }

        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, uri)
            )
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Unable to open this link",
                Toast.LENGTH_SHORT
            ).show()
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
