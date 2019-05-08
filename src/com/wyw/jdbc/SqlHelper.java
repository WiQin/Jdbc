package com.wyw.jdbc;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName SqlHelper
 * @Description
 * @Author Wangyw
 */
public class SqlHelper {
    private static Connection connection = null;//企业级开发中，connection不要用static修饰，在哪用旧在哪初始化
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    private static List<Stu> list = null;
    private static CallableStatement cs = null;

    private static String driver = "";
    private static String url = "";
    private static String userName = "";
    private static String passWord = "";

    private static FileInputStream fis = null;
    private static Properties properties = null;

    //connection只连接一次，写到static代码块中
    static {

        try {

            properties = new Properties();
            fis = new FileInputStream("DbInfo.properties");
            properties.load(fis);

            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            userName = properties.getProperty("userName");
            passWord = properties.getProperty("passWord");

            //加载驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(Connection connection) {
        SqlHelper.connection = connection;
    }

    public static PreparedStatement getStatement() {
        return statement;
    }//为了在调用程序中使用close方法

    public static void setStatement(PreparedStatement statement) {
        SqlHelper.statement = statement;
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, userName, passWord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConn(){
        return connection;
    }

    public static void executeUpdate(String sql, String... peramaters) {
        connection = getConnection();
        try {
            connection.setAutoCommit(true);
            statement = connection.prepareStatement(sql);
            if (peramaters != null) {
                for (int i = 0; i < peramaters.length; i++) {
                    statement.setString(i + 1, peramaters[i]);
                }
            }
            //执行sql语句
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection,statement,null);
        }
    }

    public static void executeUpdate2(String[] sql, String[][] peramaters) {
        /**
        * @Author Wangyw
        * @Description  执行多条sql语句
        * @Date 2019/3/5 0005
        * @Param [sql, peramaters]
        * @Return void
        **/
        connection = getConnection();
        try {
            connection.setAutoCommit(false);
            for(int i = 0;i<sql.length;i++){
                statement = connection.prepareStatement(sql[i]);
                if(peramaters != null){
                    for(int j = 0;j < peramaters[i].length;j++){
                        statement.setString(j+1,peramaters[i][j]);
                    }
                }
                statement.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            close(connection,statement,null);
        }
    }

    public static ResultSet executeQuery(String sql,String[] parameters) {
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if(parameters != null && !parameters.equals("")){
                for(int i = 0;i<parameters.length;i++){
                    statement.setString(i+1,parameters[i]);
                }
            }
            resultSet = statement.executeQuery();
        }catch (Exception e){

        }
        return resultSet;
    }

    public static List<Stu> executeQuery2(String sql, String[] parameters) {
        /**
        * @Author Wangyw
        * @Description
        * @Date 2019/3/6 0006
         * 将返回结果设置为resultSet,在调用程序中还需要解析resuleSet结果，更好的办法吗，将返回值设置为集合（加泛型），在方法中就去除所需数据
        * @Param [sql, parameters]
        * @Return java.util.List<com.wyw.jdbc.Stu>
        **/

        try {
            list = new ArrayList();

            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if(parameters != null && !parameters.equals("")){
                for(int i = 0;i<parameters.length;i++){
                    statement.setString(i+1,parameters[i]);
                }
            }
            resultSet = statement.executeQuery();
            Stu stu = null;//写在外面，只需要新建一个stu对象，在完成一次取值后，将它置为空，取下一次的参数
            while (resultSet.next()){
                stu = new Stu(resultSet.getInt("stuid"),resultSet.getString("stuname"),resultSet.getString("stusex"));
                list.add(stu);
                stu = null;
            }
        }catch (Exception e){

        }finally {
            close(connection,statement,resultSet);
        }
        return list;
    }

    //调用过程（只针对某一个过程）
    public static CallableStatement callPro(String sql,Object[] inparameters,int[] outparameters){
        /**
        * @Author Wangyw
        * @Description
         * 输入参数类型 Object
         * 输出参数类型  根据过程
        * @Date 2019/3/6 0006
        * @Param [sql, inparameters, outparameters]
        * @Return void
        **/
        try{
            Connection connection = SqlHelper.getConnection();
            cs = connection.prepareCall(sql);
            //输入参数
            if(inparameters != null){
                for(int i = 0;i<inparameters.length;i++){
                    cs.setObject(i+1,inparameters[i]);
                }
            }

            //输出参数
            if(outparameters != null){
                for(int i = 0;i<outparameters.length;i++){
                    cs.registerOutParameter(inparameters.length+1+i,outparameters[i]);
                }
            }


            //提交
            cs.execute();

            return cs;

        }catch (Exception e){

        }


        return null;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (connection != null) {//connection:晚开早关
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
