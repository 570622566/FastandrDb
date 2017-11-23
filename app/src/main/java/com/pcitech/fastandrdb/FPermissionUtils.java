package com.pcitech.fastandrdb;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author laijian
 * @version 2017/9/20
 * @Copyright (C)下午2:49 , www.hotapk.cn
 * Permission
 */
public class FPermissionUtils {
    private static int mRequestCode = -1;

    private static OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener {

        void onPermissionGranted();

        void onPermissionDenied(String[] deniedPermissions);

        void manifestUnPermission(String[] unpermission);
    }

    public abstract static class RationaleHandler {
        private Context context;
        private int requestCode;
        private String[] permissions;

        protected abstract void showRationale();

        void showRationale(Context context, int requestCode, String[] permissions) {
            this.context = context;
            this.requestCode = requestCode;
            this.permissions = permissions;
            showRationale();
        }

        /**
         * 重新请求权限
         */
        public void requestPermissionsAgain() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) context).requestPermissions(permissions, requestCode);
            }
        }
    }

    /**
     * 请求权限
     *
     * @param context
     * @param requestCode
     * @param permissions
     * @param listener
     */
    public static void requestPermissions(Context context, int requestCode
            , String[] permissions, OnPermissionListener listener) {
        requestPermissions(context, requestCode, permissions, listener, null);
    }

    /**
     * 请求权限
     *
     * @param context
     * @param requestCode
     * @param permissions
     * @param listener
     * @param handler
     */
    public static void requestPermissions(Context context, int requestCode
            , String[] permissions, OnPermissionListener listener, RationaleHandler handler) {

        if (context instanceof Activity) {
            mRequestCode = requestCode;
            mOnPermissionListener = listener;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onPermissionGranted();
                }
            } else {
                String[] deniedPermissions = getDeniedPermissions(context, permissions);
                if (deniedPermissions.length > 0) {
                    boolean rationale = shouldShowRequestPermissionRationale(context, deniedPermissions);
                    if (rationale && handler != null) {
                        handler.showRationale(context, requestCode, deniedPermissions);
                    } else {
                        ((Activity) context).requestPermissions(deniedPermissions, requestCode);
                    }
                } else {
                    if (mOnPermissionListener != null)
                        mOnPermissionListener.onPermissionGranted();
                }
            }
        } else {
            throw new RuntimeException("Context must be an Activity");
        }
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions, int[]
            grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                String[] manifestPer = checkManifestPermission(permissions);
                String[] deniedPermissions = getDeniedPermissions(context, manifestPer);
                if (deniedPermissions.length > 0) {
                    mOnPermissionListener.onPermissionDenied(deniedPermissions);
                } else {
                    mOnPermissionListener.onPermissionGranted();
                }
                mOnPermissionListener.manifestUnPermission(manifestUnPermission(manifestPer, permissions));
            }
        }
    }

    /**
     * manifest 没注册该权限
     *
     * @param manifestPer
     * @param permissions
     * @return
     */
    private static String[] manifestUnPermission(String[] manifestPer, String[] permissions) {

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < permissions.length; i++) {
            map.put(permissions[i], i);
        }

        for (int i = 0; i < manifestPer.length; i++) {
            if (map.containsKey(manifestPer[i])) {
                map.remove(manifestPer[i]);
            }
        }
        Set<String> key = map.keySet();
        List<String> unperls = new ArrayList<>();
        for (String unper : key) {
            unperls.add(unper);
        }
        return unperls.toArray(new String[unperls.size()]);
    }

    /**
     * 检测添加的权限是否在manifest中注册如果没有就删掉该权限
     */
    private static String[] checkManifestPermission(final String[] permissions) {
        String[] manifestpers = FManifestUtils.getRegPermission();
        List<String> manigestls = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0; j < manifestpers.length; j++) {
                if (permissions[i].equals(manifestpers[j])) {
                    manigestls.add(permissions[i]);
                }
            }
        }
        return manigestls.toArray(new String[manigestls.size()]);
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private static String[] getDeniedPermissions(final Context context, final String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否彻底拒绝了某项权限
     */
    public static boolean hasAlwaysDeniedPermission(final Context context, final String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!shouldShowRequestPermissionRationale(context, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有权限需要说明提示
     */
    private static boolean shouldShowRequestPermissionRationale(final Context context, final String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
            if (rationale) return true;
        }
        return false;
    }


}
