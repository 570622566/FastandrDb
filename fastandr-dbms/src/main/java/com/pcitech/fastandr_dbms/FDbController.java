package com.pcitech.fastandr_dbms;


import com.pcitech.fastandr_dbms.bean.ResponseData;
import com.pcitech.fastandr_dbms.utils.FConstant;

import java.io.InputStream;
import java.util.Map;

import cn.hotapk.fhttpserver.NanoHTTPD;
import cn.hotapk.fhttpserver.annotation.RequestMapping;
import cn.hotapk.fhttpserver.annotation.RequestParam;
import cn.hotapk.fhttpserver.annotation.ResponseBody;

/**
 * @author laijian
 * @version 2017/11/25
 * @Copyright (C)上午1:19 , www.hotapk.cn
 * web数据操作
 */
public class FDbController {

    @ResponseBody
    @RequestMapping("getDbList")
    public ResponseData getDbList() {
        return FDbService.getDbList();
    }

    @ResponseBody
    @RequestMapping("getTables")
    public ResponseData getTables(@RequestParam("dbname") String dbname) {
        return FDbService.getTableList(dbname);
    }

    @ResponseBody
    @RequestMapping("getTableDatas")
    public ResponseData getTableDatas(@RequestParam("dbname") String dbname, @RequestParam("tableName") String tableName) {
        return FDbService.getTableDataList(dbname, tableName);
    }

    @ResponseBody
    @RequestMapping("addData")
    public ResponseData addData(Map<String, String> parms) {
        return FDbService.addData(parms);
    }

    @ResponseBody
    @RequestMapping("delData")
    public ResponseData delData(Map<String, String> parms) {
        return FDbService.delData(parms);
    }

    @ResponseBody
    @RequestMapping("editData")
    public ResponseData editData(Map<String, String> parms) {
        return FDbService.editData(parms);
    }

    @RequestMapping("queryDb")
    public String queryDb(@RequestParam("dbname") String dbname) {
        return FDbService.queryDb(dbname);
    }

    @RequestMapping("downloaddb")
    public NanoHTTPD.Response downloaddb(@RequestParam("dbname") String dbname) {
        InputStream inputStream = FDbService.downloaddb(dbname);
        NanoHTTPD.Response response = NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", inputStream);//这代表任意的二进制数据传输。
        response.addHeader("Accept-Ranges", "bytes");
        if (dbname.contains(FConstant.SHAREDPREFS_XML)) {
            dbname = dbname.replace(FConstant.SHAREDPREFS_XML, "");
        }
        response.addHeader("Content-Disposition", "attachment; filename=" + dbname);

        return response;
    }


}
