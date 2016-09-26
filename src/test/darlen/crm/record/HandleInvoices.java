/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HandleInvoices.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.Invoices.Response;
import darlen.crm.jaxb_xml_object.Invoices.Result;
import darlen.crm.jaxb_xml_object.Invoices.Row;
import darlen.crm.jaxb_xml_object.commjaxb.FL;
import darlen.crm.jaxb_xml_object.commjaxb.ProdDetails;
import darlen.crm.jaxb_xml_object.commjaxb.Product;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.model.common.Module;
import darlen.crm.model.result.Invoices;
import darlen.crm.model.result.ProductDetails;
import darlen.crm.model.result.SO;
import darlen.crm.model.result.User;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import darlen.crm.util.ThreadLocalDateUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * 暂时在程序中不处理删除数据，只处理更新或者添加数据
 * ====================Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime)中： 参考（JaxbAccountsTest.java/JaxbInvoicesTest.java/JaxbLeadsTest.java）
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
public class HandleInvoices extends Module{
    private static HandleInvoices handleInvoices;
    //
//    Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime)中： 参考（JaxbAccountsTest.java/JaxbInvoicesTest.java/JaxbLeadsTest.java）
//
    private static Logger logger =  Logger.getLogger(HandleModules.class);


    /**
     * BeforeClass 与Before的区别
     */
    @BeforeClass
    public static void getInstance(){
        handleInvoices = new HandleInvoices();
//        initZohoProps();
        handleInvoices.getProperties();
    }



    /**
     * Ⅰ：这里仅仅只是组装zohoAcctObjList
     */
    @Test
    public void testAssembleZOHOAcctObjList(){
        handleInvoices.buildZohoObjSkeletonList();
    }

    /**
     * 从ZOHO获取所有的XML并转化为java对象，所有的Invoices记录，并且最后组装为3组对象：erpZohoIDMap，erpIDTimeMap，delZOHOIDList
     *
     * 1. 从ZOHO获取有效的xml ： retriveZohoRecords()
     * 2. xml 转 java bean : JaxbUtil.converyToJavaBean(zohoStr, Response.class);
     * 3. 组装 zohoListObj : buildZohoComponentList() -->
     *    作用是：拿出ZOHO ID、ERP ID和LastModified这三个字段，用作将来的判断是否存在于已有的DB中，并且lastModified时间是否一直
     *        ZOHO ID:用作delete、update
     *        ERP ID ： 用作判断是否存在于DB中，不存在则加入删除列表
     *        LastModified： 当ERP存在DB中，则判断LastModified是否被修改，如果修改则加入更新列表里面
     * 其中里面的element有：
     * erpZohoIDMap<ERP ID,ZOHO ID> = zohoComponentObjList.get(0)
     * erpIDTimeMap<ERP ID,lastEditTime> = zohoComponentObjList.get(1)
     * delZOHOIDList = = zohoComponentObjList.get(2): 里面是所有 ERP ID 为空时的 ZOHO ID
     */
    public List buildZohoObjSkeletonList(){
        // TODO ：：：Notice: 最大只能取到200条数据，这边可能需要另外的逻辑控制判断数据是否取完
//       1. 从ZOHO获取有效的xml
        System.out.println("从ZOHO获取回来的所有记录的XML:::");
        String zohoStr =  retriveZohoRecords();
//       2. xml 转 java bean
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        System.out.println("转化ZOHO获取回来的XML:::"+response);
//       3. 组装 zohoComponentObjList的三大对象
        List  zohoComponentObjList = handleInvoices.buildZohoComponentList(response, "SALEInvoicesRDERID", "ERP ID");
        Map<String,String> erpZohoIDMap = (Map)zohoComponentObjList.get(0);
        Map<String,String> erpIDTimeMap = (Map)zohoComponentObjList.get(1);
        List delZohoIDList = (List)zohoComponentObjList.get(2);
        CommonUtils.printMap(erpZohoIDMap, "ERP ID 和ZOHO ID Map");
        CommonUtils.printMap(erpIDTimeMap,"ERP ID 和LastEditTime Map");
        CommonUtils.printList(delZohoIDList,"Remove ZOHO ID list");

        return zohoComponentObjList;
    }

    /**
     * Ⅱ：这里组装db中的AcctObjList
     * 1.Accounts --> dbAcctList.get(0)
     * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
     */
    @Test
    public void testAssembleDBAcctObjList() throws ParseException {
        handleInvoices.buildDBObjList();
    }
    public List buildDBObjList() throws ParseException {
        List dbAcctList = new ArrayList();
        Map<String,Invoices> idInvoicesMap = new HashMap<String, Invoices>();
        Invoices accounts = getInvoicesDBObj(idInvoicesMap);
        Invoices accouts2 = getInvoicesDBObj2(idInvoicesMap);
        accouts2.setInvoiceSubject("tree31701");
        dbAcctList.add(accounts);
        dbAcctList.add(idInvoicesMap);
        CommonUtils.printList(dbAcctList, "DB Account obj");
        return dbAcctList;
    }


    /**
     * Ⅲ：由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     */
    @Test
    public void testAssembelSendToZOHOAcctList() throws ParseException {
        handleInvoices.build2ZohoObjSkeletonList();
    }

    /**
     * 由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     * 1.updateAccountMap<>：如果Zoho ID存在于DB对象集合中，则判断 lastEditTime是否被修改，如果修改了，则直接组装到updateAccountMap中
     * 2.delZOHOIDList：如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：
     * 3.addAccountMap：如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：
     * @return
     */
    public List build2ZohoObjSkeletonList() throws ParseException {
        //1. 获取ZOHO对象的骨架集合
        List allZohoObjList = buildZohoObjSkeletonList();
        //Map<ERPID，ZOHOID>
        Map<String,String> erpZohoIDMap = (Map)allZohoObjList.get(0);
        Map<String,String> erpIDTimeMap = (Map)allZohoObjList.get(1);
        List<String> delZohoIDList = (List)allZohoObjList.get(2);
        //2.组装DB 对象List
        List dbAcctList = buildDBObjList();
        Map<String,Invoices> idAccountsMap = (Map<String,Invoices>)dbAcctList.get(1);

        //3. 解析并组装addMap、updateMap、delZohoIDList
        Map<String,Invoices> addMap = new HashMap<String, Invoices>();
        Map<String,Invoices> updateMap = new HashMap<String, Invoices>();

        for(Map.Entry<String,String> entry : erpIDTimeMap.entrySet()){
            String erpID = entry.getKey();
            if(idAccountsMap.containsKey(erpID)){//update
                updateMap.put(erpZohoIDMap.get(erpID), idAccountsMap.get(erpID));
            }else{ //delete
                if(!delZohoIDList.contains(erpZohoIDMap.get(erpID))){
                    delZohoIDList.add(erpZohoIDMap.get(erpID));
                }
            }
        }

        for(Map.Entry<String,Invoices> entry : idAccountsMap.entrySet()){
            String dbID = entry.getKey();
            if(!erpIDTimeMap.containsKey(dbID)){//add
                addMap.put(dbID, entry.getValue());
            }
        }

        List sendToZohoList = new ArrayList();
        CommonUtils.printMap(addMap,"addInvoicesMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoList.add(addMap);
        CommonUtils.printMap(updateMap, "updateInvoicesMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoList.add(updateMap);
        CommonUtils.printList(delZohoIDList, "delZOHOInvoicesIDList组装到ZOHO的对象的集合：：：\n");
        sendToZohoList.add(delZohoIDList);

        return sendToZohoList;
    }

    /**
     * Ⅳ：由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
     * addZOHOXml:最大100条数据
     * updateZOHOXml：一次只能更新1条
     * deleteZOHOIDsList：转换为以逗号分割ZOHO ID的字符串
     */
    @Test
    public void testAssembleZOHOXml() throws Exception {
        handleInvoices.build2ZohoXmlSkeleton();
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
        //1. 获取发送到ZOHO对象集合骨架
        List zohoComponentList = build2ZohoObjSkeletonList();
        Map<String,Invoices> addAccountMap =  (Map<String,Invoices> )zohoComponentList.get(0);
        Map<String,Invoices> updateAccountMap =(Map<String,Invoices> )zohoComponentList.get(1);

        //TODO add最大条数为100，
        //2. 添加
        logger.debug("begin组装 AddZOHOXML...\n");
        List<String> addZohoXmlList = buildAdd2ZohoXml(addAccountMap);
        logger.debug("end组装 AddZOHOXML...size:::"+addZohoXmlList.size());

        //TODO confirm to 王继：如果有多条记录，因为每条API调用都需要带id， 该如何更新？ 是否支持批量更新？
        //3. 更新
        logger.debug("begin组装 updateZOHOXml...\n");
        Map<String,String> updateZOHOXmlMap  = buildUpd2ZohoXml(updateAccountMap);
        logger.debug("end组装 updateZOHOXml...size:::"+updateZOHOXmlMap.size());

        List zohoXMLList = new ArrayList();
        zohoXMLList.add(addZohoXmlList);
        zohoXMLList.add(updateZOHOXmlMap);
        //TODO: for delete
        //4. 删除
        List deleteZOHOIDsList  = (List)zohoComponentList.get(2);
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
    public void testAddRecords(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/insertRecords";
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
    public void testUpdateRecords(){
        try {
            String id = "85333000000113197";//客户1ID
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/updateRecords";
            //TODO: qq:85333000000071039, tree3170:85333000000071001
            Map<String,String> updZohoXMLMap = (Map<String,String>) build2ZohoXmlSkeleton().get(1);
            int i = 1 ;
            for(Map.Entry<String,String> zohoIDUpdXmlEntry : updZohoXMLMap.entrySet()){
                System.err.println("更新第"+(i)+"条数据，xml为："+zohoIDUpdXmlEntry.getValue());
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

    /**
     * 因为API一次只能删除一条
     */
    @Test
    public void testDelInvoicesRecords(){
        try {
            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/deleteRecords";
            List delZOHOIDList = (List) build2ZohoXmlSkeleton().get(2);
            //String[] delZOHOIDStrArr = new String[]{"85333000000116059"};//delZOHOIDStr.split(",");
            for(int i = 0; i < delZOHOIDList.size(); i++){
                System.err.println("删除"+(i)+"条数据，xml为："+delZOHOIDList.get(i));
                Map<String,String> postParams = new HashMap<String, String>();
                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
                postParams.put(Constants.HTTP_POST_PARAM_ID,(String)delZOHOIDList.get(i));//"85333000000113197"
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
     * 根据accountMap 组装成每100条数据的addZohoXmlList中
     * 注意：getAddRowsMap
     *
     * @param accountMap
     * @return
     * @throws Exception
     */
    private List<String> buildAdd2ZohoXml(Map<String, Invoices> accountMap) throws Exception {
        List<String> addZohoXmlList= new ArrayList<String>();
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.Invoices.Invoices invoices = new darlen.crm.jaxb_xml_object.Invoices.Invoices();
        Map<Integer,List<Row>> addRowsMap = getAddRowsMap(accountMap);
        if(addRowsMap==null || addRowsMap.size() == 0){
            return addZohoXmlList;
        }else{

            for(int i = 0 ; i< addRowsMap.size(); i ++){
                invoices.setRows(addRowsMap.get(i));
                result.setInvoices(invoices);
                response.setResult(result);
                String str  = JaxbUtil.convertToXml(response);
                //转换pds为FL
                addZohoXmlList.add(str.replace("pds","FL"));
            }
        }
        return addZohoXmlList;
    }

    /**
     * 获取Add的row的Map： 每100条rows放入Map，最后不满100条rows的放到最后的Map中
     * @param accountMap
     * @return
     * @throws Exception
     */
    private Map<Integer,List<Row>> getAddRowsMap(Map<String, Invoices> accountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();
        Map<Integer,List<Row>>  rowsMap = new HashMap<Integer, List<Row>>();
        int i = 1;
        for(Map.Entry<String,Invoices> entry : accountMap.entrySet()){
            Row row = new Row();
            String key = entry.getKey();
            Invoices invoices  = entry.getValue();
            //1. 获取所有的FL集合
            List fls = getAllFLList(invoices);
            row.setNo(i);
            //设置Common FL list
            row.setFls((List<FL>)fls.get(0));
            //设置Product Detail FL list
            ProdDetails prodDetails = new ProdDetails();
            prodDetails.setVal("Product Details");
            List<Product> products = (List<Product>)fls.get(1);
            prodDetails.setProducts(products);
            row.setPds(prodDetails);
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
     * 根据accountMap 组装成updateZphoXmlMap <zohoID,updateXml>
     * @param accountMap
     * @return
     * @throws Exception
     */
    private Map<String,String> buildUpd2ZohoXml(Map<String, Invoices> accountMap) throws Exception {
        Map<String,String> updateZphoXmlMap = new HashMap<String, String>();
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.Invoices.Invoices invoices = new darlen.crm.jaxb_xml_object.Invoices.Invoices();
        List<Row> rows = getUpdRowByMap(accountMap);
        if(rows==null || rows.size() == 0){
            return updateZphoXmlMap;
        }else{
            int i = 0;
            for (Map.Entry<String,Invoices> zohoIDAccountEntry : accountMap.entrySet()){
                invoices.setRows(Arrays.asList(rows.get(i)));
                result.setInvoices(invoices);
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
    private List<Row> getUpdRowByMap(Map<String, Invoices> accountMap) throws Exception {
        List<Row> rows = new ArrayList<Row>();

        int i = 1;
        for(Map.Entry<String,Invoices> entry : accountMap.entrySet()){
            Row row = new Row();
            String key = entry.getKey();
            Invoices invoices  = entry.getValue();
            List fls = getAllFLList(invoices);
            row.setNo(i);
            row.setFls((List<FL>)fls.get(0));
            ProdDetails prodDetails = new ProdDetails();
            prodDetails.setVal("Product Details");
            List<Product> products = (List<Product>)fls.get(1);
            prodDetails.setProducts(products);
            row.setPds(prodDetails);
            rows.add(row);
            i++;
        }
        return rows;
    }

    /**
     * 获取所有FL的集合，返回的List中存在2大对象：commonFls，products
     *  1. commonFls --> Common FL 集合
     *  2. List<Product> products -->  当前记录下所有的product集合
     *
     * @param invoices
     * @return
     * @throws Exception
     */
    private List getAllFLList(Invoices invoices) throws Exception {
        Map<String,Object> dbFieldNameValueMap = getDBFieldNameValueMap("darlen.crm.model.result.Invoices",invoices);
        List zohoFieldList = getZOHOFLsByProps(CommonUtils.readProperties("/mapping/dbRdInvoicesFieldMapping.properties"), dbFieldNameValueMap);
        return zohoFieldList;
    }
    /**
     * 根据properties和有效的dbFieldNameValueMap确定返回给zoho的fieldname（获取properties中的key对应的value）和fieldvalue
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    private List getZOHOFLsByProps(Properties properties, Map dbFieldNameValueMap) {
        List allFls = new ArrayList();
        List<Product> products = new ArrayList<Product>();
        List<FL> commonFls = new ArrayList<FL>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                String dbFieldName = (String)entry.getKey();
                if("pds".equalsIgnoreCase(dbFieldName)){
                    /**
                     1.获取记录no、新创建一个Product对象、新建一个List<FL> fls对象
                     2.pdMap:这个是每个no的product中的fieldname和field value组装成的map
                     3.如果properties中含有当前dbFieldName，那么把当前properties value和dbFieldName组合成一个新的FL放入FL的集合中
                     4.组装product， 首先组装no，然后组装FL的集合fls
                     */
                    //1.
                    Map<String,Map> pdsMap = (Map)dbFieldNameValueMap.get("pds");
                    for (Map.Entry<String,Map> pdsEntry : pdsMap.entrySet()) {//多少条prduct detail;记录
                        //1. 获取记录no和新创建一个Product对象
                        Product pd = new Product();
                        String no = pdsEntry.getKey().toString();
                        List<FL> fls = new ArrayList<FL>();

                        //2. 这个是每个no的product中的fieldname和field value组装成的map
                        Map<String,String> pdMap = pdsEntry.getValue();
                        for(Map.Entry<String,String> pdEnry : pdMap.entrySet()){
                            String dbFielName = pdEnry.getKey().toString();
                            //3.如果properties中含有当前dbFieldName，那么把当前properties value和dbFieldName组合成一个新的FL放入FL的集合中
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


    /**
     * 获取DB某个Module下的所有有效的fieldname 和value的Map
     * @param className 包名+类名
     * @return
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<String,Object> getDBFieldNameValueMap(String className,Invoices dbFields) throws Exception {
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
     * @param invoices
     * @return  key : product detail no --> value : map(key:fieldName-->value:fieldValue)
     * @throws IllegalAccessException
     */
    private static Map<String,Object> getDBProdDetlFieldMap(Field field,Invoices invoices) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        List<ProductDetails> pds = (List<ProductDetails>)field.get(invoices);
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
     * TODO: 注意其中一些字段该放入值
     * 1. 注意productid 和product name一定要存在与系统中
     * 2. 注意SALEInvoicesRDERID是系统生成的，不需要设入
     * 3.
     * @param idInvoicesMap
     * @return
     */
    private Invoices getInvoicesDBObj(Map<String, Invoices> idInvoicesMap) throws ParseException {
        Invoices invoices = new Invoices();
        /**DB中的Invoices id*/
        invoices.setErpID("10");
        invoices.setInvoiceSubject("PInv0004");
        invoices.setUser(new User("85333000000071039","qq"));
//        invoices.setOwerID("85333000000071039");
//        invoices.setOwner("qq");
        invoices.setInvoiceDate("2016-09-26 08:48:48");
        /**
         * TODO：注意SO一定要存在，先运行SO
         */
        SO so = new SO();
        so.setSubject("PSO30190412");
        so.setSALESORDERID("");
        invoices.setSo(so);

        invoices.setErp_ExchangeRate("7.77");
        invoices.setPaymentTerm("USD");
        /**
         * Account id 和Name一般是同时存在的；
         * 如果只存在id，Name可以不对；
         * 如果只存在Name，那么会新创建一个客户；
         * 例外：但如果有ID，那么ID必需存在已经创建的客户中
         */
        invoices.setACCOUNTID("85333000000106011");
        /**
         * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
         */
        invoices.setAcctName("富士廊紙品有限公司");
        invoices.setContact("Herris Chan");
        //TODO CusEmail如何处理
        invoices.setEmail("tree317035791@163.com");
        invoices.setDeliveryAddress("2111 9551");
        invoices.setMailAddress("502-6, 5/F, Stelux House, 698 Prince Rd East, San Po Kong, Kln, H.K.");
        invoices.setFax("");
        invoices.setTel("2111 9551");
        invoices.setErp_Currency("USD");
        invoices.setPayMethod("Cheque");
        invoices.setDeliveryMethod("");
        /**不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
        invoices.setPoNO("CCMKT-P12-003\n");
        invoices.setDnNo("");
        invoices.setDeposit("0");
        invoices.setOtherCharge("0");
        invoices.setFreightAmount("0");
        invoices.setTotal(String.valueOf(33150*7.77));
        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
        invoices.setLatestEditTime(currentDate);
        invoices.setCreationTime("2016-09-01 10:10:10");
        invoices.setLatestEditBy("qq");


        /**
         * 设置product detail右下角那堆属于Invoices的字段:Discount,Sub Total,Grand Total
         */
//        invoices.setDiscount("100");
//        invoices.setSubTotal("4000");//小计
//        invoices.setGrandTotal("3000");//累计
        /**
         * 处理list<ProductDetail>, 根据db中的InvoicesID关联Item_Invoices表，拿出所有的product Detail,注意为空情况
         */
        List<ProductDetails> pds = handlePdsList("10",Double.valueOf(invoices.getErp_ExchangeRate()));
        invoices.setPds(pds);
        idInvoicesMap.put(invoices.getErpID(),invoices);
        return invoices;
    }

    /**
     * 这个方法仅仅用于测试
     * @param idInvoicesMap
     * @return
     */
    private Invoices getInvoicesDBObj2(Map<String, Invoices> idInvoicesMap) throws ParseException {
        Invoices invoices = new Invoices();
        /**DB中的Invoices id*/
        invoices.setErpID("10");
        invoices.setInvoiceSubject("PInv0004");
        invoices.setUser(new User("85333000000071039","qq"));
//        invoices.setOwerID("85333000000071039");
//        invoices.setOwner("qq");
        invoices.setInvoiceDate("2016-09-26 08:48:48");
        /**
         * TODO：注意SO一定要存在，先运行SO
         */
        SO so = new SO();
        so.setSubject("PSO30190412");
        so.setSALESORDERID("");
        invoices.setSo(so);

        invoices.setErp_ExchangeRate("7.77");
        invoices.setPaymentTerm("USD");
        /**
         * Account id 和Name一般是同时存在的；
         * 如果只存在id，Name可以不对；
         * 如果只存在Name，那么会新创建一个客户；
         * 例外：但如果有ID，那么ID必需存在已经创建的客户中
         */
        invoices.setACCOUNTID("85333000000106011");
        /**
         * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
         */
        invoices.setAcctName("富士廊紙品有限公司");
        invoices.setContact("Herris Chan");
        //TODO CusEmail如何处理
        invoices.setEmail("tree317035791@163.com");
        invoices.setDeliveryAddress("2111 9551");
        invoices.setMailAddress("502-6, 5/F, Stelux House, 698 Prince Rd East, San Po Kong, Kln, H.K.");
        invoices.setFax("");
        invoices.setTel("2111 9551");
        invoices.setErp_Currency("USD");
        invoices.setPayMethod("Cheque");
        invoices.setDeliveryMethod("");
        /**不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
        invoices.setPoNO("CCMKT-P12-003\n");
        invoices.setDnNo("");
        invoices.setDeposit("0");
        invoices.setOtherCharge("0");
        invoices.setFreightAmount("0");
        invoices.setTotal(String.valueOf(33150*7.77));
        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
        invoices.setLatestEditTime(currentDate);
        invoices.setCreationTime("2016-09-01 10:10:10");
        invoices.setLatestEditBy("qq");


        /**
         * 设置product detail右下角那堆属于Invoices的字段:Discount,Sub Total,Grand Total
         */
//        invoices.setDiscount("100");
//        invoices.setSubTotal("4000");//小计
//        invoices.setGrandTotal("3000");//累计
        /**
         * 处理list<ProductDetail>, 根据db中的InvoicesID关联Item_Invoices表，拿出所有的product Detail,注意为空情况
         */
        List<ProductDetails> pds = handlePdsList("10",Double.valueOf(invoices.getErp_ExchangeRate()));
        invoices.setPds(pds);
        idInvoicesMap.put(invoices.getErpID(),invoices);

        return invoices;
    }

    /**
     *  根据db中的InvoicesID关联Item_Invoices表，拿出所有的product Detail,这里假设找到2条数据
     * @param invoicesID
     * @return
     */
    private List<ProductDetails> handlePdsList(String invoicesID,double erpExchangeRate) {
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        ProductDetails pd =new ProductDetails();
        String realUnitPrice = String.valueOf(0.73 * erpExchangeRate);
        String listPrice = String.valueOf(0.73 * erpExchangeRate);
        pd.setPd_Unit_Price(realUnitPrice);//定价  ： DB-->InvoicesPrice,注意价格要跟Currency一致
        pd.setPd_List_Price(listPrice);//单价  ： DB-->InvoicesPrice,注意价格要跟Currency一致
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
     * 获取Zoho组件的集合，其中包含三个对象，分别为 erpZohoID，erpIDTime，delZohoIDList（zoho ID list）
     * 1. erpZohoID<erpID,zohoID> = zohoListObj.get(0)
     * 2. erpIDTime<erpID,lastEditTime> = zohoListObj.get(1)
     * 3. delZohoIDList
     * @param response
     * @param zohoIDName
     * @param erpIDName
     * @return  zohoCompList
     */
    private List buildZohoComponentList(Response response, String zohoIDName, String erpIDName) {
        List  zohoCompList = new ArrayList();

        Map<String,String> erpZohoIDMap = new HashMap<String, String>();
        Map<String,String> erpIDTimeMap = new HashMap<String, String>();
        List delZohoIDList = new ArrayList();
        zohoCompList.add(erpZohoIDMap);
        zohoCompList.add(erpIDTimeMap);
        zohoCompList.add(delZohoIDList);
        List<Row> rows = response.getResult().getInvoices().getRows();
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

    private String retriveZohoRecords() {
        String retZohoStr = "";
        try {
//            String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecordById";
            String targetURL_Invoices = "https://crm.zoho.com.cn/crm/private/xml/SalesOrders/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Invoices);
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
 <FL val="Subject">PInvoices30190412</FL>
 <FL val="MailAddress">Saltängsvägen 18, 721 32 Västerås Sweden</FL>
 <FL val="DeliveryMethod">FOB Shenzhen</FL>
 <FL val="ACCOUNTID">85333000000106011</FL>
 <FL val="Account Name">富士廊紙品有限公司</FL>
 <FL val="ERP_ExchangeRate">7.77</FL>
 <FL val="InvoicesID">10</FL>
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