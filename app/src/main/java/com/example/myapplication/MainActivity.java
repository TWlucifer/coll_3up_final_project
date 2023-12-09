package com.example.myapplication;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WebView mWebView;
    private final String VideoEmbededAdress = "<iframe width=\"410\" height=\"650\" src=\"https://www.youtube.com/embed/SXHMnicI6Pg?si=bhR2CfWHhJi8iK5q?&autoplay=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
    private final String mimeType = "text/html";
    private final String encoding = "UTF-8";
    private String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";
    private Handler mHandler;
    private static final int LOOP_INTERVAL = 7900; // Loop interval in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.youtubeView);
        mHandler = new Handler(Looper.getMainLooper());

        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: Url = [" + request.getUrl() + "]");
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setUserAgentString(USERAGENT);
        webSettings.setLoadsImagesAutomatically(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        // Load the YouTube video
        mWebView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");

        // Start the loop after a delay
        mHandler.postDelayed(loopRunnable, LOOP_INTERVAL);
    }

    // Runnable for the loop
    private final Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            if (mWebView != null) {
                // Reload the WebView to replay the video
                mWebView.reload();
                // Schedule the next loop
                mHandler.postDelayed(this, LOOP_INTERVAL);

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the loop Runnable when the activity is destroyed
        mHandler.removeCallbacks(loopRunnable);
    }
}
