package darlen.crm; /** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   darlen.crm.QuotesTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2017 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */

import darlen.crm.controller.FunctionalControler;
import darlen.crm.manager.AbstractModule;
import darlen.crm.manager.ConfigManager;
import darlen.crm.manager.handler.QuotesHandler;
import darlen.crm.util.Constants;
import darlen.crm.util.DBUtils;
import darlen.crm.util.ModuleNameKeys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * PACKAGE_NAME
 * Description：ZOHO_CRM
 * Created on  2017/05/01 10：02
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        10：02   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class QuotesTest {

    public static void main(String[] args) throws Exception {
        testAddByManually();
    }

    //add
    public static void testAddByManually() throws Exception {
        QuotesHandler handle = QuotesHandler.getInstance();
        //1.组装DB 对象List
        String sql = FunctionalControler.getDetailSql(ModuleNameKeys.Quotes.toString(),"1000000002","");
        List dbModuleList = handle.buildDBObjList(true,sql);

        Map<String,Object> dbIDModuleObjMap = (Map<String,Object>)dbModuleList.get(0);
        Map<String,Object> addModuleObjMap = new HashMap<String, Object>();
        for(Map.Entry<String,Object> entry : dbIDModuleObjMap.entrySet()){
            String dbID = entry.getKey();
            addModuleObjMap.put(dbID, entry.getValue());
        }
        String className = "darlen.crm.model.result.Quotes";
        //"/mapping/dbRdQuotesFieldMapping.properties"
        Properties fieldMappingProps = ConfigManager.readProperties(Constants.PROPS_QUOTE_DB_MAPPING);
        System.out.println(fieldMappingProps);
        //2 获取add xml
        List<String> addZohoXmlList =  handle.buildAdd2ZohoXml(addModuleObjMap, className, fieldMappingProps);
        String url = "https://crm.zoho.com.cn/crm/private/xml/Quotes/insertRecords";
        String id="";
        String xmlData=addZohoXmlList.get(0);
        String moduleName=ModuleNameKeys.Quotes.toString();
        //Constants.ZOHO_CRUD_UPDATE,DELETE,ADD
        int crudKey=Constants.ZOHO_CRUD_ADD;
        System.out.println(xmlData);
        AbstractModule.commonPostMethodForSepRuun(url,id,xmlData,moduleName,crudKey);
    }

}
