package cn.hotapk.fastandrdb;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.hotapk.fastandr_dbms.FDbController;
import cn.hotapk.fastandr_dbms.FDbManager;
import cn.hotapk.fastandr_dbms.utils.FSharedPrefsUtils;
import cn.hotapk.fastandrdb.bean.StudenBean;
import cn.hotapk.fastandrdb.bean.UserBean;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import java.util.HashSet;
import java.util.Set;

import cn.hotapk.fhttpserver.FHttpManager;

public class MainActivity extends AppCompatActivity {

    private TextView iptv;

    private FHttpManager fHttpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iptv = (TextView) findViewById(R.id.ip_tv);
        fHttpManager = FDbManager.init(this).getFHttpManager(FDbController.class);
        fHttpManager.setPort(9999);//默认端口8080
        FPermissionUtils.requestPermissions(this, 200, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, new FPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                iptv.setText("内网打开：http://" + FNetworkUtils.getIPAddress(true) + ":" + fHttpManager.getPort());
                fHttpManager.startServer();
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {

            }

            @Override
            public void manifestUnPermission(String[] unpermission) {

            }
        });
        FSharedPrefsUtils.putString("fastdb", "test1", "dddd--");
        Set<String> strset = new HashSet<>();
        strset.add("ddddddd");
        strset.add("ddddddddddddddd");
        FSharedPrefsUtils.putStringSet("fastdb", "test2", strset);

        LitePalDB litePalDB = new LitePalDB("demo", 6);
        litePalDB.addClassName(UserBean.class.getName());
        LitePal.use(litePalDB);
        UserBean userBean = new UserBean("pantom2", "12345678", 10, "13800138000", false, 20d);
        userBean.save();
        UserBean userBean2 = new UserBean("admin2", "admin", 10, "13800138000", true, 300d);
        userBean2.save();

        LitePalDB litePalDB2 = new LitePalDB("demo2", 1);
        litePalDB2.addClassName(StudenBean.class.getName());
        LitePal.use(litePalDB2);
        StudenBean studenBean = new StudenBean("张三", "三班", "一年级");
        studenBean.save();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

    }
}
