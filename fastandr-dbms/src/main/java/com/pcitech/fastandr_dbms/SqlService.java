package com.pcitech.fastandr_dbms;

import com.google.gson.Gson;
import com.pcitech.fastandr_dbms.bean.FieldInfor;
import com.pcitech.fastandr_dbms.bean.ResponseData;
import com.pcitech.fastandr_dbms.utils.FAssetsUtils;
import com.pcitech.fastandr_dbms.utils.FConstant;
import com.pcitech.fastandr_dbms.utils.FDatabaseUtils;
import com.pcitech.fastandr_dbms.utils.FDbUtils;
import com.pcitech.fastandr_dbms.utils.FFileUtils;
import com.pcitech.fastandr_dbms.utils.FSharedPrefsUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author laijian
 * @version 2017/11/13
 * @Copyright (C)下午2:39 , www.hotapk.cn
 */
public class SqlService {

    /**
     * 获取首页静态资源 css，js等
     *
     * @param filename
     * @return
     */
    public static String index(String filename) {
        if (filename.contains("/")) {
            filename = filename.split("/")[1];
        }
        return FAssetsUtils.getAssetsToString(filename);
    }


    /**
     * 获取数据库和sharedprefs列表
     *
     * @return
     */
    public static String getDbList() {
        ResponseData responseData = new ResponseData();
        try {
            List<String> templs = Arrays.asList(FDbUtils.getAppContext().databaseList());
            List<String> rows = new ArrayList<>();
            for (int i = 0; i < templs.size(); i++) {
                if (!templs.get(i).contains("-journal") && !templs.get(i).equals("undefined") && !templs.get(i).contains(".xml") && !templs.get(i).contains("shared_prefs")) {
                    rows.add(templs.get(i));
                }
            }
            rows.add(FConstant.SHAREDPREFS_XML);
            responseData.setRows(rows);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return new Gson().toJson(responseData);
    }

    /**
     * 获取table列表
     *
     * @param databaseName 数据库名称
     * @return
     */
    public static String getTableList(String databaseName) {
        ResponseData responseData = new ResponseData();
        try {
            List<String> rows = new ArrayList<>();
            if (databaseName.equals(FConstant.SHAREDPREFS_XML)) {
                rows.addAll(FSharedPrefsUtils.getSharedPreferenceXMl());
            } else {
                List<String> allTableNames = FDatabaseUtils.getAllTableName(databaseName);
                for (int i = 0; i < allTableNames.size(); i++) {
                    if (!allTableNames.get(i).equals("android_metadata") && !allTableNames.get(i).equals("table_schema") && !allTableNames.get(i).equals("sqlite_sequence")) {
                        rows.add(allTableNames.get(i));
                    }
                }
            }
            responseData.setRows(rows);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());

        }
        return new Gson().toJson(responseData);
    }

    /**
     * 获取table数据
     *
     * @param databaseName
     * @param tableName
     * @return
     */
    public static String getTableDataList(String databaseName, String tableName) {
        ResponseData responseData = new ResponseData();

        try {
            if (databaseName.equals(FConstant.SHAREDPREFS_XML)) {
                List<FieldInfor> allTablefield = new ArrayList<>();
                allTablefield.add(new FieldInfor("Key", "", false));
                allTablefield.add(new FieldInfor("Value", "", false));
                allTablefield.add(new FieldInfor("dataType", "", false));
                responseData.setAllTablefield(allTablefield);
                responseData.setDatas(FSharedPrefsUtils.getPrefData(tableName));
            } else {
                responseData = FDatabaseUtils.getAllTableData(FDatabaseUtils.getDataBase(databaseName), tableName);
            }
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return new Gson().toJson(responseData);
    }

    /**
     * 添加数据
     *
     * @param parms
     * @return
     */
    public static String addData(Map<String, String> parms) {
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            String dbname = parms.get("fdbname");
            String tablename = parms.get("ftablename");
            parms.remove("fdbname");
            parms.remove("ftablename");
            if (dbname.equals(FConstant.SHAREDPREFS_XML)) {
                result = FSharedPrefsUtils.addPrefData(tablename, parms);
            } else {
                result = FDatabaseUtils.addData(dbname, tablename, parms);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return new Gson().toJson(responseData);
    }

    /**
     * 修改数据
     *
     * @param parms
     * @return
     */
    public static String editData(Map<String, String> parms) {
        ResponseData responseData = new ResponseData();

        boolean result;
        try {
            String dbname = parms.get("fdbname");
            String tablename = parms.get("ftablename");
            parms.remove("fdbname");
            parms.remove("ftablename");
            if (dbname.equals(FConstant.SHAREDPREFS_XML)) {
                result = FSharedPrefsUtils.addPrefData(tablename, parms);
            } else {
                result = FDatabaseUtils.editData(dbname, tablename, parms);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);
        return new Gson().toJson(responseData);
    }

    /**
     * 删除数据
     *
     * @param parms
     * @return
     */
    public static String delData(Map<String, String> parms) {
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            String dbname = parms.get("fdbname");
            String tablename = parms.get("ftablename");
            parms.remove("fdbname");
            parms.remove("ftablename");
            if (dbname.equals(FConstant.SHAREDPREFS_XML)) {
                if (FSharedPrefsUtils.clearByKey(tablename.replace(".xml", ""), parms.get("Key"))) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = FDatabaseUtils.delData(dbname, tablename, parms);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);
        return new Gson().toJson(responseData);

    }

    /**
     * 查询数据
     *
     * @param dbname
     * @return
     */
    public static String queryDb(String dbname) {
//        String dbname = parms.get("fdbname");
//        String tablename = parms.get("ftablename");
//        parms.remove("fdbname");
//        parms.remove("ftablename");
//        String result = FDatabaseUtils.delData(dbname, tablename, parms);
        return "";
    }

    /**
     * 数据库下载
     *
     * @param databaseName
     * @return
     */
    public static InputStream downloaddb(String databaseName) {
        if (databaseName.contains(FConstant.SHAREDPREFS_XML)) {
            return FFileUtils.file2Inp(FSharedPrefsUtils.getSharedPreferencePath(databaseName.replace(FConstant.SHAREDPREFS_XML, "")));
        }

        return FFileUtils.file2Inp(FDbUtils.getAppContext().getDatabasePath(databaseName).getAbsolutePath());
    }
}
