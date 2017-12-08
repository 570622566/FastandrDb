package cn.hotapk.fastandrdb.bean;

import org.litepal.crud.DataSupport;

/**
 * @author laijian
 * @version 2017/11/27
 * @Copyright (C)下午1:53 , www.hotapk.cn
 */
public class StudenBean extends DataSupport{
    private String name = "";
    private String s_className = "";
    private String s_grade = "";

    public StudenBean() {
    }

    public StudenBean(String name, String s_className, String s_grade) {
        this.name = name;
        this.s_className = s_className;
        this.s_grade = s_grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS_className() {
        return s_className;
    }

    public void setS_className(String s_className) {
        this.s_className = s_className;
    }

    public String getS_grade() {
        return s_grade;
    }

    public void setS_grade(String s_grade) {
        this.s_grade = s_grade;
    }
}
