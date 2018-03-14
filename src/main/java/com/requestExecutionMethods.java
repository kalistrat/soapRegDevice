package com;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.security.MessageDigest;
import java.sql.*;

/**
 * Created by kalistrat on 13.03.2018.
 */
public class requestExecutionMethods {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost/teljournal";
    public static final String USER = "kalistrat";
    public static final String PASS = "045813";


    public static void updateSoldDeviceStatus(
            String iUID
            ,String iUserLog
    ){
        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );

            CallableStatement Stmt = Con.prepareCall("{call updateSoldDeviceStatus(?, ?)}");
            Stmt.setString(1, iUID);
            Stmt.setString(2, iUserLog);
            Stmt.execute();

            Con.close();


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        }
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static int fisUIDExists(String qUID){
        int IsUIDExists = 0;

        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );

            CallableStatement Stmt = Con.prepareCall("{? = call fisUIDExists(?)}");
            Stmt.registerOutParameter (1, Types.INTEGER);
            Stmt.setString(2, qUID);
            Stmt.execute();
            IsUIDExists = Stmt.getInt(1);
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return IsUIDExists;
    }

    public static String fGetUserPassSha(String qUserLog){
        String userPass = null;

        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );


            CallableStatement Stmt = Con.prepareCall("{? = call fGetUserPassSha(?)}");
            Stmt.registerOutParameter (1, Types.VARCHAR);
            Stmt.setString(2, qUserLog);
            Stmt.execute();
            userPass = Stmt.getString(1);
            Con.close();

            return userPass;

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return userPass;
    }


}
