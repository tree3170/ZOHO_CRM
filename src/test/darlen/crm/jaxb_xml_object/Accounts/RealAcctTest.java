/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   RealAcctTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb_xml_object.Accounts;

import darlen.crm.manager.ModuleManager;
import darlen.crm.manager.handler.AccountsHandler;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.ModuleNameKeys;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * darlen.crm.jaxb_xml_object.Accounts
 * Description：ZOHO_CRM
 * Created on  2016/11/28 21：13
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：13   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class RealAcctTest {
    private static AccountsHandler handleAccounts;
    //    Ⅰ：或者ZOHO xml并组装到zohoMap(id,lastEditTime) 参考（JaxbAccountsTest.java/JaxbSOTest.java/JaxbLeadsTest.java）
    private static Logger logger =  Logger.getLogger(RealAcctTest.class);


    public synchronized  static AccountsHandler getInstance(){
        if(handleAccounts == null){
            handleAccounts = new AccountsHandler();
            handleAccounts.getProperties();
        }
        return handleAccounts;
    }

    public static void main(String[] args) throws Exception {
        //ModuleManager.exeAccounts();
        //handleAccounts = handleAccounts.getInstance();
        //handleAccounts.build2ZohoObjSkeletonList();
        retrieveZohoRecords(ModuleNameKeys.Accounts.toString(),1,100);
    }

    public static String retrieveZohoRecords(String moduleName,int fromIndex,int toIndex) throws Exception {//,String moduleName
        String retZohoStr = "";
        String url = "";
        String selectedColumns = "";
        url = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
        url = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getSearchRecordsByPDC";
        selectedColumns = "Accounts(Modified Time,ACCOUNTID,Account Name,ERP ID,LatestEditTime)";
        logger.debug("[retrieveZohoRecords], Get All Records As XML From ZOHO ::: moduleName = " + moduleName + ", url =" + url);
        try {
            String sortOrderString = "desc";
            String sortColumnString = "Modified Time";
            //String targetURL_Accounts = "https://crm.zoho.com.cn/crm/private/xml/Accounts/getRecords";
            Map<String,String> postParams = new HashMap<String, String>();
            postParams.put(Constants.HTTP_POST_PARAM_TARGETURL,url);
            postParams.put(Constants.HTTP_POST_PARAM_AUTHTOKEN, "f19d6f4ad3d2a491ef52f83a7a68bf04");
            postParams.put(Constants.HTTP_POST_PARAM_SCOPE, "2");
            postParams.put(Constants.HTTP_POST_FROM_INDEX, fromIndex+"");
            postParams.put(Constants.HTTP_POST_TO_INDEX, toIndex+"");
            //default set the newFormat as 2，因为有可能需要的字段为空
            postParams.put(Constants.HTTP_POST_PARAM_NEW_FORMAT, "2");
            if(!StringUtils.isEmptyString(selectedColumns)){
                postParams.put(Constants.HTTP_POST_PARAM_SELECTCOLS,selectedColumns);
            }
            if(!StringUtils.isEmptyString(sortOrderString)){
                postParams.put(Constants.HTTP_POST_PARAM_SORTORDER,sortOrderString);
            }
            if(!StringUtils.isEmptyString(sortColumnString)){
                postParams.put(Constants.HTTP_POST_PARAM_SORTORDER_COL,sortColumnString);
            }
            postParams.put("searchColumn","ACCOUNTID");
            postParams.put("searchValue","85333000000127011");
            retZohoStr =  CommonUtils.executePostMethod(postParams);
        } catch(Exception e) {
            logger.error("1.1 [retrieveZohoRecords: "+moduleName+"] Execute Search Module occurs Error",e);
            throw e;
        }
        return retZohoStr;
    }

}
