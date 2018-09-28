package com.wu.first;

/**
 * Created by Administrator on 2018/9/25.
 */
public class User {
    private int id;
    private String name;
    private String password;
    private String user_phone;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {

        this.user_phone = user_phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public User(String name, String password,String phone) {
        this.name = name;
        this.password = password;
        this.user_phone =phone;
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public User(){}
}
