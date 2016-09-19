/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HanderAccouts.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.Accounts.FL;
import darlen.crm.jaxb_xml_object.Accounts.Response;
import darlen.crm.jaxb_xml_object.Accounts.Result;
import darlen.crm.jaxb_xml_object.Accounts.Row;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.model.result.Accounts;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 暂时在程序中不处理删除数据，只处理更新或者添加数据
 * ====================Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime)中： 参考（JaxbAccountsTest.java/JaxbSOTest.java/JaxbLeadsTest.java）
 * 1. 从ZOHO获取有效的xml
 * 2. xml 转 java bean
 * 3. 组装 zohoListObj ，其中里面的element有：
 * zohoIDMap<ERPID,ZOHOID> = zohoListObj.get(0)
 * zohoTimeMap<ERPID,lastEditTime> = zohoListObj.get(1)
 * delZOHOIDList:里面是所有 ERP ID 为空时的 ZOHO ID
 *
 *注意：
 * 1.ERPID，当ERPID为空时，把
 * 2.ZOHOID一定不能为空
 * 3.lastEditTime
 *
 * ===================Ⅱ：获取db数据，组装成 dbAcctList  : assembleDBAcctObjList()
 * 1.Accounts --> dbAcctList.get(0)
 * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
 *
 * ===================Ⅲ：组装需要真正需要传输到ZOHO的Account对象集合（判断zoho的 id 和 lastEditTime 是否有被修改）
 * 1.如果zohoid存在于dbModel中，则判断 lastEditTime是否被修改，如果修改了，则直接组装dbModel为xml并调用ZOHO中的更新API：updateAccountMap
 * 2.如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：delZOHOIDList
 * 3.如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：addAccountMap
 *
 *
 * ===================Ⅳ：组装updateAccountMap，
 *1. 获取每个Accounts对应的  dbFieldNameValueMap = getDBFieldNameValueMap<dbFieldName,FiledValue>
 *2. 根据dbRdAccountsFieldMapping.properties 过滤 dbFieldNameValueMap,  形成zohoFieldNameValueMap <zohoFieldName,FiledValue>
 *
 *
 * ===================Ⅴ：发送XML到ZOHO
 *
 * 注意账号信息：qq:85333000000071039, tree3170:85333000000071001
 * Description：ZOHO_CRM
 * Created on  2016/09/16 21：22
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：22   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class HandleAccounts {
    private static HandleAccounts handleAccounts;
    //
//    Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime)中： 参考（JaxbAccountsTest.java/JaxbSOTest.java/JaxbLeadsTest.java）
//
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
        handleAccounts = new HandleAccounts();
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
        logger.debug("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
    }

    /**
     * Ⅰ：这里仅仅只是组装zohoAcctObjList
     * 1. 从ZOHO获取有效的xml
     * 2. xml 转 java bean
     * 3. 组装 zohoListObj ，其中里面的element有：
     * zohoIDMap<ERPID,ZOHOID> = zohoListObj.get(0)
     * zohoTimeMap<ERPID,lastEditTime> = zohoListObj.get(1)
     * delZOHOIDList:里面是所有 ERP ID 为空时的 ZOHO ID
     */
    @Test
    public void assembleZOHOAcctObjListTest(){
        handleAccounts.assembleZOHOAcctObjList();
    }
    public List assembleZOHOAcctObjList(){
        //因为这里仅仅是测试数据这里仅仅只演示一条record，免得ZOHO的数据
        String id = "85333000000088001";//客户ID：THINK Pad
//        1. 从ZOHO获取有效的xml
        String zohoStr =  retriveZohoAcctRecordID(id);
//       2. xml 转 java bean
        System.out.println("zohoStr:::"+zohoStr);
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        System.out.println("response object:::"+response);
//     3. 组装 zohoAcctObjList
        List  zohoAcctObjList = handleAccounts.assembleZOHOList(response,"ACCOUNTID","CustomerID");
        Map<String,String> zohoIDMap = (Map)zohoAcctObjList.get(0);
        Map<String,String> zohoIDTimeMap = (Map)zohoAcctObjList.get(1);
        //TODO:暂时在test测序中不处理删除数据
        List delZOHOIDList = (List)zohoAcctObjList.get(2);
        CommonUtils.printMap(zohoIDMap,"ERPID 和ZOHOID Map");
        CommonUtils.printMap(zohoIDTimeMap,"ERPID 和LastEditTime Map");
        CommonUtils.printList(delZOHOIDList,"Remove ZOHO ID list");

        return zohoAcctObjList;
    }

    /**
     * Ⅱ：这里组装db中的AcctObjList
     * 1.Accounts --> dbAcctList.get(0)
     * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
     */
    @Test
    public void assembleDBAcctObjListTest(){
        handleAccounts.assembleDBAcctObjList();
    }
    public List assembleDBAcctObjList(){
        List dbAcctList = new ArrayList();
        Map<String,Accounts> idAccountsMap = new HashMap<String, Accounts>();
        Accounts accounts = getAcctDBObj(idAccountsMap);
        dbAcctList.add(accounts);
        dbAcctList.add(idAccountsMap);
        CommonUtils.printList(dbAcctList, "DB Account obj");
        return dbAcctList;
    }

    /**
     * Ⅲ：组装需要真正需要传输到ZOHO的Account对象集合
     * 1.如果zohoid存在于dbModel中，则判断 lastEditTime是否被修改，如果修改了，则直接组装dbModel为xml并调用ZOHO中的更新API：updateAccountMap
     * 2.如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：delZOHOIDList
     * 3.如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：addAccountMap
     * @return
     */
    @Test
    public void assembelSendToZOHOAcctListTest(){
        handleAccounts.assembelSendToZOHOAcctList();
    }
    public List assembelSendToZOHOAcctList(){
        List zohoAcctObjList = assembleZOHOAcctObjList();
        //Map<ERPID，ZOHOID>
        Map<String,String> erpZohoIDMap = (Map)zohoAcctObjList.get(0);
        Map<String,String> zohoIDTimeMap = (Map)zohoAcctObjList.get(1);
        List<String> delZOHOIDList = (List)zohoAcctObjList.get(2);
        List dbAcctList = assembleDBAcctObjList();
        Map<String,Accounts> idAccountsMap = (Map<String,Accounts>)dbAcctList.get(1);

        Map<String,Accounts> addAccountMap = new HashMap<String, Accounts>();
        Map<String,Accounts> updateAccountMap = new HashMap<String, Accounts>();
//        Map<String,Accounts> deleteAccountMap = new HashMap<String, Accounts>();

        for(Map.Entry<String,String> entry : zohoIDTimeMap.entrySet()){
            String erpID = entry.getKey();
            if(idAccountsMap.containsKey(erpID)){//update
                updateAccountMap.put(erpID,idAccountsMap.get(erpID));
            }else{ //delete
//                deleteAccountMap.put(erpID,idAccountsMap.get(erpID));
                if(!delZOHOIDList.contains(erpZohoIDMap.get(erpID))){
                     delZOHOIDList.add(erpZohoIDMap.get(erpID));
                }
            }
        }

        for(Map.Entry<String,Accounts> entry : idAccountsMap.entrySet()){
            String dbID = entry.getKey();
            if(!zohoIDTimeMap.containsKey(dbID)){//add
                addAccountMap.put(dbID,entry.getValue());
            }
        }

        List sendToZohoAcctList = new ArrayList();
        sendToZohoAcctList.add(addAccountMap);
        sendToZohoAcctList.add(updateAccountMap);
        sendToZohoAcctList.add(delZOHOIDList);

        return sendToZohoAcctList;
    }

    /**
     * Ⅳ：组装Map为obj
     */
    @Test
    public void assembleZOHOXmlTest() throws Exception {
        handleAccounts.assembleZOHOXml();
    }

    public List<String> assembleZOHOXml() throws Exception {
        List zohoAcctObjList = assembelSendToZOHOAcctList();
        Map<String,Accounts> addAccountMap =  (Map<String,Accounts> )zohoAcctObjList.get(0);
        Map<String,Accounts> updateAccountMap =  (Map<String,Accounts> )zohoAcctObjList.get(1);
        logger.debug("begin组装 AddZOHOXML...\n");
        String addZOHOXml = assembelZOHOXml(addAccountMap);
        logger.debug("end组装 AddZOHOXML...");

        logger.debug("begin组装 updateZOHOXml...\n");
        String updateZOHOXml = assembelZOHOXml(updateAccountMap);
        logger.debug("end组装 updateZOHOXml...");

        List<String> zohoXMLList = new ArrayList<String>();
        zohoXMLList.add(addZOHOXml);
        zohoXMLList.add(updateZOHOXml);
        //TODO: for delete
        List deleteList  = (List)zohoAcctObjList.get(2);
        //zohoXMLList.add();
        return zohoXMLList;
    }


    /**
     * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
     * 更新（testUpdateAcctRecord）
     * 添加（testAddAcctRecord）
     * 删除（）
     */
    @Test
    public void testUpdateAcctRecord(){
        try {
            String id_Accounts = "85333000000088001";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/updateRecords";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            String updZohoXML = assembleZOHOXml().get(1);
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,updZohoXML);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }

    @Test
    public void testAddAcctRecord(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/insertRecords";
            String addZohoXML = assembleZOHOXml().get(0);
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXML);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }
    @Test
    public void testdelAcctRecord(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/deleteRecords";
            String addZohoXML = assembleZOHOXml().get(0);
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXML);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }

    /**
     * 根据accountMap 组装成ZOHO XML
     * @param accountMap
     * @return
     * @throws Exception
     */
    private String  assembelZOHOXml(Map<String, Accounts> accountMap) throws Exception {
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.Accounts.Accounts accounts = new darlen.crm.jaxb_xml_object.Accounts.Accounts();
        List<Row> rows = getRowByMap(accountMap);
        if(rows==null || rows.size() == 0){
            return str;
        }else{
            accounts.setRows(rows);
            result.setAccounts(accounts);
            response.setResult(result);
            str = JaxbUtil.convertToXml(response);
        }
        return str;
    }

    /**
     * 获取Row对象的集合
     * @param addAccountMap
     * @return
     * @throws Exception
     */
    private List<Row> getRowByMap(Map<String, Accounts> addAccountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();

        int i = 1;
        for(Map.Entry<String,Accounts> entry : addAccountMap.entrySet()){
            Row row = new Row();

            String key = entry.getKey();
            Map<String,String> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.Accounts",entry.getValue());
            Map<String,String> zohoFieldMap = getZOHOFieldMap(CommonUtils.readProperties("/mapping/dbRdAccountsFieldMapping.properties"), dbFieldNameValueMap);
            List<FL> fls =  getAllFLsByCRMMap(zohoFieldMap);
            row.setNo(i);
            row.setFls(fls);
            rows.add(row);
            i++;
        }

        return rows;

    }
    public static List<FL> getAllFLsByCRMMap(Map<String,String> zohoFieldMap){
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry<String,String> entry : zohoFieldMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            FL fl = new FL();
            fl.setFieldName(key);
            fl.setFieldValue(value);
            fls.add(fl);
        }
        return fls;
    }

    private Map getZOHOFieldMap(Properties properties, Map dbFieldNameValueMap) {
        Map<String,String> crmFieldMap = new HashMap<String, String>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                crmFieldMap.put((String)entry.getValue(),(String)dbFieldNameValueMap.get(entry.getKey()));
            }
        }
        CommonUtils.printMap(crmFieldMap,"ZOHO Field Map:");
        return crmFieldMap;

    }

    /**
     * 获取DB某个Accounts所有有效的fieldname 和value的Map
     * @param className 包名+类名
     * @return
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<String,String> getDBFieldNameValueMap(String className,Accounts dbFields) throws Exception {
        Map<String,String> map = new HashMap();
        Class clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        for(Field field : fields){
            String fieldName = field.getName();
            field.setAccessible(true) ;
            if (field.getGenericType().toString().equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                String fieldValue =String.valueOf(field.get(dbFields));
                if(!StringUtils.isEmptyString(fieldValue)){
                    map.put(fieldName,fieldValue);
                }
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }



    private Accounts getAcctDBObj(Map<String,Accounts> idAccountsMap){
        Accounts accounts = new Accounts();
        accounts.setSMOWNERID("85333000000071039");
        accounts.setAcctOwner("qq");
        accounts.setCustomerID("2");
        accounts.setCustomerNO("Ven0001");
        accounts.setAcctName("富士廊紙品有限公司");
        //TODO: 数据库中Enable是1或者0，但是在ZOHO中是true或者false，需要转换下
        accounts.setEnabled("1".equals("1")? "true":"false");
        accounts.setPhone("020-34335570");
        accounts.setFax("020-34335579");
        accounts.setContact("徐先生");
        accounts.setDirect("徐先生");
        accounts.setDeliveryAddress("香港灣仔軒尼詩226號  寶華商業中心12樓");
        accounts.setEmail("fsl-printing@163.com");
        accounts.setMailAddress("廣東省廣州市工業大道廣紙路南箕工業區A2﹐A3幢");
        accounts.setWebsite("https://crm.zoho.com.cn/crm");
        accounts.setState("");
        accounts.setPostNo("");
        accounts.setDeliveryMethod("Free for delivery HK (one way, one location)");
        accounts.setCity("Hong Kong");
        accounts.setCreationTime("2016-09-05 17:00");
        accounts.setLatestEditTime("2016-09-18 17:00");
        accounts.setLatestEditBy("qq");
        idAccountsMap.put(accounts.getCustomerID(),accounts);
        return accounts;
    }


    public String retriveZohoAcctRecordID(String id_Accounts) {
        String retZohoStr = "";
        try {
//            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecordById";
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
            retZohoStr =  CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("执行搜索Module操作出现错误",e);
        }
        return retZohoStr;
    }

    /**
     * //* zohoIDMap<ERPID,ZOHOID> = zohoListObj.get(0)
     * //* zohoTimeMap<ERPID,lastEditTime> = zohoListObj.get(1)
     * @param response
     * @param zohoIDName
     * @param erpIDName
     * @return
     */
    private List assembleZOHOList(Response response,String zohoIDName,String erpIDName){
        List  zohoAcctObjList = new ArrayList();

        Map<String,String> zohoIDMap = new HashMap<String, String>();
        Map<String,String> zohoIDTimeMap = new HashMap<String, String>();
        List delZOHOIDList = new ArrayList();
        zohoAcctObjList.add(zohoIDMap);
        zohoAcctObjList.add(zohoIDTimeMap);
        zohoAcctObjList.add(delZOHOIDList);
        List<Row> rows = response.getResult().getAccounts().getRows();
        for (int i = 0; i < rows.size() ; i++){
            logger.debug("遍历第"+(i+1)+"条数据");
            String zohoID = "";
            String erpID = "";
            String lastEditTime = "";
            List<FL> fls = rows.get(i).getFls();
            boolean hasERPID = false;
            for(FL fl : fls){
                String fieldName = fl.getFieldName();
                String fieldVal = fl.getFieldValue();
                if(zohoIDName.equals(fieldName)){
                    zohoID = fieldVal;
                }
                if(erpIDName.equals(fieldName)){
                    erpID = fieldVal;
                    hasERPID = true;
                }
                if("LatestEditTime".equals(fieldName)){
                    lastEditTime = fieldVal;
                }
            }
            zohoIDMap.put(erpID,zohoID);
            zohoIDTimeMap.put(erpID,lastEditTime);
            if(!hasERPID) delZOHOIDList.add(zohoID);
        }
        return zohoAcctObjList;
    }

}
