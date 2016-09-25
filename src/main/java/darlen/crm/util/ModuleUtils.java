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
    public static  String retriveZohoRecords(String url,String newFormat,String selectedColumns,String sortOrderString,String sortColumnString) throws Exception {
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


}
