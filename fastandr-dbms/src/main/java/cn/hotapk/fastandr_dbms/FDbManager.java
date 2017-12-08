package cn.hotapk.fastandr_dbms;

import android.content.Context;

import cn.hotapk.fhttpserver.FHttpManager;

/**
 * @author laijian
 * @version 2017/12/8
 * @Copyright (C)下午3:18 , www.hotapk.cn
 */
public class FDbManager {

    private Context context;

    private volatile static FDbManager dbManager;
    private FHttpManager fHttpManager;

    private FDbManager(Context context) {
        this.context = context;
    }

    public static FDbManager init(Context context) {
        if (dbManager == null) {
            synchronized (FDbManager.class) {
                if (dbManager == null) {
                    dbManager = new FDbManager(context);
                }
            }
        }
        return dbManager;
    }

    public static Context getAppContext() {
        if (dbManager != null) return dbManager.context.getApplicationContext();
        throw new NullPointerException("To initialize first");
    }

    /**
     * 获取http服务
     *
     * @param cls 根据所提供的类，获取所有带RequestMapping注解的方法
     */
    public FHttpManager getFHttpManager(Class... cls) {
        fHttpManager = FHttpManager.init(context, cls);
        return fHttpManager;
    }


}
