/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ConfigManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.manager
 * Description：ZOHO_CRM
 * Created on  2016/09/27 22：29
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：29   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class ConfigManager {
    private static Logger logger = Logger.getLogger(ConfigManager.class);
    private static ConfigManager configManager = new ConfigManager();
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

//    public static Map<String,String> zohoDBPropsMap = new HashMap<String, String>();

//    public static Map<String,String> zohoPropsMap = new HashMap<String,String>();
      private static Map<String,Object> zohoMap = new HashMap<String,Object>();
      private static Map<String,String> acctPropsMap = new HashMap<String, String>();


    private ConfigManager(){}

//    public static void getInstance(){
//        ConfigManager.initZohoProps();
//    }

    /**
     * 获取内容,这部分key一定是固定点的
     * @param fileName: 1 -->zoho.properties, 2 -->db.properties , 3 -->Accounts.properties
     * @param property
     * @return
     */
    public static String get(String fileName, String property) throws IOException, ConfigurationException {
        if(!zohoMap.containsKey(fileName)) {
            configManager.initConfig(fileName,false);
        }
        PropertiesConfiguration config = (PropertiesConfiguration) zohoMap.get(fileName);
        return config == null ? "" : StringUtils.nullToString(config.getString(property));

    }

    /**
     * Accounts.properties
     * @param property
     * @return
     */
    public static String getAcctfromProps( String property) throws IOException, ConfigurationException {
        if(acctPropsMap.size() == 0){
            configManager.initConfig(Constants.PROPS_ACCT_3, true);
        }
        return StringUtils.nullToString(acctPropsMap.get(property));
    }


    /**
     * 载入配置文件，初始化后加入map
     * @param configFile
     */
    private synchronized void initConfig(String configFile,boolean isAcct) throws ConfigurationException, IOException {
        try {
            if(isAcct){
                Properties prop = new Properties();
                prop.load(ConfigManager.class.getResourceAsStream(configFile));
                for(Map.Entry entry : prop.entrySet()){
                    acctPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
                }
            }else{
                PropertiesConfiguration config = new PropertiesConfiguration(configFile);
                zohoMap.put(configFile, config);
            }

        } catch (ConfigurationException e) {
            logger.error("Intial PropertiesConfiguration出错",e);
            throw e;
        } catch (IOException e) {
            logger.error("Intial PropertiesConfiguration出错", e);
            throw e;
        }
    }
    public static void main(String [] arges) throws IOException, ConfigurationException {
        ConfigManager.get("secure/db.properties", "DB_USERNAME");
    }

    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
//    private synchronized static void initZohoProps() {
//        try {
//            Properties prop = new Properties();
//            prop.load(ConfigManager.class.getResourceAsStream("/secure/zoho.properties"));
//            AUTHTOKEN = prop.getProperty(Constants.ZOHO_PROPs_AUTHTOKEN_TREE);
//            NEWFORMAT_1 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_1);
//            NEWFORMAT_2 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_2);
//            SCOPE = prop.getProperty(Constants.HTTP_POST_PARAM_SCOPE);
//            MODULES = prop.getProperty(Constants.ZOHO_PROPS_MODULES);
//            //for url TODO  将来需要把properties中的字段全部放入到Cache或者静态变量中
//            for(Map.Entry entry : prop.entrySet()){
//                zohoPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
//            }
//
//            prop = new Properties();
//            prop.load(ConfigManager.class.getResourceAsStream("/secure/db.properties"));
//            //for url TODO  将来需要把properties中的字段全部放入到Cache或者静态变量中
//            for(Map.Entry entry : prop.entrySet()){
//                zohoDBPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
//            }
//            prop = new Properties();
//            prop.load(ConfigManager.class.getResourceAsStream("/secure/Accounts.properties"));
//            for(Map.Entry entry : prop.entrySet()){
//                accountPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
//            }
//        } catch(IOException e) {
//            logger.error("读取zoho properties出现错误",e);
////            System.err.println("读取zoho properties出现错误"+e.getMessage());
//        }
////        logger.debug("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
//        CommonUtils.printMap(zohoPropsMap, "db.properties的value：：：");
//        CommonUtils.printMap(zohoDBPropsMap, "db.properties的value：：：");
//        CommonUtils.printMap(accountPropsMap,"db.properties的value：：：");
////        System.err.println("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
//    }

}
