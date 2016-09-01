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
        Map dbSoMap = getDBFieldNameValueMap("darlen.crm.model.result.SO", so);
        Map crmSOMap = getCRMFieldMap(CommonUtils.readProperties("/mapping/dbRdSOFieldMapping.properties"), dbSoMap);
        //assembelXML(getAllFLsByCRMMap(crmSOMap));
       // List<FL> fls = getCommonFLsByCRMMap(crmSOMap);
       // Map pds = getProdDetlFLsByCRMMap(crmSOMap);

    }

    /**
     * Common FLs
     * @param crmSOMap
     * @return
     */
    private static List<FL> getCommonFLsByCRMMap(Map<String,String> crmSOMap) {
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry entry : crmSOMap.entrySet()){
            String key = String.valueOf(entry.getKey());
            if(!"pds".equals(key)){
                String value = String.valueOf(entry.getValue());
                FL fl = new FL();
                fl.setFieldName(key);
                fl.setFieldValue(value);
                fls.add(fl);
            }
        }
        System.out.println("Common FLs:::"+fls);
        return fls;
    }

    /**
     * 如果字符在product detail下就是product
     * @param crmSOMap
     * @return
     */
    private static Map getProdDetlFLsByCRMMap(Map crmSOMap) {
        Map allPdsMap = new HashMap();
        Map<Integer,Map> map  = (Map)crmSOMap.get("pds");
        for(Map.Entry<Integer,Map> entry : map.entrySet()){
            int no = entry.getKey();
            Map<String,String> fieldNameMap = entry.getValue();
            List<FL> tmpfls = new ArrayList<FL>();
            for(Map.Entry<String,String> entry1 : fieldNameMap.entrySet()){
                FL tmpfl = new FL();
                tmpfl.setFieldName(entry1.getKey());
                tmpfl.setFieldValue(entry1.getValue());
                tmpfls.add(tmpfl);
            }
            allPdsMap.put(no,tmpfls);
        }

        System.out.println("Product Detail FLs:::"+allPdsMap);
        return allPdsMap;
    }

    /**
     * 得到需要传输到CRM中的所有的字段组合
     * 其中注意Product detail的处理方法
     * @param properties
     * @param dbSoMap
     * @return
     */
    private static Map getCRMFieldMap(Properties properties, Map<String,Object> dbSoMap) {
        Map<Object,Object> crmFieldMap = new HashMap<Object, Object>();

        for(Map.Entry entry :dbSoMap.entrySet()){
            String key = entry.getKey().toString();
            if("pds".equalsIgnoreCase(key)){
                /**
                 * 1. 取出key为pds的Map<no,Map<fieldName,FieldValue> --> pdsMap,并新建一个newPdsMap<<no,Map<fieldName,FieldValue>>,
                 * 2. 遍历这个map(pdsMap)，取出每个Map<FieldName,FieldValue> -->pdMap ,判断fieldName是否在properties存在
                 * 3. 如果存在，则取perperties中的value和fieldvalue，放入一个新的newPdMap<properties.get(fieldName),fieldVaue>
                 * 4. 如果不存在，忽略这个字段
                 * 5. 将新的newPdMap放入newPdsMap，
                 * 6. 最终newPdsMap放入crmFieldMap
                 */
                Map<Integer,Map> pdsMap = (Map)dbSoMap.get("pds");
                Map newPdsMap = new HashMap();
                for (Map.Entry<Integer,Map> pdsEntry : pdsMap.entrySet()) {//多少条prduct detail;记录
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
                crmFieldMap.put("pds",newPdsMap);
            }else{
                /**
                 * for common field : 跟properties做对比，
                 * 如果存在，则取properties的value作为key，以前的value还是作为value
                 * 如果不存在，则忽略这个field，也就是说不会作为最后发送到CRM中的字段
                 */
                if(properties.containsKey(key)){//取出properyies中没有的db key,放入一个新的map中
                      crmFieldMap.put(properties.get(key),entry.getValue());
                }
            }
        }

       /* for(Map.Entry entry : properties.entrySet()){
            String entryKey = (String)entry.getKey();
            if(entryKey.equalsIgnoreCase("pds")){//for common fields
                Map<Integer,Map> pdsMap = (Map)dbSoMap.get("pds");
                Map newPdsMap = new HashMap();
                for (Map.Entry<Integer,Map> entry1 : pdsMap.entrySet()) {//多少条prduct detail;记录
                    int no = entry1.getKey();
                    Map<String,String> flMap = entry1.getValue();
                    for(Map.Entry<String,String> entry2 : flMap.entrySet()){
                        if(flMap.containsKey(entry.getKey())){
                            flMap.put((String)entry.getValue(),entry2.getValue());
                        }
                    }
                    newPdsMap.put(no,flMap);
                }
                crmFieldMap.put("pds",newPdsMap);
            }else{//common fields
                if(dbSoMap.containsKey(entry.getKey())){
                    crmFieldMap.put(entry.getValue(),dbSoMap.get(entry.getKey()));
                }
            }
        }*/
        CommonUtils.printMap(crmFieldMap,"CRM Field Map:");
        return crmFieldMap;
    }

    private static Map getDBFieldNameValueMap(String className, SO so) throws Exception{
        Map<Object,Object> map = new HashMap();
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
            }else if("pds".equals(field.getName())){//handle the product detail
                map.put("pds", getDBProdDetlFieldMap(field,so));
            }
        }
        CommonUtils.printMap(map,"打印DBfield的dbSoMap:");
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
    private static Map<Object,Object> getDBProdDetlFieldMap(Field field,SO so) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        List<ProductDetails> pds = (List<ProductDetails>)field.get(so);
        Map<Object,Object> map = new HashMap<Object, Object>();
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
            map.put(i,tmpMap);
        }
        CommonUtils.printMap(map,"打印 DB Product Detail Field Map:");
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
        pd.setPd_Discount("10%");

        ProductDetails pd1 = new ProductDetails();
        pd1.setPd_Product_Id("2");
        pd1.setPd_Product_Name("Darlen's Product2");
        pd1.setPd_Discount("20%");
        //TODO 处理多product detail 问题
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        pds.add(pd);
        pds.add(pd1);
        so.setPds(pds);
        return so;
    }
}
