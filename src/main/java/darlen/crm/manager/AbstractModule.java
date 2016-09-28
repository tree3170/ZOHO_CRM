/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Module.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.model.result.User;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.ModuleNameKeys;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
                zohoPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        } catch(IOException e) {
            logger.error("读取zoho properties出现错误",e);
//            System.err.println("读取zoho properties出现错误"+e.getMessage());
        }
        logger.debug("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
//        System.err.println("[readProperties], AUTHTOKEN:::" + AUTHTOKEN + "; NEWFORMAT_1:::" + NEWFORMAT_1 + "; NEWFORMAT_2:::" + NEWFORMAT_2 + "; SCOPE:::" + SCOPE);
    }


    /**
     * 获取ZOHO某个module的XML数据
     * @param moduleName
     * @return
     * @throws Exception
     */
    public static  String retrieveZohoRecords(String moduleName) throws Exception {//,String moduleName
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
            //default set the newFormat as 2
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
            if (field.getGenericType().toString().equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                String fieldValue =String.valueOf(field.get(dbFields));
                fieldValue = StringUtils.isEmptyString(fieldValue)? "":fieldValue;
//                if(!StringUtils.isEmptyString(fieldValue)){
                map.put(fieldName,fieldValue);
//                }
            }else if(field.getGenericType().toString().equals("class darlen.crm.model.result.User")){//处理User对象:拥有者
                map.putAll(getDBFieldNameValueMap("darlen.crm.model.result.User",field.get(dbFields)));
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
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
     * 关联拥有者，仅用做测试
     * @param isDev
     * @return
     */
    public static User fetchDevUser(boolean isDev){
        return isDev ? new User("85333000000071039","qq"): new User("80487000000076001","marketing");
    }
}
