/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   TestFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import darlen.crm.model.Fields;
import darlen.crm.model.SectionFields;
import darlen.crm.model.LeadsFields;
import darlen.crm.model.fields.common.CommonSection;
import darlen.crm.util.CommonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java.darlen.crm
 * Description：ZOHO_CRM:这是一个测试Fields的一些方法
 * Created on  2016/08/16 08：23
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：23   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class TestFields {
    public static void main(String[] args) {
        TestFields tf = new TestFields();
       // tf.testJson2();;
//        tf.testJson(tf.getLeadsJsonString(tf.getFileNamePath("sampledata/fields", "testjson.json")));
//        tf.testJson(tf.getLeadsJsonString(tf.getFileNamePath("", "testjson.json")));
        List functions = new ArrayList();
        functions.add("leads");
        tf.getFunctionsFields(functions);

    }

    private Map<String,List<String>> getFunctionsFields(List<String> functions){
        // //1.for loop function to get the section fields array
        Map<String,List<String>>  funcFieldsMap = new HashMap();
        for(int i = 0; i < functions.size(); i++){
            String function = functions.get(i);
            //TODO：：：2. get current function jsonstr, 暂时hardcode
            String funcJsonString = CommonUtils.getJsonStringByPathAndName("", "testjson.json");
            //3.解析整个function的json字符串到JSONObject对象
            JSONObject wholeObj = JSON.parseObject(funcJsonString);
            //4.获取某个function部分的JSONObject对象
            JSONObject functionObj =  wholeObj.getJSONObject(function);
            //5.获取某个function下面的section对应的json到JSONObject对象
            JSONArray sectionObj=  functionObj.getJSONArray("section");
            //6.解析所有fields
            List<CommonSection> sectionFields  = JSON.parseArray(sectionObj.toJSONString(),CommonSection.class);
//            funcFieldsMap.put(function,sectionFields);

            //7.得到某个function下面所有的fields
            getAllFields(function,sectionFields,funcFieldsMap);
        }

        return funcFieldsMap;
    }

    /**
     * get all fields by function name
     * @param function
     * @param sectionFields
     * @return key : functionName  value : all fields in function
     */
    public Map<String,List<String>>  getAllFields(String function, List<CommonSection> sectionFields,Map<String,List<String>> funcFieldsMap){
        List<String> fields = new ArrayList<String>();
        for(int i = 0; i < sectionFields.size(); i++ ){
            CommonSection section = sectionFields.get(i);
            String sectionfieldName = section.getDv();
            //fields.add(sectionfieldName);
            System.out.println(function + "::: section["+i+"] label name ="+sectionfieldName);
            List<darlen.crm.model.fields.common.Fields> tmpFields = section.getFl();
            for(int j = 0; j < tmpFields.size(); j++){
                darlen.crm.model.fields.common.Fields tmpField =tmpFields.get(j);
                fields.add(tmpField.getDv());
                System.out.println(function + "::: field ="+tmpField.getDv());
            }
        }
        funcFieldsMap.put(function,fields);
        return funcFieldsMap;
    }

    public String testJson(String jsonString){

        System.out.println("testJson output json String:::"+jsonString);
        LeadsFields fields = JSON.parseObject(jsonString,LeadsFields.class);
        String dvName =  fields.getLeads().getSection().get(0).getFl().get(0).getDv();

        JSONObject wholeObj = JSON.parseObject(jsonString);
        //get section part json string
        JSONObject sectionObj =  wholeObj.getJSONObject("leads");

       // List<SectionFields> sections  = JSON.parseArray(sectionObj.toJSONString(), SectionFields.class);
        List<SectionFields> sections1  = JSON.parseArray(sectionObj.get("section").toString(),SectionFields.class);





        return "";
    }



    public void testJson2(){
        String jsonstr = "{'section':[{'dv':'darlendv1','fl':'darlenfl1','name':'darlenname1'},{'dv':'darlendv2','fl':'darlenfl2','name':'darlenname2'}]}";
        String jsonstr2 = "{fields:{'section':[{'dv':'darlendv1','fl':'darlenfl1','name':'darlenname1'},{'dv':'darlendv2','fl':'darlenfl2','name':'darlenname2'}]}}";
        JSONObject obj ;
        JSONObject obj2 ;
        obj = JSON.parseObject(jsonstr);
        obj2 = JSON.parseObject(jsonstr2);
        JSONArray arr = obj.getJSONArray("section");
        JSONArray arr2 = ((JSONObject) obj2.get("fields")).getJSONArray("section");
        SectionFields section = JSON.toJavaObject((JSON)arr.get(0),SectionFields.class);
        SectionFields section2 = JSON.toJavaObject((JSON)arr2.get(0),SectionFields.class);
        System.out.println();
    }
    public String getLeadsJsonString(String fileName){
        String retStr = "";
        try {
//            String fileName = this.getClass().getClassLoader().getResource("sampledata\\fields\\getFields_leads.json").getPath();
            System.out.println("getLeadsJsonString:::"+fileName);
//            FileReader fr = new FileReader("sampledata\\fields\\getFields_leads.json");
//            FileReader fr = new FileReader("F:\\java_workspace\\ZOHO_CRM\\target\\classes\\sampledata\\fields\\getFields_leads.json");
            FileReader fr = new FileReader(fileName);
            char[] buf = new char[1024];
            int num=0;
            while((num = fr.read(buf)) != -1){
                retStr+=new String(buf,0,num);
            }

//            FileInputStream fio = new FileInputStream("sampledata/fields/getFields_leads.json");
//            BufferedOutputStream bout = new BufferedOutputStream(fio);
        } catch (Exception e) {
            System.err.println(e);
        }
        return retStr;
    }

    public String getFileNamePath(String path,String fileName){
        String filePath = ClassLoader.getSystemResource(fileName).getPath();
        System.out.println("getFileNamePath:::"+filePath);
        return filePath;
    }
}
