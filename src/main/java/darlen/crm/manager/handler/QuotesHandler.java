/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HandleInvoices.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager.handler;

import darlen.crm.jaxb.Quotes.Response;
import darlen.crm.jaxb.Quotes.Result;
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.manager.AbstractModule;
import darlen.crm.manager.ConfigManager;
import darlen.crm.model.result.Invoices;
import darlen.crm.model.result.ProductDetails;
import darlen.crm.model.result.Quotes;
import darlen.crm.model.result.User;
import darlen.crm.util.*;
import org.apache.log4j.Logger;

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
 *

 * Description：ZOHO_CRM
 * Created on  2016/09/19 22：42
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：42   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class QuotesHandler extends AbstractModule {
    private static QuotesHandler quotesHandler;
    private static Logger logger =  Logger.getLogger(QuotesHandler.class);
    private QuotesHandler(){};

    public synchronized  static QuotesHandler getInstance(){
        if(quotesHandler == null){
            quotesHandler = new QuotesHandler();
            quotesHandler.getProperties();
        }
        return quotesHandler;
    }


    /**
     * Ⅰ： 从ZOHO获取所有的XML并转化为java对象，所有的Invoices记录，并且最后组装为3组对象：erpZohoIDMap，erpIDTimeMap，delZOHOIDList
     *
     * 1. 从ZOHO获取有效的xml ： retrieveZohoRecords()
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
    public List buildSkeletonFromZohoList() throws Exception {
        logger.debug("# Ⅰ InvoicesHandler 【buildSkeletonFromZohoList】...");
//      1. 获取所有的记录
        List<ProdRow> rows = new ArrayList<ProdRow>();
        retrieveAllRowsFromZoho(1, Constants.MAX_FETCH_SIZE, rows);

//      2. 获取Zoho组件的集合，其中包含四个对象，分别为 erpZohoIDMap，erpZohoIDTimeMap，delZohoIDList（zoho ID list），zohoIDProdIDsMap
        List  zohoModuleList = buildZohoComponentList(rows, Constants.MODULE_QUOTES_ID, Constants.ERPID,ModuleNameKeys.Quotes.toString());

        return zohoModuleList;
    }

    /**
     *  从ZOHO获取所有的记录，并返回所有的记录（原因是因为每次ZOHO最大能获取200条，并且没法知道获取最大条数）
     *  1. 从ZOHO获取有效的xml
     *  2. xml 转 java bean
     *  3. 由javabean获取所有的row记录，如果没有取完，需要重新取
     *     //如果已经达到了最大的查询条数，则代表还可以继续下一次查询；如果没有，则代表记录已经获取完
     * @param fromIndex
     * @param toIndex
     * @param allRows
     * @return
     * @throws Exception
     */
    private List<ProdRow> retrieveAllRowsFromZoho(int fromIndex, int toIndex, List<ProdRow> allRows) throws Exception {
        logger.debug("# 1.1 InvoicesHandler 【retrieveAllRowsFromZoho】...");
//     1. 从ZOHO获取有效的xml
        String zohoStr =  retrieveZohoRecords(ModuleNameKeys.Quotes.toString(),fromIndex,toIndex);

        if(StringUtils.isEmptyString(zohoStr) || zohoStr.indexOf("<error>") != -1){ //如果获取不到
            throw  new Exception("【QuotesHandler】,retrieveAllRowsFromZoho出现错误, retrieveZohoRecords返回Error");
        }

//      2. xml 转 java bean
        Response response = JaxbUtil.converyToJavaBean(convertFLToPdsXmlTag2(zohoStr), Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        logger.debug("1.2 【retrieveAllRowsFromZoho】，转化ZOHO获取XML回来的Java对象\n#" + response);

//      3. 由javabean获取所有的row记录，如果没有取完，需要重新取
        logger.debug("# 1.3 【retrieveAllRowsFromZoho】，如果没有取完，需要迭代遍历...");
        //TODO 如果没有数据<response uri="/crm/private/xml/Products/getRecords"><nodata><code>4422</code><message>There is no data to show</message></nodata></response>
        if(null != response.getResult()){
            List<ProdRow>  currentRows = response.getResult().getQuotes().getRows();
            allRows.addAll(currentRows);
            //如果已经达到了最大的查询条数，则代表还可以继续下一次查询；如果没有，则代表记录已经获取完
            if(currentRows.size() == Constants.MAX_FETCH_SIZE){
                logger.debug("#通过RetrieveRecord需要已经遍历的次数：" + ((toIndex / Constants.MAX_FETCH_SIZE) ));
                retrieveAllRowsFromZoho(fromIndex + Constants.MAX_FETCH_SIZE, toIndex + Constants.MAX_FETCH_SIZE, allRows);
            }
        }
        return allRows;
    }


    /**
     * Ⅱ：这里组装db中的AcctObjList
     * 1.Accounts --> dbAcctList.get(0)
     * 2.idAccountsMap<CustomerID,Accounts> --> dbAcctList.get(1)
     */
    public List buildDBObjList() throws Exception {
        logger.debug("# Ⅱ：QuotesHandler 【buildDBObjList】...");
        List dbAcctList =  DBUtils.getQuotesList();
        //Map<String,Object> idInvoicesMap = DBUtils.getInvoiceMap();
//        getDBObj(idInvoicesMap);
//        Invoices accouts2 = getDBObj2(idInvoicesMap);

        //dbAcctList.add(idInvoicesMap);
        logger.debug("【buildDBObjList】,打印DB对象："+Constants.COMMENT_PREFIX +dbAcctList);
        //CommonUtils.printList(dbAcctList, "打印DB对象：：：");
        return dbAcctList;
    }


    /**
     * Ⅲ：由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     */

    /**
     * 由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     * 1.updateAccountMap<>：如果Zoho ID存在于DB对象集合中，则判断 lastEditTime是否被修改，如果修改了，则直接组装到updateAccountMap中
     * 2.delZOHOIDList：如果zohoid不存在于dbModel中，则直接调用ZOHO删除API：
     * 3.addAccountMap：如果dbModel中的id不存在于zohoMap中，则组装dbModel为xml并调用Zoho中的添加API：
     * @return
     */
    public List build2ZohoObjSkeletonList() throws Exception {
        logger.debug("# Ⅲ: QuotesHandler 【build2ZohoObjSkeletonList】...");
        //1. 获取ZOHO对象的骨架集合
        List allZohoObjList = buildSkeletonFromZohoList();
        Map<String,String> erpZohoIDMap = new HashMap<String, String>();
        Map<String,String> erpIDTimeMap =  new HashMap<String, String>();
        //get delZohoIDList when ZOHO erp id is null
        List<String> delZohoIDList = new ArrayList<String>();
        if(allZohoObjList != null && allZohoObjList.size() > 0){
            erpZohoIDMap = (Map)allZohoObjList.get(0);
            erpIDTimeMap =  (Map)allZohoObjList.get(1);
            delZohoIDList = (List)allZohoObjList.get(2);
        }

        //2.组装DB 对象List
        List dbModuleList = buildDBObjList();
        //Map<String,Object> idInvoicesMap = (Map<String,Object>)dbModuleList.get(0);

        //3. 解析并组装addMap、updateMap、delZohoIDList
        return build2Zoho3PartObj(erpZohoIDMap,erpIDTimeMap,delZohoIDList,dbModuleList);
    }

    /**
     * Ⅳ：由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
     * addZOHOXml:最大100条数据
     * updateZOHOXml：一次只能更新1条
     * deleteZOHOIDsList：转换为以逗号分割ZOHO ID的字符串
     */

    /**
     * Ⅳ: 由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
     * List<String> addZohoXmlList :每一百条数据组装成xml放入list里面
     * Map<zohoID,zohoXML> updateZOHOXmlMap ：以zohoID为key，xml为value
     * deleteZOHOIDsList： zohoID的集合
     * @return  zohoComponentList
     * @throws Exception
     */
    public List build2ZohoXmlSkeleton() throws Exception {
        logger.debug("# Ⅳ: QuotesHandler 【build2ZohoXmlSkeleton】...");
        //1. 获取发送到ZOHO对象集合骨架
        logger.debug("4.1 【build2ZohoXmlSkeleton】, 开始执行方法：build2ZohoObjSkeletonList");
        List zohoComponentList = build2ZohoObjSkeletonList();
        Map<String,Invoices> addAccountMap =  (Map<String,Invoices> )zohoComponentList.get(0);
        Map<String,Invoices> updateAccountMap =(Map<String,Invoices> )zohoComponentList.get(1);
        List deleteZOHOIDsList  = (List)zohoComponentList.get(2);

        String className = "darlen.crm.model.result.Quotes";
        //"/mapping/dbRdQuotesFieldMapping.properties"
        Properties fieldMappingProps = ConfigManager.readProperties(Constants.PROPS_QUOTE_DB_MAPPING);
        //2. 添加
        logger.debug("4.2 【build2ZohoXmlSkeleton】, 开始获取 Quotes【insert】的的XML#####################");
        List<String> addZohoXmlList =  buildAdd2ZohoXml(addAccountMap,className,fieldMappingProps);
        logger.debug("end组装 AddZOHOXML...size:::"+addZohoXmlList.size());

        //3. 更新
        logger.debug("4.3 【build2ZohoXmlSkeleton】, 开始获取 Quotes【update】的的XML#####################");
        Map<String,String> updateZOHOXmlMap  = buildUpd2ZohoXml(updateAccountMap,className,fieldMappingProps);
        logger.debug("end组装 updateZOHOXml...size:::"+updateZOHOXmlMap.size());

        List zohoXMLList = new ArrayList();
        zohoXMLList.add(addZohoXmlList);
        zohoXMLList.add(updateZOHOXmlMap);
        //4. 删除
        logger.debug("4.4 【build2ZohoXmlSkeleton】, 打印需要删除的ZOHO ID的集合"+Constants.COMMENT_PREFIX+org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,","));
        zohoXMLList.add(deleteZOHOIDsList);//org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,",")
        return zohoXMLList;
    }


    /**
     * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
     * 更新（testUpdateAcctRecord）
     * 添加（testAddAcctRecord）
     * 删除（testDelAcctRecord）
     */
    public List execSend() throws Exception {
        logger.debug("# Ⅴ：InvoicesHandler [execSend]...");
        List zohoXMLList = build2ZohoXmlSkeleton();
        int addFailNum = addRecords(ModuleNameKeys.Quotes.toString(),Constants.ZOHO_CRUD_ADD,(List<String>)zohoXMLList.get(0));
        int updFailNum = updateRecords(ModuleNameKeys.Quotes.toString(),Constants.ZOHO_CRUD_UPDATE,(Map<String,String>) zohoXMLList.get(1));
        int delFailNum = delRecords(ModuleNameKeys.Quotes.toString(),Constants.ZOHO_CRUD_DELETE,(List)zohoXMLList.get(2));
        List result = new ArrayList();
        result.add(0,addFailNum);
        result.add(1,updFailNum);
        result.add(2,delFailNum);
        return result;
    }

    /**
     * 根据accountMap 组装成每100条数据的addZohoXmlList中
     * 注意：getAddRowsMap
     *
     * @param accountMap<String, Invoices>
     * @return
     * @throws Exception
     */
    private List<String> buildAdd2ZohoXml(Map accountMap,String className,Properties fieldMappingProps) throws Exception {
        logger.debug("# 4.2 InvoicesHandler [buildAdd2ZohoXml]...");
        List<String> addZohoXmlList= new ArrayList<String>();
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb.Quotes.Quotes quotes = new darlen.crm.jaxb.Quotes.Quotes();
        Map<Integer,List<ProdRow>> addRowsMap = getAddRowsMap(accountMap,className,fieldMappingProps);
        if(addRowsMap==null || addRowsMap.size() == 0){
            return addZohoXmlList;
        }else{
            for(int i = 0 ; i< addRowsMap.size(); i ++){
                quotes.setRows(addRowsMap.get(i));
                result.setQuotes(quotes);
                response.setResult(result);
                String str  = JaxbUtil.convertToXml(response);
                //转换pds为FL
                addZohoXmlList.add(str.replace("pds","FL"));
            }
        }
        return addZohoXmlList;
    }


    /**
     * 根据accountMap 组装成updateZphoXmlMap <zohoID,updateXml>
     * @param accountMap<String, Invoices>
     * @return
     * @throws Exception
     */
    private Map<String,String> buildUpd2ZohoXml(Map accountMap,String className,Properties fieldMappingProps) throws Exception {
        logger.debug("# 4.3 InvoicesHandler [buildUpd2ZohoXml]...");
        Map<String,String> updateZphoXmlMap = new HashMap<String, String>();
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb.Quotes.Quotes quotes = new darlen.crm.jaxb.Quotes.Quotes();
        List<ProdRow> rows = getUpdRowByMap(accountMap,className,fieldMappingProps);
        if(rows==null || rows.size() == 0){
            return updateZphoXmlMap;
        }else{
            int i = 0;
            Iterator it = accountMap.keySet().iterator();
            while(it.hasNext()){
                Object key = it.next( );
                Object value = accountMap.get(key);
                quotes.setRows(Arrays.asList(rows.get(i)));
                result.setQuotes(quotes);
                response.setResult(result);
                if(ConfigManager.isDevMod())
                logger.debug("4.3 组装更新的第"+(i+1)+"条数据：：：");
                str = JaxbUtil.convertToXml(response);
                updateZphoXmlMap.put(StringUtils.nullToString(key),str.replace("pds","FL"));
                i++;
            }

        }
        return updateZphoXmlMap;
    }



    //TODO 注意价格： 汇率

    /**
     * TODO: 注意其中一些字段该放入值
     * 1. 注意拥有者User一定要存在系统中
     * 2. 注意SO中的SALESORDERID与Sales Order一定要存在系统
     * 3. 注意Account一定要存在系统
     * 4. 注意Product中的productid 和product name一定要存在与系统中
     * @param idInvoicesMap
     * @return
     */
    private Quotes getDBObj(Map<String, Quotes> idInvoicesMap) throws ParseException {
        logger.debug("# InvoicesHandler [getDBObj]...");
        Quotes invoices = new Quotes();
        /**DB中的Invoices id*/
//        invoices.setErpID("10");
//        invoices.setInvoiceSubject("PInv0004");
//        //1. 注意拥有者User一定要存在系统中
//        User user = CommonUtils.fetchDevUser(false);
//        invoices.setUser(user);
////        invoices.setOwerID("85333000000071039");
////        invoices.setOwner("qq");
//        invoices.setInvoiceDate("2016-09-26 08:48:48");
//        /**
//         * TODO：2. 注意SO中的SALESORDERID与Sales Order一定要存在系统
//         */
////        SO so = new SO();
////        so.setSubject("PSO30190412");
////        so.setSALESORDERID("85333000000106003");
//        invoices.setSALESORDERID("80487000000096013");
//        invoices.setSoName("tree31701");
//
//        invoices.setErp_ExchangeRate("7.77");
//        invoices.setPaymentTerm("30 days credit after delivery");
//        invoices.setCustomerNo("test");
//        invoices.setDueDate("2016-10-16 23:59:59");
//        /**
//         * 3. 注意Account一定要存在系统
//         * Account id 和Name一般是同时存在的；
//         * 如果只存在id，Name可以不对；
//         * 如果只存在Name，那么会新创建一个客户；
//         * 例外：但如果有ID，那么ID必需存在已经创建的客户中
//         */
//        invoices.setACCOUNTID("80487000000094005");
//        /**
//         * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
//         */
//        invoices.setAcctName("富士廊紙品有限公司");
//        invoices.setContact("Herris Chan");
//        //TODO CusEmail如何处理
//        invoices.setEmail("tree317035791@163.com");
//        invoices.setDeliveryAddress("2111 9551");
//        invoices.setMailAddress("502-6, 5/F, Stelux House, 698 Prince Rd East, San Po Kong, Kln, H.K.");
//        invoices.setFax("123456");
//        invoices.setTel("2111 9551");
//        invoices.setErp_Currency("USD");
//        invoices.setPayMethod("Cheque");
//        invoices.setDeliveryMethod("test");
//        /**不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
//        invoices.setPoNO("CCMKT-P12-003");
//        invoices.setDnNo("test");
//        invoices.setDeposit("0");
//        invoices.setOtherCharge("0");
//        invoices.setFreightAmount("0");
//        invoices.setTotal(StringUtils.nullToString(33150 * 7.77));
//        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
//        invoices.setLatestEditTime(currentDate);
//        invoices.setCreationTime("2016-09-01 10:10:10");
//        invoices.setLatestEditBy(user.getUserName());
//
//
//        /**
//         * 设置product detail右下角那堆属于Invoices的字段:Discount,Sub Total,Grand Total
//         */
//        invoices.setCusDiscount("100");
////        invoices.setDiscount("100");
//        invoices.setSubTotal("4000");//小计
//        invoices.setGrandTotal("3000");//累计
//        /**
//         * 4. 注意Product中的productid 和product name一定要存在与系统中
//          * 处理list<ProductDetail>, 根据db中的InvoicesID关联Item_Invoices表，拿出所有的product Detail,注意为空情况
//         */
//        List<ProductDetails> pds = handlePdsList("10",Double.valueOf(invoices.getErp_ExchangeRate()));
//        invoices.setPds(pds);
        idInvoicesMap.put(invoices.getErpID(),invoices);
        return invoices;
    }

    /**
     * 这个方法仅仅用于测试
     * @param idInvoicesMap
     * @return
     */
    private Invoices getDBObj2(Map<String, Invoices> idInvoicesMap) throws ParseException {
        logger.debug("# InvoicesHandler [getDBObj2]...");
        Invoices invoices = new Invoices();
        /**DB中的Invoices id*/
        invoices.setErpID("11");
        invoices.setInvoiceSubject("PInv0005");
        User user = CommonUtils.fetchDevUser(false);
        invoices.setUser(user);
//        invoices.setOwerID("85333000000071039");
//        invoices.setOwner("qq");
        invoices.setInvoiceDate("2016-09-26 08:48:48");
        /**
         * TODO：注意SO一定要存在，先运行SO
         */
//        SO so = new SO();
//        so.setSubject("PSO30190412—1");
//        so.setSALESORDERID("");
        invoices.setSALESORDERID("80487000000094025");
        invoices.setSoName("PSO30190412");

        invoices.setErp_ExchangeRate("7.77");
        invoices.setPaymentTerm("USD");
        /**
         * Account id 和Name一般是同时存在的；
         * 如果只存在id，Name可以不对；
         * 如果只存在Name，那么会新创建一个客户；
         * 例外：但如果有ID，那么ID必需存在已经创建的客户中
         */
        invoices.setACCOUNTID("80487000000096005");
        /**
         * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
         */
        invoices.setAcctName("永昌紙品");
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
        invoices.setPoNO("CCMKT-P12-003");
        invoices.setDnNo("");
        invoices.setDeposit("0");
        invoices.setOtherCharge("0");
        invoices.setFreightAmount("0");
        invoices.setTotal(StringUtils.nullToString(33150 * 7.77));
        String currentDate = ThreadLocalDateUtil.formatDate(new Date());
        invoices.setLatestEditTime(currentDate);
        invoices.setCreationTime("2016-09-01 10:10:10");
        invoices.setLatestEditBy(user.getUserName());


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
        logger.debug("# InvoicesHandler [handlePdsList]...");
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        ProductDetails pd =new ProductDetails();
        String realUnitPrice = StringUtils.nullToString(0.73 * erpExchangeRate);
        String listPrice = StringUtils.nullToString(0.73 * erpExchangeRate);
        pd.setPd_Unit_Price(realUnitPrice);//定价  ： DB-->InvoicesPrice,注意价格要跟Currency一致
        pd.setPd_List_Price(listPrice);//单价  ： DB-->InvoicesPrice,注意价格要跟Currency一致
        pd.setPd_Quantity("10000");//数量
        pd.setPd_Discount("0");//折扣
        pd.setPd_Tax("0");//税，Matrix默认这个字段是0，因为不用税

        pd.setPd_Product_Description("Wesc Cardboard drawer box ");
        /**
         * 注意 product id和Name一定要是已经存在与产品里面的
         */
        pd.setPd_Product_Id("80487000000095003");
        pd.setPd_Product_Name("尼龍背心環保袋");//TODO 需要找ken确认ItemName为【Name】是表示什么意思？是否根据id从Item表找
        pd.setPd_Total("86664.0");//金额
        pd.setPd_Net_Total("86666.0");//总计

        pds.add(pd);
        return pds;
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