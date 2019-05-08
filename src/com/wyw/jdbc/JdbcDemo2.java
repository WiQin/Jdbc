package com.wyw.jdbc;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.sql.*;

/**
 * @ClassName JdbcDemo2
 * @Description
 * @Author Wangyw
 */
public class JdbcDemo2 {
    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //1.加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //2.获取连接
            connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ORCL","wangyw","wangyw");
            //connection.setAutoCommit(true);
            //3.获取statement对象，向数据库发送sql语句
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //4.执行sql语句
            resultSet = statement.executeQuery("select * from wangyw.t_stu");
            while(resultSet.next()){
                System.out.println(resultSet.getInt("stuid"));
                System.out.println(resultSet.getString(2));
            }
            System.out.println("-----------------------------------------");
            resultSet.beforeFirst();//回到第一行
            while(resultSet.next()){
                System.out.println(resultSet.getInt("stuid"));
                System.out.println(resultSet.getString(2));
            }
            System.out.println("--------------------------------------------");
            while(resultSet.previous()){
                System.out.println(resultSet.getInt("stuid"));
                System.out.println(resultSet.getString(2));
            }
            System.out.println("---------------------------------------------");
            resultSet.afterLast();//最后一行之后
            while(resultSet.previous()){
                System.out.println(resultSet.getInt("stuid"));
                System.out.println(resultSet.getString(2));
            }
            System.out.println("-------------------------------------");
            resultSet.absolute(2);//定位在第二行之后
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            SqlHelper.close(connection,statement,null);

        }
    }
}
