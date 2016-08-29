/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   SORdsMockTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.leads.FL;
import darlen.crm.model.result.ProductDetails;
import darlen.crm.model.result.SO;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * darlen.crm.record
 * Description：ZOHO_CRM ，这是一个模拟从数据库去数据并且判断更新删除还是添加操作，最后转化为xml发送到ZOHO处理的过程
 *  Q: db 数据如何和java object的mapping问题?  UpdLeadsRdsTest.java
 *  A: 比如说更新<strong>(需要先删除再插入)</strong>某一条Leads数据
 * 1. 先列出一个db的javaObject（DBLeads.java）-->Leads.java
 * 2. 生成CRM中Record的Object（RdLeads.java-->Response.java），仅仅只需要获取必要的字段，如果说id，name，modify time,created by
 * 3. 准备一个properties(dbRdLeadsFieldMapping.properties) （暂时用2，3个数据）
 * 4. 利用反射原理, 遍历 DBLeads对象，取出每个fieldname和fieldVal , 并放入一个LeadsDBMap中（如果没值或者为空，则不变：：注意）
 * 5. 遍历LeadsDBMap，将key value分别设入FL（field name,field value），这里需要从properties中获取有效的fieldname，从而得到List<FL> fls，并组成response对象
 * 6. 利用jaxb将response对象转换为xml，post到ZOHO，从而得到最终结果
 * Created on  2016/08/29 08：38
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：38   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class SORdsMockTest {
    public static void main(String[] args) throws Exception{
        SO so = getDBSO();
        Map dbSoMap = getFieldNameValueMap("darlen.crm.model.result.SO",so);
        Map crmSOMap = getCRMFieldMap(CommonUtils.readProperties("/mapping/dbRdSOFieldMapping.properties"),dbSoMap);
        //assembelXML(getAllFLsByCRMMap(crmSOMap));
    }

    private static List<FL> getCommonFLsByCRMMap(Map<String,String> crmSOMap) {
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry<String,String> entry : crmSOMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            FL fl = new FL();
            fl.setFieldName(key);
            fl.setFieldValue(value);
            fls.add(fl);
        }
        return fls;
    }

    /**
     * 如果字符在product detail下就是product
     * @param crmSOMap
     * @return
     */
    private static List<FL> getProdDetlFLsByCRMMap(Map<String,String> crmSOMap) {
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry<String,String> entry : crmSOMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            FL fl = new FL();
            fl.setFieldName(key);
            fl.setFieldValue(value);
            fls.add(fl);
        }
        return fls;
    }

    private static Map getCRMFieldMap(Properties properties, Map<String,String> dbSoMap) {
        Map<String,String> crmFieldMap = new HashMap<String, String>();

        for(Map.Entry entry : properties.entrySet()){
            if(dbSoMap.containsKey(entry.getKey())){
                crmFieldMap.put((String)entry.getValue(),dbSoMap.get(entry.getKey()));
            }
        }
        CommonUtils.printMap(crmFieldMap,"CRM Field Map:");
        return crmFieldMap;
    }

    private static Map getFieldNameValueMap(String className, SO so) throws Exception{
        Map<String,String> map = new HashMap();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        for(Field field : fields){
            String fieldName = field.getName();
//            System.out.println(field.getGenericType());//打印该类的所有属性类型
//            System.out.println(field);
//            //System.out.println(field.equals(property1));
//            //私有变量必须先设置Accessible为true
//            field.setAccessible(true);
//            //获取用get类方法。
//            System.out.println(field.get(stu));
            field.setAccessible(true) ;
//            Method method = dbFields.getClass().getMethod("get"+getMethodName(fieldName));
//            System.out.println(method.invoke(dbFields));
            // 如果类型是String
            if (field.getGenericType().toString().equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                String fieldValue =String.valueOf(field.get(so));
                if(!StringUtils.isEmptyString(fieldValue)){
                    map.put(fieldName,fieldValue);
                }
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }

    private static SO getDBSO() {
        SO so = new SO();
        so.setSubject("Darlen's SO");
        so.setCarrier("平邮");
        so.setStatus("创建");
        so.setDiscount("10%");
        ProductDetails pd = new ProductDetails();
        pd.setPd_Product_Id("1");
        pd.setPd_Product_Name("Darlen's Product");
        pd.setPd_Discount("20%");
        //TODO 处理多product detail 问题
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        pds.add(pd);
        so.setPds(pds);
        return so;
    }
}
