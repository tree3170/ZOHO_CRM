/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   UpdLeadsRdsMockTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.record;

import darlen.crm.jaxb_xml_object.leads.FL;
import darlen.crm.jaxb_xml_object.leads.Response;
import darlen.crm.jaxb_xml_object.leads.Result;
import darlen.crm.jaxb_xml_object.leads.Row;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.model.result.Leads;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
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
 * Created on  2016/08/28 15：51
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        15：51   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class LeadsRdsMockTest {

    public static void main(String[] args) throws Exception {
//        CommonUtils.readProperties("/mapping/dbRdLeadsFieldMapping.properties");
//        getLeadsById("85333000000072001");
        Leads leads = getDBFields();
        Map dbMap = getFieldNameValueMap("darlen.crm.model.result.Leads",leads);
        Map crmMap = getCRMFieldMap(CommonUtils.readProperties("/mapping/dbRdLeadsFieldMapping.properties"),dbMap);
        assembelXML(getAllFLsByCRMMap(crmMap));

    }



    //1. 准备properties，暂定更新的列为company，first name ， last name
    // --》CommonUtils.readProperties("/mapping/dbRdLeadsFieldMapping.properties");
    //2. get CRM object by id
    /**
     * 根据id获取Leads数据 id: qq:85333000000071039, tree3170:85333000000071001
     * @param id
     */
    public static void getLeadsById(String id){
        try
        {
            //----------------------------Fetch Auth Token ----------------------
            String authtoken = "f19d6f4ad3d2a491ef52f83a7a68bf04";//If you don't have a authtoken please refer this wiki https://zohocrmapi.wiki.zoho.com/using-authtoken.html
            String scope = "crmapi";

            String newFormat = "1";
            String selectColumns ="Leads(Lead Owner,First Name,Last Name,Email,Company)";

            String targetURL = "https://crm.zoho.com.cn/crm/private/xml/Leads/getRecordById";
            String paramname = "content";

            Map<String,String> map = new HashMap();
            map.put(Constants.HTTP_POST_PARAM_TARGETURL,targetURL);
            map.put(Constants.HTTP_POST_PARAM_AUTHTOKEN,authtoken);
            map.put(Constants.HTTP_POST_PARAM_SCOPE,scope);
            map.put(Constants.HTTP_POST_PARAM_NEW_FORMAT,newFormat);
            //map.put(Constants.HTTP_POST_PARAM_XMLDATA,"");
            map.put(Constants.HTTP_POST_PARAM_ID,id);
            map.put("selectColumns",selectColumns);

            CommonUtils.executePostMethod(map);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //3.get db fields
    private static Leads getDBFields(){
        Leads dbLeads = new Leads();
//        dbLeads.setLEADID("123");
        dbLeads.setCompany("Darlen'Company");
        dbLeads.setFirst_Name("Darlen");
        dbLeads.setLast_Name("Liu");

        return dbLeads;
    }


    //3. 利用发射得到db对应的java object中的fieldname和fieldvalue的map
    /**
     * 仅仅只是拿到db的fieldname 和db 的value
     * @param className 包名+类名
     * @return
     * @throws ClassNotFoundException
     * refer:http://blog.csdn.net/sd4000784/article/details/7448221
     */
    public static Map<Object,Object> getFieldNameValueMap(String className,Leads dbFields) throws Exception {
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
                String fieldValue =String.valueOf(field.get(dbFields));
                if(!StringUtils.isEmptyString(fieldValue)){
                    map.put(fieldName,fieldValue);
                }
            }
        }
        CommonUtils.printMap(map,"打印DBfield的map");
        return map;
    }
    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    //4.遍历Map和properties，如果key匹配，则拿proerties的value设入到Map的key中，组成有效的Map<CRM FieldName,CRM Field Value>设值到list<FL>
    public static Map<Object,Object> getCRMFieldMap(Properties props,Map<String,Object> dbFieldMap){
        Map<Object,Object> crmFieldMap = new HashMap<Object, Object>();

        for(Map.Entry entry : props.entrySet()){
            if(dbFieldMap.containsKey(entry.getKey())){
                crmFieldMap.put((String)entry.getValue(),dbFieldMap.get(entry.getKey()));
            }
        }
        CommonUtils.printMap(crmFieldMap,"CRM Field Map:");
        return crmFieldMap;
    }

    //4.遍历crm Map,取出key和value并设值到FL中的fieldname和field value中，最后组成list<FL>
    public static List<FL> getAllFLsByCRMMap(Map<String,String> crmMap){
        List<FL> fls = new ArrayList<FL>();

        for(Map.Entry<String,String> entry : crmMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            FL fl = new FL();
            fl.setFieldName(key);
            fl.setFieldValue(value);
            fls.add(fl);
        }
        return fls;
    }

    //5.解析并组装成有效的xml，并发送到ZOHO
    public static String assembelXML(List<FL> fls){
        Response response = new Response();
        Result result = new Result();
        darlen.crm.jaxb_xml_object.leads.Leads leads = new darlen.crm.jaxb_xml_object.leads.Leads();
        List<Row> rows = new ArrayList<Row>();
        //TODO ：： 如果有多条row数据该如何处理
        Row row = new Row();
        row.setNo(1);
        row.setFls(fls);
        rows.add(row);
        leads.setRows(rows);
        result.setLeads(leads);
        response.setResult(result);

        String str = JaxbUtil.convertToXml(response);
        System.out.println(str);
       // System.out.println("String to Object::: "+JaxbUtil.converyToJavaBean(str,Response.class));
        return str;
    }


}
