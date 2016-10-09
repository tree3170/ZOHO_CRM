/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HanderAccouts.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager.handler;

import darlen.crm.jaxb.Accounts.Response;
import darlen.crm.jaxb.Accounts.Result;
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.manager.AbstractModule;
import darlen.crm.manager.ConfigManager;
import darlen.crm.model.result.Accounts;
import darlen.crm.model.result.User;
import darlen.crm.util.*;
import org.apache.log4j.Logger;

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
public class AccountsHandler  extends AbstractModule {
    private static AccountsHandler handleAccounts;
//    Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime) 参考（JaxbAccountsTest.java/JaxbSOTest.java/JaxbLeadsTest.java）
    private static Logger logger =  Logger.getLogger(AccountsHandler.class);


    public synchronized  static AccountsHandler getInstance(){
        if(handleAccounts == null){
            handleAccounts = new AccountsHandler();
            handleAccounts.getProperties();
        }
        return handleAccounts;
    }

    /**
     * Ⅰ：这里仅仅只是组装zohoAcctObjList
     * 1. 从ZOHO获取有效的xml
     * 2. xml 转 java bean
     * 3. 组装 zohoModuleList ，其中里面的element有：
     * 1. erpZohoIDMap<erpID,zohoID> = zohoListObj.get(0)
     * 2. erpIDTimeMap<erpID,lastEditTime> = zohoListObj.get(1)
     * 3. delZohoIDList = zohoListObj.get(2) ：zoho ID list-->ERP ID 为空时的 加入删除列表
     */
    public List buildSkeletonFromZohoList() throws Exception {
        logger.debug("# Ⅰ: AccountHandler 【buildSkeletonFromZohoList】...");
        //0 遍历获取List<Response>

//      1. 从ZOHO获取有效的xml
//        String zohoStr =  handleAccounts.retrieveZohoRecords(ModuleNameKeys.Accounts.toString(),1,100);

//      2. xml 转 java bean
//        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
//        logger.debug("转化ZOHO获取XML回来的Java对象\n#" + response);

//      3. 组装 zohoModuleList
//        List  zohoModuleList;
//        //TODO 如果没有数据<response uri="/crm/private/xml/Products/getRecords"><nodata><code>4422</code><message>There is no data to show</message></nodata></response>
//        if(null != response.getResult()){
//            List<ProdRow> rows = response.getResult().getAccounts().getRows();
//            zohoModuleList = buildZohoComponentList(rows, Constants.MODULE_ACCOUNTS_ID, Constants.ERPID);
//        }else{
//            //TODO 解析response ， 出了错
//            logger.debug("没有数据了：：：\n" + zohoStr);
//            zohoModuleList = new ArrayList();
//        }
//      1. 获取所有的记录
        List<ProdRow> rows = new ArrayList<ProdRow>();
        retrieveAllRowsFromZoho(1, Constants.MAX_FETCH_SIZE, rows);

//      2. 获取Zoho组件的集合，其中包含三个对象，分别为 erpZohoIDMap，erpZohoIDTimeMap，delZohoIDList（zoho ID list）
        List  zohoModuleList = buildZohoComponentList(rows, Constants.MODULE_ACCOUNTS_ID, Constants.ERPID,ModuleNameKeys.Accounts.toString());
        return zohoModuleList;
    }

    /**
     *  从ZOHO获取所有的记录，并返回所有的记录（原因是因为每次ZOHO最大能获取200条，并且没法知道获取最大条数）
     *  1.1  从ZOHO获取有效的xml
     *  1.2. xml 转 java bean
     *  1.3. 由javabean获取所有的row记录，如果没有取完，需要重新取
     *     //如果已经达到了最大的查询条数，则代表还可以继续下一次查询；如果没有，则代表记录已经获取完
     * @param fromIndex
     * @param toIndex
     * @param allRows
     * @return
     * @throws Exception
     */
    private List<ProdRow> retrieveAllRowsFromZoho(int fromIndex, int toIndex, List<ProdRow> allRows) throws Exception {
        logger.debug("# 1.1 AccountHandler 【retrieveAllRowsFromZoho】...fromIndex="+fromIndex+", toIndex="+toIndex+", row size="+allRows.size());
//     1. 从ZOHO获取有效的xml
        String zohoStr =  handleAccounts.retrieveZohoRecords(ModuleNameKeys.Accounts.toString(),fromIndex,toIndex);

//      2. xml 转 java bean
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        logger.debug("1.2 【retrieveAllRowsFromZoho】，转化ZOHO获取XML回来的Java对象\n#" + response);

//      3. 由javabean获取所有的row记录，如果没有取完，需要重新取
        logger.debug("# 1.3 AccountHandler 【retrieveAllRowsFromZoho】，如果没有取完，需要迭代遍历...");
        //TODO 如果没有数据<response uri="/crm/private/xml/Products/getRecords"><nodata><code>4422</code><message>There is no data to show</message></nodata></response>
        if(null != response.getResult()){
            List<ProdRow>  currentRows = response.getResult().getAccounts().getRows();
            allRows.addAll(currentRows);
            //如果已经达到了最大的查询条数，则代表还可以继续下一次查询；如果没有，则代表记录已经获取完
            if(currentRows.size() == Constants.MAX_FETCH_SIZE){
                fromIndex = fromIndex + Constants.MAX_FETCH_SIZE;
                toIndex = toIndex + Constants.MAX_FETCH_SIZE;
                logger.debug("#通过RetrieveRecord需要已经遍历的次数：" + ((toIndex / Constants.MAX_FETCH_SIZE) ));
                retrieveAllRowsFromZoho(fromIndex,toIndex , allRows);
            }
        }
        return allRows;
    }

    /**
     * Ⅱ：这里组装db中的AcctObjList
     * 1. Map<String,Account> dbIDModuleObjMap
     * 2.  Map<String,String> dbIDEditTimeMap
     */
    public List buildDBObjList() throws Exception {
        logger.debug("# Ⅱ： AccountHandler 【buildDBObjList】...");
        List dbAcctList = DBUtils.getAccountMap();
        //Map<String,Object> erpIDProductsMap = DBUtils.getAccountMap();;
//        for(int i = 6; i< 300;i++){
//            getDBObj(erpIDProductsMap,1);
//        }
//        getAcctDBObj2(erpIDProductsMap);

        //dbAcctList.add(erpIDProductsMap);
        logger.debug("【buildDBObjList】,打印DB对象："+Constants.COMMENT_PREFIX +dbAcctList);
        //CommonUtils.printList(dbAcctList, "Build DB Object :::");
        return dbAcctList;
    }

    /**
     * Ⅲ 由获得的ZOHO所有对象集合和从DB获取的对象集合(dbIDModuleObjMap)，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     * 1.updateModuleObjMap<>：如果Zoho ID存在于DB对象集合中，则判断 lastEditTime是否被修改，如果修改了，则直接组装到updateAccountMap中
     * 2.delZohoIDList：如果ZOHO ID不存在于dbIDModuleObjMap中，则直接把DB ID添加到delZohoIDList
     * 3.addModuleObjMap：如果dbIDModuleObjMap中的id不存在于erpIDTimeMap中，则添加ZOHOID和Module对象到addModuleObjMap
     * erpZohoIDMap： ERP ID 和 ZOHO ID的集合
     * erpIDTimeMap： ERP ID 和LastEditTime的集合
     * delZohoIDList： ZOHO ID的集合
     * dbIDModuleObjMap： DB ID 和Module对象的集合
     * @return
     */
    public List build2ZohoObjSkeletonList() throws Exception {
        logger.debug("# Ⅲ: AccountHandler 【build2ZohoObjSkeletonList】...");
//        1. 获取ZOHO对象的骨架集合
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

//        2.组装DB 对象List
        List dbAcctList = buildDBObjList();
        Map<String,Object> dbIDModuleObjMap = (Map<String,Object>)dbAcctList.get(0);
        Map<String,String> dbIDEditTimeMap = (Map<String,String>)dbAcctList.get(1);

//        3. 组装发送到ZOHO的三大对象并放入到List中:addMap、updateMap、delZohoIDList
        return build2Zoho3PartObj(erpZohoIDMap,erpIDTimeMap,delZohoIDList,dbAcctList);
    }


    /**
     * Ⅳ 由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
     * addZohoXmlList : List<String> --> 每一百条数据组装成xml放入list里面
     * updateZOHOXmlMap ：Map<zohoID,zohoXML>  --> 以zohoID为key，xml为value
     * deleteZOHOIDsList： zohoID的集合
     * @return  zohoComponentList
     * @throws Exception
     */
    public List build2ZohoXmlSkeleton() throws Exception {
        logger.debug("# Ⅳ: AccountHandler 【build2ZohoXmlSkeleton】...");
        //1. 获取发送到ZOHO的三大对象集合骨架
        logger.debug("4.1 【build2ZohoXmlSkeleton】, 开始执行方法：build2ZohoObjSkeletonList");
        List zohoComponentList = build2ZohoObjSkeletonList();
        Map<String,Accounts> addAccountMap =  (Map<String,Accounts> )zohoComponentList.get(0);
        Map<String,Accounts> updateAccountMap =  (Map<String,Accounts> )zohoComponentList.get(1);
        List deleteZOHOIDsList  = (List)zohoComponentList.get(2);

        String className = "darlen.crm.model.result.Accounts";
        //"/mapping/dbRdAccountsFieldMapping.properties"
        Properties fieldMappingProps = ConfigManager.readProperties(Constants.PROPS_ACCT_DB_MAPPING);

//        2. 添加
        logger.debug("4.2 【build2ZohoXmlSkeleton】, 开始获取 Accounts【insert】的的XML#####################");
        List<String> addZohoXmlList = buildAdd2ZohoXml(addAccountMap,className,fieldMappingProps);
        logger.debug("end组装 AddZOHOXML..size:"+addZohoXmlList.size());

//        3. 更新
        logger.debug("4.3 【build2ZohoXmlSkeleton】, 开始获取 Accounts【update】的的XML#####################");
        logger.debug("begin组装 updateZOHOXml...\n");
        Map<String,String> updateZOHOXmlMap  = buildUpd2ZohoXml(updateAccountMap,className,fieldMappingProps);
        logger.debug("end组装 updateZOHOXml...size:"+updateZOHOXmlMap.size());

        List zohoXMLList = new ArrayList();
        zohoXMLList.add(addZohoXmlList);
        zohoXMLList.add(updateZOHOXmlMap);
//        4. 删除
        logger.debug("4.4 【build2ZohoXmlSkeleton】, 打印需要删除的ZOHO ID的集合"+Constants.COMMENT_PREFIX+org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,","));
        zohoXMLList.add(deleteZOHOIDsList);//org.apache.commons.lang.StringUtils.join(deleteZOHOIDsList,",")
        return zohoXMLList;
    }


    /**
     * ========================Ⅴ：发送xml data到ZOHO，并执行更新、添加或者删除操作
     * 更新（testUpdateAcctRecord）
     * 添加（testAddAcctRecord）
     * 删除（testDelAcctRecord） --->最后才执行，因为如果产品存在于其他模块，会产生删不掉的情况
     *
     * @return
     */
    public List execSend() throws Exception {
        logger.debug("# Ⅴ: AccountHandler 【execSend】...");
        List zohoXMLList = build2ZohoXmlSkeleton();
        int addFailNum = addRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_ADD,(List<String>)zohoXMLList.get(0));
        int updFailNum = updateRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_UPDATE,(Map<String,String>) zohoXMLList.get(1));
        int delFailNum = delRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_DELETE,(List)zohoXMLList.get(2));
        List result = new ArrayList();
        result.add(0,addFailNum);
        result.add(1,updFailNum);
        result.add(2,delFailNum);
        return result;
    }
//    public void addRecords(String moduleName){
//        try {
//            String targetURL_Accounts = zohoPropsMap.get(Constants.INSERT_ACCOUTNS_URL);//"https://crm.zoho.com.cn/crm/private/xml/Accounts/insertRecords";
//            List<String> addZohoXMLList = (List<String> ) build2ZohoXmlSkeleton().get(0);
//            for(int i = 0; i < addZohoXMLList.size(); i ++){
//                //System.err.println("添加第"+(i+1)+"条数据，xml为："+addZohoXMLList.get(i));
//                logger.debug("添加第"+(i+1)+"条数据");
//                Map<String,String> postParams = new HashMap<String, String>();
//                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//                postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXMLList.get(i));
//                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
//                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
//                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
//
//                CommonUtils.executePostMethod(postParams);
//            }
//
//        } catch(Exception e) {
//            logger.error("执行更新Module操作出现错误",e);
//        }
//    }
//    public void updateRecords(){
//        try {
////            String id_Accounts = "85333000000088001";//客户1ID
//            //"https://crm.zoho.com.cn/crm/private/xml/Accounts/updateRecords";
//            String targetURL_Accounts = zohoPropsMap.get(Constants.UPDATE_ACCOUTNS_URL);
//            //TODO: qq:85333000000071039, tree3170:85333000000071001
//            Map<String,String> updZohoXMLMap = (Map<String,String>) build2ZohoXmlSkeleton().get(1);
//            int i = 1 ;
//            for(Map.Entry<String,String> zohoIDUpdXmlEntry : updZohoXMLMap.entrySet()){
//                System.err.println("更新第"+(i)+"条数据，ZOHO ID为"+zohoIDUpdXmlEntry.getKey()+"\nxml为："+zohoIDUpdXmlEntry.getValue());
//                Map<String,String> postParams = new HashMap<String, String>();
//                postParams.put(Constants.HTTP_POST_PARAM_ID,zohoIDUpdXmlEntry.getKey());
//                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//                postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,zohoIDUpdXmlEntry.getValue());
//                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
//                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
//                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
//
//                CommonUtils.executePostMethod(postParams);
//                i++;
//            }
//        } catch(Exception e) {
//            logger.error("执行更新Module操作出现错误",e);
//        }
//    }
//
//
//    public void delRecords(){
//        try {
//            String targetURL_Accounts = zohoPropsMap.get(Constants.DELETE_ACCOUTNS_URL);//"https://crm.zoho.com.cn/crm/private/xml/Accounts/deleteRecords";
//            List deleteZOHOIDsList = (List)build2ZohoXmlSkeleton().get(2);
//            for(int i = 0; i < deleteZOHOIDsList.size(); i++){
//                logger.debug("删除第"+(i+1)+"条数据");
//                Map<String,String> postParams = new HashMap<String, String>();
//                postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL_Accounts);
//                postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
//                postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
//                postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
//                postParams.put(Constants.HTTP_POST_PARAM_ID, deleteZOHOIDsList.get(i).toString());
//
//                CommonUtils.executePostMethod(postParams);
//            }
//        } catch(Exception e) {
//            logger.error("执行更新Module操作出现错误",e);
//        }
//    }

    /**
     * 根据accountMap 组装成每100条数据的addZohoXmlList中
     * 注意：getAddRowsMap
     *
     * @param accountMap
     * @return
     * @throws Exception
     */
    public List<String>  buildAdd2ZohoXml(Map accountMap,String className,Properties fieldMappingProps) throws Exception {
        logger.debug("# 4.2 AccountHandler 【buildAdd2ZohoXml】...");
        List<String> addZohoXmlList= new ArrayList<String>();
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb.Accounts.Accounts accounts = new darlen.crm.jaxb.Accounts.Accounts();
//        获取被添加的所有Row的集合
        Map<Integer,List<ProdRow>> addRowsMap = getAddRowsMap(accountMap,className,fieldMappingProps);
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
    private Map<String,String> buildUpd2ZohoXml(Map accountMap,String className,Properties fieldMappingProps) throws Exception {
        logger.debug("# 4.3 AccountHandler 【buildAdd2ZohoXml】...");
        Map<String,String> updateZphoXmlMap = new HashMap<String, String>();
        String str = "";
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb.Accounts.Accounts accounts = new darlen.crm.jaxb.Accounts.Accounts();
        List<ProdRow> rows = getUpdRowByMap(accountMap,className,fieldMappingProps);
        if(rows==null || rows.size() == 0){
            return updateZphoXmlMap;
        }else{
            int i = 0;
            //            for (Map.Entry<?,?> zohoIDAccountEntry : accountMap.entrySet()){
            Iterator it = accountMap.keySet().iterator();
            while(it.hasNext()){
                Object key = it.next( );
                Object value = accountMap.get(key);
                accounts.setRows(Arrays.asList(rows.get(i)));
                result.setAccounts(accounts);
                response.setResult(result);
                if(ConfigManager.isDevMod())
                     logger.debug("4.3 组装更新的第"+(i+1)+"条数据：：：");
                str = JaxbUtil.convertToXml(response);
                updateZphoXmlMap.put(StringUtils.nullToString(key),str);
                i++;
            }

        }
        return updateZphoXmlMap;
    }


    /**
     * TODO: 注意其中一些字段该放入值
     * 1. 注意productid 和product name一定要存在与系统中
     * 2. 注意PRODUCTSID是系统生成的，不需要设入
     * 3. 注意User的设入
     * @param idAccountsMap
     * @return
     */
    private Accounts getDBObj(Map<String, Object> idAccountsMap,int i) throws ParseException {
        logger.debug("# AccountHandler [getDBObj]...");
        Accounts accounts = new Accounts();
        //for Tree account
//        User user = new User("85333000000071039","qq");
        //for Matrix Account
        User user = new User("80487000000076001","marketing");
//        accounts.setSMOWNERID("85333000000071039");
//        accounts.setAcctOwner("qq");
        accounts.setUser(user);
        accounts.setErpID(i+"");
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
    private Accounts getAcctDBObj2(Map<String,Object> idAccountsMap) throws ParseException {
        logger.debug("# AccountHandler [getAcctDBObj2]...");
        Accounts accounts = new Accounts();
        //for Tree account
//        User user = new User("85333000000071039","qq");
        //for Matrix Account
//        User user = new User("80487000000076001","marketing");
//        User user = new User("80487000000080005","Gary Tang");
        User user = new User();
        user.setUserName("Gary Tang");
        user.setUserID("80487000000080005");
        CommonUtils.fetchDevUser(false);
        accounts.setUser(user);
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

}
