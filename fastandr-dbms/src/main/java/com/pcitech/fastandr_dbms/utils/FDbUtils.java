package com.pcitech.fastandr_dbms.utils;

import android.app.Application;

/**
 * @author laijian
 * @version 2017/9/18
 * @Copyright (C)下午4:56 , www.hotapk.cn
 */
public class FDbUtils {

    private Application application;

    private volatile static FDbUtils fUtils;

    private FDbUtils(Application application) {
        this.application = application;
    }

    public static void init(Application application) {
        if (fUtils == null) {
            synchronized (FDbUtils.class) {
                if (fUtils == null) {
                    fUtils = new FDbUtils(application);
                }
            }
        }
    }

    public static Application getAppContext() {
        if (fUtils != null) return fUtils.application;
        throw new NullPointerException("To initialize first");
    }

}
