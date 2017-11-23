package com.pcitech.fastandr_dbms.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author laijian
 * @version 2017/9/12
 * @Copyright (C)上午11:18 , www.hotapk.cn
 * assets raw 相关操作类
 */
public final class FAssetsUtils {
    private FAssetsUtils() {

    }


    /**
     * 读取assets文件内容
     *
     * @param assetsName
     * @return
     */
    public static String getAssetsToString(String assetsName) {
        try {
            return FFileUtils.readInp(FDbUtils.getAppContext().getAssets().open(assetsName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
