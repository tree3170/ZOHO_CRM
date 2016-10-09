/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ConfigManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.util.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
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
    private static Map<String,String> timePropsMap = new HashMap<String, String>();
    private static final  int TIME_FILE_INDEX = 5;


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
     * Account.properties
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

    public static long getTimefromProps( String property) throws Exception {
        if(timePropsMap.size() == 0){
            configManager.initConfig(Constants.PROPS_TIME_FILE, TIME_FILE_INDEX);
//            configManager.initConfig(Constants.PROPS_DB_FILE, true);
        }
        long retVal = 0;
        if(!"0".equals(StringUtils.nullToString(timePropsMap.get(property)))){
            retVal = ThreadLocalDateUtil.parse(StringUtils.nullToString(timePropsMap.get(property))).getTime();
        }
        return retVal;
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
                //使用相对路径
                PropertiesConfiguration config = new PropertiesConfiguration(configFile);
                defaultMap.put(configFile, config);
            }else{
                Properties prop = new Properties();
                //1. 直接送Source目录拿Config文件
                // ConfigManager.class.getResourceAsStream(configFile);
                //2. 直接从classPath中拿Config文件，CommonUtils.getFileNamePath("",configFile)
                //prop.load(new FileInputStream(CommonUtils.getFileNamePath("",configFile)) );
                //3.使用绝对路径，当然也可以采用方法2
                prop.load(ConfigManager.class.getResourceAsStream("/"+configFile));
                for(Map.Entry entry : prop.entrySet()){
                    if(ZOHOUSER_FILE_INDEX == fileNameKey){
                        zohoUserPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }else if(PROD_FILE_INDEX == fileNameKey){
                        prodsPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }else if(ACCT_FILE_INDEX == fileNameKey){
                        acctsPropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }else if(TIME_FILE_INDEX == fileNameKey){
                        timePropsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }
                }
                if(ZOHOUSER_FILE_INDEX == fileNameKey){
                    CommonUtils.printMap(zohoUserPropsMap," zohoUser.properties的具体内容");
                }
                if(PROD_FILE_INDEX == fileNameKey){
                     CommonUtils.printMap(prodsPropsMap," Product.properties的具体内容");
                }
                if(ACCT_FILE_INDEX == fileNameKey){
                     CommonUtils.printMap(acctsPropsMap," Accounts.properties的具体内容");
                }
                if(TIME_FILE_INDEX == fileNameKey){
                    CommonUtils.printMap(timePropsMap," lastExecSuccessTime.properties的具体内容");
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

    /**
     * 根据Map的值写入某个配置文件
     * @param map
     * @param configName
     */
    public static void writeVal2Props( Map<String,String> map,String configName)  {
        logger.debug("# 写入Product.properties,Accounts.properties,lastExecSuccessTime.properties文件：erpID<-->zohoID, configName="+configName);
        PrintWriter writer = null;
        try{
//            URL url = ClassLoader.getSystemResource(configName);
//            File file = new File(url.toURI());
            // 用相对路径，不需要前面的"/"
            //TODO 如果文件不存在就创建
            File file = new File(CommonUtils.getFileNamePath("",configName));
            writer = new PrintWriter(file, "UTF-8");
            if( Constants.PROPS_ACCT_FILE.equals(configName)){
                writer.println("#这个是一个有关DB中的Customer表和ZOHO中的Accounts模块映射文件Accounts.properties，左边是DB中的ID，即ERP ID ，右边是ZOHO客户中的ZOHO ID ");
            }else if(Constants.PROPS_PROD_FILE.equals(configName)){
                writer.println("#这个是一个有关DB中的Item表和ZOHO中的Products模块映射文件Products.properties，左边是关于产品的ID， 即ERP ID ，右边是ZOHO产品中的ZOHO ID ");
            }else if(Constants.PROPS_TIME_FILE.equals(configName)){
                writer.println("##上次执行成功的时间，第一次默认是0 ， 格式为YYYY-MM-DD HH:MM:SS");
            }
            for(Map.Entry<String,String> entry : map.entrySet()){
                writer.println(cvtPropsSpace(StringUtils.nullToString(entry.getKey()))+"="+ cvtPropsSpace(StringUtils.nullToString(entry.getValue())));
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
     * 注意相对路径
     * //http://riddickbryant.iteye.com/blog/436693
     * 前面有"/"--> 根路径，也就是classes目录下的查找的文件  --> 绝对路径
     * 如果前面没有"/"，代表本目录下查找的文件--> 相对路径
     * @param relativePath
     * @return
     * @throws IOException
     */
    public static Properties readProperties(String relativePath) throws IOException {
        Properties prop = new Properties();
        try {
            //用绝对路径，需要前面的"/"
            prop.load(CommonUtils.class.getResourceAsStream("/"+relativePath));// "/secure/db.properties"
        } catch(IOException e) {
            logger.error("读取properties文件出错【"+relativePath+"】",e);
            throw e;
        }

        //TODO 开发环境中禁止打印
        String propValues = "打印properties的键值对：：：";
        for(Object obj : prop.keySet()){
            propValues += obj+"="+prop.get(obj)+"; ";
        }
        logger.debug(propValues);

        return  prop;
    }

    /**
     * 是开发模式吗？ 1 --》开发模式
     * @return
     * @throws IOException
     * @throws ConfigurationException
     */
    public static boolean isDevMod()  {
        try {
            return  "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
        } catch (ConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

    /**
     * 转化properties中的空格为
     * 因为如果不转化的话，后面读取properties的时候会自动把空格转化为“=”，导致读取数据不正确
     * @param propsKy
     * @return
     */
    private static String cvtPropsSpace(String propsKy){
        return propsKy.replace(" ", "\\u0020");
    }

    /**
     * 环境自检
     */
    public static void envAutoChecking(){
        try{
            //测试写入文件是否能写入成功
            testWriteVal2Props();

            //测试secure/*.properties的5个properties是否能读写成功
            testGetProps();

            //测试
            DBUtils.getConnection();
        }catch (Exception e){
            logger.error("环境测试： 读取配置文件出现问题,程序终止...",e);
        }
    }
    public static void main(String [] arges) throws Exception {
//        System.out.println(ConfigManager.get("secure/db.properties", "DB_USERNAME"));
//        System.out.println("======"+ConfigManager.getProdfromProps("marketing"));
        //
//        System.out.println(zohoUserPropsMap.get("Gary Tang"));
//        writeVal2Props(zohoUserPropsMap,Constants.PROPS_PROD_FILE);
//        URL url = ClassLoader.getSystemResource("secure/Product.properties");
//        File file = new File(url.toURI());
//        System.out.println(url.toURI());
//        System.out.println();
//        OutputStream fos = new FileOutputStream("./secure/Product.properties");
//        envAutoChecking();
        System.err.println("6. lastExecSuccessTime.properties======LAST_EXEC_SUCCESS_TIME="+ConfigManager.getTimefromProps(Constants.LAST_EXEC_SUCCESS_TIME));
        Map<String,String> map = new HashMap<String, String>();
        map.put("LAST_EXEC_SUCCESS_TIME","2016-10-10 00:43:54");
        writeVal2Props(map, Constants.PROPS_TIME_FILE);
        System.err.println("6. lastExecSuccessTime.properties======LAST_EXEC_SUCCESS_TIME="+ConfigManager.getTimefromProps(Constants.LAST_EXEC_SUCCESS_TIME));


    }


    private static void testGetProps() throws Exception {
        System.err.println("########################获取5个properties中的value######################");
        //"secure/db.properties"
        System.err.println("1. db.properties=========DB_USERNAME="+ConfigManager.get(Constants.PROPS_DB_FILE, "DB_USERNAME"));
        //"secure/zoho.properties"
        System.err.println("2. zoho.properties=========DEV_MODE="+ConfigManager.get(Constants.PROPS_ZOHO_FILE, "DEV_MODE"));
        System.err.println("3. Products.properties======0="+ConfigManager.getProdfromProps("0"));
        System.err.println("4. Accounts.properties======0="+ConfigManager.getAcctsfromProps("0"));
        System.err.println("5. zohoUser.properties======Gary Tang="+ConfigManager.getZohoUserfromProps("Gary Tang"));
        System.err.println("6. lastExecSuccessTime.properties======LAST_EXEC_SUCCESS_TIME=="+ConfigManager.getTimefromProps("LAST_EXEC_SUCCESS_TIME"));
        //"/secure/db.properties"
        System.err.println("7. 读取/mapping/*.properties======marketing="+ConfigManager.readProperties(Constants.PROPS_DB_FILE));
    }
    private static void testWriteVal2Props(){
        System.out.println("测试写入文件Accounts");
        Map<String,String> map = new HashMap<String, String>();
        map.put(cvtPropsSpace("0"),"123456789_tree_account");
        map.put(cvtPropsSpace("1"),"80487000000099661");
        map.put(cvtPropsSpace("2"),"80487000000099663");
        writeVal2Props(map, Constants.PROPS_ACCT_FILE);
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
