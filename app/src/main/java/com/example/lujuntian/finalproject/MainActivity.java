package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;

import com.example.lujuntian.finalproject.blur.BlurBehind;
import com.example.lujuntian.finalproject.blur.OnBlurCompleteListener;

public class MainActivity extends Activity {

    WebView webView;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        searchFunction();
        webFunction();
    }

    /**
     * 注册widget组建
     */
    public void init(){
        searchView = (SearchView)findViewById(R.id.search_box);
        webView = (WebView)findViewById(R.id.mainweb);
    }

    /**
     * searchView功能
     */
    public void searchFunction(){
        searchView.setIconifiedByDefault(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(MainActivity.this, Search.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);          //背景模糊特效~
                        finish();
                    }
                });
            }
        });
    }

    /**
     * WebView功能
     */
    public void webFunction(){
        webView.loadUrl("https://m.taobao.com");        //设置网页
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键时的操作
                        webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // Set progress bar during loading
            public void onProgressChanged(WebView view, int progress) {
                MainActivity.this.setProgress(progress * 100);
            }
        });
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);     // Warning! You can have XSS vulnerabilities!
        websettings.setBuiltInZoomControls(true);
    }
}
