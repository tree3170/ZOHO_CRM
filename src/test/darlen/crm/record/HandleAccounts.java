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
import darlen.crm.model.common.Module;
import darlen.crm.model.result.Accounts;
import darlen.crm.model.result.User;
import darlen.crm.util.*;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
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
 * ===================Ⅱ：获取db数据，组装成 dbAcctList  : buildDBObjList()
 * 1.Accounts --> dbAcctList.get(0)
 * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
 *
 * ===================Ⅲ：组装需要真正需要传输到ZOHO的Account对象集合（判断zoho的 id 和 lastEditTime 是否有被修改）
 * 1.如果zohoid存在于dbModel中，则判断 lastEditTime是否被修改，如果修改了，则直接组装dbModel为xml并调用ZOHO中的更新API：updateAccountMap
 * 2.如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：delZOHOIDList
 * 3.如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：addAccountMap
 *
 *
 * ===================Ⅳ(assembleZOHOXmlTest)：组装XML
 *
 * 重点在getRowByMap方法
 * getRowByMap: getDBFieldNameValueMap()+getZOHOFieldMap()+getAllFLsByCRMMap()
 * 1. dbFieldNameValueMap ： 获取每个Accounts对应的  dbFieldNameValueMap = getDBFieldNameValueMap<dbFieldName,FiledValue>
 * 2. zohoFieldNameValueMap <zohoFieldName,dbFiledValue> --> getZOHOFieldMap()： 根据 dbRdAccountsFieldMapping.properties 过滤 dbFieldNameValueMap,  形成zohoFieldNameValueMap <zohoFieldName,FiledValue>
 * 3. getAllFLsByCRMMap() --> 获得zohoFieldNameValueMap形成的List<FL>
 *
 * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
 * 更新（testUpdateAcctRecord）
 * 添加（testAddAcctRecord）
 * 删除（testDelAcctRecord）
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
public class HandleAccounts  extends Module {
    private static HandleAccounts handleAccounts;
//    Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime)中： 参考（JaxbAccountsTest.java/JaxbSOTest.java/JaxbLeadsTest.java）
    private static Logger logger =  Logger.getLogger(HandleModules.class);

    /**
     * BeforeClass 与Before的区别
     */
    @BeforeClass
    public static void getInstance(){
        handleAccounts = new HandleAccounts();
        handleAccounts.getProperties();
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
    public void testAssembleZOHOAcctObjList() throws Exception {
        handleAccounts.buildSkeletonFromZohoList();
    }
    public List buildSkeletonFromZohoList() throws Exception {
//      1. 从ZOHO获取有效的xml
        String zohoURL = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
        String selectedColumns = "Products(Modified Time,ACCOUNTID,Account Name,ERP ID,LatestEditTime)";
        String sortOrderString = "desc";
        String sortColumnString = "Modified Time";
        //注意：format 一定要为2，因为有可能需要的字段为空
        String zohoStr =  ModuleUtils.retrieveZohoRecords(zohoURL, Module.NEWFORMAT_2, selectedColumns, sortOrderString, sortColumnString);
//       2. xml 转 java bean
        System.out.println("zohoStr:::"+zohoStr);
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        System.out.println("response object:::"+response);
//     3. 组装 zohoModuleList
        List  zohoModuleList = new ArrayList();
        Map<String,String> erpZohoIDMap = new HashMap<String, String>();
        Map<String,String> erpZohoIDTimeMap = new HashMap<String, String>();
        List delZOHOIDList = new ArrayList();
        //如果没有数据<response uri="/crm/private/xml/Products/getRecords"><nodata><code>4422</code><message>There is no data to show</message></nodata></response>
        if(null != response.getResult()){
            List<Row> rows = response.getResult().getAccounts().getRows();
            zohoModuleList = handleAccounts.buildZohoComponentList(rows, "ACCOUNTID", "ERP ID");
            erpZohoIDMap = (Map)zohoModuleList.get(0);
            erpZohoIDTimeMap = (Map)zohoModuleList.get(1);
            delZOHOIDList = (List)zohoModuleList.get(2);
            CommonUtils.printMap(erpZohoIDMap,"ERPID 和ZOHOID Map");
            CommonUtils.printMap(erpZohoIDTimeMap,"ERPID 和LastEditTime Map");
            CommonUtils.printList(delZOHOIDList,"Remove ZOHO ID list");
        }else{
            logger.debug("没有数据了：：：\n" + zohoStr);
            zohoModuleList.add(erpZohoIDMap);
            zohoModuleList.add(erpZohoIDTimeMap);
            zohoModuleList.add(delZOHOIDList);
        }

        return zohoModuleList;
    }

    /**
     * Ⅱ：这里组装db中的AcctObjList
     * 1.Accounts --> dbAcctList.get(0)
     * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
     */
    @Test
    public void testAssembleDBAcctObjList() throws ParseException {
        handleAccounts.buildDBObjList();
    }
    public List buildDBObjList() throws ParseException {
        List dbAcctList = new ArrayList();
        Map<String,Accounts> erpIDProductsMap = new HashMap<String, Accounts>();
        getDBObj(erpIDProductsMap);
        getAcctDBObj2(erpIDProductsMap);
        dbAcctList.add(erpIDProductsMap);
        CommonUtils.printList(dbAcctList, "Build DB Object :::");
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
    public void testAssembelSendToZOHOAcctList() throws Exception {
        handleAccounts.build2ZohoObjSkeletonList();
    }
    /**
     * 由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     * 1.updateAccountMap<>：如果Zoho ID存在于DB对象集合中，则判断 lastEditTime是否被修改，如果修改了，则直接组装到updateAccountMap中
     * 2.delZOHOIDList：如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：
     * 3.addAccountMap：如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：
     * @return
     */
    public List build2ZohoObjSkeletonList() throws Exception {
//        1. 获取ZOHO对象的骨架集合
        List allZohoObjList = buildSkeletonFromZohoList();
        //Map<ERPID，ZOHOID>
        Map<String,String> erpZohoIDMap = (Map)allZohoObjList.get(0);
        Map<String,String> erpIDTimeMap = (Map)allZohoObjList.get(1);
        List<String> delZohoIDList = (List)allZohoObjList.get(2);

//        2.组装DB 对象List
        List dbAcctList = buildDBObjList();
        Map<String,Accounts> idAccountsMap = (Map<String,Accounts>)dbAcctList.get(0);

//        3. 解析并组装addMap、updateMap、delZohoIDList
        Map<String,Accounts> addAccountMap = new HashMap<String, Accounts>();
        Map<String,Accounts> updateAccountMap = new HashMap<String, Accounts>();

        for(Map.Entry<String,String> entry : erpIDTimeMap.entrySet()){
            String erpID = entry.getKey();
            if(idAccountsMap.containsKey(erpID)){//update
                updateAccountMap.put(erpZohoIDMap.get(erpID), idAccountsMap.get(erpID));
            }else{ //delete
                if(!delZohoIDList.contains(erpZohoIDMap.get(erpID))){
                    delZohoIDList.add(erpZohoIDMap.get(erpID));
                }
            }
        }

        for(Map.Entry<String,Accounts> entry : idAccountsMap.entrySet()){
            String dbID = entry.getKey();
            if(!erpIDTimeMap.containsKey(dbID)){//add
                addAccountMap.put(dbID, entry.getValue());
            }
        }

        List sendToZohoAcctList = new ArrayList();
        CommonUtils.printMap(addAccountMap,"addMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(addAccountMap);
        CommonUtils.printMap(updateAccountMap,"updateMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(updateAccountMap);
        CommonUtils.printList(delZohoIDList, "delZOHOSOIDList组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(delZohoIDList);

        return sendToZohoAcctList;
    }

    /**
     * Ⅳ：组装addZOHOXml，updateZOHOXml，deleteZOHOIDsList,放进zohoXMLList集合对象中
     */
    @Test
    public void testAssembleZOHOXml() throws Exception {
        handleAccounts.build2ZohoXmlSkeleton();
    }

    /**
     * 由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
     * List<String> addZohoXmlList :每一百条数据组装成xml放入list里面
     * Map<zohoID,zohoXML> updateZOHOXmlMap ：以zohoID为key，xml为value
     * deleteZOHOIDsList： zohoID的集合
     * @return  zohoComponentList
     * @throws Exception
     */
    public List build2ZohoXmlSkeleton() throws Exception {
        //1. 获取发送到ZOHO的三大对象集合骨架
        List zohoComponentList = build2ZohoObjSkeletonList();
        Map<String,Accounts> addAccountMap =  (Map<String,Accounts> )zohoComponentList.get(0);
        Map<String,Accounts> updateAccountMap =  (Map<String,Accounts> )zohoComponentList.get(1);
        List deleteZOHOIDsList  = (List)zohoComponentList.get(2);

        //TODO add最大条数为100，
//        2. 添加
        logger.debug("begin组装 AddZOHOXML...\n");
        List<String> addZohoXmlList = buildAdd2ZohoXml(addAccountMap);
        logger.debug("end组装 AddZOHOXML..size:::."+addZohoXmlList.size());

//        3. 更新
        logger.debug("begin组装 updateZOHOXml...\n");
        Map<String,String> updateZOHOXmlMap  = buildUpd2ZohoXml(updateAccountMap);
        logger.debug("end组装 updateZOHOXml...size:::"+updateZOHOXmlMap.size());

        List zohoXMLList = new ArrayList();
        zohoXMLList.add(addZohoXmlList);
        zohoXMLList.add(updateZOHOXmlMap);
//        4. 删除
        logger.debug("打印删除ZohoIDs集合 deleteZOHOIDsList...\n"+org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,","));
        zohoXMLList.add(deleteZOHOIDsList);//org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,",")
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
            String targetURL_Accounts = zohoPropsMap.get(Constants.INSERT_ACCOUTNS_URL);//"https://crm.zoho.com.cn/crm/private/xml/Accounts/insertRecords";
            List<String> addZohoXMLList = (List<String> ) build2ZohoXmlSkeleton().get(0);
            for(int i = 0; i < addZohoXMLList.size(); i ++){
                System.err.println("添加第"+(i+1)+"条数据，xml为："+addZohoXMLList.get(i));
                Map<String,String> postParams = new HashMap<String, String>();
                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
                postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXMLList.get(i));
                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

                CommonUtils.executePostMethod(postParams);
            }

        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }
    @Test
    public void testUpdateAcctRecord(){
        try {
//            String id_Accounts = "85333000000088001";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/updateRecords";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            Map<String,String> updZohoXMLMap = (Map<String,String>) build2ZohoXmlSkeleton().get(1);
            int i = 1 ;
            for(Map.Entry<String,String> zohoIDUpdXmlEntry : updZohoXMLMap.entrySet()){
                System.err.println("更新第"+(i)+"条数据，ZOHO ID为"+zohoIDUpdXmlEntry.getKey()+"\nxml为："+zohoIDUpdXmlEntry.getValue());
                Map<String,String> postParams = new HashMap<String, String>();
                postParams.put(Constants.HTTP_POST_PARAM_ID,zohoIDUpdXmlEntry.getKey());
                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
                postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,zohoIDUpdXmlEntry.getValue());
                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

                CommonUtils.executePostMethod(postParams);
                i++;
            }
        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }


    @Test
    public void testDelAcctRecord(){
        try {
//            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/deleteRecords";
//            String addZohoXML = build2ZohoXmlSkeleton().get(0);
//            Map<String,String> postParams = new HashMap<String, String>();
//            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXML);
//            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
//            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
//            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);

//            CommonUtils.executePostMethod(postParams);

        } catch(Exception e) {
            logger.error("执行更新Module操作出现错误",e);
        }
    }

    /**
     * 根据accountMap 组装成每100条数据的addZohoXmlList中
     * 注意：getAddRowsMap
     *
     * @param accountMap
     * @return
     * @throws Exception
     */
    private List<String>  buildAdd2ZohoXml(Map<String, Accounts> accountMap) throws Exception {
        List<String> addZohoXmlList= new ArrayList<String>();
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.Accounts.Accounts accounts = new darlen.crm.jaxb_xml_object.Accounts.Accounts();
        Map<Integer,List<Row>> addRowsMap = getAddRowsMap(accountMap);
        if(addRowsMap==null || addRowsMap.size() == 0){
            return addZohoXmlList;
        }else{
            for(int i = 0 ; i< addRowsMap.size(); i ++){
                accounts.setRows(addRowsMap.get(i));
                result.setAccounts(accounts);
                response.setResult(result);
                String str  = JaxbUtil.convertToXml(response);
                addZohoXmlList.add(str);
            }
        }
        return addZohoXmlList;
    }
    /**
     * 根据accountMap 组装成updateZphoXmlMap <zohoID,updateXml>
     * @param accountMap
     * @return
     * @throws Exception
     */
    private Map<String,String> buildUpd2ZohoXml(Map<String, Accounts> accountMap) throws Exception {
        Map<String,String> updateZphoXmlMap = new HashMap<String, String>();
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.Accounts.Accounts accounts = new darlen.crm.jaxb_xml_object.Accounts.Accounts();
        List<Row> rows = getUpdRowByMap(accountMap);
        if(rows==null || rows.size() == 0){
            return updateZphoXmlMap;
        }else{
            int i = 0;
            for (Map.Entry<String,Accounts> zohoIDAccountEntry : accountMap.entrySet()){
                accounts.setRows(Arrays.asList(rows.get(i)));
                result.setAccounts(accounts);
                response.setResult(result);
                logger.debug("组装更新的第"+(i+1)+"条数据：：：");
                str = JaxbUtil.convertToXml(response);
                updateZphoXmlMap.put(zohoIDAccountEntry.getKey(),str);
                i++;
            }

        }
        return updateZphoXmlMap;
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
    private List<Row> getUpdRowByMap(Map<String, Accounts> accountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();

        int i = 1;
        for(Map.Entry<String,Accounts> entry : accountMap.entrySet()){
            Row row = new Row();
            String key = entry.getKey();
            Accounts accounts  = entry.getValue();
            List fls = getAllFLList(accounts);
            row.setNo(i);
            row.setFls(fls);
            rows.add(row);
            i++;
        }
        return rows;
    }
    /**
     * 获取Add的row的Map： 每100条rows放入Map，最后不满100条rows的放到最后的Map中
     * @param accountMap
     * @return
     * @throws Exception
     */
    private Map<Integer,List<Row>> getAddRowsMap(Map<String, Accounts> accountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();
        Map<Integer,List<Row>>  rowsMap = new HashMap<Integer, List<Row>>();
        int i = 1;
        for(Map.Entry<String,Accounts> entry : accountMap.entrySet()){
            Row row = new Row();
            String key = entry.getKey();
            Accounts products  = entry.getValue();
            List fls = getAllFLList(products);
            row.setNo(i);
            row.setFls(fls);
            rows.add(row);
            //当row的size达到了100，那么需要放入
            if(i == Constants.MAX_ADD_SIZE){
                logger.debug("Add Rows的size达到了100，需要放到Map中，然后重新计算rows的条数...");
                rowsMap.put(rowsMap.size(),rows);
                rows = new ArrayList<Row>();
                i = 1;
            }else{
                i++;
            }

        }
        //最后不满100条的，放入最后的Map中,如果刚好则不添加
        if(rows.size()>0) rowsMap.put(rowsMap.size()+1,rows);
        return rowsMap;
    }
    /**
     * 获取所有FL的集合，返回的List中存在2大对象：commonFls，products
     *  1. commonFls --> Common FL 集合
     *
     * @param accounts
     * @return
     * @throws Exception
     */
    private List getAllFLList(Accounts accounts) throws Exception {
        //通过反射拿到Products对应的所有ERP字段
        Map<String,Object> dbFieldNameValueMap = ModuleUtils.getDBFieldNameValueMap("darlen.crm.model.result.Accounts", accounts);
        // 通过properties过滤不包含在里面的所有需要发送的ZOHO字段
        List zohoFieldList = getZOHOFLsByProps(CommonUtils.readProperties("/mapping/dbRdAccountsFieldMapping.properties"), dbFieldNameValueMap);
        return zohoFieldList;
    }


    /**
     * 根据properties和有效的dbFieldNameValueMap确定返回给zoho的fieldname（获取properties中的key对应的value）和fieldvalue
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    private List<FL> getZOHOFLsByProps(Properties properties, Map dbFieldNameValueMap) {
        logger.debug("开始properties的过滤...");
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                FL fl = new FL();
                fl.setFieldName((String)entry.getValue());
                fl.setFieldValue((String)dbFieldNameValueMap.get(entry.getKey()));
                fls.add(fl);
            }
        }
        CommonUtils.printList(fls, "过滤后的 所有FL的集合:");
        return fls;

    }
    /**
     * 获取DB某个Accounts所有有效的fieldname 和value的Map
     * @param className 包名+类名
     * @return
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<String,Object> getDBFieldNameValueMap(String className,Object dbFields) throws Exception {
        Map<String,Object> map = new HashMap();
        Class clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        for(Field field : fields){
            String fieldName = field.getName();
            field.setAccessible(true) ;
            if (field.getGenericType().toString().equals("class java.lang.String")
                    ||"".equals("class darlen.crm.model.result.User") ) {// 如果type是类类型，则前面包含"class "，后面跟类名
                String fieldValue =String.valueOf(field.get(dbFields));
                if(!StringUtils.isEmptyString(fieldValue)){
                    map.put(fieldName,fieldValue);
                }
            }else if(field.getGenericType().toString().equals("class darlen.crm.model.result.User")){//处理User对象:拥有者
                map.putAll(getDBFieldNameValueMap("darlen.crm.model.result.User", field.get(dbFields)));
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }
// /**
//     * 根据accountMap 组装成ZOHO XML
//     * @param accountMap
//     * @return
//     * @throws Exception
//     */
//    private String  assembelZOHOXml(Map<String, Accounts> accountMap) throws Exception {
//        String str = "";
//        Response response = new Response();
//        Result result = new Result();
//        darlen.crm.jaxb_xml_object.Accounts.Accounts accounts = new darlen.crm.jaxb_xml_object.Accounts.Accounts();
//        List<Row> rows = getRowByMap(accountMap);
//        if(rows==null || rows.size() == 0){
//            return str;
//        }else{
//            accounts.setRows(rows);
//            result.setAccounts(accounts);
//            response.setResult(result);
//            str = JaxbUtil.convertToXml(response);
//        }
//        return str;
//    }

    /**
     * 获取Row对象的集合 : getDBFieldNameValueMap() + getZOHOFieldMap() + getAllFLsByCRMMap()
     * 1. dbFieldNameValueMap ： 获取每个Accounts对应的  dbFieldNameValueMap = getDBFieldNameValueMap<dbFieldName,FiledValue>
     * 2. zohoFieldNameValueMap <zohoFieldName,dbFiledValue> --> getZOHOFieldMap()： 根据dbRdAccountsFieldMapping.properties 过滤 dbFieldNameValueMap,  形成zohoFieldNameValueMap <zohoFieldName,FiledValue>
     * 3. getAllFLsByCRMMap() --> 获得zohoFieldNameValueMap形成的List<FL>
     * @param accountMap
     * @return
     * @throws Exception
     */
//    private List<Row> getRowByMap(Map<String, Accounts> accountMap) throws Exception {
//        List<Row> rows = new ArrayList<Row>();
//
//        int i = 1;
//        for(Map.Entry<String,Accounts> entry : accountMap.entrySet()){
//            Row row = new Row();
//
//            String key = entry.getKey();
//            Map<String,String> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.Accounts",entry.getValue());
//            Map<String,String> zohoFieldMap = getZOHOFieldMap(CommonUtils.readProperties("/mapping/dbRdAccountsFieldMapping.properties"), dbFieldNameValueMap);
//            List<FL> fls =  getAllFLsByCRMMap(zohoFieldMap);
//            row.setNo(i);
//            row.setFls(fls);
//            rows.add(row);
//            i++;
//        }
//
//        return rows;
//
//    }
//    public static List<FL> getAllFLsByCRMMap(Map<String,String> zohoFieldMap){
//        List<FL> fls = new ArrayList<FL>();
//
//        for(Map.Entry<String,String> entry : zohoFieldMap.entrySet()){
//            String key = entry.getKey();
//            String value = entry.getValue();
//            FL fl = new FL();
//            fl.setFieldName(key);
//            fl.setFieldValue(value);
//            fls.add(fl);
//        }
//        return fls;
//    }

//    private Map getZOHOFieldMap(Properties properties, Map dbFieldNameValueMap) {
//        Map<String,String> crmFieldMap = new HashMap<String, String>();
//
//        for(Map.Entry entry : properties.entrySet()){
//            if(dbFieldNameValueMap.containsKey(entry.getKey())){
//                crmFieldMap.put((String)entry.getValue(),(String)dbFieldNameValueMap.get(entry.getKey()));
//            }
//        }
//        CommonUtils.printMap(crmFieldMap,"ZOHO Field Map:");
//        return crmFieldMap;
//
//    }

    /**
     * TODO: 注意其中一些字段该放入值
     * 1. 注意productid 和product name一定要存在与系统中
     * 2. 注意PRODUCTSID是系统生成的，不需要设入
     * 3. 注意User的设入
     * @param idAccountsMap
     * @return
     */
    private Accounts getDBObj(Map<String, Accounts> idAccountsMap) throws ParseException {
        Accounts accounts = new Accounts();
        //for Tree account
//        User user = new User("85333000000071039","qq");
        //for Matrix Account
        User user = new User("80487000000076001","marketing");
//        accounts.setSMOWNERID("85333000000071039");
//        accounts.setAcctOwner("qq");
        accounts.setUser(user);
        accounts.setErpID("3");
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
        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
        accounts.setLatestEditTime(currentDate);
        accounts.setLatestEditBy("qq");
        idAccountsMap.put(accounts.getErpID(),accounts);
        return accounts;
    }
    //for update
    private Accounts getAcctDBObj2(Map<String,Accounts> idAccountsMap) throws ParseException {
        Accounts accounts = new Accounts();
        //for Tree account
//        User user = new User("85333000000071039","qq");
        //for Matrix Account
//        User user = new User("80487000000076001","marketing");
//        accounts.setSMOWNERID("85333000000071039");
//        accounts.setAcctOwner("qq");
        accounts.setUser(ModuleUtils.fetchDevUser(false));
        accounts.setErpID("2");
        accounts.setCustomerNO("Ven0002");
        accounts.setAcctName("永昌紙品");
        //TODO: 数据库中Enable是1或者0，但是在ZOHO中是true或者false，需要转换下
        accounts.setEnabled("1".equals("1") ? "true" : "false");
        accounts.setPhone("12345678901");
        accounts.setFax("123456789");
        accounts.setContact("Gary");
        accounts.setDirect("Gary");
        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
        accounts.setLatestEditTime(currentDate);
        accounts.setLatestEditBy("qq");
        idAccountsMap.put(accounts.getErpID(),accounts);
        return accounts;
    }



    /**
     * 获取Zoho组件的集合，其中包含三个对象，分别为 erpZohoID，erpIDTime，delZohoIDList（zoho ID list）
     * 1. erpZohoID<erpID,zohoID> = zohoListObj.get(0)
     * 2. erpIDTime<erpID,lastEditTime> = zohoListObj.get(1)
     * 3. delZohoIDList
     * @param rows
     * @param zohoIDName
     * @param erpIDName ERP ID-->
     * @return  zohoCompList
     */
    private List buildZohoComponentList(List<Row> rows, String zohoIDName, String erpIDName){
        List  zohoCompList = new ArrayList();

        Map<String,String> erpZohoIDMap = new HashMap<String, String>();
        Map<String,String> erpIDTimeMap = new HashMap<String, String>();
        List delZohoIDList = new ArrayList();
        zohoCompList.add(erpZohoIDMap);
        zohoCompList.add(erpIDTimeMap);
        zohoCompList.add(delZohoIDList);

        for (int i = 0; i < rows.size() ; i++){
            logger.debug("遍历第"+(i+1)+"条数据:::"+rows.get(i));
            String zohoID = "";
            String erpID = "";
            String lastEditTime = "";
            List<FL> fls = rows.get(i).getFls();
            boolean hasERPID = true;
            for(FL fl : fls){
                String fieldName = fl.getFieldName();
                String fieldVal = fl.getFieldValue();
                if(zohoIDName.equals(fieldName) && !StringUtils.isEmptyString(fieldVal)){
                    zohoID = fieldVal;
                }
                if(erpIDName.equals(fieldName)){
                    if(StringUtils.isEmptyString(fieldVal)){
                        fieldVal = "emptyERPID_"+i;
                        hasERPID = false;
                    }
                    erpID = fieldVal;
                    //如果出现重复的erpID，那么删除其中一条
                    if(erpZohoIDMap.containsKey(erpID)){
                        hasERPID = false;
                        erpID = "dulERPID_"+erpID+"_"+i;
                    }
                }
                if("LatestEditTime".equals(fieldName)){
                    lastEditTime = fieldVal;
                }
            }
            erpZohoIDMap.put(erpID, zohoID);
            erpIDTimeMap.put(erpID, lastEditTime);
            //如果ERPID为空，那么加入到删除列表中
            if(!hasERPID) delZohoIDList.add(zohoID);
        }
        return zohoCompList;
    }

}
