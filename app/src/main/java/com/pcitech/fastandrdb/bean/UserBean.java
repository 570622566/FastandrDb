package com.pcitech.fastandrdb.bean;

import org.litepal.crud.DataSupport;

/**
 * @author laijian
 * @version 2017/11/27
 * @Copyright (C)上午11:01 , www.hotapk.cn
 */
public class UserBean extends DataSupport {

    private String userName = "";
    private String passW = "";
    private int age;
    private String phone = "";
    private boolean isAdmin = false;
    private double money = 200d;

    public UserBean() {
    }

    public UserBean(String userName, String passW, int age, String phone, boolean isAdmin, double money) {
        this.userName = userName;
        this.passW = passW;
        this.age = age;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassW() {
        return passW;
    }

    public void setPassW(String passW) {
        this.passW = passW;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
