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
import darlen.crm.manager.ConfigManager;
import darlen.crm.model.fields.common.Fields;
import darlen.crm.model.fields.common.Section;
import darlen.crm.model.result.User;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
    private static Logger logger = Logger.getLogger(CommonUtils.class);

    /**
     * get file name  absolute  path by relative path + filename
     * @param path
     * @param fileName
     * @return
     */
    public static String getFileNamePath(String path,String fileName){
        //判断path是不是以/或者\结尾，如果不是，需要加上来
        String wholePath = "";
        if(!StringUtils.isEmptyString(path)){
            int lastLocation = path.lastIndexOf("/") + 1;
            if( lastLocation!= path.length() || lastLocation != path.length()){
                path += "/";
            }
        }
        wholePath = path + fileName;
        //在单独的java程序中 ClassLoader.getSystemResource(wholePath).getPath();
        String filePath = ClassLoader.getSystemResource(wholePath).getPath();
        //在WEB中，CommonUtils.class.getClassLoader().getResource("/").getPath()+wholePath
//        String  filePath =CommonUtils.class.getClassLoader().getResource("/").getPath()+wholePath;
        logger.debug("[getFileNamePath], file path :"+filePath);
        return filePath;
    }

    /**
     * get content by path and file name
     * @param fileName
     * @return
     */
    public static String getContentsByPathAndName(String path, String fileName) throws Exception{
        String retStr = "";
        try {
            String realPath = getFileNamePath(path,fileName);
            logger.debug("#[getContentsByPathAndName],file real path:::" + realPath);
            FileReader fr = new FileReader(realPath);
            char[] buf = new char[1024];
            int num=0;
            while((num = fr.read(buf)) != -1){
                retStr+=new String(buf,0,num);
            }
        } catch (Exception e) {
            throw e;
        }
        //TODO::: If uncomment this logger , it will print the whole json string
//        logger.debug("[getLeadsJsonString],whole json string:::"+retStr);
        return retStr;
    }

    /**
     * Get function and all fields mapping
     * @param funcsJsonMap   function and Json file name mapping
     * @return map key-->function, value --> fields collection
     */
    public static Map<String,List<String>> getFunctionsFields(Map<String,String> funcsJsonMap) throws Exception{
        //1.for loop function to get the section fields array
        Map<String,List<String>>  funcFieldsMap = new HashMap();
        for(Map.Entry<String,String> entry : funcsJsonMap.entrySet()){
            String func = entry.getKey();
            String jsonName = entry.getValue();
            List<Section> sectionFields  = parseJsonToJavaObj(func,jsonName);
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
    public static List<Section>  parseJsonToJavaObj(String function,String jsonName) throws Exception{
        List<Section> sectionFields = new ArrayList<Section>();
        try {
            //TODO：：：2. get current function jsonstr, 暂时hardcode
            String funcJsonString = CommonUtils.getContentsByPathAndName("", jsonName);//"testjson.json"
            //3.解析整个function的json字符串到JSONObject对象
            JSONObject wholeObj = JSON.parseObject(funcJsonString);
            //4.获取某个function部分的JSONObject对象
            JSONObject functionObj =  wholeObj.getJSONObject(function);
            //5.获取某个function下面的section对应的json到JSONObject对象
            JSONArray sectionObj=  functionObj.getJSONArray("section");
            //6.解析所有fields
            sectionFields  = JSON.parseArray(sectionObj.toJSONString(),Section.class);
        }catch (Exception e){
            logger.error("[parseJsonToJavaObj],parse json to java object occurs error..." + e.getMessage());
            throw e;
        }
        return sectionFields;
    }

    /**
     * get all fields name in multiple sections  by function name
     * @param function
     * @param sectionFields
     * @return key : functionName  value : all fields in function
     */
    public static List<String> getAllFieldsBySection(String function, List<Section> sectionFields){
        List<String> fields = new ArrayList<String>();
        for(int i = 0; i < sectionFields.size(); i++ ){
            Section section = sectionFields.get(i);
            String sectionfieldName = section.getDv();
            logger.debug(function + "::: section["+i+"] label name ="+sectionfieldName);
//            List<Fields> tmpFields = section.getFl();
//            for(int j = 0; j < tmpFields.size(); j++){
//                Fields tmpField =tmpFields.get(j);
//                fields.add(tmpField.getDv());
//                logger.debug(function + "::: field =" + tmpField.getDv());
//            }
            fields.addAll(parseFieldsToFieldNameList(section.getFl()));
        }
        return fields;
    }

    public static List<String> parseFieldsToFieldNameList(List<Fields> fields){
        List<String> fieldNames = new ArrayList<String>();
        for(int j = 0; j < fields.size(); j++){
            Fields field =fields.get(j);
            fieldNames.add(field.getDv());
            logger.debug( "field name = " + field.getDv());
        }
        return fieldNames;
    }

    /**
     * 重新组装从CRM网站上download的json字符串，因为FL字段是数组类型（详见Fields对象），
     * 但是有的json是对象类型，导致解析的时候会出错，所以需要对原json字符串的"FL":前后加中括号
     * 例如：如果遇到"FL":{, 那么需要在大括号之前添加中括号："FL":[{,并且在最近的}大括号之后也相应添加反中括号]
     * 原来：{"name":darlen,"FL":{"dv":"描述"},"dv":"darlen"}
     * 改后：{"name":darlen,"FL":[{"dv":"描述"}],"dv":"darlen"}
     * @param oriJsonStr
     * @return
     */
    public static String handleOriCrmFieldJson(String oriJsonStr){
        String wholeJsonString = "";
        int length = oriJsonStr.indexOf("\"FL\":{");
        //如果有"FL":{
        while (length > 0){
            //1.第一部分：修改"FL":{ to "FL":[{， 最后字符串为"FL":[{之前的所有字符串:{"name":darlen,"FL":[{
            String firstPart = oriJsonStr.substring(0,length+5);//需要加上"FL":的长度：5 然后这个字符串再拼凑一个[{
            System.out.println("第一部分拼凑[{之前：：："+firstPart);
            firstPart+="[{";
            System.out.println("第一部分拼凑[{之后（从括号{前面内容开始所有的字符串）：：："+firstPart);

            //2.{}中间的内容::"dv":"描述"}]
            String secondPart = oriJsonStr.substring(length+6);//从括号{后面内容开始所有的字符串
            System.out.println("第二部分（从括号{后面内容开始所有的字符串）：：："+secondPart);
            int length2 = secondPart.indexOf("}");
            String thirdPart = secondPart.substring(length2 + 1);
            //重新组装第二部分字符串：：： (获取第一个｝之前的内容
            secondPart = secondPart.substring(0,length2+1) +"]";
            System.out.println("第二部分(获取第一个｝之前的内容):::"+secondPart);

            //3.}之后的内容,"dv":"darlen"}
            System.out.println("第三部分：：："+thirdPart);
            wholeJsonString  = firstPart+secondPart+thirdPart;
            System.out.println(wholeJsonString);
            oriJsonStr = wholeJsonString ;
            length = wholeJsonString.indexOf("\"FL\":{");
        }
        System.out.println("whole string:::"+wholeJsonString);
        return wholeJsonString;
    }

    public static void parseJsonToJavaObj(){

    }
    public static void parseSection(){

    }
    public static void parseFields(){

    }

    public static void setPostMethodParams(PostMethod post,Map<String,String> map){
        String url = map.get(Constants.HTTP_POST_PARAM_TARGETURL)+"?";
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.HTTP_POST_PARAM_UTF8);
        for(Map.Entry<String,String> entry: map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if(!StringUtils.isEmptyString(value) && !Constants.HTTP_POST_PARAM_TARGETURL.equals(key)){
                url += key+"="+value+"&";
                post.setParameter(key, value);
            }
        }
        logger.debug("发送的URL是:::"+url.substring(0,url.length()-1));
        //System.err.println("发送的URL是:::"+url.substring(0,url.length()-1));
        /*post.setParameter(Constants.HTTP_POST_PARAM_AUTHTOKEN, authToken);
        post.setParameter(Constants.HTTP_POST_PARAM_SCOPE, scope);
        post.setParameter(Constants.HTTP_POST_PARAM_NEW_FORMAT, format);
        post.setParameter(Constants.HTTP_POST_PARAM_XMLDATA, xmlData);
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.HTTP_POST_PARAM_UTF8);
        if(!StringUtils.isEmptyString(id)) post.setParameter(Constants.HTTP_POST_PARAM_ID,id);*/
    }

    /**
     * Execute post method
     * @param map
     */
    public static String executePostMethod(Map<String,String> map){
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");
        PostMethod post = new PostMethod(map.get(Constants.HTTP_POST_PARAM_TARGETURL));
        /*post.setParameter(Constants.HTTP_POST_PARAM_AUTHTOKEN, map.get(Constants.HTTP_POST_PARAM_AUTHTOKEN));
        post.setParameter(Constants.HTTP_POST_PARAM_SCOPE, map.get(Constants.HTTP_POST_PARAM_SCOPE));
        post.setParameter(Constants.HTTP_POST_PARAM_NEW_FORMAT, map.get(Constants.HTTP_POST_PARAM_NEW_FORMAT));
        post.setParameter(Constants.HTTP_POST_PARAM_XMLDATA, map.get(Constants.HTTP_POST_PARAM_XMLDATA));
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.HTTP_POST_PARAM_UTF8);

        String id = map.get(Constants.HTTP_POST_PARAM_ID);
        if(!StringUtils.isEmptyString(id)) post.setParameter(Constants.HTTP_POST_PARAM_ID,id);*/
        setPostMethodParams(post,map);
        HttpClient httpclient = new HttpClient();
        PrintWriter myout = null;
        String postResp = "";
        try
        {
            long t1 = System.currentTimeMillis();
            int result = httpclient.executeMethod(post);
            logger.error("HTTP Response status code: " + result);
            logger.error(">> Time taken " + (System.currentTimeMillis() - t1));

            // writing the response to a file
            myout = new PrintWriter(new File("response.xml"));
            myout.print(post.getResponseBodyAsString());

            //-----------------------Get response as a string ----------------
            postResp = post.getResponseBodyAsString();
            logger.error("postResp=======>"+postResp);

            /**
             * sample response=======><?xml version="1.0" encoding="UTF-8" ?>
             <response uri="/crm/private/xml/Leads/updateRecords"><result><message>Record(s) updated successfully</message><recorddetail><FL val="Id">85333000000072001</FL><FL val="Created Time">2016-07-13 23:58:11</FL><FL val="Modified Time">2016-08-22 21:36:36</FL><FL val="Created By"><![CDATA[qq qq]]></FL><FL val="Modified By"><![CDATA[tree3170]]></FL></recorddetail></result></response>
             */
        }catch(Exception e){
            logger.error("#[executePostMethod] executed occurs error"+e);
        } finally{
            myout.close();
            post.releaseConnection();
        }
        return  postResp;
    }

    /**
     * 读取properties 文件 根据相对路径
     */
    public static Properties readProperties(String relativePath) throws IOException {
        Properties prop = new Properties();
        try {
            prop.load(CommonUtils.class.getResourceAsStream(relativePath));// "/secure/db.properties"
        } catch(IOException e) {
            logger.error("读取properties文件出错【"+relativePath+"】",e);
            throw e;
        }

        //TODO 开发环境中禁止打印
        String propValues = "打印properties的键值对：：：";
        for(Object obj : prop.keySet()){
            propValues += obj+"="+prop.get(obj)+"; ";
        }
        logger.debug(propValues);

        return  prop;
    }

    /**
     * 打印map
     * @param map
     * @param custMsg， 自定义打印前的message
     */
    public static void printMap(Map<?,?> map,String custMsg){
        String str = "";
        for(Map.Entry<?,?> entry : map.entrySet()){
            str += entry.getKey()+"="+entry.getValue()+"; ";
        }
        logger.debug("begin print map : "+custMsg+"\n"+str);
    }
    /**
     * 打印list
     * @param list
     * @param custMsg， 自定义打印前的message
     */
    public static void printList(List list,String custMsg){
        String str = "";
        for(int i = 0; i < list.size(); i++){
            str+= list.get(i)+"; ";
        }
        logger.debug("begin print List : "+custMsg+"\n"+str);
    }

    /**
     * 关联拥有者，仅用做测试
     * @param isDev
     * @return
     */
    public static User fetchDevUser(boolean isDev){
        return isDev ? new User("85333000000071039","qq"): new User("80487000000076001","marketing");
    }

    /**
     * 关联拥有者，仅用做测试
     * @return
     */
    public static String getLastEditTime(){
        String time = "";
        try {
            boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
            if(isDevMod) time =  ThreadLocalDateUtil.formatDate(new Date());
        } catch (Exception e) {
            logger.error("getLastEditTime 出错",e);
        }
        return time;
    }
}
