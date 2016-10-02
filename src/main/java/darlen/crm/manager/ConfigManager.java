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
import darlen.crm.util.ThreadLocalDateUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.Date;
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

      //    * 1 --> zoho.properties ; db.properties, 对于确定的key value的properties文件，可以用默认的Map
      private static Map<String,Object> defaultMap = new HashMap<String,Object>();
    private static final  int DEFAULT_ZOHO_DB_FILE_INDEX = 1;
      //    * 2 --> Accounts.properties;
      private static Map<String,String> zohoUserPropsMap = new HashMap<String, String>();
    private static final  int ZOHOUSER_FILE_INDEX = 2;
      //    * 3 --> Product.properties;
      private static Map<String,String> prodsPropsMap = new HashMap<String, String>();
    private static final  int PROD_FILE_INDEX = 3;
      //    * 4 --> Accounts.properties;
      private static Map<String,String> acctsPropsMap = new HashMap<String, String>();
    private static final  int ACCT_FILE_INDEX = 4;


    private ConfigManager(){}

//    public static void getInstance(){
//        ConfigManager.initZohoProps();
//    }

    /**
     * 获取内容,这部分key一定是固定点的
     * @param fileName: 1 -->zoho.properties, db.properties , 2 -->Accounts.properties； 3 --> Product.properties;
     * @param property
     * @return
     */
    public static String get(String fileName, String property) throws ConfigurationException, IOException {
        if(!defaultMap.containsKey(fileName)) {
            configManager.initConfig(fileName, DEFAULT_ZOHO_DB_FILE_INDEX);
        }
        PropertiesConfiguration config = (PropertiesConfiguration) defaultMap.get(fileName);
        return config == null ? "" : StringUtils.nullToString(config.getString(property));

    }

    /**
     * Accounts.properties
     * @param property
     * @return
     */
    public static String getZohoUserfromProps(String property) throws ConfigurationException, IOException {
        if(zohoUserPropsMap.size() == 0){
            configManager.initConfig(Constants.PROPS_USER_FILE, ZOHOUSER_FILE_INDEX);
        }
        return StringUtils.nullToString(zohoUserPropsMap.get(property));
    }
    /**
     * Product.properties
     * @param property
     * @return
     */
    public static String getProdfromProps( String property) throws Exception {
        if(prodsPropsMap.size() == 0){
            configManager.initConfig(Constants.PROPS_PROD_FILE, PROD_FILE_INDEX);
//            configManager.initConfig(Constants.PROPS_DB_FILE, true);
        }
        return StringUtils.nullToString(prodsPropsMap.get(property));
    }

    /**
     * Product.properties
     * @param property
     * @return
     */
    public static String getAcctsfromProps( String property) throws Exception {
        if(acctsPropsMap.size() == 0){
            configManager.initConfig(Constants.PROPS_ACCT_FILE, ACCT_FILE_INDEX);
//            configManager.initConfig(Constants.PROPS_DB_FILE, true);
        }
        return StringUtils.nullToString(acctsPropsMap.get(property));
    }


    /**
     * 根据Map的值写入某个配置文件
     * @param map
     * @param configName
     */
    public static void writeVal2Props( Map<String,String> map,String configName)  {
        logger.debug("# 写入Product.properties文件：erpID<-->zohoID, configName="+configName);
        PrintWriter writer = null;
        try{
            URL url = ClassLoader.getSystemResource(configName);
            File file = new File(url.toURI());
            writer = new PrintWriter(file, "UTF-8");
            writer.println("#这个是ZOHO的propertes mapping表，左边是ERP ID ，右边是ZOHO ID ");
            for(Map.Entry<String,String> entry : map.entrySet()){
                writer.println(StringUtils.nullToString(entry.getKey())+"="+StringUtils.nullToString(entry.getValue()));
            }
            writer.println("# 写入结束时间： " + ThreadLocalDateUtil.formatDate(new Date()));
        }catch (Exception e){
            logger.error("# 写入"+configName +"出错",e);
        }finally {
            if(null != writer) {
                writer.close();
            }
        }
    }



    /**
     * 载入配置文件，初始化后加入map
     * fileNameKey :
     * DEFAULT_ZOHO_DB_FILE_INDEX --> zoho.properties ; db.properties
     * ZOHOUSER_FILE_INDEX --> Accounts.properties;
     * PROD_FILE_INDEX --> Product.properties;
     * ACCT_FILE_INDEX --> Product.properties;
     * @param configFile
     */
    private synchronized void initConfig(String configFile,int fileNameKey) throws ConfigurationException, IOException {
        try {
            if( DEFAULT_ZOHO_DB_FILE_INDEX == fileNameKey ){
                PropertiesConfiguration config = new PropertiesConfiguration(configFile);
                defaultMap.put(configFile, config);
            }else{
                Properties prop = new Properties();
                //1. 直接送Source目录拿Config文件
                // ConfigManager.class.getResourceAsStream(configFile);
                //2. 直接从classPath中拿Config文件，CommonUtils.getFileNamePath("",configFile)
                prop.load(new FileInputStream(CommonUtils.getFileNamePath("",configFile)) );
                for(Map.Entry entry : prop.entrySet()){
                    if(ZOHOUSER_FILE_INDEX == fileNameKey){
                        zohoUserPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }else if(PROD_FILE_INDEX == fileNameKey){
                        prodsPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                        CommonUtils.printMap(prodsPropsMap," Product.properties的具体内容");
                    }else if(ACCT_FILE_INDEX == fileNameKey){
                        acctsPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                        CommonUtils.printMap(acctsPropsMap," Accounts.properties的具体内容");
                    }

                }
                // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
                // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
//                URL url = ClassLoader.getSystemResource("secure/Product.properties");
//                File file = new File(url.toURI());
//                OutputStream fos = new FileOutputStream(file);
//                prop.setProperty(new String(String.valueOf("name sd sz,").getBytes(), "UTF-8"),  new String(String.valueOf("test sdf;';").getBytes(), "UTF-8"));
//                // 以适合使用 load 方法加载到 Properties 表中的格式，
//                // 将此 Properties 表中的属性列表（键和元素对）写入输出流
//                prop.store(fos, "Update '" + "ddd" + "' value");
            }


        } catch (ConfigurationException e) {
            logger.error("Intial PropertiesConfiguration出错",e);
            throw e;
        } catch (IOException e) {
            logger.error("Intial PropertiesConfiguration出错", e);
            throw e;
        }
    }
    public static void main(String [] arges) throws Exception {
//        System.out.println(ConfigManager.get("secure/db.properties", "DB_USERNAME"));
//        ConfigManager.getProdfromProps("secure/db.properties");
//        System.out.println(zohoUserPropsMap.get("Gary Tang"));
//        writeVal2Props(zohoUserPropsMap,Constants.PROPS_PROD_FILE);
//        URL url = ClassLoader.getSystemResource("secure/Product.properties");
//        File file = new File(url.toURI());
//        System.out.println(url.toURI());
//        System.out.println();
//        OutputStream fos = new FileOutputStream("./secure/Product.properties");
    }
    private void test(){
        //ClassLoader.getResource("");
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
