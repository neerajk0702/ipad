package com.apitechnosoft.ipad.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apitechnosoft.ipad.R;

public class DocOpenActivity extends AppCompatActivity {
    private WebView webView;
    String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_open);
        fileUrl = getIntent().getStringExtra("FileUrl");
        webView = findViewById(R.id.web);

        // Enable Javascript

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        webView.getSettings().setAllowFileAccess(true);

        /* webView.setWebViewClient(new AppWebViewClients());
         */
        //webView.loadData( doc , "text/html",  "UTF-8");
        if (fileUrl != null) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + fileUrl);
        }
    }

    public class AppWebViewClients extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //view.loadUrl(url);
            view.loadUrl("https://docs.google.com/gview?embedded=true&url="
                    + fileUrl);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }
}
