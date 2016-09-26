/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Module.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.common;

import darlen.crm.util.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.model.common
 * Description：ZOHO_CRM
 * Created on  2016/09/25 10：37
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        10：37   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public abstract  class Module {
    private static Logger logger =  Logger.getLogger(Module.class);
    /**
     * 使用ZOHO API时必需要的密钥
     */
    public static String AUTHTOKEN ="";
    /**
     * 查询的数据中不包含null
     */
    public static String NEWFORMAT_1 ="1";
    /**
     * 查询的数据中包含null
     */
    public static String NEWFORMAT_2 ="2";//include null
    /**
     * API 使用范围
     */
    public static String SCOPE ="crmapi";
    public static String MODULES= "";
    /**传递方式为JSON或者XML，默认是xml*/
    public static String FORMAT="xml";
    public static Map<String,String> zohoPropsMap = new HashMap<String,String>();


    public  void getProperties(){
        initZohoProps();
    }


    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
    private static void initZohoProps() {
        try {
            Properties prop = new Properties();
            prop.load(Module.class.getResourceAsStream("/secure/zoho.properties"));
            AUTHTOKEN = prop.getProperty(Constants.ZOHO_PROPs_AUTHTOKEN_TREE);
            NEWFORMAT_1 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_1);
            NEWFORMAT_2 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_2);
            SCOPE = prop.getProperty(Constants.HTTP_POST_PARAM_SCOPE);
            MODULES = prop.getProperty(Constants.ZOHO_PROPS_MODULES);
            //for url TODO  将来需要把properties中的字段全部放入到Cache或者静态变量中
            for(Map.Entry entry : prop.entrySet()){
                zohoPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        } catch(IOException e) {
            logger.error("读取zoho properties出现错误",e);
//            System.err.println("读取zoho properties出现错误"+e.getMessage());
        }
        logger.debug("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
//        System.err.println("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
    }
}
