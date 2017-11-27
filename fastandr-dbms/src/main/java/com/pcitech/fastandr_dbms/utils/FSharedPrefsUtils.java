package com.pcitech.fastandr_dbms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author laijian
 * @version 2017/9/12
 * @Copyright (C)下午2:32 , www.hotapk.cn
 * SharedPreferences 工具类
 */
public final class FSharedPrefsUtils {
    private FSharedPrefsUtils() {

    }


    public static SharedPreferences getSharedPreferences(String name) {
        return FDbUtils.getAppContext().getSharedPreferences(name, Activity.MODE_PRIVATE);
    }


    public static boolean putInt(String name, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean putBoolean(String name, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean putFloat(String name, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static boolean putString(String name, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean putLong(String name, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static boolean putStringSet(String name, String key, Set<String> value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }


    /**
     * 清除数据
     *
     * @param name
     * @return
     */
    public static boolean clear(String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit().clear();
        return editor.commit();
    }

    /**
     * 清除key中的数据
     *
     * @param name
     * @param key
     * @return
     */
    public static boolean clearByKey(String name, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit().remove(key);
        return editor.commit();
    }


    /**
     * 获取SharedPreference xml数据
     *
     * @return
     */
    public static List<String> getSharedPreferenceXMl() {

        ArrayList<String> tags = new ArrayList<>();
        String rootPath = getSharePrefsRootPath();
        File root = new File(rootPath);
        if (root.exists()) {
            for (File file : root.listFiles()) {
                String fileName = file.getName();
                if (fileName.endsWith(".xml")) {
                    tags.add(fileName);
                }
            }
        }
        Collections.sort(tags);
        return tags;
    }

    public static File getSharedPreferenceFile(String name) {
        File file = new File(getSharePrefsRootPath(), name.endsWith(".xml") ? name : name + ".xml");
        return file;
    }

    public static String getSharedPreferencePath(String name) {
        return getSharedPreferenceFile(name).getAbsolutePath();
    }

    public static String getSharePrefsRootPath() {
        return FDbUtils.getAppContext().getApplicationInfo().dataDir + "/shared_prefs";

    }

    /**
     * 获取pref的数据
     *
     * @param prefname
     * @return
     */
    public static List<Map<String, Object>> getPrefData(String prefname) {
        SharedPreferences preferences = FDbUtils.getAppContext().getSharedPreferences(prefname.replace(".xml", ""), Context.MODE_PRIVATE);
        Map<String, ?> entries = preferences.getAll();
        List<Map<String, Object>> datas = new ArrayList<>();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("Key", entry.getKey());
            map.put("Value", entry.getValue().toString());
            if (entry.getValue() instanceof Integer) {
                map.put("dataType", FConstant.INTEGER);
            } else if (entry.getValue() instanceof Long) {
                map.put("dataType", FConstant.LONG);
            } else if (entry.getValue() instanceof Float) {
                map.put("dataType", FConstant.FLOAT);
            } else if (entry.getValue() instanceof Boolean) {
                map.put("dataType", FConstant.BOOLEAN);
            } else if (entry.getValue() instanceof Set) {
                map.put("dataType", FConstant.STRING_SET);
            } else {
                map.put("dataType", FConstant.TEXT);
            }
            datas.add(map);
        }
        return datas;
    }

    /**
     * 添加数据
     *
     * @param prefName
     * @param map
     * @return
     */
    public static boolean addPrefData(String prefName, Map<String, String> map) {
        String name = prefName.replace(".xml", "");
        String key = map.get("Key");
        String value = map.get("Value");
        String dataType = map.get("dataType");
        try {
            switch (dataType) {
                case FConstant.INTEGER:
                    putInt(name, key, Integer.parseInt(value));
                    break;
                case FConstant.LONG:
                    putLong(name, key, Long.parseLong(value));
                    break;
                case FConstant.FLOAT:
                    putFloat(name, key, Float.parseFloat(value));
                    break;
                case FConstant.BOOLEAN:
                    putBoolean(name, key, Boolean.parseBoolean(value));
                    break;
                case FConstant.STRING_SET:
                    JSONArray jsonArray = new JSONArray(value);
                    Set<String> stringSet = new HashSet<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        stringSet.add(jsonArray.getString(i));
                    }
                    putStringSet(name, key, stringSet);
                    break;
                default:
                    putString(name, key, value);
                    break;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
