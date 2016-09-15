/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   RetrieveFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.fields;

import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.fields   这是一个获取每个功能的fields的功能,
 * ***包括获取某个Module的所有字段，获取某个modules的所有数据
 * Description：ZOHO_CRM
 * Created on  2016/09/15 18：10
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        18：10   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class HandleFields {
    private static HandleFields fields;
    private static Logger logger =  Logger.getLogger(HandleFields.class);
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
        fields = new HandleFields();
        initZohoProps();
    }


    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
    private static void initZohoProps() {
        try {
            Properties prop = new Properties();
            prop.load(HandleFields.class.getResourceAsStream("/secure/zoho.properties"));
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
    public void getModulesFields() {
        if(!StringUtils.isEmptyString(MODULES)){
            String[] modulesArr = MODULES.split(",");
            for(String module : modulesArr){
                System.err.println("当前操作的Module是："+module);
                getModuleField(FORMAT, module);
            }
        }
    }

    @Test
    public void getModulesRecords() {
        if(!StringUtils.isEmptyString(MODULES)){
            String[] modulesArr = MODULES.split(",");
            for(String module : modulesArr){
                System.err.println("当前操作的Module是："+module);
                getModuleRecord(FORMAT, module);
            }
        }

    }


    /**
     * 更新Leads数据
     * 1.修改线索拥有人时必需是SMOWNERID + Lead Owner同时存在，否则结果就算成功但是这个线索拥有人也不会更新
     * 2. created/updated by /time 等系统字段，不能自己设定
     * 3. 可以填写不存在的值，比如线索名前面的称呼
     *  sample response=======>
     *  <Accounts>
     <section name="Account Information" dv="客户信息">
     <FL req="false" type="Lookup" isreadonly="false" maxlength="120"  label="Account Owner" dv="客户所有者" customfield="false"/>
     <FL req="true" type="Text" isreadonly="false" maxlength="200"  label="Account Name" dv="客户名" customfield="false"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="CustomerNO" dv="CustomerNO" customfield="true"/>
     <FL req="false" type="Boolean" isreadonly="false" maxlength="3"  label="Enabled" dv="Enabled" customfield="true" enabled="false"/>
     <FL req="false" type="Phone" isreadonly="false" maxlength="30"  label="Phone" dv="电话" customfield="false"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="30"  label="Fax" dv="传真" customfield="false"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="Contact" dv="Contact" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="Direct" dv="Direct" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="DeliveryAddress" dv="DeliveryAddress" customfield="true"/>
     <FL req="false" type="Email" isreadonly="false" maxlength="100"  label="Email" dv="Email" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="MailAddress" dv="MailAddress" customfield="true"/>
     <FL req="false" type="Website" isreadonly="false" maxlength="120"  label="Website" dv="网站" customfield="false"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="CountryID" dv="CountryID" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="PostNo" dv="PostNo" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="State" dv="State" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="City" dv="City" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="LatestEditTime" dv="LatestEditTime" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="DeliveryMethod" dv="DeliveryMethod" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="LatestEditBy" dv="LatestEditBy" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="CreationTime" dv="CreationTime" customfield="true"/>
     <FL req="false" type="Text" isreadonly="false" maxlength="250"  label="Remark" dv="Remark" customfield="true"/>
     <FL req="false" type="DateTime" isreadonly="false" maxlength="25"  label="Last Activity Time" dv="最近操作时间" customfield="false"/>
     </section>
     </Accounts>
     */
    public static void getModuleField(String format, String moduleName){
        try {
            String targetURL = "https://crm.zoho.com.cn/crm/private/"+format+"/"+moduleName+"/getFields";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
            CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
           logger.error("getField出现错误",e);
        }
    }


    /**
     * 获取某个module的所有Record
     */
    public static void getModuleRecord(String format, String moduleName) {
        try {
            String targetURL = "https://crm.zoho.com.cn/crm/private/"+format+"/"+moduleName+"/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
//            postParams.put("selectColumns",selectColumns);
//            postParams.put("fromIndex",fromIndex);
//            postParams.put("toIndex",toIndex);
            CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("getRecord出现错误",e);
        }
    }
}
