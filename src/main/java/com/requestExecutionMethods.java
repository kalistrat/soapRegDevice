package com;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            ,String statusCode
    ){
        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );

            CallableStatement Stmt = Con.prepareCall("{call updateSoldDeviceStatus(?, ?, ?)}");
            Stmt.setString(1, iUID);
            Stmt.setString(2, iUserLog);
            Stmt.setString(3, statusCode);
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

    public static List<String> GetListFromString(String DevidedString, String Devider){
        List<String> StrPieces = new ArrayList<String>();
        try {
            int k = 0;
            String iDevidedString;
            // 123|321|456|

            if (DevidedString.startsWith(Devider)) {
                DevidedString = DevidedString.substring(1,DevidedString.length());
            }

            if (!DevidedString.contains(Devider)) {
                iDevidedString = DevidedString + Devider;
            } else {
                if (!DevidedString.endsWith(Devider)) {
                    iDevidedString = DevidedString + Devider;
                } else {
                    iDevidedString = DevidedString;
                }
            }

            while (!iDevidedString.equals("")) {
                int Pos = iDevidedString.indexOf(Devider);
                StrPieces.add(iDevidedString.substring(0, Pos));
                iDevidedString = iDevidedString.substring(Pos + 1);
                k = k + 1;
                if (k > 100000) {
                    iDevidedString = "";
                }
            }

        } catch (Exception e){
            //e.printStackTrace();
        }

        return StrPieces;
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

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public static String getUIDStatus(String qUID){
        String uidStatus = "";

        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );

            CallableStatement Stmt = Con.prepareCall("{? = call getUIDStatus(?)}");
            Stmt.registerOutParameter (1, Types.VARCHAR);
            Stmt.setString(2, qUID);
            Stmt.execute();
            uidStatus = Stmt.getString(1);
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return uidStatus;
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

    public static deviceArgs getDeviceArgs(String devUID){
        deviceArgs deviceArgsValue = null;
        try {

            Class.forName(JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    DB_URL
                    , USER
                    , PASS
            );

            CallableStatement Stmt = Con.prepareCall("{call getDeviceArgs(?,?,?,?,?)}");
            Stmt.setString(1, devUID);
            Stmt.registerOutParameter(2, Types.VARCHAR);
            Stmt.registerOutParameter(3, Types.VARCHAR);
            Stmt.registerOutParameter(4, Types.VARCHAR);
            Stmt.registerOutParameter(5, Types.VARCHAR);

            Stmt.execute();

            deviceArgsValue = new deviceArgs(
                    Stmt.getString(2)
                    ,Stmt.getString(3)
                    ,Stmt.getString(4)
                    ,Stmt.getString(5)
            );

            Con.close();


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        }
        return deviceArgsValue;
    }

    public static String userWsLinkDevice(
            String chainUID
            ,deviceArgs callArgs
    ){
        String respWs = null;

        try {


            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial((KeyStore)null, new TrustSelfSignedStrategy())
                    //I had a trust store of my own, and this might not work!
                    .build();

            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();

            HttpPost post = new HttpPost(callArgs.devWsURL);

            post.setHeader("Content-Type", "text/xml");

            String reqBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <com:linkUserDevice>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg0>"+chainUID+"</arg0>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg1>"+callArgs.userLog+"</arg1>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg2>"+callArgs.userPassSha+"</arg2>\n" +
                    "      </com:linkUserDevice>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            StringEntity input = new StringEntity(reqBody, Charset.forName("UTF-8"));
            post.setEntity(input);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            Document resXml = loadXMLFromString(rd.lines().collect(Collectors.joining()));
            respWs = XPathFactory.newInstance().newXPath()
                    .compile("//return").evaluate(resXml);


        } catch (Exception e){
            e.printStackTrace();

        }
        return respWs;
    }


}
