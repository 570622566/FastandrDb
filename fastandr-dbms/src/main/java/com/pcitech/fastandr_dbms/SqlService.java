package com.pcitech.fastandr_dbms;

import com.google.gson.Gson;
import com.pcitech.fastandr_dbms.bean.SqlFieldInfor;
import com.pcitech.fastandr_dbms.bean.SqlResponse;
import com.pcitech.fastandr_dbms.utils.FAssetsUtils;
import com.pcitech.fastandr_dbms.utils.FDatabaseUtils;
import com.pcitech.fastandr_dbms.utils.FDbUtils;
import com.pcitech.fastandr_dbms.utils.FFileUtils;
import com.pcitech.fastandr_dbms.utils.FSharedPreferencesUtils;

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
    public static final String SHAREDPREFS_XML = "SHAREDPREFS_XML";

    public static String index(String filename) {
        if (filename.contains("/")) {
            filename = filename.split("/")[1];
        }
        String sqlindex = FAssetsUtils.getAssetsToString(filename);
        return sqlindex;
    }

    public static String getDbList() {
        Gson gson = new Gson();
        SqlResponse sqlResponse = new SqlResponse();
        List<String> templs = Arrays.asList(FDbUtils.getAppContext().databaseList());
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < templs.size(); i++) {
            if (!templs.get(i).contains("-journal") && !templs.get(i).equals("undefined") && !templs.get(i).contains(".xml") && !templs.get(i).contains("shared_prefs")) {
                rows.add(templs.get(i));
            }
        }
        rows.add(SHAREDPREFS_XML);
        sqlResponse.setRows(rows);
        String getDbList = gson.toJson(sqlResponse);
        return getDbList;
    }

    public static String getTableList(String database) {
        SqlResponse sqlResponse = new SqlResponse();
        List<String> rows = new ArrayList<>();
        if (database.equals(SHAREDPREFS_XML)) {
            rows.addAll(FSharedPreferencesUtils.getSharedPreferenceXMl());
        } else {
            List<String> allTableNames = FDatabaseUtils.getAllTableName(database);
            for (int i = 0; i < allTableNames.size(); i++) {
                if (!allTableNames.get(i).equals("android_metadata") && !allTableNames.get(i).equals("table_schema") && !allTableNames.get(i).equals("sqlite_sequence")) {
                    rows.add(allTableNames.get(i));
                }
            }
            sqlResponse.setDbVersion(FDatabaseUtils.getDataBaseVersion(FDatabaseUtils.getDataBase(database)));
        }
        sqlResponse.setRows(rows);
        return new Gson().toJson(sqlResponse);
    }

    public static String getTableDataList(String database, String tableName) {
        SqlResponse sqlResponse = null;
        if (database.equals(SHAREDPREFS_XML)) {
            sqlResponse = new SqlResponse();
            List<SqlFieldInfor> allTablefield = new ArrayList<>();
            allTablefield.add(new SqlFieldInfor("Key", "", false));
            allTablefield.add(new SqlFieldInfor("Value", "", false));
            sqlResponse.setAllTablefield(allTablefield);
            sqlResponse.setDatas(FSharedPreferencesUtils.getPrefData(tableName));

        } else {
            sqlResponse = FDatabaseUtils.getAllTableData(FDatabaseUtils.getDataBase(database), tableName);
        }
        sqlResponse.setSuccessful(true);
        return new Gson().toJson(sqlResponse);
    }

    public static String addData(Map<String, String> parms) {
        String dbname = parms.get("fdbname");
        String tablename = parms.get("ftablename");
        parms.remove("fdbname");
        parms.remove("ftablename");
        String result = FDatabaseUtils.addData(dbname, tablename, parms);
        return result;
    }

    public static String editData(Map<String, String> parms) {
        String dbname = parms.get("fdbname");
        String tablename = parms.get("ftablename");
        parms.remove("fdbname");
        parms.remove("ftablename");
        String result = FDatabaseUtils.editData(dbname, tablename, parms);
        return result;
    }

    public static String delData(Map<String, String> parms) {
        String dbname = parms.get("fdbname");
        String tablename = parms.get("ftablename");
        parms.remove("fdbname");
        parms.remove("ftablename");
        String result = FDatabaseUtils.delData(dbname, tablename, parms);
        return result;
    }

    public static String queryDb(String dbname) {
//        String dbname = parms.get("fdbname");
//        String tablename = parms.get("ftablename");
//        parms.remove("fdbname");
//        parms.remove("ftablename");
//        String result = FDatabaseUtils.delData(dbname, tablename, parms);
        return "";
    }

    public static InputStream downloaddb(String dbname) {
        return FFileUtils.file2Inp(FDbUtils.getAppContext().getDatabasePath(dbname).getAbsolutePath());
    }
}
