/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   TestGetLeadsRds.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.getrecord;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
public class TestGetLeadsRds {

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
    public static void main(String a[])
    {
        getLeadsById("85333000000072001");
//        getAllLeadsRds();
//        updateRecords();
//        readProperties();
    }

    public static void readProperties(){

        try {
            Properties prop = new Properties();
            prop.load(TestGetLeadsRds.class.getResourceAsStream("/secure/zoho.properties"));
            AUTHTOKEN = prop.getProperty("AUTHTOKEN");
            NEWFORMAT_1 = prop.getProperty("NEWFORMAT_1");
            NEWFORMAT_2 = prop.getProperty("NEWFORMAT_2");
            SCOPE = prop.getProperty("SCOPE");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateRecords(){
        try {
//            String authtoken = "f19d6f4ad3d2a491ef52f83a7a68bf04";
            String scope = "crmapi";
            String newFormat = "1";

            String targetURL = "https://crm.zoho.com.cn/crm/private/xml/Leads/updateRecords";
            String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Leads><row no=\"1\"><FL val=\"LEADID\">85333000000072001</FL><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Lead Owner\"><![CDATA[qq qq]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
            String paramname = "content";
            PostMethod post = new PostMethod(targetURL);
            post.setParameter("authtoken",AUTHTOKEN);
            post.setParameter("scope",scope);
            post.setParameter("newFormat",newFormat);
            post.setParameter("id","85333000000072001");
            post.setParameter("xmlData",newFormat);
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
}
