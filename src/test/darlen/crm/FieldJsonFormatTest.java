/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   FieldJsonFormatTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm;

import com.alibaba.fastjson.JSON;
import darlen.crm.model.fields.common.Fields;
import darlen.crm.model.fields.common.Section;
import darlen.crm.util.CommonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * darlen.crm
 * Description：ZOHO_CRM 这个测试方法主要测试拿到的json字符串有没有正确的[],因为当parse数组[]的时候如果是{},那么转换为对象的时候会出错
 * "FL":{"dv":"描述","customfield":"false","maxlength":"32000","isreadonly":"false","label":"Description","type":"TextArea","req":"false"}
 * "FL":[{"dv":"描述","customfield":"false","maxlength":"32000","isreadonly":"false","label":"Description","type":"TextArea","req":"false"}]
 * Created on  2016/08/18 23：27
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：27   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class FieldJsonFormatTest {

    public static void main(String[] args) throws Exception {
        String jsonStr = "{\"name\": \"Address Information\",\"dv\": \"地址信息\",\"FL\":{\"dv\":\"描述\",\"customfield\":\"false\",\"maxlength\":\"32000\",\"isreadonly\":\"false\",\"label\":\"Description\",\"type\":\"TextArea\",\"req\":\"false\"}}";

//        Section section = JSON.parseObject(jsonStr, Section.class);
//        CommonUtils.getAllFieldsBySection("test",Arrays.asList(section));
//        CommonUtils.parseFieldsToFieldNameList(Arrays.asList(section));
        /**
         * 第一部分，在｛前加[组成第一部分字符串
         * 第二部分，从｛开始所有的字符串
         */
       /* int length = jsonStr.indexOf("\"FL\":{");
        String firstPart = jsonStr.substring(0,length+5);//需要加上"FL":的长度：5 然后这个字符串再拼凑一个[{
        System.out.println("第一部分拼凑[{之前：：："+firstPart);
        firstPart+="[{";
        System.out.println("第一部分拼凑[{之后（从括号{前面内容开始所有的字符串）：：："+firstPart);
        String secondPart = jsonStr.substring(length+6);//从括号{后面内容开始所有的字符串
        //
        System.out.println("第二部分（从括号{后面内容开始所有的字符串）：：："+secondPart);
        int length2 = secondPart.indexOf("}");
        String thirdPart = secondPart.substring(length2 + 1);
        //重新组装第二部分字符串：：： (获取第一个｝之前的内容
        secondPart = secondPart.substring(0,length2+1) +"]";
        System.out.println("第二部分(获取第一个｝之前的内容):::"+secondPart);
//        System.out.println(length2+"====="+secondPart);


        System.out.println("第三部分：：："+thirdPart);
        String wholeJsonString = firstPart+secondPart+thirdPart;
        System.out.println(wholeJsonString);

        Section section = JSON.parseObject(wholeJsonString, Section.class);
        CommonUtils.getAllFieldsBySection("test",Arrays.asList(section));*/

//        String orignJsonString = CommonUtils.getJsonStringByPathAndName("sampledata/fields/", "getFields_Leads.orign.json");
        String orignJsonString = CommonUtils.getJsonStringByPathAndName("sampledata/fields/", "getFields_Invoices.orign.json");
        System.out.println(orignJsonString);
        String wholeStr = getWholeJsonString(orignJsonString);
//        Map<String,String> funcJsonMap = new HashMap();
//        funcJsonMap.put("Accounts", "sampledata/fields/getFields_Accounts.json");
//        try{
//            CommonUtils.getFunctionsFields(funcJsonMap);
//        }catch (Exception e){
//
//             throw e;
//        }

    }

    public static String getWholeJsonString(String jsonStr){
        String wholeJsonString = "";
        int length = jsonStr.indexOf("\"FL\":{");
        while (length > 0){
            String firstPart = jsonStr.substring(0,length+5);//需要加上"FL":的长度：5 然后这个字符串再拼凑一个[{
            System.out.println("第一部分拼凑[{之前：：："+firstPart);
            firstPart+="[{";
            System.out.println("第一部分拼凑[{之后（从括号{前面内容开始所有的字符串）：：："+firstPart);
            String secondPart = jsonStr.substring(length+6);//从括号{后面内容开始所有的字符串
            System.out.println("第二部分（从括号{后面内容开始所有的字符串）：：："+secondPart);
            int length2 = secondPart.indexOf("}");
            String thirdPart = secondPart.substring(length2 + 1);
            //重新组装第二部分字符串：：： (获取第一个｝之前的内容
            secondPart = secondPart.substring(0,length2+1) +"]";
            System.out.println("第二部分(获取第一个｝之前的内容):::"+secondPart);

            System.out.println("第三部分：：："+thirdPart);
            wholeJsonString  = firstPart+secondPart+thirdPart;
            System.out.println(wholeJsonString);
            jsonStr = wholeJsonString ;
            length = wholeJsonString.indexOf("\"FL\":{");
        }
        System.out.println("whole string:::"+wholeJsonString);
        return wholeJsonString;

    }
}
