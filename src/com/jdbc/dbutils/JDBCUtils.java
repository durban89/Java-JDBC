package com.jdbc.dbutils;

import javax.xml.transform.Result;
import java.lang.reflect.Field;
import java.sql.*;

import java.util.ArrayList;
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
    private final String URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
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
     *
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
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(index++, params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLen = metaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 0; i < colLen; i++) {
                String colName = metaData.getColumnName(i);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                map.put(colName, colValue);
            }
        }
        return map;
    }

    /**
     * 获取多条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findMoreResult(String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();

        int index = 1;
        preparedStatement = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(index++, params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLen = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();

            for (int i = 0; i < colLen; i++) {
                String colName = metaData.getColumnName(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                map.put(colName, colValue);
            }

            list.add(map);

        }

        return list;
    }

    /**
     * 利用反射机制 实现单条查询
     * @param sql
     * @param params
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T findSingleRefResult(String sql, List<Object> params, Class<T> cls) throws Exception{
        T resultObject = null;

        int index = 1;
        preparedStatement = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(index++, params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLen = metaData.getColumnCount();
        while (resultSet.next()) {
            resultObject = cls.newInstance();

            for (int i = 0; i < colLen; i++) {
                String colName = metaData.getColumnName(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                Field field = cls.getDeclaredField(colName);
                field.setAccessible(true);
                field.set(resultObject, colValue);
            }
        }



        return resultObject;
    }

    /**
     * 通过反射机制 多条记录查询
     * @param sql
     * @param params
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> findMoreRefResult(String sql, List<Object> params, Class<T> cls) throws Exception{
        List<T> list = new ArrayList<T>();

        int index = 1;
        preparedStatement = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(index++, params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLen = metaData.getColumnCount();
        while (resultSet.next()) {
            T resultObject = cls.newInstance();

            for (int i = 0; i < colLen; i++) {
                String colName = metaData.getColumnName(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                Field field = cls.getDeclaredField(colName);
                field.setAccessible(true);
                field.set(resultObject, colValue);
            }

            list.add(resultObject);

        }

        return list;
    }

    public void release(){

    }

    public static void main(String[] args) {
        JDBCUtils jdbcUtils = new JDBCUtils();
        jdbcUtils.getConnection();

        //插入测试
        String sql = "INSERT INTO userinfo (username,pass) VALUES (?,?)";
        List<Object> params = new ArrayList<>();
        params.add("jack");
        params.add("123456");
        try {
            jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.release();
        }

    }
}
