package com.pcitech.fastandr_dbms;

import android.text.TextUtils;

import com.pcitech.fastandr_dbms.utils.NanoHTTPD;

import java.io.InputStream;
import java.util.Map;

/**
 * @author laijian
 * @version 2017/11/13
 * @Copyright (C)上午10:17 , www.hotapk.cn
 * sql内网查询服务
 */
public class FSqlNetServer extends NanoHTTPD {

    public FSqlNetServer(int port) {
        super(port);
    }

    public FSqlNetServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String file_name = session.getUri().substring(1);
        Map<String, String> parms = session.getParms();
        StringBuffer br = new StringBuffer();
        if (parms.size() == 0) {
            if (file_name.endsWith("getDbList")) {
                br.append(SqlService.getDbList());
            } else {
                if (TextUtils.isEmpty(file_name)) {
                    file_name = "index.html";
                }
                br.append(SqlService.index(file_name));
            }
        } else {
            if (file_name.endsWith("getTables")) {
                String dbname = parms.get("dbname");
                br.append(SqlService.getTableList(dbname));
            } else if (file_name.endsWith("getTableDatas")) {
                String dbname = parms.get("dbname");
                String tableName = parms.get("tableName");
                br.append(SqlService.getTableDataList(dbname, tableName));
            } else if (file_name.endsWith("addData")) {
                br.append(SqlService.addData(parms));
            } else if (file_name.endsWith("editData")) {
                br.append(SqlService.editData(parms));
            } else if (file_name.endsWith("delData")) {
                br.append(SqlService.delData(parms));
            } else if (file_name.endsWith("delData")) {
                br.append(SqlService.delData(parms));
            }else if (file_name.endsWith("queryDb")) {
                String dbname = parms.get("dbname");
                br.append(SqlService.queryDb(dbname));
            }
            else if (file_name.endsWith("downloaddb")) {
                String dbname = parms.get("dbname");
                InputStream inputStream = SqlService.downloaddb(dbname);
                Response response = newChunkedResponse(Response.Status.OK, "application/octet-stream",inputStream);//这代表任意的二进制数据传输。
                response.addHeader("Accept-Ranges", "bytes");
                return response;

            }
        }
        return newFixedLengthResponse(Response.Status.OK, detectMimeType(file_name), br.toString());
    }

    public static String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }
}
