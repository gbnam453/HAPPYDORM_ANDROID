package com.hoseo.happydorm;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GoingOutActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private TextView description;
    private WebView happydorm_webview;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd (E)");
    private String url = "http://gbnam453.dothome.co.kr/happydorm/goingout_redirect.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_out);

        // Set date (날짜 설정)
        description = findViewById(R.id.description);
        description.setText(getTime());

        // WebView load (웹 뷰 표시)
        happydorm_webview = findViewById(R.id.happydorm_webview);
        happydorm_webview.setWebViewClient(new WebViewClientClass());
        happydorm_webview.getSettings().setJavaScriptEnabled(true);
        happydorm_webview.setVisibility(View.GONE);
        happydorm_webview.loadUrl(url);

        // Go to main (메인화면으로 이동)
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });
    }

    // Load date (날짜 불러오기)
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    // WebViewClient to handle URL redirection
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals("https://happydorm.hoseo.ac.kr/")) {
                view.loadUrl("https://happydorm.hoseo.ac.kr/dormitory/view");
                return true; // URL을 가로채서 변경
            }
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

    // Press to go back (한 번 눌러 뒤로가기)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.none, R.anim.to_right_exit);
        }
        return super.onKeyDown(keyCode, event);
    }
}