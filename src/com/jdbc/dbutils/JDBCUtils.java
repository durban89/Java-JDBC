package com.jdbc.dbutils;

import java.sql.PreparedStatement;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by durban126 on 16/10/4.
 */
public class JDBCUtils {
    private final String USERNAME = "root";
    private final String PASS = "123456";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://127.0.0.1:3306/test";
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public JDBCUtils() {
        try {
            Class.forName(DRIVER);
            System.out.println("注册驱动成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定义数据库的链接
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * UPDATE
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<Object> params) throws SQLException {
        boolean flag = false;

        int result = 1;
        preparedStatement = connection.prepareStatement(sql);
        int index = 1;
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(index++, params.get(i));
            }
        }

        result = preparedStatement.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;

    }


    public static void main(String[] args) {
        JDBCUtils jdbcUtils = new JDBCUtils();
    }
}
