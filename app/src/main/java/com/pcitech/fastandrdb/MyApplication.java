package com.pcitech.fastandrdb;

import android.app.Application;

import com.pcitech.fastandr_dbms.FSqlNetServer;
import com.pcitech.fastandr_dbms.utils.FDbUtils;

import java.io.IOException;

/**
 * @author laijian
 * @version 2017/11/23
 * @Copyright (C)上午11:59 , www.hotapk.cn
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FDbUtils.init(this);
        try {
            new FSqlNetServer(8888).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
