package com.activity;

/**
 * Created by Administrator on 2016/5/7.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


/**
 * Created by Administrator on 2016/5/7.
 */

public class WebViewActivity extends Activity {

    WebView webView;
    //网页的网址
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView)findViewById(R.id.webView);
        final Button back1 = (Button) findViewById(R.id.back1);
        Intent intent = getIntent();
        this.url = intent.getStringExtra("url");
        webView.loadUrl(url);
        webViewSettings(webView);
        webView.setWebViewClient(new WebViewClientDemo());
        back1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(WebViewActivity.this, BackActivity.class);
                startActivity(intent);
                WebViewActivity.this.finish();
            }
        });

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSettings(WebView webView2) {
        // TODO Auto-generated method stub
        WebSettings settings = webView.getSettings();
        //支持通过js打开新的窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
    }

    //在WebView中显示页面，而不是在默认的浏览器中显示页面
    private class WebViewClientDemo extends WebViewClient {

        @SuppressWarnings("unused")
        public boolean shouldOverideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
