package com.hoseo.happydorm;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_PERMISSION_PUSH = 1001;
    private long backBtnTime = 0;
    private ImageButton btn_notice;
    private ImageButton btn_menu;
    private ImageButton btn_goingout;
    private ImageButton btn_washer;
    private ImageView img_title;
    private Bitmap bitmap;
    private TextView VersionName_description;

    public String getVersionName(Context context){
        String version_name = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version_name = i.versionName + " gbnam";
        } catch(PackageManager.NameNotFoundException e) { }
        String version = version_name; // version을 여기서 할당
        return version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VersionName_description = (TextView) findViewById(R.id.VersionName_description);
        VersionName_description.setText(getVersionName(this));

        //Notification permission (알림 권한 요청)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_PERMISSION_PUSH);
        }

        //Title img function (대표 이미지 기능)
        img_title = findViewById(R.id.img_title);
        Thread uThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://gbnam.dothome.co.kr/happydorm/img_title.png");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        uThread.start();
        try {
            uThread.join();
            img_title.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Notice function (공지사항 기능)
        btn_notice = findViewById(R.id.btn_notice);
        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.checkNetworkState(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않아요\n연결 상태를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                }
            }
        });

        //Menu function (식단표 기능)
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.checkNetworkState(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않아요\n연결 상태를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                }
            }
        });

        //Goingout function (외출/외박신청 기능)
        btn_goingout = findViewById(R.id.btn_goingout);
        btn_goingout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.checkNetworkState(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않아요\n연결 상태를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, GoingOutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                }
            }
        });

        //Washer function (세탁실 결제 기능)
        btn_washer = findViewById(R.id.btn_washer);
        btn_washer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.fingerverse.metapoint", "com.fingerverse.metapoint.ui.activity.GateActivity"));
                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                } else {
                    Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fingerverse.metapoint"));
                    startActivity(intentMarket);
                    overridePendingTransition(R.anim.from_right_enter, R.anim.none);
                    Toast.makeText(getApplicationContext(), "먼저 메타클럽 앱을 설치 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Network connect detect (네트워크 접속 여부)
    public static boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities actNetwork = connectivityManager.getNetworkCapabilities(network);
        if (actNetwork == null) {
            return false;
        }

        if (actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true;
        } else if (actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true;
        } else if (actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true;
        } else {
            return false;
        }

    }

    //Notification permission (알림 권한 요청)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PERMISSION_PUSH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                Toast.makeText(this, "기숙사 알림을 받기 위해 알림 권한을 허용해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Press twice to go back (두 번 눌러 뒤로가기)
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime){
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한 번 더 누르면 앱을 끌 수 있어요", Toast.LENGTH_SHORT).show();
        }

    }
}