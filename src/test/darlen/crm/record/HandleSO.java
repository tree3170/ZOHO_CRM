/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HandleSO.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.SO.*;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.model.result.ProductDetails;
import darlen.crm.model.result.SO;
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
        Map<String,SO> idSOMap = new HashMap<String, SO>();
        SO accounts = getSODBObj(idSOMap);
        dbAcctList.add(accounts);
        dbAcctList.add(idSOMap);
        CommonUtils.printList(dbAcctList, "DB Account obj");
        return dbAcctList;
    }

    /**
     * Ⅲ：组装需要真正需要传输到ZOHO的Account对象集合
     * 1.updateAccountMap<>：如果zohoid存在于dbModel中，则判断 lastEditTime是否被修改，如果修改了，则直接组装dbModel为xml并调用ZOHO中的更新API：
     * 2.delZOHOIDList：如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：
     * 3.addAccountMap：如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：
     * @return
     */
    @Test
    public void testAssembelSendToZOHOAcctList(){
        handleSO.assembelSendToZOHOAcctList();
    }

    public List assembelSendToZOHOAcctList(){
        List zohoAcctObjList = assembleZOHOAcctObjList();
        //Map<ERPID，ZOHOID>
        Map<String,String> erpZohoIDMap = (Map)zohoAcctObjList.get(0);
        Map<String,String> zohoIDTimeMap = (Map)zohoAcctObjList.get(1);
        List<String> delZOHOIDList = (List)zohoAcctObjList.get(2);
        List dbAcctList = assembleDBAcctObjList();
        Map<String,SO> idAccountsMap = (Map<String,SO>)dbAcctList.get(1);

        Map<String,SO> addAccountMap = new HashMap<String, SO>();
        Map<String,SO> updateAccountMap = new HashMap<String, SO>();
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

        for(Map.Entry<String,SO> entry : idAccountsMap.entrySet()){
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
     * Ⅳ：组装
     * addZOHOXml:最大100条数据
     * updateZOHOXml：一次只能更新1条
     * deleteZOHOIDsList：转换为以逗号分割ZOHO ID的字符串
     */
    @Test
    public void testAssembleZOHOXml() throws Exception {
        handleSO.assembleZOHOXml();
    }

    public List<String> assembleZOHOXml() throws Exception {
        List zohoAcctObjList = assembelSendToZOHOAcctList();
        Map<String,SO> addAccountMap =  (Map<String,SO> )zohoAcctObjList.get(0);
        Map<String,SO> updateAccountMap =  (Map<String,SO> )zohoAcctObjList.get(1);
        logger.debug("begin组装 AddZOHOXML...\n");
        String addZOHOXml = assembelZOHOXml(addAccountMap);
        addZOHOXml = addZOHOXml.replace("pds","FL");
        logger.debug("end组装 AddZOHOXML...");

        //TODO confirm to 王继：如果有多条记录，因为每条API调用都需要带id， 该如何更新？ 是否支持批量更新？
        logger.debug("begin组装 updateZOHOXml...\n");
        String updateZOHOXml = assembelZOHOXml(updateAccountMap);
        logger.debug("end组装 updateZOHOXml...");

        List<String> zohoXMLList = new ArrayList<String>();
        zohoXMLList.add(addZOHOXml);
        zohoXMLList.add(updateZOHOXml);
        //TODO: for delete
        List deleteZOHOIDsList  = (List)zohoAcctObjList.get(2);
        zohoXMLList.add(org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,","));
        return zohoXMLList;
    }


    /**
     * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
     * 更新（testUpdateAcctRecord）
     * 添加（testAddAcctRecord）
     * 删除（testDelAcctRecord）
     */
    @Test
    public void testAddAcctRecord(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/insertRecords";
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
    public void testUpdateAcctRecord(){
        try {
            String id = "85333000000113197";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/updateRecords";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            String updZohoXML = assembleZOHOXml().get(1);
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_ID,id);
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

    /**
     * 因为API一次只能删除一条
     */
    @Test
    public void testDelSORecord(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/deleteRecords";
            String delZOHOIDStr = assembleZOHOXml().get(2);
            String[] delZOHOIDStrArr = delZOHOIDStr.split(",");
            for(String id : delZOHOIDStrArr){
                Map<String,String> postParams = new HashMap<String, String>();
                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
                postParams.put(Constants.HTTP_POST_PARAM_ID,id);//"85333000000113197"
                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

                CommonUtils.executePostMethod(postParams);
            }


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
    private String  assembelZOHOXml(Map<String, SO> accountMap) throws Exception {
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.SO.SO so = new darlen.crm.jaxb_xml_object.SO.SO();
        List<Row> rows = getRowByMap(accountMap);
        if(rows==null || rows.size() == 0){
            return str;
        }else{
            so.setRows(rows);
            result.setSo(so);
            response.setResult(result);
            str = JaxbUtil.convertToXml(response);
        }
        return str;
    }
    /**
     * ************注意这里需要处理普通的FL和product detail的FL
     * 获取Row对象的集合 : getDBFieldNameValueMap() + getZOHOFieldMap() + getAllFLsByCRMMap()
     * 1. dbFieldNameValueMap ： 获取每个Accounts对应的  dbFieldNameValueMap = getDBFieldNameValueMap<dbFieldName,FiledValue>
     * 2. zohoFieldNameValueMap <zohoFieldName,dbFiledValue> --> getZOHOFieldMap()： 根据dbRdAccountsFieldMapping.properties 过滤 dbFieldNameValueMap,  形成zohoFieldNameValueMap <zohoFieldName,FiledValue>
     * 3. getAllFLsByCRMMap() --> 获得zohoFieldNameValueMap形成的List<FL>
     * @param accountMap
     * @return
     * @throws Exception
     */
    private List<Row> getRowByMap(Map<String, SO> accountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();

        int i = 1;
        for(Map.Entry<String,SO> entry : accountMap.entrySet()){
            Row row = new Row();
            String key = entry.getKey();

            SO so  = entry.getValue();
            List<ProductDetails> pds = so.getPds();
            //Map<String,Object> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.SO",so);
            //for(Map.Entry<String,String> soEntry : entry.getValue())

//            List fls = getCommonFLList(so);
            List fls2 = getAllFLList(so);

            row.setNo(i);
            row.setFls((List<FL>)fls2.get(0));
            ProdDetails prodDetails = new ProdDetails();
            prodDetails.setVal("Product Details");
            List<Product> products = (List<Product>)fls2.get(1);
            prodDetails.setProducts(products);
            row.setPds(prodDetails);
            rows.add(row);
            i++;
        }

        return rows;

    }

    private List getAllFLList(SO so) throws Exception {
        Map<String,Object> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.SO",so);
        List zohoFieldList = getZOHOFLs(CommonUtils.readProperties("/mapping/dbRdSOFieldMapping.properties"), dbFieldNameValueMap);
        return zohoFieldList;
    }
    /**
     * 根据properties确定返回给zoho的fieldname和fieldvalue
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    private List getZOHOFLs(Properties properties, Map dbFieldNameValueMap) {
        List allFls = new ArrayList();
        List<Product> products = new ArrayList<Product>();
        List<FL> commonFls = new ArrayList<FL>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                String dbFieldName = (String)entry.getKey();
                if("pds".equalsIgnoreCase(dbFieldName)){
                    /**
                     * 1. pdsMap: 取出key为pds的Map<no,Map<fieldName,FieldValue> --> pdsMap,并新建一个newPdsMap<<no,Map<fieldName,FieldValue>>,
                     * 2. pdMap: 遍历这个map(pdsMap)，取出每个Map<FieldName,FieldValue> -->pdMap ,判断fieldName是否在properties存在
                     * 3. 如果dbFielName存在与properties中，则取properties中的value和dbFieldValue，放入一个新的FL对象中，最后放入List<FL>中;如果不存在，忽略这个字段
                     * 4. 组装product，并把product对象放入List<Product>中
                     */
                    //1.
                    Map<String,Map> pdsMap = (Map)dbFieldNameValueMap.get("pds");
                    for (Map.Entry<String,Map> pdsEntry : pdsMap.entrySet()) {//多少条prduct detail;记录
                        Product pd = new Product();
                        //这个是记录no
                        String no = pdsEntry.getKey().toString();
                        //2. 这个是每个no的product中的fieldname和field value组装成的map
                        Map<String,String> pdMap = pdsEntry.getValue();
                        List<FL> fls = new ArrayList<FL>();
                        for(Map.Entry<String,String> pdEnry : pdMap.entrySet()){
                            String dbFielName = pdEnry.getKey().toString();
                            //3.取出properyies中没有的db key,放入一个新的map中，组成一个最新的crm field name对应的producrt detail 的map
                            if(properties.containsKey(dbFielName)){
                                FL fl = new FL();
                                fl.setFieldName((String) properties.get(dbFielName));
                                fl.setFieldValue((String)pdEnry.getValue());
                                fls.add(fl);
                            }
                        }
                        // 4.1 组装product  no
                        pd.setNo(no);
                        // 4.2.组装product FLs
                        pd.setFls(fls);
                        products.add(pd);
                    }
                }else{
                    /**
                     * for common field : 跟properties做对比，
                     * 如果存在，则取properties的value作为key，以前的value还是作为value
                     * 如果不存在，则忽略这个field，也就是说不会作为最后发送到CRM中的字段
                     */
                    String dbFieldValue = (String)dbFieldNameValueMap.get(dbFieldName);
                    String zohoFieldName = String.valueOf(entry.getValue());
                    FL fl = new FL();
                    fl.setFieldName(zohoFieldName);
                    fl.setFieldValue(dbFieldValue);
                    commonFls.add(fl);
                }
            }
        }
        allFls.add(commonFls);
        allFls.add(products);
        CommonUtils.printList(allFls, "ZOHO Field List:");
        return allFls;

    }

    public List getCommonFLList(SO so) throws Exception {
        Map<String,Object> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.SO",so);
        Map<String,Object> zohoFieldMap = getZOHOFieldMap(CommonUtils.readProperties("/mapping/dbRdSOFieldMapping.properties"), dbFieldNameValueMap);
        List commonFLs =getAllFLsByCRMMap(zohoFieldMap);
        return commonFLs;
    }
    public static List getAllFLsByCRMMap(Map<String,Object> zohoFieldMap){
        List allFls = new ArrayList();
        List<Product> products = new ArrayList<Product>();
        List<FL> commonFls = new ArrayList<FL>();


        for(Map.Entry<String,Object> entry : zohoFieldMap.entrySet()){
            String key = entry.getKey();
            if(!"pds".equals(key)){
                String value = String.valueOf(entry.getValue());
                FL fl = new FL();
                fl.setFieldName(key);
                fl.setFieldValue(value);
                commonFls.add(fl);
            }else{
                Map<String,Map<String,String>> pdsFlsMap = (Map)entry.getValue();
                for(Map.Entry<String,Map<String,String>> pdEntry : pdsFlsMap.entrySet()){
                    Product pd = new Product();
                    //1. 组装no
                    pd.setNo(pdEntry.getKey());
                    //2. 组装product中的List FL
                    Map<String,String> map  = (Map<String,String>)pdEntry.getValue();
                   // pd.setFls(pdEntry.getValue());
                    List<FL> pdFls = new ArrayList<FL>();
                    for(Map.Entry<String,String> entry2 :map.entrySet() ){
                        FL fl = new FL();
                        fl.setFieldName(entry2.getKey());
                        fl.setFieldValue(entry2.getValue());
                        pdFls.add(fl);
                    }
                    pd.setFls(pdFls);
                    products.add(pd);
                }
            }
        }
        allFls.add(commonFls);
        allFls.add(products);
        return allFls;
    }

    /**
     * 根据properties确定返回给zoho的fieldname和fieldvalue
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    private Map getZOHOFieldMap(Properties properties, Map dbFieldNameValueMap) {
        Map<String,Object> zohoFieldMap = new HashMap<String, Object>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                String dbFieldName = (String)entry.getKey();
                if("pds".equalsIgnoreCase(dbFieldName)){
                    /**
                     * 1. 取出key为pds的Map<no,Map<fieldName,FieldValue> --> pdsMap,并新建一个newPdsMap<<no,Map<fieldName,FieldValue>>,
                     * 2. 遍历这个map(pdsMap)，取出每个Map<FieldName,FieldValue> -->pdMap ,判断fieldName是否在properties存在
                     * 3. 如果存在，则取properties中的value和fieldvalue，放入一个新的newPdMap<properties.get(fieldName),fieldVaue>
                     * 4. 如果不存在，忽略这个字段
                     * 5. 将新的newPdMap放入newPdsMap，
                     * 6. 最终newPdsMap放入crmFieldMap
                     */
                    Map<String,Map> pdsMap = (Map)dbFieldNameValueMap.get("pds");
                    Map newPdsMap = new HashMap();
                    for (Map.Entry<String,Map> pdsEntry : pdsMap.entrySet()) {//多少条prduct detail;记录
                        String no = pdsEntry.getKey().toString();//这个是记录no
                        Map<String,String> pdMap = pdsEntry.getValue();//这个是每个no的product中的fieldname和field value组装成的map
                        Map newPdMap = new HashMap();
                        for(Map.Entry pdEnry : pdMap.entrySet()){
                            String pdKey = pdEnry.getKey().toString();
                            if(properties.containsKey(pdKey)){//取出properyies中没有的db key,放入一个新的map中，组成一个最新的crm field name对应的producrt detail 的map
                                newPdMap.put(properties.get(pdKey),pdEnry.getValue());
                            }
                        }
                        newPdsMap.put(no,newPdMap);

                    }
                    zohoFieldMap.put("pds",newPdsMap);
                }else{
                    /**
                     * for common field : 跟properties做对比，
                     * 如果存在，则取properties的value作为key，以前的value还是作为value
                     * 如果不存在，则忽略这个field，也就是说不会作为最后发送到CRM中的字段
                     */
                    String dbFieldValue = (String)dbFieldNameValueMap.get(dbFieldName);
                    String zohoFieldName = String.valueOf(entry.getValue());
                    zohoFieldMap.put(zohoFieldName,dbFieldValue);
                }
            }
        }
        CommonUtils.printMap(zohoFieldMap,"ZOHO Field Map:");
        return zohoFieldMap;

    }

    /**
     * 获取DB某个Accounts所有有效的fieldname 和value的Map
     * @param className 包名+类名
     * @return
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<String,Object> getDBFieldNameValueMap(String className,SO dbFields) throws Exception {
        Map<String,Object> map = new HashMap();
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
            }else if("pds".equals(field.getName())){//handle the product detail
                map.put("pds", getDBProdDetlFieldMap(field,dbFields));
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }

    /**
     *  取出所有的db中的product details，然后每个遍历，有值的就放入tmpMap中，最后组装成一个Map ，
     *  key是product no，value是tmpMap(fieldName,fieldValue)
     * @param field
     * @param so
     * @return  key : product detail no --> value : map(key:fieldName-->value:fieldValue)
     * @throws IllegalAccessException
     */
    private static Map<String,Object> getDBProdDetlFieldMap(Field field,SO so) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        List<ProductDetails> pds = (List<ProductDetails>)field.get(so);
        Map<String,Object> map = new HashMap<String, Object>();
        for(int i = 0;i < pds.size(); i++) {
            ProductDetails pd = pds.get(i);
            Class clazz = Class.forName(pd.getClass().getName());
            Object object = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getMethods();
            Map<String,String> tmpMap = new HashMap<String, String>();
            for(Field f : fields){
                String fieldName = f.getName();
                f.setAccessible(true) ;
                if (f.getGenericType().toString().equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                    String fieldValue =String.valueOf(f.get(pd));
                    if(!StringUtils.isEmptyString(fieldValue)){
                        tmpMap.put(fieldName,fieldValue);
                    }
                }
            }
            map.put((i+1)+"",tmpMap);
        }
        CommonUtils.printMap(map,"打印 DB Product Detail Field Map:");
        return map;
    }


    //TODO 注意价格： 汇率

    /**
     * TODO:
     * 1. 注意productid 和product name一定要存在与系统中
     * 2. 注意SALESORDERID是系统生成的，不需要设入
     * 3.
     * @param idSOMap
     * @return
     */
    private SO getSODBObj(Map<String, SO> idSOMap) {
        SO so = new SO();
        so.setSALESORDERID("12345678");
        so.setOwerID("85333000000071039");
        so.setOwner("qq");
        so.setSubject("PSO30190412");
        /**ZOHO生成的字段，似乎没什么作用*/
        //so.setSoNumber("PSO30190412");
        so.setQuoteNO("PQ000112");
        so.setErpCurrency("USD");
        /**
         * Account id 和Name一般是同时存在的；
         * 如果只存在id，Name可以不对；
         * 如果只存在Name，那么会新创建一个客户；
         * 例外：但如果有ID，那么ID必需存在已经创建的客户中
         */
        so.setACCOUNTID("85333000000106011");
        /**
         * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
         */
        so.setAcctName("富士廊紙品有限公司");
        so.setContact("Mr. Johan Svard");
        so.setErpCurrency("USD");
        so.setMailAddress("Saltängsvägen 18, 721 32 Västerås Sweden");
        so.setEmail("tree317035791@163.com");
        so.setPoNO("496");
        so.setDeliveryAddress("test");
        so.setTel("12345678");
        so.setFax("12345678");
        so.setErpExchangeRate("7.77");
        /**不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
        so.setPaymentTerm("2");
        so.setPayMethod("T/T");
        so.setDeliveryMethod("FOB Shenzhen");
        so.setPaymentPeriod("0");
        so.setErpDueDate("2016-09-31 10:10:10");
        so.setLatestEditTime("2016-09-31 10:10:10");
        so.setCreationTime("2016-09-01 10:10:10");
        so.setLatestEditBy("qq");
        /**DB中的SO id*/
        so.setSOID("10");

        /**
         * 设置product detail右下角那堆属于SO的字段:Discount,Sub Total,Grand Total
         */
        so.setDiscount("100");
        so.setSubTotal("4000");//小计
        so.setGrandTotal("3000");//累计
        /**
         * 处理list<ProductDetail>, 根据db中的SOID关联Item_SO表，拿出所有的product Detail,注意为空情况
         */
        List<ProductDetails> pds = handlePdsList("10",Double.valueOf(so.getErpExchangeRate()));
        so.setPds(pds);
        idSOMap.put(so.getSOID(),so);
        return so;
    }

    /**
     *  根据db中的SOID关联Item_SO表，拿出所有的product Detail,这里假设找到2条数据
     * @param soID
     * @return
     */
    private List<ProductDetails> handlePdsList(String soID,double erpExchangeRate) {
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        ProductDetails pd =new ProductDetails();
        String realUnitPrice = String.valueOf(0.73 * erpExchangeRate);
        String listPrice = String.valueOf(0.73 * erpExchangeRate);
        pd.setPd_Unit_Price(realUnitPrice);//定价  ： DB-->SOPrice,注意价格要跟Currency一致
        pd.setPd_List_Price(listPrice);//单价  ： DB-->SOPrice,注意价格要跟Currency一致
        pd.setPd_Quantity("10000");//数量
        pd.setPd_Discount("0");//折扣
        pd.setPd_Tax("0");//税，Matrix默认这个字段是0，因为不用税

        pd.setPd_Product_Description("Wesc Cardboard drawer box ");
        /**
         * 注意 product id和Name一定要是已经存在与产品里面的
         */
        pd.setPd_Product_Id("85333000000101001");
        pd.setPd_Product_Name("Test Item 1");//TODO 需要找ken确认ItemName为【Name】是表示什么意思？是否根据id从Item表找
        pd.setPd_Total("86664.0");//金额
        pd.setPd_Net_Total("86666.0");//总计

        ProductDetails pd2 = pd;
        pd2.setPd_Quantity("10000");//数量
        pds.add(pd2);
        pds.add(pd);
        return pds;
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



/**
 * 实例
 https://crm.zoho.com.cn/crm/private/xml/SalesOrders/insertRecords?authtoken=f19d6f4ad3d2a491ef52f83a7a68bf04&scope=crmapi&xmlData=<response>
 <result>
 <SalesOrders>
 <row no="1">
 <FL val="Sales Order Owner">qq</FL>
 <FL val="PayMethod">T/T</FL>
 <FL val="QuoteNO">PQ000112</FL>
 <FL val="PaymentTerm">2</FL>
 <FL val="ERP_Currency">USD</FL>
 <FL val="Tel">12345678</FL>
 <FL val="Subject">PSO30190412</FL>
 <FL val="MailAddress">Saltängsvägen 18, 721 32 Västerås Sweden</FL>
 <FL val="DeliveryMethod">FOB Shenzhen</FL>
 <FL val="ACCOUNTID">85333000000106011</FL>
 <FL val="Account Name">富士廊紙品有限公司</FL>
 <FL val="ERP_ExchangeRate">7.77</FL>
 <FL val="SOID">10</FL>
 <FL val="CreationTime">2016-09-01 10:10:10</FL>
 <FL val="SMOWNERID">85333000000071039</FL>
 <FL val="LatestEditTime">2016-09-31 10:10:10</FL>
 <FL val="DeliveryAddress">test</FL>
 <FL val="Contact">Mr. Johan Svard</FL>
 <FL val="ERP Due Date">2016-09-31 10:10:10</FL>
 <FL val="Fax">12345678</FL>
 <FL val="LatestEditBy">qq</FL>
 <FL val="PONO">496</FL>
 <FL val="Discount">100</FL>
 <FL val="Sub Total">4961</FL>
 <FL val="Grand Total">4962</FL>
 <FL val="Product Details">
 <product no="1">
 <FL val='Discount'>0</FL>
 <FL val='Tax'>0</FL>
 <FL val='Product Description'>Wesc Cardboard drawer box </FL>
 <FL val='Unit Price'>123</FL>
 <FL val='Product Name'>Test Item 1</FL>
 <FL val='Product Id'>85333000000101001</FL>
 <FL val='Quantity'>10000</FL>
 <FL val='Total'>86664.0</FL>
 <FL val='List Price'>72223.0</FL>
 <FL val='Net Total'>86665.0</FL>
 </product>
 </FL>
 </row>
 </SalesOrders>
 </result>
 </response>&newFormat=1

 *
 **/