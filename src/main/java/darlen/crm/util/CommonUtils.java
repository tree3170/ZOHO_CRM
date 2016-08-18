/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   CommonUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import darlen.crm.model.fields.common.CommonSection;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * darlen.util
 * Description：ZOHO_CRM
 * Created on  2016/08/18 00：20
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        00：20   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class CommonUtils {
    public static void main(String[] args) {
        getJsonStringByPathAndName("sampledata/fields/", "getFields_leads.json");
    }

    /**
     * get file name  absolute  path by relative path + filename
     * @param path
     * @param fileName
     * @return
     */
    public static String getFileNamePath(String path,String fileName){
        String filePath = ClassLoader.getSystemResource(path+fileName).getPath();
        System.out.println("getFileNamePath:::"+filePath);
        return filePath;
    }

    /**
     * get json string by
     * @param fileName
     * @return
     */
    public static String getJsonStringByPathAndName(String path, String fileName){
        String retStr = "";
        try {
            String realPath = getFileNamePath(path,fileName);
            System.out.println("[getLeadsJsonString],file real path:::"+realPath);
            FileReader fr = new FileReader(realPath);
            char[] buf = new char[1024];
            int num=0;
            while((num = fr.read(buf)) != -1){
                retStr+=new String(buf,0,num);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        System.out.println(retStr);
        return retStr;
    }

    /**
     * Get function and all fields mapping
     * @param funcsJsonMap   function and Json file name mapping
     * @return map key-->function, value --> fields collection
     */
    public static Map<String,List<String>> getFunctionsFields(Map<String,String> funcsJsonMap){
        //1.for loop function to get the section fields array
        Map<String,List<String>>  funcFieldsMap = new HashMap();
        for(Map.Entry<String,String> entry : funcsJsonMap.entrySet()){
            String func = entry.getKey();
            String jsonName = entry.getValue();
            List<CommonSection> sectionFields  = parseJsonToJavaObj(func,jsonName);
            //7.得到某个function下面所有的fields
            List<String> allFieldsFunc =  getAllFieldsBySection(func,sectionFields);
            funcFieldsMap.put(func,allFieldsFunc);
        }

        return funcFieldsMap;
    }

    /**
     * parse Json String to CommonSection collection java object
     * @param function
     * @return
     */
    public static List<CommonSection>  parseJsonToJavaObj(String function,String jsonName){
        List<CommonSection> sectionFields = new ArrayList<CommonSection>();
        try {
            //TODO：：：2. get current function jsonstr, 暂时hardcode
            String funcJsonString = CommonUtils.getJsonStringByPathAndName("", jsonName);//"testjson.json"
            //3.解析整个function的json字符串到JSONObject对象
            JSONObject wholeObj = JSON.parseObject(funcJsonString);
            //4.获取某个function部分的JSONObject对象
            JSONObject functionObj =  wholeObj.getJSONObject(function);
            //5.获取某个function下面的section对应的json到JSONObject对象
            JSONArray sectionObj=  functionObj.getJSONArray("section");
            //6.解析所有fields
            sectionFields  = JSON.parseArray(sectionObj.toJSONString(),CommonSection.class);
        }catch (Exception e){

            System.err.println("[parseJsonToJavaObj],parse json to java object occurs error..."+e);
        }


        return sectionFields;

    }

    /**
     * get all fields by function name
     * @param function
     * @param sectionFields
     * @return key : functionName  value : all fields in function
     */
    public static List<String> getAllFieldsBySection(String function, List<CommonSection> sectionFields){
        List<String> fields = new ArrayList<String>();
        for(int i = 0; i < sectionFields.size(); i++ ){
            CommonSection section = sectionFields.get(i);
            String sectionfieldName = section.getDv();
            System.out.println(function + "::: section["+i+"] label name ="+sectionfieldName);
            List<darlen.crm.model.fields.common.Fields> tmpFields = section.getFl();
            for(int j = 0; j < tmpFields.size(); j++){
                darlen.crm.model.fields.common.Fields tmpField =tmpFields.get(j);
                fields.add(tmpField.getDv());
                System.out.println(function + "::: field ="+tmpField.getDv());
            }
        }
        return fields;
    }
}
