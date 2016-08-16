/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   TestFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

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
        tf.testClassPath();
        System.out.println(tf.getLeadsJsonString());

        JSONObject obj = JSON.parseObject(tf.getLeadsJsonString());
        System.out.print("1");
    }
    public String getLeadsJsonString(){
        String retStr = "";
        try {
//            FileReader fr = new FileReader("sampledata/fields/getFields_leads.json");
            FileReader fr = new FileReader("F:\\java_workspace\\ZOHO_CRM\\target\\classes\\sampledata\\fields\\getFields_leads.json");
            char[] buf = new char[1024];
            int num=0;
            while((num = fr.read(buf)) != -1){
                retStr+=new String(buf,0,num);
            }
//            FileInputStream fio = new FileInputStream("sampledata/fields/getFields_leads.json");
//            BufferedOutputStream bout = new BufferedOutputStream(fio);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retStr;
    }

    public void testClassPath(){
        System.out.println(this.getClass().getResource(""));
    }
}
