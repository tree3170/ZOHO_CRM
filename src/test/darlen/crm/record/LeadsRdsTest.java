/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   TestGetLeadsRds.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.getrecord
 * Description：ZOHO_CRM  测试获取Leads数据
 * Created on  2016/08/21 14：46
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        14：46   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class LeadsRdsTest {

    /**
     * 使用ZOHO API时必需要的密钥
     */
    private static String AUTHTOKEN ="";
    /**
     * 查询的数据中不包含null
     */
    private static String NEWFORMAT_1 ="1";
    /**
     * 查询的数据中包含null
     */
    private static String NEWFORMAT_2 ="2";//include null
    /**
     * API 使用范围
     */
    private static String SCOPE ="crmapi";
    private static String DB_URL ="";
    private static String DB_USERNAME ="";
    private static String DB_PWD ="";
    private static String DB_DRIVER_NAME ="";
    public static void main(String a[])
    {
        readProperties();
        //getAllLeadsRds();
//        getLeadsById("85333000000072001");
        updateRecords();
//        testjdbc();
    }

    /**
     * 获取DB连接
     */
    public static void testjdbc(){
        String driverName = DB_DRIVER_NAME;//"com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动
        String dbURL = DB_URL;//"jdbc:sqlserver://localhost:1433; DatabaseName=test";  //连接服务器和数据库test
        String userName = DB_USERNAME;//"sa";  //默认用户名
        String userPwd = DB_PWD;//"zaq1@WSX";  //密码
        Connection dbConn;

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
            System.out.println("Connection Successful!");  //如果连接成功 控制台输出Connection Successful!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取zoho和db的properties文件
     */
    public static void readProperties(){

        try {
            Properties prop = new Properties();
            prop.load(LeadsRdsTest.class.getResourceAsStream("/secure/zoho.properties"));
            AUTHTOKEN = prop.getProperty(Constants.HTTP_POST_PARAM_AUTHTOKEN);//("AUTHTOKEN");
            NEWFORMAT_1 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_1);//("NEWFORMAT_1");
            NEWFORMAT_2 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_2);//("NEWFORMAT_2");
            SCOPE = prop.getProperty(Constants.HTTP_POST_PARAM_SCOPE);
            prop.load(LeadsRdsTest.class.getResourceAsStream("/secure/db.properties"));
            DB_URL = prop.getProperty(Constants.DB_CONNECT_DB_URL);//("DB_URL");
            DB_PWD = prop.getProperty(Constants.DB_CONNECT_DB_PWD);//("DB_PWD");
            DB_USERNAME = prop.getProperty(Constants.DB_CONNECT_DB_USERNAME);//("DB_USERNAME");
            DB_DRIVER_NAME = prop.getProperty(Constants.DB_CONNECT_DRIVER_NAME);//("DB_DRIVER_NAME");
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.err.println("[readProperties], AUTHTOKEN:::"+AUTHTOKEN+"; NEWFORMAT_1:::"+NEWFORMAT_1+"; NEWFORMAT_2:::"+NEWFORMAT_2+"; SCOPE:::"+SCOPE
            +"; DB_URL:::"+DB_URL+"; DB_PWD:::"+DB_PWD+"; DB_USERNAME:::"+DB_USERNAME+"; DB_DRIVER_NAME:::"+DB_DRIVER_NAME);
    }

    /**
     * 更新Leads数据
     * 1.修改线索拥有人时必需是SMOWNERID + Lead Owner同时存在，否则结果就算成功但是这个线索拥有人也不会更新
     * 2. created/updated by /time 等系统字段，不能自己设定
     * 3. 可以填写不存在的值，比如线索名前面的称呼
     *  sample response=======>
     *  <?xml version="1.0" encoding="UTF-8" ?><response uri="/crm/private/xml/Leads/updateRecords"><result><message>Record(s) updated successfully</message><recorddetail><FL val="Id">85333000000072001</FL><FL val="Created Time">2016-07-13 23:58:11</FL><FL val="Modified Time">2016-08-22 21:36:36</FL><FL val="Created By"><![CDATA[qq qq]]></FL><FL val="Modified By"><![CDATA[tree3170]]></FL></recorddetail></result></response>
     */
    public static void updateRecords(){
        try {
//            String authtoken = "f19d6f4ad3d2a491ef52f83a7a68bf04";
            String scope = SCOPE;//"crmapi";
            String newFormat = NEWFORMAT_1;//"1";
//            String id = "85333000000072001";
            String targetURL = "https://crm.zoho.com.cn/crm/private/xml/Leads/updateRecords";
//            String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Leads><row no=\"1\"><FL val=\"LEADID\">85333000000072001</FL><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Lead Owner\"><![CDATA[qq qq]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
            String xmlData = "<Leads><row no=\"1\"><FL val=\"Lead Owner\"><![CDATA[tree3170]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
//            xmlData = "<Leads><row no=\"1\"><FL val=\"Salutation\"><![CDATA[先生]]></FL></row></Leads>";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            //
            xmlData = "<Leads><row no=\"1\"><FL val=\"LEADID\">85333000000072001</FL><FL val=\"SMOWNERID\">85333000000071001</FL><FL val=\"Lead Owner\"><![CDATA[tree3170]]></FL><FL val=\"First Name\"><![CDATA[null]]></FL><FL val=\"Salutation\"><![CDATA[先生1]]></FL></row></Leads>";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.ZOHO_PROPS_NEWFORMAT_1, NEWFORMAT_1);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,xmlData);

            executePostMethod(postParams);
            /*  PostMethod post = new PostMethod(targetURL);
            post.setParameter("authtoken",AUTHTOKEN);
            post.setParameter("scope",scope);
            post.setParameter("newFormat",newFormat);
            post.setParameter("id","85333000000072001");
            post.setParameter("xmlData",xmlData);
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            HttpClient httpclient = new HttpClient();
            PrintWriter myout = null;

            // Execute http request
            try
            {
                long t1 = System.currentTimeMillis();
                int result = httpclient.executeMethod(post);
                System.out.println("HTTP Response status code: " + result);
                System.out.println(">> Time taken " + (System.currentTimeMillis() - t1));

                // writing the response to a file
                myout = new PrintWriter(new File("response.xml"));
                myout.print(post.getResponseBodyAsString());

                //-----------------------Get response as a string ----------------
                String postResp = post.getResponseBodyAsString();
                System.out.println("postResp=======>"+postResp);

            }catch(Exception e){
                e.printStackTrace();
            } finally{
                myout.close();
                post.releaseConnection();
            }*/

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有数据
     */
    public static void getAllLeadsRds(){
        try {
            //----------------------------Fetch Auth Token ----------------------
//            String authtoken = "f19d6f4ad3d2a491ef52f83a7a68bf04";
            String scope = "crmapi";
            String selectColumns ="Leads(id,Lead Owner,First Name,Last Name,Email,Company)";
            String newFormat = "1";
            String fromIndex = "1";
            String toIndex = "50";

            String targetURL = "https://crm.zoho.com.cn/crm/private/json/Leads/getRecords";
            String paramname = "content";
            PostMethod post = new PostMethod(targetURL);
            post.setParameter("authtoken",AUTHTOKEN);
            post.setParameter("scope",scope);
            post.setParameter("newFormat",newFormat);
            post.setParameter("selectColumns",selectColumns);
            post.setParameter("fromIndex",fromIndex);
            post.setParameter("toIndex",toIndex);
            HttpClient httpclient = new HttpClient();
            PrintWriter myout = null;

            // Execute http request
            try
            {
                long t1 = System.currentTimeMillis();
                int result = httpclient.executeMethod(post);
                System.out.println("HTTP Response status code: " + result);
                System.out.println(">> Time taken " + (System.currentTimeMillis() - t1));

                // writing the response to a file
                myout = new PrintWriter(new File("response.xml"));
                myout.print(post.getResponseBodyAsString());

                //-----------------------Get response as a string ----------------
                String postResp = post.getResponseBodyAsString();
                System.out.println("postResp=======>"+postResp);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                myout.close();
                post.releaseConnection();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id获取Leads数据
     * @param id
     */
    public static void getLeadsById(String id){
        try
        {
            //----------------------------Fetch Auth Token ----------------------
            String authtoken = "f19d6f4ad3d2a491ef52f83a7a68bf04";//If you don't have a authtoken please refer this wiki https://zohocrmapi.wiki.zoho.com/using-authtoken.html
            String scope = "crmapi";

            String recordId = id;//"1";
            String newFormat = "1";
            String selectColumns ="Leads(Lead Owner,First Name,Last Name,Email,Company)";

            String targetURL = "https://crm.zoho.com.cn/crm/private/xml/Leads/getRecordById";
            String paramname = "content";

            PostMethod post = new PostMethod(targetURL);
            post.setParameter("authtoken",authtoken);
            post.setParameter("scope",scope);
            post.setParameter("newFormat",newFormat);
            post.setParameter("id",recordId);
            //post.setParameter("selectColumns",selectColumns);

            HttpClient httpclient = new HttpClient();
            PrintWriter myout = null;

            // Execute http request
            try
            {
                long t1 = System.currentTimeMillis();
                int result = httpclient.executeMethod(post);
                System.out.println("HTTP Response status code: " + result);
                System.out.println(">> Time taken " + (System.currentTimeMillis() - t1));

                // writing the response to a file
                myout = new PrintWriter(new File("response.xml"));
                myout.print(post.getResponseBodyAsString());

                //------------Get response as a string ----------
                String postResp = post.getResponseBodyAsString();
                System.out.println("postResp=======>"+postResp);
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                myout.close();
                post.releaseConnection();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Execute post method
     * @param map
     */
    public static void executePostMethod(Map<String,String> map){
        PostMethod post = new PostMethod(map.get(Constants.HTTP_POST_PARAM_TARGETURL));
        post.setParameter(Constants.HTTP_POST_PARAM_AUTHTOKEN, AUTHTOKEN);
        post.setParameter(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
        post.setParameter(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
        post.setParameter(Constants.HTTP_POST_PARAM_XMLDATA, map.get(Constants.HTTP_POST_PARAM_XMLDATA));
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.HTTP_POST_PARAM_UTF8);

        String id = map.get(Constants.HTTP_POST_PARAM_ID);
        if(!StringUtils.isEmptyString(id)) post.setParameter(Constants.HTTP_POST_PARAM_ID,id);
        HttpClient httpclient = new HttpClient();
        PrintWriter myout = null;

        // Execute http request
        try
        {
            long t1 = System.currentTimeMillis();
            int result = httpclient.executeMethod(post);
            System.out.println("HTTP Response status code: " + result);
            System.out.println(">> Time taken " + (System.currentTimeMillis() - t1));

            // writing the response to a file
            myout = new PrintWriter(new File("response.xml"));
            myout.print(post.getResponseBodyAsString());

            //-----------------------Get response as a string ----------------
            String postResp = post.getResponseBodyAsString();
            System.out.println("postResp=======>"+postResp);
            /**
             * sample response=======><?xml version="1.0" encoding="UTF-8" ?>
             <response uri="/crm/private/xml/Leads/updateRecords"><result><message>Record(s) updated successfully</message><recorddetail><FL val="Id">85333000000072001</FL><FL val="Created Time">2016-07-13 23:58:11</FL><FL val="Modified Time">2016-08-22 21:36:36</FL><FL val="Created By"><![CDATA[qq qq]]></FL><FL val="Modified By"><![CDATA[tree3170]]></FL></recorddetail></result></response>
             */
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            myout.close();
            post.releaseConnection();
        }
    }
}
