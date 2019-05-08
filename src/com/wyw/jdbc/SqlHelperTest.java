package com.wyw.jdbc;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.List;

/**
 * @ClassName SqlHelperTest
 * @Description
 * @Author Wangyw
 */
public class SqlHelperTest {
    public static void main(String[] args){

        SqlHelper.executeUpdate("insert into wangyw.t_stu values(?,?,?)",new String[]{"002", "SiriusKing","男"});

        ResultSet resultSet = null;

        test();

        //调用executeQuery
        try{
            String sql = "select * from wangyw.t_stu";
            resultSet = SqlHelper.executeQuery(sql,null);
            while (resultSet.next()){
                System.out.println(resultSet.getString("name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            SqlHelper.close(SqlHelper.getConnection(),SqlHelper.getStatement(),resultSet);
        }

        //调用executeQuery2
        try{
            String sql = "select * from wangyw.t_stu";
            List<Stu> list = SqlHelper.executeQuery2(sql,null);
            for (Stu stu:list
                 ) {
                System.out.println(stu);//重写Stu中toString方法后，调用的是toString方法
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            SqlHelper.close(SqlHelper.getConn(),SqlHelper.getStatement(),resultSet);
        }

        //调用存储过程（未封装）
        try{
            Connection connection = SqlHelper.getConnection();
            CallableStatement cs = connection.prepareCall("{call 过程名(?,?,?,?,?,?)}");
            //输入参数
            cs.setString(1,"xxx");
            cs.setInt(2,2);
            cs.setDouble(3,11.0);
            //输出参数（oracle数据类型）
            cs.registerOutParameter(4,oracle.jdbc.OracleTypes.NUMBER);
            cs.registerOutParameter(5,oracle.jdbc.OracleTypes.NUMBER);
            cs.registerOutParameter(6,oracle.jdbc.OracleTypes.CURSOR);

            //提交
            cs.execute();

            //得到返回结果（java数据类型）
            System.out.println(cs.getInt(4));
            ResultSet resultSet1 = (ResultSet)cs.getObject(6);//返回游标类型的输出参数
            //遍历
            while (resultSet1.next()){
                System.out.println(resultSet1.getString(1));
            }

        }catch (Exception e){

        }finally {

        }

        //调用存储过程（封装）
        try{
            String sql = "{call 过程名(?,?,?,?)}";

            Object[] in = new Object[]{"xxx",1,2,};
            int[] out = new int[]{oracle.jdbc.OracleTypes.NUMBER, oracle.jdbc.OracleTypes.NUMBER, OracleTypes.CURSOR};

            CallableStatement cs = SqlHelper.callPro(sql, in, out);

            System.out.println(cs.getInt(4));
            ResultSet resultSet1 = (ResultSet)cs.getObject(6);//返回游标类型的输出参数
            //遍历
            while (resultSet1.next()){
                System.out.println(resultSet1.getString(1));
            }
        }catch(Exception e){

        }

    }

    public static void test(){
        String sql1="update briup_emp set sal=sal-10 where ename=?";
        String sql2="update briup_emp set sal=sal+10 where ename=?";
        String[] sqls={sql1,sql2};
        String sql1_paras[]={"Larry"};
        String sql2_paras[]={"小红"};
        String[][] parmeters={sql1_paras,sql2_paras};
        SqlHelper.executeUpdate2(sqls, parmeters);
    }
}
