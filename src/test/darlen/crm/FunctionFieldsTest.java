/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   FunctionFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm;

import darlen.crm.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * darlen.crm
 * Description：ZOHO_CRM
 * Created on  2016/08/18 08：14
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：14   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class FunctionFieldsTest {
    public static void main(String[] args) {
        Map<String,String> funcJsonMap = new HashMap();
        funcJsonMap.put("leads","sampledata/fields/getFields_leads.json");
        CommonUtils.getFunctionsFields(funcJsonMap);

    }



}
