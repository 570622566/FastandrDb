package com.pcitech.fastandrdb;

import android.app.Application;

import org.litepal.LitePal;

/**
 * @author laijian
 * @version 2017/11/23
 * @Copyright (C)上午11:59 , www.hotapk.cn
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
