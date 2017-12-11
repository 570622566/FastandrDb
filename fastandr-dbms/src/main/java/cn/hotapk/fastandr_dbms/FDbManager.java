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
        fHttpManager = FHttpManager.init(context, FDbController.class);
    }

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public static FDbManager init(Context context) {
        if (dbManager == null) {
            synchronized (FDbManager.class) {
                if (dbManager == null) {
                    dbManager = new FDbManager(context.getApplicationContext());
                }
            }
        }
        return dbManager;
    }

    public static Context getAppContext() {
        if (dbManager != null) return dbManager.context;
        throw new NullPointerException("To initialize first");
    }

    /**
     * 设置端口号
     *
     * @param port
     * @throws Exception
     */
    public void setPort(int port) throws Exception {
        fHttpManager.setPort(port);
        throw new Exception("please init FHttpManager");
    }

    /**
     * 启动服务
     */
    public void startServer() throws Exception {
        fHttpManager.startServer();
        throw new Exception("please init FHttpManager");
    }

    /**
     * 关闭服务
     *
     * @throws Exception
     */
    public void stopServer() throws Exception {
        fHttpManager.stopServer();
        throw new Exception("please init FHttpManager");
    }

    /**
     * 获取http服务
     */
    public FHttpManager getFHttpManager() {
        return fHttpManager;
    }


}
