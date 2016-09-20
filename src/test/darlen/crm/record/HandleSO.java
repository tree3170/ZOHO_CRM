/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HandleSO.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.SO.FL;
import darlen.crm.jaxb_xml_object.SO.Response;
import darlen.crm.jaxb_xml_object.SO.Row;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.model.result.SO;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
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
 * 重点在getRowByMap方法
 * getRowByMap: getDBFieldNameValueMap()+getZOHOFieldMap()+getAllFLsByCRMMap()
 *1. dbFieldNameValueMap ： 获取每个Accounts对应的  dbFieldNameValueMap = getDBFieldNameValueMap<dbFieldName,FiledValue>
 *2. zohoFieldNameValueMap <zohoFieldName,dbFiledValue> --> getZOHOFieldMap()： 根据 dbRdAccountsFieldMapping.properties 过滤 dbFieldNameValueMap,  形成zohoFieldNameValueMap <zohoFieldName,FiledValue>
 *3. getAllFLsByCRMMap() --> 获得zohoFieldNameValueMap形成的List<FL>
 *
 * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
 * 更新（testUpdateAcctRecord）
 * 添加（testAddAcctRecord）
 * 删除（testDelAcctRecord）
 *
 * 注意账号信息：qq:85333000000071039, tree3170:85333000000071001
 * Description：ZOHO_CRM
 * Created on  2016/09/19 22：42
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：42   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class HandleSO {
    private static HandleSO handleSO;
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
        handleSO = new HandleSO();
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
    public void testAssembleZOHOAcctObjList(){
        handleSO.assembleZOHOAcctObjList();
    }
    public List assembleZOHOAcctObjList(){
        //因为这里仅仅是测试数据这里仅仅只演示一条record，免得ZOHO的数据
        String id = "85333000000106003";//ID：中联重科
//        1. 从ZOHO获取有效的xml
        String zohoStr =  retriveZohoSORecordID(id);
//       2. xml 转 java bean
        System.out.println("zohoStr:::"+zohoStr);
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        System.out.println("response object:::"+response);
//     3. 组装 zohoAcctObjList
        List  zohoAcctObjList = handleSO.assembleZOHOList(response,"SALESORDERID","SOID");
        Map<String,String> zohoIDMap = (Map)zohoAcctObjList.get(0);
        Map<String,String> zohoIDTimeMap = (Map)zohoAcctObjList.get(1);
        //TODO:暂时在test测序中不处理删除数据
        List delZOHOIDList = (List)zohoAcctObjList.get(2);
        CommonUtils.printMap(zohoIDMap, "ERPID 和ZOHOID Map");
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
    public void testAssembleDBAcctObjList(){
        handleSO.assembleDBAcctObjList();
    }
    public List assembleDBAcctObjList(){
        List dbAcctList = new ArrayList();
        Map<String,SO> idAccountsMap = new HashMap<String, SO>();
        SO accounts = getSODBObj(idAccountsMap);
        dbAcctList.add(accounts);
        dbAcctList.add(idAccountsMap);
        CommonUtils.printList(dbAcctList, "DB Account obj");
        return dbAcctList;
    }

    private SO getSODBObj(Map<String, SO> idAccountsMap) {
        SO so = new SO();
        so.setOwerID("85333000000071039");
        so.setOwner("qq");
        so.setSubject("PSO30190412");
        so.setSoNumber("PSO30190412");
        so.setQuoteNO("PQ000112");
        so.setErpCurrency("USD");
        so.setACCOUNTID("1");
        so.setAcctName("PriPac Design & Communication AB");
        so.setErpCurrency("");
        so.setMailAddress("");
        so.setEmail("");
        so.setPoNO("");
        so.setFax("");
        so.setTel("");
        so.setFax("");
        so.setPaymentTerm("");
        so.setErpExchangeRate("");
        so.setDeliveryMethod("");
        so.setPayMethod("");
        so.setDueDate("");
        so.setPaymentPeriod("");
        so.setLatestEditTime("");
        so.setCreationTime("");
        so.setLatestEditBy("");
        so.setSOID("");
        return null;
    }


    /**
     * //* zohoIDMap<ERPID,ZOHOID> = zohoListObj.get(0)
     * //* zohoTimeMap<ERPID,lastEditTime> = zohoListObj.get(1)
     * @param response
     * @param zohoIDName
     * @param erpIDName
     * @return
     */
    private List assembleZOHOList(Response response, String zohoIDName, String erpIDName) {
        List  zohoAcctObjList = new ArrayList();

        Map<String,String> zohoIDMap = new HashMap<String, String>();
        Map<String,String> zohoIDTimeMap = new HashMap<String, String>();
        List delZOHOIDList = new ArrayList();
        zohoAcctObjList.add(zohoIDMap);
        zohoAcctObjList.add(zohoIDTimeMap);
        zohoAcctObjList.add(delZOHOIDList);
        List<Row> rows = response.getResult().getSo().getRows();
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

    private String retriveZohoSORecordID(String id) {
        String retZohoStr = "";
        try {
//            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecordById";
            String targetURL_SO = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_SO);
//            postParams.put(Constants.HTTP_POST_PARAM_ID,id_Accounts);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_2);
            retZohoStr =  CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("执行搜索Module操作出现错误",e);
        }
        return retZohoStr;

    }
}
