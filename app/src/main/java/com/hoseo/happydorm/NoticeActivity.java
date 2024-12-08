package com.hoseo.happydorm;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {

    private Button btn_hidden;
    private ImageButton btn_home;
    private WebView happydorm_webview;
    private String url = "http://gbnam.dothome.co.kr/happydorm/notice.html";
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        //Developer option start (개발자 모드 진입)
        btn_hidden = (Button) findViewById(R.id.btn_hidden);
        btn_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 9){
                    count++;
                } else {
                    Intent intent = new Intent(NoticeActivity.this, DevActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                    count = 0;
                }
            }
        });

        //Go to main (메인화면으로 이동)
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });

        //Webview load (웹 뷰 표시)
        happydorm_webview = findViewById(R.id.happydorm_webview);
        happydorm_webview.setWebViewClient(new WebViewClientClass());
        happydorm_webview.getSettings().setJavaScriptEnabled(true);
        happydorm_webview.setVisibility(View.GONE);
        happydorm_webview.loadUrl(url);
    }

    //Webview load (웹 뷰 표시)
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // 웹 페이지가 완전히 로드된 후 웹뷰를 표시
            happydorm_webview.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    //Press to go back (한 번 눌러 뒤로가기)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (happydorm_webview.canGoBack()) {
                String currentUrl = happydorm_webview.getUrl();
                if (currentUrl != null && currentUrl.equals("https://happydorm.hoseo.ac.kr/board/notice/list")) {
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.to_right_exit);
                } else {
                    happydorm_webview.goBack();
                }
            } else {
                finish();
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}