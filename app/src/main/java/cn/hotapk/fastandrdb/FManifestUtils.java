package cn.hotapk.fastandrdb;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import cn.hotapk.fastandr_dbms.FDbManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laijian
 * @version 2017/10/24
 * @Copyright (C)上午10:46 , www.hotapk.cn
 * 获取AndroidManifest.xml的部分数据
 */
public class FManifestUtils {

    /**
     * 获取manifest有注册的权限
     *
     * @return
     */
    public static String[] getRegPermission() {
        PackageInfo appInfo = null;
        List<String> perls = new ArrayList<>();

        try {
            appInfo = FDbManager.getAppContext().getPackageManager()
                    .getPackageInfo(FDbManager.getAppContext().getPackageName(),
                            PackageManager.GET_PERMISSIONS);
            String[] perms = appInfo.requestedPermissions;
            for (String perm : perms) {
                perls.add(perm);
            }
            return perls.toArray(new String[perls.size()]);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
