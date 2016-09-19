/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HandleModule.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.record
 * Description：ZOHO_CRM  这是一个操作Module的类，主要针对SO、Accounts的增删改查
 * //TODO: qq:85333000000071039, tree3170:85333000000071001
 * Created on  2016/09/15 21：11
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：11   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class HandleModules {
    private static HandleModules modules;
    private static Logger logger =  Logger.getLogger(HandleModules.class);
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
    private static String MODULES= "";
    /**传递方式为JSON或者XML，默认是xml*/
    private static String FORMAT="xml";


    /**
     * BeforeClass 与Before的区别
     */
    @BeforeClass
    public static void getInstance(){
        modules = new HandleModules();
        initZohoProps();
    }


    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
    private static void initZohoProps() {
        try {
            Properties prop = new Properties();
            prop.load(HandleModules.class.getResourceAsStream("/secure/zoho.properties"));
            AUTHTOKEN = prop.getProperty(Constants.ZOHO_PROPs_AUTHTOKEN_TREE);
            NEWFORMAT_1 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_1);
            NEWFORMAT_2 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_2);
            SCOPE = prop.getProperty(Constants.HTTP_POST_PARAM_SCOPE);
            MODULES = prop.getProperty(Constants.ZOHO_PROPS_MODULES);
        } catch(IOException e) {
            logger.error("读取zoho properties出现错误",e);
        }
        logger.error("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
    }
    @Test
    public void retriveModuleRecord(){
        retriveModuleRecordID();
    }

    private void retriveModuleRecordID() {
        //TODO getRecordById
        try {
            String id_Accounts = "85333000000088001";//客户1ID
            String id_SO = "85333000000089051";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecordById";
            String targetURL_SO = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/getRecordById";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id_SO);
//            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_SO);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行搜索Module操作出现错误",e);
        }
    }

    /**
     * 暂时只支持Accounts和SO的更新
     * 根据id:ACCOUNTID:85333000000089007 三一重工/85333000000088001 客户1
     * sample Accounts response with null data :<response uri="/crm/private/xml/Accounts/getRecords"><result><Accounts><row no="1"><FL val="ACCOUNTID">85333000000089007</FL><FL val="SMOWNERID">85333000000071001</FL><FL val="Account Owner"><![CDATA[tree3170]]></FL><FL val="Account Name"><![CDATA[三一重工]]></FL><FL val="Phone"><![CDATA[null]]></FL><FL val="Fax"><![CDATA[null]]></FL><FL val="Website"><![CDATA[null]]></FL><FL val="Created Time"><![CDATA[2016-08-22 23:51:06]]></FL><FL val="Modified Time"><![CDATA[2016-08-22 23:51:06]]></FL><FL val="Last Activity Time"><![CDATA[2016-08-23 00:07:52]]></FL><FL val="CustomerNO"><![CDATA[null]]></FL><FL val="MailAddress"><![CDATA[null]]></FL><FL val="Direct"><![CDATA[null]]></FL><FL val="CreationTime"><![CDATA[null]]></FL><FL val="DeliveryAddress"><![CDATA[null]]></FL><FL val="Email"><![CDATA[null]]></FL><FL val="LatestEditBy"><![CDATA[null]]></FL><FL val="LatestEditTime"><![CDATA[null]]></FL><FL val="Enabled"><![CDATA[false]]></FL><FL val="DeliveryMethod"><![CDATA[null]]></FL><FL val="PostNo"><![CDATA[null]]></FL><FL val="CountryID"><![CDATA[null]]></FL><FL val="State"><![CDATA[null]]></FL><FL val="City"><![CDATA[null]]></FL><FL val="Remark"><![CDATA[null]]></FL><FL val="Contact"><![CDATA[null]]></FL></row></Accounts></result></response>
     * sample Accounts response without null data :<response uri="/crm/private/xml/Accounts/getRecords"><result><Accounts><row no="1"><FL val="ACCOUNTID">85333000000089007</FL><FL val="SMOWNERID">85333000000071001</FL><FL val="Account Owner"><![CDATA[tree3170]]></FL><FL val="Account Name"><![CDATA[三一重工]]></FL><FL val="Created Time"><![CDATA[2016-08-22 23:51:06]]></FL><FL val="Modified Time"><![CDATA[2016-08-22 23:51:06]]></FL><FL val="Last Activity Time"><![CDATA[2016-08-23 00:07:52]]></FL><FL val="Enabled"><![CDATA[false]]></FL></row></Accounts></result></response>
     * 注意：
     * 1. 修改所有者必需包含ID和Name
     * 2. SO：Unit Price为什么在API修改不了，但在页面可以
     * 3. 关于product details中的里面表示总计的有4个字段但是在API中就Total和Net Total
     *
     */
    @Test
    public void testUpdateModuleRecord(){
        try {
            String id_Accounts = "85333000000088001";//客户1ID
            String id_SO = "85333000000089051";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/updateRecords";
            String targetURL_SO = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/updateRecords";
//            String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Leads><row no=\"1\"><FL val=\"LEADID\">85333000000072001</FL><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Lead Owner\"><![CDATA[qq qq]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
//            String xmlData = "<Leads><row no=\"1\"><FL val=\"Lead Owner\"><![CDATA[tree3170]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
//            xmlData = "<Leads><row no=\"1\"><FL val=\"Salutation\"><![CDATA[先生]]></FL></row></Leads>";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
//            String  xmlData = "<Accounts><row no=\"1\"><FL val=\"ACCOUNTID\">85333000000089007</FL><FL val=\"SMOWNERID\">85333000000071001</FL><FL val=\"Account Owner\"><![CDATA[tree3170]]></FL><FL val=\"Account Name\"><![CDATA[三一重工]]></FL><FL val=\"Phone\"><![CDATA[null]]></FL><FL val=\"Fax\"><![CDATA[null]]></FL><FL val=\"Website\"><![CDATA[null]]></FL><FL val=\"Created Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-23 00:07:52]]></FL><FL val=\"CustomerNO\"><![CDATA[null]]></FL><FL val=\"MailAddress\"><![CDATA[null]]></FL><FL val=\"Direct\"><![CDATA[null]]></FL><FL val=\"CreationTime\"><![CDATA[null]]></FL><FL val=\"DeliveryAddress\"><![CDATA[null]]></FL><FL val=\"Email\"><![CDATA[null]]></FL><FL val=\"LatestEditBy\"><![CDATA[null]]></FL><FL val=\"LatestEditTime\"><![CDATA[null]]></FL><FL val=\"Enabled\"><![CDATA[false]]></FL><FL val=\"DeliveryMethod\"><![CDATA[null]]></FL><FL val=\"PostNo\"><![CDATA[null]]></FL><FL val=\"CountryID\"><![CDATA[null]]></FL><FL val=\"State\"><![CDATA[null]]></FL><FL val=\"City\"><![CDATA[null]]></FL><FL val=\"Remark\"><![CDATA[null]]></FL><FL val=\"Contact\"><![CDATA[null]]></FL></row></Accounts>";
            String xmlDataWithoutNull_Accounts = "<Accounts><row no=\"1\"><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Account Owner\"><![CDATA[qq]]></FL><FL val=\"Account Name\"><![CDATA[THINK Pad]]></FL><FL val=\"Created Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-23 00:07:52]]></FL><FL val=\"Enabled\"><![CDATA[true]]></FL><FL val=\"LatestEditBy\"><![CDATA[qq]]></FL><FL val=\"LatestEditTime\"><![CDATA[2016-09-15 23:51:06]]></FL><FL val=\"Phone\"><![CDATA[12345678]]></FL><FL val=\"Website\"><![CDATA[https://crm.zoho.com.cn/crm]]></FL></row></Accounts>";
            String xmlDataWithoutNull_SO = "<SalesOrders><row no=\"1\"><FL val=\"SO Number\"><![CDATA[85333000000089054]]></FL><FL val=\"Subject\"><![CDATA[三一重工合同]]></FL><FL val=\"ACCOUNTID\">85333000000089007</FL><FL val=\"Account Name\"><![CDATA[三一重工]]></FL><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Sales Order Owner\"><![CDATA[qq]]></FL><FL val=\"Sub Total\"><![CDATA[79445]]></FL><FL val=\"Discount\"><![CDATA[0]]></FL><FL val=\"Tax\"><![CDATA[0]]></FL><FL val=\"Adjustment\"><![CDATA[0]]></FL><FL val=\"Grand Total\"><![CDATA[79441]]></FL><FL val=\"Product Details\"><product no=\"1\"><FL val=\"Product Id\">85333000000089011</FL><FL val=\"Product Name\"><![CDATA[服务器]]></FL><FL val=\"Unit Price\">7224.0</FL><FL val=\"Quantity\">12.0</FL><FL val=\"Quantity in Stock\">10.0</FL><FL val=\"Total\">79442.0</FL><FL val=\"Discount\">1.0</FL><FL val=\"Total After Discount\">79443.0</FL><FL val=\"List Price\">7222.0</FL><FL val=\"Net Total\">79444.0</FL><FL val=\"Tax\">0.0</FL><FL val=\"Product Description\"><![CDATA[测试Product的描述]]></FL></product></FL></row></SalesOrders>";
            Map<String,String> postParams = new HashMap<String, String>();
//            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,xmlDataWithoutNull_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_ID,id_SO);
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_SO);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,xmlDataWithoutNull_SO);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
           logger.error("执行更新Module操作出现错误",e);
        }
    }

    @Test
    public void testAddModuleRecord(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/insertRecords";
//            String targetURL_SO = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/insertRecords";
//            String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Leads><row no=\"1\"><FL val=\"LEADID\">85333000000072001</FL><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Lead Owner\"><![CDATA[qq qq]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
//            String xmlData = "<Leads><row no=\"1\"><FL val=\"Lead Owner\"><![CDATA[tree3170]]></FL><FL val=\"Company\"><![CDATA[qq's company2]]></FL><FL val=\"First Name\"><![CDATA[qq's first name]]></FL><FL val=\"Last Name\"><![CDATA[qq's last name]]></FL><FL val=\"No of Employees\"><![CDATA[0]]></FL><FL val=\"Annual Revenue\"><![CDATA[0]]></FL><FL val=\"Email Opt Out\"><![CDATA[false]]></FL><FL val=\"SMCREATORID\">85333000000071039</FL><FL val=\"Created By\"><![CDATA[qq qq]]></FL><FL val=\"MODIFIEDBY\">85333000000071001</FL><FL val=\"Modified By\"><![CDATA[tree3170]]></FL><FL val=\"Created Time\"><![CDATA[2016-07-13 23:58:11]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-21 15:25:07]]></FL><FL val=\"Salutation\"><![CDATA[先生]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-21 15:25:07]]></FL></row></Leads>";
//            xmlData = "<Leads><row no=\"1\"><FL val=\"Salutation\"><![CDATA[先生]]></FL></row></Leads>";

//            String  xmlData = "<Accounts><row no=\"1\"><FL val=\"ACCOUNTID\">85333000000089007</FL><FL val=\"SMOWNERID\">85333000000071001</FL><FL val=\"Account Owner\"><![CDATA[tree3170]]></FL><FL val=\"Account Name\"><![CDATA[三一重工]]></FL><FL val=\"Phone\"><![CDATA[null]]></FL><FL val=\"Fax\"><![CDATA[null]]></FL><FL val=\"Website\"><![CDATA[null]]></FL><FL val=\"Created Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-23 00:07:52]]></FL><FL val=\"CustomerNO\"><![CDATA[null]]></FL><FL val=\"MailAddress\"><![CDATA[null]]></FL><FL val=\"Direct\"><![CDATA[null]]></FL><FL val=\"CreationTime\"><![CDATA[null]]></FL><FL val=\"DeliveryAddress\"><![CDATA[null]]></FL><FL val=\"Email\"><![CDATA[null]]></FL><FL val=\"LatestEditBy\"><![CDATA[null]]></FL><FL val=\"LatestEditTime\"><![CDATA[null]]></FL><FL val=\"Enabled\"><![CDATA[false]]></FL><FL val=\"DeliveryMethod\"><![CDATA[null]]></FL><FL val=\"PostNo\"><![CDATA[null]]></FL><FL val=\"CountryID\"><![CDATA[null]]></FL><FL val=\"State\"><![CDATA[null]]></FL><FL val=\"City\"><![CDATA[null]]></FL><FL val=\"Remark\"><![CDATA[null]]></FL><FL val=\"Contact\"><![CDATA[null]]></FL></row></Accounts>";
            String xmlDataWithoutNull_Accounts = "<Accounts><row no=\"1\"><FL val=\"SMOWNERID\">85333000000071039</FL><FL val=\"Account Owner\"><![CDATA[qq]]></FL><FL val=\"Account Name\"><![CDATA[IBM]]></FL><FL val=\"Created Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-22 23:51:06]]></FL><FL val=\"Last Activity Time\"><![CDATA[2016-08-23 00:07:52]]></FL><FL val=\"Enabled\"><![CDATA[true]]></FL><FL val=\"LatestEditBy\"><![CDATA[qq]]></FL><FL val=\"LatestEditTime\"><![CDATA[2016-09-15 23:51:06]]></FL><FL val=\"Phone\"><![CDATA[12345678]]></FL><FL val=\"Website\"><![CDATA[https://crm.zoho.com.cn/crm]]></FL></row></Accounts>";
//            String xmlDataWithoutNull_SO = "<SalesOrders><row no=\"1\"><FL val=\"SO Number\"><![CDATA[85333000000089054]]></FL><FL val=\"Subject\"><![CDATA[三一重工合同]]></FL><FL val=\"ACCOUNTID\">85333000000089007</FL><FL val=\"Account Name\"><![CDATA[三一重工]]></FL><FL val=\"SMOWNERID\">85333000000071001</FL><FL val=\"Sales Order Owner\"><![CDATA[tree3170]]></FL><FL val=\"Sub Total\"><![CDATA[79445]]></FL><FL val=\"Discount\"><![CDATA[0]]></FL><FL val=\"Tax\"><![CDATA[0]]></FL><FL val=\"Adjustment\"><![CDATA[0]]></FL><FL val=\"Grand Total\"><![CDATA[79441]]></FL><FL val=\"Product Details\"><product no=\"1\"><FL val=\"Product Id\">85333000000089011</FL><FL val=\"Product Name\"><![CDATA[服务器]]></FL><FL val=\"Unit Price\">7224.0</FL><FL val=\"Quantity\">12.0</FL><FL val=\"Quantity in Stock\">10.0</FL><FL val=\"Total\">79442.0</FL><FL val=\"Discount\">1.0</FL><FL val=\"Total After Discount\">79443.0</FL><FL val=\"List Price\">7222.0</FL><FL val=\"Net Total\">79444.0</FL><FL val=\"Tax\">0.0</FL><FL val=\"Product Description\"><![CDATA[测试Product的描述]]></FL></product></FL></row></SalesOrders>";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,xmlDataWithoutNull_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_SO);
//            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,xmlDataWithoutNull_SO);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }

    @Test
    public void deleteModuleRecord(){
        //TODO getRecordById
        try {
            String id_Accounts = "85333000000108003";//客户1ID
            String id_SO = "85333000000089051——12";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/deleteRecords";
            String targetURL_SO = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/deleteRecords";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id_SO);
//            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_SO);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行删除Module操作出现错误",e);
        }
    }



}
