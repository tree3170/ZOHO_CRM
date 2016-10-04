/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Module.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.jaxb.common.FL;
import darlen.crm.jaxb.common.ProdDetails;
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.jaxb.common.Product;
import darlen.crm.model.result.ProductDetails;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.ModuleNameKeys;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * darlen.crm.model.common
 * Description：ZOHO_CRM
 * Created on  2016/09/25 10：37
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        10：37   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public abstract  class AbstractModule  implements IModule{
    private static Logger logger =  Logger.getLogger(AbstractModule.class);
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
    public static Map<String,String> zohoPropsMap = new HashMap<String,String>();


    public  void getProperties(){
        initZohoProps();
    }


    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
    private synchronized static void initZohoProps() {
        try {
            Properties prop = new Properties();
            prop.load(AbstractModule.class.getResourceAsStream("/secure/zoho.properties"));
            AUTHTOKEN = prop.getProperty(Constants.ZOHO_PROPs_AUTHTOKEN_TREE);
            NEWFORMAT_1 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_1);
            NEWFORMAT_2 = prop.getProperty(Constants.ZOHO_PROPS_NEWFORMAT_2);
            SCOPE = prop.getProperty(Constants.HTTP_POST_PARAM_SCOPE);
            MODULES = prop.getProperty(Constants.ZOHO_PROPS_MODULES);
            //for url TODO  将来需要把properties中的字段全部放入到Cache或者静态变量中
            for(Map.Entry entry : prop.entrySet()){
                zohoPropsMap.put(StringUtils.nullToString(entry.getKey()),StringUtils.nullToString(entry.getValue()));
            }
        } catch(IOException e) {
            logger.error("读取zoho properties出现错误",e);
//            System.err.println("读取zoho properties出现错误"+e.getMessage());
        }
        logger.debug("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
//        System.err.println("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
    }


    /**
     * 1.1 获取ZOHO某个module的XML数据
     * 暂定每次遍历100条
     * @param moduleName
     * @return
     * @throws Exception
     */
    public  String retrieveZohoRecords(String moduleName,int fromIndex,int toIndex) throws Exception {//,String moduleName
        //TODO remove url and selectedColums and newFormat三个参数
        String retZohoStr = "";
        String url = "";
        String selectedColumns = "";
        if(ModuleNameKeys.Accounts.toString().equalsIgnoreCase(moduleName)){
            url = zohoPropsMap.get(Constants.FETCH_ACCOUTNS_URL);
            selectedColumns = "Accounts(Modified Time,ACCOUNTID,Account Name,ERP ID,LatestEditTime)";
        }
        if(ModuleNameKeys.Products.toString().equalsIgnoreCase(moduleName)){
            url = zohoPropsMap.get(Constants.FETCH_PRODUCTS_URL);
            selectedColumns = "Products(Modified Time,PRODUCTID,Product Name,ERP ID,LatestEditTime)";
        }
        if(ModuleNameKeys.SalesOrders.toString().equalsIgnoreCase(moduleName)){
            url = zohoPropsMap.get(Constants.FETCH_SO_URL);
            selectedColumns = "SalesOrders(Modified Time,SALESORDERID,Subject,ERP ID,LatestEditTime)";
        }
        if(ModuleNameKeys.Invoices.toString().equalsIgnoreCase(moduleName)){
            url = zohoPropsMap.get(Constants.FETCH_INVOICES_URL);
            selectedColumns = "Invoices(Modified Time,INVOICEID,Subject,ERP ID,LatestEditTime)";
        }
        System.out.println("从ZOHO获取回来的所有记录的XML:::moduleName = "+moduleName+", url ="+url);
        try {
            String sortOrderString = "desc";
            String sortColumnString = "Modified Time";
            //String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,url);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN, AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_FROM_INDEX, fromIndex+"");
            postParams.put(Constants.HTTP_POST_TO_INDEX, toIndex+"");
            //default set the newFormat as 2，因为有可能需要的字段为空
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, zohoPropsMap.get(Constants.ZOHO_PROPS_NEWFORMAT_2));
            if(!StringUtils.isEmptyString(selectedColumns)){
                postParams.put(Constants.HTTP_POST_PARAM_SELECTCOLS,selectedColumns);
            }
            if(!StringUtils.isEmptyString(sortOrderString)){
                postParams.put(Constants.HTTP_POST_PARAM_SORTORDER,sortOrderString);
            }
            if(!StringUtils.isEmptyString(sortColumnString)){
                postParams.put(Constants.HTTP_POST_PARAM_SORTORDER_COL,sortColumnString);
            }
            retZohoStr =  CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("执行搜索Module操作出现错误",e);
            throw e;
        }
        return retZohoStr;
    }



    /**
     * 关于Product Detail，把product detail所在的FL标签转化为pds标签
     * 使用范围：SO/Invoice/Quota等含有product Detail的Module
     * @param str
     * @return
     */
    public static String convertFLToPdsXmlTag(String str){
        //1.由 Product Details之前的FL转换为pds:如何找到前后匹配的FL
        if(str.indexOf("Product Details") != -1){
            String prexPds = str.replace("<FL val=\"Product Details\">","<pds val=\"Product Details\">");
            //        System.out.println("prex::"+prexPds );
            //2. 获取最后一个</product>前面的字符串
            String prefProduct = prexPds.substring(0,prexPds.lastIndexOf("</product>")+10);
            String suffixProduct= prexPds.substring(prexPds.lastIndexOf("</product>")+10);
            //3.取出suffixProduct字符串的第一个为</FL>之后的字符串
            suffixProduct = suffixProduct.substring(suffixProduct.indexOf("</FL>")+5);
            //4. 组装finalStr=prefProduct+"</pds>" + suffixProduct
            String finalStr = prefProduct+"</pds>" + suffixProduct;
            System.out.println("suffix::"+finalStr );
            str = finalStr;
        }
        return str;
    }

    /**
     * 1.3
     * 获取Zoho组件的集合，其中包含三个对象，分别为 erpZohoIDMap，erpZohoIDTimeMap，delZohoIDList（zoho ID list）
     * 1. erpZohoIDMap<erpID,zohoID> = zohoListObj.get(0)
     * 2. erpIDTimeMap<erpID,lastEditTime> = zohoListObj.get(1)
     * 3. delZohoIDList = zohoListObj.get(2) ：zoho ID list-->ERP ID 为空时的 加入删除列表
     * @param rows
     * @param zohoIDName  -->ZOHO
     * @param erpIDName ERP ID-->  DB
     * @return  zohoCompList
     */
    public List buildZohoComponentList(List<ProdRow> rows, String zohoIDName, String erpIDName,String moduleName) throws Exception {
        logger.debug("entering the buildZohoComponentList...zohoIDName="+zohoIDName+", erpIDName = "+erpIDName+", moduleName="+moduleName);
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

        CommonUtils.printMap(erpZohoIDMap,"ERPID 和 ZOHOID Map");
        CommonUtils.printMap(erpIDTimeMap,"ERPID 和 LastEditTime Map");
        CommonUtils.printList(delZohoIDList,"Remove ZOHO ID list");

        //当Module是Product或者是Account的时候，需要把所有的ERPID 和ZOHOID分别写入不同文件中，为了以后在数据库读取Accounts和Product使用
        if(ModuleNameKeys.Accounts.toString().equals(moduleName)  || ModuleNameKeys.Products.toString().equals(moduleName)){
            ConfigManager.writeVal2Props(erpZohoIDMap,ModuleNameKeys.Accounts.toString().
                    equals(moduleName) ? Constants.PROPS_ACCT_FILE : Constants.PROPS_PROD_FILE);
        }
        return zohoCompList;
    }



    /**
     * 3.3 组装发送到ZOHO的三大对象
     * 由获得的ZOHO所有对象集合和从DB获取的对象集合(dbIDModuleObjMap)，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
     * 1.updateModuleObjMap<>：如果Zoho ID存在于DB对象集合中，则判断 lastEditTime是否被修改，如果修改了，则直接组装到updateAccountMap中
     * 2.delZohoIDList：如果ZOHO ID不存在于dbIDModuleObjMap中，则直接把DB ID添加到delZohoIDList
     * 3.addModuleObjMap：如果dbIDModuleObjMap中的id不存在于erpIDTimeMap中，则添加ZOHOID和Module对象到addModuleObjMap
     * @param erpZohoIDMap： ERP ID 和 ZOHO ID的集合
     * @param erpIDTimeMap： ERP ID 和LastEditTime的集合
     * @param delZohoIDList： ZOHO ID的集合
     * @param dbIDModuleObjMap： DB ID 和Module对象的集合
     * @return
     */
    public List build2Zoho3PartObj(Map<String,String> erpZohoIDMap,Map<String,String> erpIDTimeMap,
                                    List<String> delZohoIDList,Map<String,Object> dbIDModuleObjMap){
        Map<String,Object> addModuleObjMap = new HashMap<String, Object>();
        Map<String,Object> updateModuleObjMap = new HashMap<String, Object>();

        for(Map.Entry<String,String> entry : erpIDTimeMap.entrySet()){
            String erpID = entry.getKey();
            String zohoID = erpZohoIDMap.get(erpID);
            if(dbIDModuleObjMap.containsKey(erpID)){//update
                //TODO 添加lastestEditTime是否被修改
                updateModuleObjMap.put(zohoID, dbIDModuleObjMap.get(erpID));
            }else{ //delete
                if(!delZohoIDList.contains(zohoID)){
                    delZohoIDList.add(zohoID);
                }
            }
        }

        for(Map.Entry<String,Object> entry : dbIDModuleObjMap.entrySet()){
            String dbID = entry.getKey();
            if(!erpIDTimeMap.containsKey(dbID)){//add
//                key可以随意
                addModuleObjMap.put(dbID, entry.getValue());
            }
        }

        List sendToZohoAcctList = new ArrayList();
        CommonUtils.printMap(addModuleObjMap, "addMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(addModuleObjMap);
        CommonUtils.printMap(updateModuleObjMap,"updateMap组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(updateModuleObjMap);
        CommonUtils.printList(delZohoIDList, "delZOHOSOIDList组装到ZOHO的对象的集合：：：\n");
        sendToZohoAcctList.add(delZohoIDList);

        return sendToZohoAcctList;
    }

    /**
     * 获取Add的row的Map： 每100条rows放入Map，最后不满100条rows的放到最后的Map中
     * @param accountMap
     * @return
     * @throws Exception
     */
    public Map<Integer,List<ProdRow>> getAddRowsMap(Map<String, Object> accountMap,String className,Properties fieldMappingProps ) throws Exception {
        List<ProdRow> rows = new ArrayList<ProdRow>();
        Map<Integer,List<ProdRow>>  rowsMap = new HashMap<Integer, List<ProdRow>>();
        int i = 1;
        for(Map.Entry<String,Object> entry : accountMap.entrySet()){
            String key = entry.getKey();
            logger.debug("getAddRowsMap...遍历第"+i+"条记录，DB ID为"+key);
            ProdRow row = new ProdRow();
//            得到所有的FL集合，包括common和Product Detail
            List fls = getAllFLList(entry.getValue(),className,fieldMappingProps);
            row.setNo(i);
            //设置Common FL list
            row.setFls((List<FL>)fls.get(0));
            //设置Product Detail FL list
            ProdDetails prodDetails = getProdDetails(fls);
            if(null != prodDetails) row.setPds(prodDetails);

            rows.add(row);
            //当row的size达到了100，那么需要放入
            if(i == Constants.MAX_ADD_SIZE){
                logger.debug("Add Rows的size达到了100，需要放到Map中，然后重新计算rows的条数...");
                rowsMap.put(rowsMap.size(),rows);
                rows = new ArrayList<ProdRow>();
                i = 1;
            }else{
                i++;
            }

        }
        //最后不满100条的，放入最后的Map中,如果刚好则不添加
        if(rows.size()>0) rowsMap.put(rowsMap.size(),rows);
        return rowsMap;
    }

    /**
     *
     * @param fls
     * @return
     */
    private ProdDetails getProdDetails(List fls){
        //设置Product Detail FL list
        List<Product> products = (List<Product>)fls.get(1);
        if(products != null && products.size() > 0){
            ProdDetails prodDetails = new ProdDetails();
            prodDetails.setVal("Product Details");
            products = (List<Product>)fls.get(1);
            prodDetails.setProducts(products);
            return prodDetails;
        }
        return null;
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
    public List<ProdRow> getUpdRowByMap(Map<String, Object> accountMap,String className,Properties fieldMappingProps) throws Exception {
        List<ProdRow> rows = new ArrayList<ProdRow>();

        int i = 1;
        for(Map.Entry<String, Object> entry : accountMap.entrySet()){
            ProdRow row = new ProdRow();
            String key = entry.getKey();
//            Accounts accounts  = entry.getValue();
            List fls = getAllFLList(entry.getValue(),className,fieldMappingProps);
            row.setNo(i);
            //1. for common FL 集合
            row.setFls((List<FL>)fls.get(0));

            //2. for Product Detail中的Product的FL集合
            //设置Product Detail FL list
            ProdDetails prodDetails = getProdDetails(fls);
            if(null != prodDetails) row.setPds(prodDetails);

            rows.add(row);
            i++;
        }
        return rows;
    }

    /**
     * 获取所有FL的集合，返回的List中存在2大对象：commonFls，products
     *  1. commonFls --> Common FL 集合
     *
     * @param accounts
     * @return
     * @throws Exception
     */
    public List getAllFLList(Object accounts,String className,Properties fieldMappingProps) throws Exception {
        //通过反射拿到Products对应的所有ERP字段"darlen.crm.model.result.Accounts"
        Map<String,Object> dbFieldNameValueMap = getDBFieldNameValueMap(className, accounts);
        // 通过properties过滤不包含在里面的所有需要发送的ZOHO字段
        List zohoFieldList = getZOHOFLsByProps(fieldMappingProps, dbFieldNameValueMap);
        return zohoFieldList;
    }
    /**
     * 获取DB某个Accounts所有有效的fieldname 和value的Map
     * @param className 包名+类名
     * @return
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<String,Object> getDBFieldNameValueMap(String className,Object dbFields) throws Exception {
//        logger.debug("entering the getDBFieldNameValueMap...className = "+className+", dbFields = "+dbFields);
        Map<String,Object> map = new HashMap();
        Class clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        for(Field field : fields){
            String fieldName = field.getName();
            field.setAccessible(true) ;
            if(null != dbFields){
                if (field.getGenericType().toString().equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                    String fieldValue =StringUtils.nullToString(field.get(dbFields));
                    //fieldValue = StringUtils.isEmptyString(fieldValue)? "":fieldValue;
    //                if(!StringUtils.isEmptyString(fieldValue)){ //就算DB某些值为空，也要组装，因为需要更新ZOHO上的数据
                    if(!("userID".equals(fieldName) && StringUtils.isEmptyString(fieldValue)))//排除userID为空的情况，因为只有userName的情况下也是额可以做的
                         map.put(fieldName,fieldValue);
    //                }
                }else if(field.getGenericType().toString().equals("class darlen.crm.model.result.User")){//处理User对象:拥有者
                    map.putAll(getDBFieldNameValueMap("darlen.crm.model.result.User",field.get(dbFields)));
                }else if("pds".equals(field.getName())){//handle the product detail
                    map.put("pds", getDBProdDetlFieldMap(field,dbFields));
                }
            }
        }
//        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }
    /**
     * 根据properties和有效的dbFieldNameValueMap确定返回给zoho的fieldname（获取properties中的key对应的value）和fieldvalue
     * 注意：此方法是用于含有Product Detail的情况
     * 默认添加API Import 标志
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    public List getZOHOFLsByProps(Properties properties, Map dbFieldNameValueMap) {
//        logger.debug("entering getZOHOFLsByProps...");
        List allFls = new ArrayList();
        List<Product> products = new ArrayList<Product>();
        List<FL> commonFls = new ArrayList<FL>();

        //默认添加是否是API导入的字段
        FL isAPIFL = new FL();
        isAPIFL.setFieldName("API Import");
        isAPIFL.setFieldValue("true");
        commonFls.add(isAPIFL);

        for(Map.Entry entry : properties.entrySet()){
            if(dbFieldNameValueMap.containsKey(entry.getKey())){
                String dbFieldName = (String)entry.getKey();
                // for product detail case
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
                                fl.setFieldValue(StringUtils.nullToString(pdEnry.getValue()));
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
                    String dbFieldValue = StringUtils.nullToString(dbFieldNameValueMap.get(dbFieldName));
                    String zohoFieldName = StringUtils.nullToString(entry.getValue());
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
     * 根据properties和有效的dbFieldNameValueMap确定返回给zoho的fieldname（获取properties中的key对应的value）和fieldvalue
     * @param properties
     * @param dbFieldNameValueMap
     * @return
     */
    public List<FL> getZOHOFLsByProps_common(Properties properties, Map dbFieldNameValueMap) {
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
     *  取出所有的db中的product details，然后每个遍历，有值的就放入tmpMap中，最后组装成一个Map ，
     *  key是product no，value是tmpMap(fieldName,fieldValue)
     * @param field
     * @param moduleObj: like Invoices,SO，Quotas
     * @return  key : product detail no --> value : map(key:fieldName-->value:fieldValue)
     * @throws IllegalAccessException
     */
    private static Map<String,Object> getDBProdDetlFieldMap(Field field,Object moduleObj) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        List<ProductDetails> pds = (List<ProductDetails>)field.get(moduleObj);
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
                    String fieldValue =StringUtils.nullToString(f.get(pd));
//                    if(!StringUtils.isEmptyString(fieldValue)){
                        tmpMap.put(fieldName,fieldValue);
//                    }
                }
            }
            map.put((i+1)+"",tmpMap);
        }
        CommonUtils.printMap(map, "打印 DB Product Detail Field Map:");
        return map;
    }

    private String getModuleUrl(String moduleName,int curdKey){
        String moduleUrl = "" ;
        if(Constants.ZOHO_CRUD_ADD == curdKey){
            if(ModuleNameKeys.Accounts.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.INSERT_ACCOUTNS_URL);
            }
            if(ModuleNameKeys.Products.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.INSERT_PRODUCTS_URL);
            }
            if(ModuleNameKeys.Quotes.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.INSERT_QUOTES_URL);
            }
            if(ModuleNameKeys.SalesOrders.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.INSERT_SO_URL);
            }
            if(ModuleNameKeys.Invoices.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.INSERT_INVOICES_URL);
            }
        }else if(Constants.ZOHO_CRUD_UPDATE == curdKey){
            if(ModuleNameKeys.Accounts.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_ACCOUTNS_URL);
            }
            if(ModuleNameKeys.Products.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_PRODUCTS_URL);
            }
            if(ModuleNameKeys.Quotes.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_QUOTES_URL);
            }
            if(ModuleNameKeys.SalesOrders.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_SO_URL);
            }
            if(ModuleNameKeys.Invoices.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_INVOICES_URL);
            }
        }else if(Constants.ZOHO_CRUD_DELETE == curdKey){
            if(ModuleNameKeys.Accounts.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.DELETE_ACCOUTNS_URL);
            }
            if(ModuleNameKeys.Products.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.DELETE_PRODUCTS_URL);
            }
            if(ModuleNameKeys.Quotes.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.UPDATE_QUOTES_URL);
            }
            if(ModuleNameKeys.SalesOrders.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.DELETE_SO_URL);
            }
            if(ModuleNameKeys.Invoices.toString().equalsIgnoreCase(moduleName)){
                moduleUrl = zohoPropsMap.get(Constants.DELETE_INVOICES_URL);
            }
        }
        return moduleUrl;
    }

    public int addRecords(String moduleName,int curdKey,List zohoXMLList){
        int failNum  = 0;
        String moduleUrl  = getModuleUrl(moduleName,curdKey);

        logger.debug("#[addRecords], 从ZOHO获取回来的所有记录的XML:::moduleName = "+moduleName+", Operatiton ="+curdKey+", url ="+moduleUrl);
        List<String> addZohoXMLList = (List<String> ) zohoXMLList.get(0);
        for(int i = 0; i < addZohoXMLList.size(); i ++){
            logger.debug("添加【"+moduleName+"】第"+(i+1)+"条数据");
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,moduleUrl);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,addZohoXMLList.get(i));
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
            try {
                 CommonUtils.executePostMethod(postParams);
            } catch(Exception e) {
                logger.error("添加第"+(i+1)+"条数据失败"+"，执行addRecords【"+moduleName+"】操作出现错误," +
                        "HTTP_POST_PARAM_XMLDATA = "+ addZohoXMLList.get(i), e);
                failNum ++;
            }
        }
        return failNum;

    }
    public int updateRecords(String moduleName, int curdKey,List zohoXMLList){
        int failNum = 0;
        String moduleUrl  = getModuleUrl(moduleName, curdKey);;
        Map<String,String> updZohoXMLMap = (Map<String,String>) zohoXMLList.get(1);
        int i = 1 ;
        for(Map.Entry<String,String> zohoIDUpdXmlEntry : updZohoXMLMap.entrySet()){
            logger.debug("更新【"+moduleName+"】第" + (i) + "条数据，ZOHO ID为" + zohoIDUpdXmlEntry.getKey());//xml为："+zohoIDUpdXmlEntry.getValue()
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_ID,zohoIDUpdXmlEntry.getKey());
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,moduleUrl);
            postParams.put(Constants.HTTP_POST_PARAM_XMLDATA,zohoIDUpdXmlEntry.getValue());
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
            try {
                CommonUtils.executePostMethod(postParams);
            } catch(Exception e) {
                failNum ++;
                logger.error("更新第"+(i+1)+"条数据失败"+"，执行updateRecords【"+moduleName+"】操作出现错误," +
                        "HTTP_POST_PARAM_ID = "+zohoIDUpdXmlEntry.getKey()+" ," +
                        " HTTP_POST_PARAM_XMLDATA = "+zohoIDUpdXmlEntry.getValue(),e);
            }
            i++;
        }
        return failNum;

    }

    /**
     * 返回第一个是失败的次数，第二个是需要删除的ZOHO IDlist
     * 因为删除的顺序必需是倒序：Invoices/SO/Quotes/Products/Accounts
     * @return
     * @throws Exception
     */
    public int delRecords(String moduleName, int curdKey,List zohoXMLList){
        //List result = new ArrayList();
        int failNum = 0;
        String moduleUrl  = getModuleUrl(moduleName, curdKey);
        List deleteZOHOIDsList = (List)zohoXMLList.get(2);
        for(int i = 0; i < deleteZOHOIDsList.size(); i++){
            String id = StringUtils.nullToString(deleteZOHOIDsList.get(i));
            logger.debug("删除【"+moduleName+"】第"+(i+1)+"条数据");
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,moduleUrl);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, NEWFORMAT_1);
            postParams.put(Constants.HTTP_POST_PARAM_ID,id );
            try {
                 CommonUtils.executePostMethod(postParams);
            } catch(Exception e) {
                logger.error("删除第"+(i+1)+"条数据失败"+"，执行delRecords【"+moduleName+"】操作出现错误," +
                        "HTTP_POST_PARAM_ID = "+id,e);
                failNum ++;
            }
        }
        //result.add(0,failNum);
        //result.add(1,deleteZOHOIDsList);
        //return result;
        return failNum;
    }


}
