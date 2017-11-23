package com.pcitech.fastandrdb;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView iptv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iptv = (TextView) findViewById(R.id.ip_tv);

        FPermissionUtils.requestPermissions(this, 200, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, new FPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                iptv.setText(FNetworkUtils.getIPAddress(true));
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {

            }

            @Override
            public void manifestUnPermission(String[] unpermission) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

    }
}
