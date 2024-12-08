package com.hoseo.happydorm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {
    private ImageButton btn_home;
    private TextView description;
    private String URL = "https://happydorm.hoseo.ac.kr";
    final Bundle bundle = new Bundle();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd (E)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Set date (날짜 설정)
        description = (TextView) findViewById(R.id.description);
        description.setText(getTime());

        //Parse img (이미지 파싱)
        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(URL).get();
                    Elements elements = doc.select(".col-md-4 div.content.notice-slide a img");
                    String src = elements.attr("src");
                    bundle.putString("message", URL + src);
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //Go to main (메인화면으로 이동)
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });
    }

    //Load date (날짜 불러오기)
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    //Show img (이미지 표시)
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message src) {
            // 액티비티가 파괴되지 않았는지 확인
            if (isDestroyed() || isFinishing()) {
                return;
            }

            Bundle bundle = src.getData();
            String Img_URL = bundle.getString("message");

            PhotoView photoView = findViewById(R.id.photo_view);

            // Glide를 사용하여 이미지를 로드
            Glide.with(MenuActivity.this)
                    .load(Img_URL)
                    .fitCenter()
                    .into(photoView);
        }
    };


    //Press to go back (한 번 눌러 뒤로가기)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.none, R.anim.to_right_exit);
        }
        return super.onKeyDown(keyCode, event);
    }
}