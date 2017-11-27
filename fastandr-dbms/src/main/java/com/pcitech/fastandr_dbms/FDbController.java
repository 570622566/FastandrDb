package com.pcitech.fastandr_dbms;

import com.pcitech.fastandr_dbms.utils.NanoHTTPD;
import com.pcitech.fastandr_dbms.utils.RequestMapping;

import java.io.InputStream;
import java.util.Map;

/**
 * @author laijian
 * @version 2017/11/25
 * @Copyright (C)上午1:19 , www.hotapk.cn
 * web数据操作
 */
public class FDbController {


    @RequestMapping("getDbList")
    public NanoHTTPD.Response getDbList() {
        return setResponse(SqlService.getDbList());
    }

    @RequestMapping("getTables")
    public NanoHTTPD.Response getTables(Map<String, String> parms) {
        String dbname = parms.get("dbname");
        return setResponse(SqlService.getTableList(dbname));
    }

    @RequestMapping("getTableDatas")
    public NanoHTTPD.Response getTableDatas(Map<String, String> parms) {
        String dbname = parms.get("dbname");
        String tableName = parms.get("tableName");
        return setResponse(SqlService.getTableDataList(dbname, tableName));
    }

    @RequestMapping("addData")
    public NanoHTTPD.Response addData(Map<String, String> parms) {
        return setResponse(SqlService.addData(parms));
    }

    @RequestMapping("delData")
    public NanoHTTPD.Response delData(Map<String, String> parms) {
        return setResponse(SqlService.delData(parms));
    }


    @RequestMapping("editData")
    public NanoHTTPD.Response editData(Map<String, String> parms) {
        return setResponse(SqlService.editData(parms));
    }

    @RequestMapping("queryDb")
    public NanoHTTPD.Response queryDb(Map<String, String> parms) {
        String dbname = parms.get("dbname");
        return setResponse(SqlService.queryDb(dbname));
    }

    @RequestMapping("downloaddb")
    public NanoHTTPD.Response downloaddb(Map<String, String> parms) {
        String dbname = parms.get("dbname");
        InputStream inputStream = SqlService.downloaddb(dbname);
        NanoHTTPD.Response response = NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", inputStream);//这代表任意的二进制数据传输。
        response.addHeader("Accept-Ranges", "bytes");
        return response;
    }


    public static NanoHTTPD.Response setResponse(String res) {
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", res);
    }
}
