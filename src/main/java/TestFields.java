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
import darlen.crm.model.LeadsFields;
import darlen.crm.model.LeadsSection;

import java.io.*;
import java.util.List;

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
        //tf.testClassPath();
        //System.out.println(tf.getLeadsJsonString());

        //JSONObject obj = JSON.parseObject(tf.getLeadsJsonString());
        //System.out.println(obj.get("section"));((JSONObject)obj.get("Leads")).get("section");
        tf.testJson2();;
        tf.testJson(tf.getLeadsJsonString(tf.getFileNamePath("", "testjson.json")));

    }

    public String testJson(String jsonString){

        System.out.println("testJson output json String:::"+jsonString);
        LeadsFields fields = JSON.parseObject(jsonString,LeadsFields.class);
        List val = fields.getLeads().getSection().get(0).getFiledss();
        //System.out.println("testJson:::-------------"+val);
        return "";
    }

    public void testJson2(){
        String jsonstr = "{'section':[{'dv':'darlendv1','fl':'darlenfl1','name':'darlenname1'},{'dv':'darlendv2','fl':'darlenfl2','name':'darlenname2'}]}";
        JSONObject obj = new JSONObject();
        obj = JSON.parseObject(jsonstr);
        JSONArray arr = obj.getJSONArray("section");
        LeadsSection section = JSON.toJavaObject((JSON)arr.get(0),LeadsSection.class);
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
        String filePath = this.getClass().getResource("")+fileName;
        filePath = ClassLoader.getSystemResource(fileName).getPath();
        System.out.println("getFileNamePath:::"+filePath);
//        return "F:/java_workspace/ZOHO_CRM/target/classes/testjson.json";

        return filePath;
    }
}
