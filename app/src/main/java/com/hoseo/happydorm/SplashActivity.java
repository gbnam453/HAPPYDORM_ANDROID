package com.hoseo.happydorm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.core.splashscreen.SplashScreen;


public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this); // 최신 API 사용
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // ✅ 스플래쉬 레이아웃 적용

        // ✅ 상단바 색상 변경
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.splash_background));

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000); // 5초 후 메인으로 이동
    }
}