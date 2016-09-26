/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ModuleUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

import darlen.crm.model.common.Module;
import darlen.crm.model.result.User;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * darlen.crm.util
 * Description：ZOHO_CRM
 * Created on  2016/09/25 13：13
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        13：13   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class ModuleUtils {
    private static Logger logger= Logger.getLogger(ModuleUtils.class);

    /**
     * 获取ZOHO某个module的XML数据
     * @param url
     * @param newFormat
     * @return
     * @throws Exception
     */
    public static  String retrieveZohoRecords(String url, String newFormat, String selectedColumns, String sortOrderString, String sortColumnString) throws Exception {
        String retZohoStr = "";
        try {
            //String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,url);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,Module.AUTHTOKEN);
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, Module.SCOPE);
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, newFormat);
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
