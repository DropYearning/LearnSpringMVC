package com.study.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 封装请求参数的javabean
 */
public class Account implements Serializable {
    private String username;
    private String password;
    private Double money;

    //// 如果继续包含引用类型
    //private User user;

    // 集合的封装
    private List<User> list;
    private Map<String, User> map;



    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                ", list=" + list +
                ", map=" + map +
                '}';
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public Map<String, User> getMap() {
        return map;
    }

    public void setMap(Map<String, User> map) {
        this.map = map;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    //public User getUser() {
    //    return user;
    //}
    //
    //public void setUser(User user) {
    //    this.user = user;
    //}
}
