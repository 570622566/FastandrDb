package cn.hotapk.fastandr_dbms;


import cn.hotapk.fastandr_dbms.bean.ResponseData;
import cn.hotapk.fastandr_dbms.utils.FConstant;

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

    /**
     * 获取数据库列表
     * @return
     */
    @ResponseBody
    @RequestMapping("getDbList")
    public ResponseData getDbList() {
        return FDbService.getDbList();
    }

    /**
     * 获取table列表
     * @param dbname
     * @return
     */
    @ResponseBody
    @RequestMapping("getTables")
    public ResponseData getTables(@RequestParam("dbname") String dbname) {
        return FDbService.getTableList(dbname);
    }

    /**
     * 获取table数据列表
     * @param dbname
     * @param tableName
     * @return
     */
    @ResponseBody
    @RequestMapping("getTableDatas")
    public ResponseData getTableDatas(@RequestParam("dbname") String dbname, @RequestParam("tableName") String tableName) {
        return FDbService.getTableDataList(dbname, tableName);
    }

    /**
     * 添加数据
     * @param parms
     * @return
     */
    @ResponseBody
    @RequestMapping("addData")
    public ResponseData addData(Map<String, String> parms) {
        return FDbService.addData(parms);
    }

    /**
     * 删除数据
     * @param parms
     * @return
     */
    @ResponseBody
    @RequestMapping("delData")
    public ResponseData delData(Map<String, String> parms) {
        return FDbService.delData(parms);
    }

    /**
     * 修改数据
     * @param parms
     * @return
     */
    @ResponseBody
    @RequestMapping("editData")
    public ResponseData editData(Map<String, String> parms) {
        return FDbService.editData(parms);
    }

    /**
     * 查询数据
     * @param dbname
     * @return
     */
    @RequestMapping("queryDb")
    public String queryDb(@RequestParam("dbname") String dbname) {
        return FDbService.queryDb(dbname);
    }

    /**
     * 下载数据库
     * @param dbname
     * @return
     */
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
