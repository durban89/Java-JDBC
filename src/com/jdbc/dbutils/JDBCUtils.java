package com.jdbc.dbutils;

import java.sql.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     *
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

    /**
     * 获取单挑记录
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findSingleResult(String sql, List<Object> params) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        int index = 1;
        preparedStatement = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for(int i = 0; i<params.size();i++){
                preparedStatement.setObject(index++, params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLen = metaData.getColumnCount();
        while (resultSet.next()){
            for(int i = 0; i<colLen; i++){
                String colName = metaData.getColumnName(i);
                Object colValue = resultSet.getObject(colName);
                if(colValue == null){
                    colValue = "";
                }
                map.put(colName, colValue);
            }
        }
        return map;
    }


    public static void main(String[] args) {
        JDBCUtils jdbcUtils = new JDBCUtils();
    }
}
