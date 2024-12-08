package com.hoseo.happydorm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class DevActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private TextView etToken;
    private TextView VersionCode_description;
    private TextView VersionName_description;
    public String getVersionCode(Context context){
        String version_code = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version_code = "" + i.versionCode;
        } catch(PackageManager.NameNotFoundException e) { }
        String version = version_code; // version을 여기서 할당
        return version;
    }

    public String getVersionName(Context context){
        String version_name = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version_name = "V" + i.versionName;
        } catch(PackageManager.NameNotFoundException e) { }
        String version = version_name; // version을 여기서 할당
        return version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        //Entered toast (입장 토스트 메세지)
        Toast.makeText(this, "Developer option entered", Toast.LENGTH_SHORT).show();

        //Set Version (버전 가져오기)
        VersionCode_description = (TextView) findViewById(R.id.VersionCode_description);
        VersionCode_description.setText(getVersionCode(this));

        VersionName_description = (TextView) findViewById(R.id.VersionName_description);
        VersionName_description.setText(getVersionName(this));

        //Firebase cloud messaging function (파이어베이스 기능)
        etToken = findViewById(R.id.etToken);
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        System.out.println("Fetching FCM registration token failed");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    System.out.println(token);

                    etToken.setText(token);
                }
        });

        //텍스트뷰 길게 클릭 이벤트
        etToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etToken.getText().toString();
                createClipData(text);
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
    }

    public void createClipData(String message){
        //클립보드 복사기능
        ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().
                getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("message", message);

        //클립보드에 배치
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Copyed", Toast.LENGTH_SHORT).show();
    }

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