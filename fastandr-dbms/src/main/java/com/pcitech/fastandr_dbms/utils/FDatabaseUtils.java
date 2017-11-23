package com.pcitech.fastandr_dbms.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pcitech.fastandr_dbms.bean.SqlFieldInfor;
import com.pcitech.fastandr_dbms.bean.SqlResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author laijian
 * @version 2017/9/19
 * @Copyright (C)下午2:54 , www.hotapk.cn
 * 数据库导出
 */
public final class FDatabaseUtils {


    private static final int MAX_BLOB_LENGTH = 512;

    private static final String UNKNOWN_BLOB_LABEL = "{blob}";

    private FDatabaseUtils() {
    }

    /**
     * 获取数据库版本
     *
     * @param database
     * @return
     */
    public static int getDataBaseVersion(SQLiteDatabase database) {
        return database.getVersion();
    }

    /**
     * 根据数据库名称 获取 SQLiteDatabase
     *
     * @param databaseName 数据库名称
     * @return
     */
    public static SQLiteDatabase getDataBase(String databaseName) {
        return SQLiteDatabase.openOrCreateDatabase(FDbUtils.getAppContext().getDatabasePath(databaseName), null);
    }

    /**
     * 根据文件路径获取SQLiteDatabase
     *
     * @param databasepath 数据库路径
     * @return
     */
    public static SQLiteDatabase getDataBase(File databasepath) {
        return SQLiteDatabase.openOrCreateDatabase(databasepath, null);
    }

    /**
     * 获取数据库所有表名
     *
     * @param databaseName 数据库文件名
     * @return
     */
    public static List<String> getAllTableName(String databaseName) {
        databaseName = databaseName.endsWith(".db") ? databaseName : databaseName + ".db";
        return getAllTableName(getDataBase(databaseName));
    }

    /**
     * 获取数据库所有表名
     *
     * @param database SQLiteDatabase
     * @return
     */
    public static List<String> getAllTableName(SQLiteDatabase database) {
        List<String> tabs = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tabs.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tabs;
    }

    /**
     * 获取数据库表的所有字段
     *
     * @param database
     * @param tableName
     * @return
     */
    public static List<SqlFieldInfor> getAllTablefield(SQLiteDatabase database, String tableName) {
        List<SqlFieldInfor> fields = new ArrayList<>();
        Cursor cursor = database.rawQuery("PRAGMA table_info([" + tableName + "])", null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    SqlFieldInfor sqlFieldInfor = new SqlFieldInfor();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        final String columnName = cursor.getColumnName(i);
                        switch (columnName) {
                            case "name":
                                sqlFieldInfor.setTitle(cursor.getString(i));
                                break;
                            case "type":
                                sqlFieldInfor.setType(cursor.getString(i));
                                break;
                            case "pk":
                                sqlFieldInfor.setPrimary(cursor.getInt(i) == 1);
                                break;
                            default:
                        }

                    }
                    fields.add(sqlFieldInfor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return fields;
    }

    /**
     * 获取所有表数据
     *
     * @param database
     * @param tableName
     */
    public static SqlResponse getAllTableData(SQLiteDatabase database, String tableName) {
        List<SqlFieldInfor> allTablefield = getAllTablefield(database, tableName);
        List<Map<String, Object>> datas = new ArrayList<>();
        SqlResponse sqlResponse = new SqlResponse();
        Cursor cursor = database.rawQuery("select * from " + tableName, null);
        getCursorData(cursor, allTablefield, datas);
        sqlResponse.setAllTablefield(allTablefield);
        sqlResponse.setDatas(datas);
        cursor.close();
        return sqlResponse;
    }

    /**
     * 获取表数据
     *
     * @param database
     * @param tableName
     * @param pageSize
     * @param index
     */
    public static SqlResponse getTableData(SQLiteDatabase database, String tableName, int pageSize, int index) {
        SqlResponse sqlResponse = new SqlResponse();

        List<SqlFieldInfor> allTablefield = getAllTablefield(database, tableName);
        List<Map<String, Object>> datas = new ArrayList<>();
        String sqlquery = "select * from " + tableName + " limit " + pageSize + " offset " + pageSize * (index - 1);
        Cursor cursor = database.rawQuery(sqlquery, null);
        getCursorData(cursor, allTablefield, datas);
        sqlResponse.setAllTablefield(allTablefield);
        sqlResponse.setDatas(datas);
        cursor.close();
        return sqlResponse;
    }

    private static void getCursorData(Cursor cursor, List<SqlFieldInfor> allTablefield, List<Map<String, Object>> datas) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < allTablefield.size(); i++) {
                    switch (allTablefield.get(i).getType()) {
                        case "blob":
                            map.put(allTablefield.get(i).getTitle(), blobToString(cursor.getBlob(i)));
                            break;
                        default:
                            map.put(allTablefield.get(i).getTitle(), cursor.getString(i));
                            break;
                    }
                }
                datas.add(map);
            } while (cursor.moveToNext());
        }

    }

    /**
     * 添加数据
     *
     * @param dbName    数据库名
     * @param tableName 数据库表
     * @param parms     key：value
     * @return
     */
    public static String addData(String dbName, String tableName, Map<String, String> parms) {
        SQLiteDatabase dataBase = null;

        try {
            dataBase = getDataBase(dbName);
            ContentValues cv = new ContentValues();
            putContentValues(cv, dataBase, tableName, parms);
            long res = dataBase.insert(tableName, null, cv);
            if (res == -1) {
                return "fail";
            }
            return "success";
        } catch (Exception e) {
            return "fail";
        } finally {
            if (dataBase != null) {
                dataBase.close();
            }
        }

    }

    /**
     * 修改数据
     *
     * @param dbName    数据库名
     * @param tableName 数据库表
     * @param parms     key：value
     * @return
     */
    public static String editData(String dbName, String tableName, Map<String, String> parms) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase dataBase = null;
        try {
            dataBase = getDataBase(dbName);
            String[] pk_table = putContentValues(cv, dataBase, tableName, parms);
            long res = dataBase.update(tableName, cv, pk_table[0] + " = ?", new String[]{pk_table[1]});
            if (res == -1) {
                return "fail";
            }
            return "success";
        } catch (Exception e) {
            return "fail";
        } finally {
            if (dataBase != null) {
                dataBase.close();
            }
        }

    }

    /**
     * 删除数据
     *
     * @param dbName    数据库名
     * @param tableName 数据库表
     * @param parms     key：value
     * @return
     */
    public static String delData(String dbName, String tableName, Map<String, String> parms) {
        SQLiteDatabase dataBase = null;
        try {
            dataBase = getDataBase(dbName);
            Set<String> keys = parms.keySet();
            String whereClause = "";
            String[] whereArgs = new String[1];
            for (String key : keys) {
                whereClause = key + " = ?";
                whereArgs[0] = parms.get(key);
            }
            long res = dataBase.delete(tableName, whereClause, whereArgs);
            if (res == -1) {
                return "fail";
            }
            return "success";
        } catch (Exception e) {
            return "fail";
        } finally {
            if (dataBase != null) {
                dataBase.close();
            }
        }

    }


    private static String[] putContentValues(ContentValues cv, SQLiteDatabase dataBase, String tableName, Map<String, String> parms) throws Exception {

        String[] pk_table = new String[2];
        List<SqlFieldInfor> allTablefield = getAllTablefield(dataBase, tableName);
        Map<String, SqlFieldInfor> fieldmap = new HashMap<>();
        for (int i = 0; i < allTablefield.size(); i++) {
            fieldmap.put(allTablefield.get(i).getTitle(), allTablefield.get(i));
        }
        Set<String> keys = parms.keySet();
        for (String key : keys) {
            if (fieldmap.containsKey(key)) {
                if (fieldmap.get(key).isPrimary()) {
                    pk_table[0] = fieldmap.get(key).getTitle();
                    pk_table[1] = parms.get(key);
                }
                if (fieldmap.get(key).isPrimary() && fieldmap.get(key).getTitle().equalsIgnoreCase("id")) {
                    continue;
                }
                switch (fieldmap.get(key).getType()) {
                    case "integer":
                        if (parms.get(key).equals("true") || parms.get(key).equals("false")) {
                            cv.put(key, Boolean.valueOf(parms.get(key)));
                        } else {
                            cv.put(key, Long.parseLong(parms.get(key)));
                        }
                        break;
                    case "real":
                        cv.put(key, Long.parseLong(parms.get(key)));
                        break;
                    case "blob":
                        byte[] bytes = null;
                        try {
                            bytes = parms.get(key).getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        cv.put(key, bytes);
                        break;
                    default:
                        cv.put(key, parms.get(key));
                        break;

                }
            }
        }
        return pk_table;
    }


    /**
     * blob转字符串
     *
     * @param blob
     * @return
     */
    public static String blobToString(byte[] blob) {
        if (blob.length <= MAX_BLOB_LENGTH) {
            if (fastIsAscii(blob)) {
                try {
                    return new String(blob, "US-ASCII");
                } catch (UnsupportedEncodingException ignored) {

                }
            }
        }
        return UNKNOWN_BLOB_LABEL;
    }

    public static boolean fastIsAscii(byte[] blob) {
        for (byte b : blob) {
            if ((b & ~0x7f) != 0) {
                return false;
            }
        }
        return true;
    }

}
