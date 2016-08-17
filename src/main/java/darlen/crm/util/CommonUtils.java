/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   CommonUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

import java.io.FileReader;

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
    public static String getFileNamePath(String path,String fileName){
        String filePath = ClassLoader.getSystemResource(fileName).getPath();
        System.out.println("getFileNamePath:::"+filePath);
        return filePath;
    }

    public static String getJsonStringByFileName(String fileName){
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
}
