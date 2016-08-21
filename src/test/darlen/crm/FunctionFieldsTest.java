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
import org.apache.log4j.Logger;

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
    private static Logger logger = Logger.getLogger(FunctionFieldsTest.class);
    public static void main(String[] args) throws Exception{
        Map<String,String> funcJsonMap = new HashMap();
        funcJsonMap.put("Leads", "sampledata/fields/getFields_Leads.json");
        funcJsonMap.put("Accounts", "sampledata/fields/getFields_Accounts.json");
        funcJsonMap.put("Invoices", "sampledata/fields/getFields_Invoices.json");
        funcJsonMap.put("Contacts", "sampledata/fields/getFields_Contacts.json");
        funcJsonMap.put("Products", "sampledata/fields/getFields_Products.json");
        funcJsonMap.put("Quotes","sampledata/fields/getFields_Quotes.json");
        funcJsonMap.put("SalesOrders","sampledata/fields/getFields_SO.json");
        try{
           CommonUtils.getFunctionsFields(funcJsonMap);
        }catch (Exception e){
            logger.error("test:::",e);
           // throw e;
        }

    }



}
