package com.wyw.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName MyFirstJdbc
 * @Description
 * @Author Wangyw
 */
public class MyFirstJdbc {
    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        try {
            //1.加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //2.获取连接
            connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ORCL","wangyw","wangyw");
            //connection.setAutoCommit(true);
            //3.获取statement对象，向数据库发送sql语句
            statement = connection.createStatement();
            connection.setSavepoint("A");
            connection.rollback();
            connection.commit();
            //4.执行sql语句
            statement.executeUpdate("insert into WANGYW.T_STU VALUES (001," + "'王艳文'," + "'男') ");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            SqlHelper.close(connection,statement,null);

        }
    }
}
