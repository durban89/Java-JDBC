package com.jdbc.dbutils.models;

import java.io.Serializable;

/**
 * Created by durban126 on 16/10/5.
 */
public class UserInfo implements Serializable {

    private int autokid;
    private String username;
    private String pass;

    @Override
    public String toString() {
        return "UserInfo{" + "autokid=" + autokid + ", username='" + username + '\'' + ", pass='" + pass + '\'' + '}';
    }

    public int getAutokid() {
        return autokid;
    }

    public void setAutokid(int autokid) {
        this.autokid = autokid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
