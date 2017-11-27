package com.pcitech.fastandr_dbms.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author laijian
 * @version 2017/11/27
 * @Copyright (C)下午4:01 , www.hotapk.cn
 * 返回数据对象
 */

public class ResponseData {

    private List<String> rows = new ArrayList<>();
    List<FieldInfor> allTablefield = new ArrayList<>();
    List<Map<String, Object>> datas = new ArrayList<>();

    private boolean isSuccessful;
    private String error = "";

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public List<FieldInfor> getAllTablefield() {
        return allTablefield;
    }

    public void setAllTablefield(List<FieldInfor> allTablefield) {
        this.allTablefield = allTablefield;
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        this.datas = datas;
    }
}
