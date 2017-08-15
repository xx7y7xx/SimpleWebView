package com.example.chenyang.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

public class MainActivity extends Activity {
    private WebView myWebView;
    private String TAG = "MainActivity";
    // in app/src/main/assets folder
    private String LOCAL_FILE = "https://acc.yonyoucloud.com/m/shouye";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the web view in our layout xml
        myWebView = (WebView) findViewById(R.id.webview);

        // Settings
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAllowUniversalAccessFromFileURLs(true);

        // Add JS interface to allow calls from webview to Android
        // code. See below for WebAppInterface class implementation
//        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Set a web view client and a chrome client
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Need to accept permissions to use the camera and audio
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        // Make sure the request is coming from our file
                        // Warning: This check may fail for local files
                        if(request.getOrigin().toString().equals(LOCAL_FILE)) {
                            request.grant(request.getResources());
                        }
                        else {
                            request.deny();
                        }
                    }
                });
            }
        });
        // Load the local HTML file into the webview
        myWebView.loadUrl(LOCAL_FILE);
    }

    // Interface b/w JS and Android code
    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        // This function can be called in our JS script now
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
        }
    }
}