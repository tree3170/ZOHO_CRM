/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   FunctionalControler.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.controller;

import darlen.crm.manager.AbstractModule;
import darlen.crm.manager.ConfigManager;
import darlen.crm.manager.ModuleManager;
import darlen.crm.model.result.*;
import darlen.crm.util.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //默认页面http://localhost:8080/zoho/module/index
 * darlen.crm.controller
 * Description：ZOHO_CRM
 * Created on  2016/10/13 22：57
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：57   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@Controller
public class FunctionalControler {
    private static Logger logger = Logger.getLogger(QuartzController.class);
    public static final String ACCT_SQL = "select  zoho.customerid as ERPID ,zoho.* from dbo.Customer as zoho  where 1 =1 ";
    public static final String PROD_SQL = "select zoho.itemid as ERPID,zoho.name as ZOHONAME ,* from dbo.item  as zoho where 1 =1 ";
    public static final String QUOTES_SQL = "SELECT zoho.QuoteID AS ERPID, zoho.CustomerID, zoho.QuoteRef, zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE ,zoho.LatestEditBy, \n" +
            "iq.Item_QuoteID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, iq.QuotePrice AS PROD_UNITPRICE,  iq.QUANTITY AS PROD_QUANTITY, iq.ITEMDISCOUNT AS PROD_DISCOUNT, iq.DESCRIPTION AS PROD_DESC , \n" +
            "pt.Name as PayTerm,\n" +
            "zoho.*\n" +
            " FROM Quote zoho\n" +
            "LEFT JOIN  Item_Quote iq  ON zoho.QuoteID = iq.QuoteID\n" +
            "LEFT JOIN  ITEM item  ON iq.ItemID = item.ITEMID\n" +
            "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
            "where 1 =1\n" +
            //"and zoho.QuoteID in (13,16,27)\n" +
            "and item.ITEMID is not null\n" ;
    public static final String SO_SQL = "SELECT zoho.SOID AS ERPID, zoho.CustomerID, zoho.soREF, zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE , zoho.LatestEditBy ,\n" +
            "itemso.ITEM_SOID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, itemso.SOPRICE AS PROD_UNITPRICE,  itemso.QUANTITY AS PROD_QUANTITY, itemso.ITEMDISCOUNT AS PROD_DISCOUNT, itemso.DESCRIPTION AS PROD_DESC , \n" +
            "pt.Name as PayTerm,\n" +
            "zoho.*\n" +
            " FROM SO zoho\n" +
            "LEFT JOIN  ITEM_SO itemso  ON zoho.SOID = itemso.SOID\n" +
            "LEFT JOIN  ITEM item  ON itemso.itemid = item.ITEMID\n" +
            "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
            "where 1 = 1 \n" +
            //"and zoho.SOID in (13,16,27)\n" +
            "and item.ITEMID is not null\n" ;
    public static final String INVOICE_SQL = "SELECT zoho.InvoiceID AS ERPID, zoho.InvoiceRef,zoho.CustomerID,zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE ,zoho.LatestEditBy,\n" +
            "item_inv.Item_InvoiceID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, item_inv.InvoicePrice AS PROD_UNITPRICE,  item_inv.QUANTITY AS PROD_QUANTITY, item_inv.ITEMDISCOUNT AS PROD_DISCOUNT, item_inv.DESCRIPTION AS PROD_DESC , \n" +
            "pt.Name as PayTerm,\n" +
            "zoho.*\n" +
            " FROM Invoice zoho\n" +
            "LEFT JOIN  ITEM_INVOICE item_inv  ON zoho.InvoiceID = item_inv.InvoiceID\n" +
            "LEFT JOIN  ITEM item  ON item_inv.itemid = item.ITEMID\n" +
            "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
            "where 1= 1 \n" +
            "and item.ITEMID is not null\n" ;

    // 删除所有数据d
    // 删除某条数据
    // 查看Report
    // SQL查询
    // HouseKeep：1.删除无用数据2.删除Report的table数据3.删除本地log的文件
    // 尽量做一个登陆页面，登陆后才能做这些功能



    public static void main(String[] args) throws Exception {
        //getDBFieldNameValueMap(SysUser.class.getName(),null);
        //getConnection();
        Map<String,?> accts = (Map<String,?>)DBUtils.getAccountMap(false,"").get(0);
        Map<String,?> so = (Map<String,?>)DBUtils.getSOMap(false,"").get(0);
        List list = new ArrayList();
        for(Map.Entry<String,?> entry : so.entrySet()){
            Object obj = entry.getValue();
            //Map<String,Object> map = AbstractModule.getDBFieldNameValueMap(Accounts.class.getName(), obj);
            Map<String,Object> map = AbstractModule.getDBFieldNameValueMap(SO.class.getName(), obj);
            list.add(map);

        }
        logger.debug("=========" + list);
        //Map map = getDBFieldNameValueMap(Accounts.class.getName(),users.get(2));
        //System.err.println("======="+accts);

    }
    //@ResponseBody
    @RequestMapping("/module/{moduleName}")
    public String getModuleList(@PathVariable String moduleName , HttpServletRequest request) throws Exception {
        List list = new ArrayList();
        List<String> fieldNameList = new ArrayList<String>();
        String className = "";
        //首先获取正确的Account和Product list，因为Quotes、SO。Invoice需要用到正确的Product ID
        ModuleManager.rewriteAcctProdProps();
        //ConfigManager.getAcctsfromProps("");
        //ConfigManager.getProdfromProps("");
        Map<String,?> erpModuleMap = null;
        String errMessage = "";
        try {
            if(ModuleNameKeys.Accounts.toString().equals(moduleName)){
                className = Accounts.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getAccountMap(false,"").get(0);
            }else if(ModuleNameKeys.Products.toString().equals(moduleName)){
                className = Products.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getProductMap(false,"").get(0);
            }else if(ModuleNameKeys.Quotes.toString().equals(moduleName)){
                className = Quotes.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getQuotesList(false,"").get(0);
            }else if(ModuleNameKeys.SalesOrders.toString().equals(moduleName)){
                className = SO.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getSOMap(false,"").get(0);
            }else if(ModuleNameKeys.Invoices.toString().equals(moduleName)){
                className = Invoices.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getInvoiceMap(false,"").get(0);
            }
            if(!StringUtils.isEmptyString(className) && null != erpModuleMap && erpModuleMap.size() > 0 ){
                for(Map.Entry<String,?> entry : erpModuleMap.entrySet()){
                    Object obj = entry.getValue();
                    Map<String,Object> map = AbstractModule.getDBFieldNameValueMap(className, obj);
                    list.add(map);
                }
            }

            if(null != erpModuleMap && erpModuleMap.size() > 0 && list.size() >0 ){
                Map<String,?> map = (Map<String,?>)list.get(0);
                for(Map.Entry<String,?> entry :map.entrySet() ){
                    fieldNameList.add(entry.getKey());
                }
            }

        } catch (Exception e) {
            logger.error("getModuleList 出错："+moduleName
                    + Constants.COMMENT_PREFIX+erpModuleMap,e);
            errMessage = e.getMessage();
        }

        request.setAttribute("moduleResultList", list);
        request.setAttribute("name",moduleName);
        request.setAttribute("fieldNames",fieldNameList);
        request.setAttribute("errMessage",errMessage);
        //logger.debug("=========request.setAttribute(moduleName)"+request.getAttribute("moduleResultList") +", ===="+ list);
        return "showModule";
    }

    @RequestMapping("/report")//{startTime}/{endTime}
    public String showReport(String startTime,String endTime, HttpServletRequest request) throws Exception {
        List params = new ArrayList();
        //select * from ZOHO_EXCE_REPORT where  START_TIME > '2016-10-08 0:29:26' and end_time < '2016-10-08 0:29:38'

        String sql = "select * from ZOHO_EXCE_REPORT WHERE 1=1 ";
        if(!StringUtils.isEmptyString(startTime)){
            sql+= "  AND START_TIME >= '"+startTime+"'" ;
        }
        if(!StringUtils.isEmptyString(endTime) ){
            if(!StringUtils.isEmptyString(startTime)) sql += " AND ";
            sql+= " END_TIME <= '"+endTime+"'" ;
        }

        List<Report> reports = new ArrayList<Report>();
        if(!StringUtils.isEmptyString(startTime) || !StringUtils.isEmptyString(endTime)){
            sql += " ORDER BY END_TIME DESC";
            logger.debug("startTime="+startTime+", endTime = "+endTime +", SQL = "+ sql);
            reports = DBUtils.fetchReportByTime(sql,params);
        }

        request.setAttribute("startTime",StringUtils.nullToString(startTime));
        request.setAttribute("endTime",StringUtils.nullToString(endTime));
        request.setAttribute("reports",reports);
        logger.debug("###########startTime="+ request.getAttribute("startTime")+", endTime = "+ request.getAttribute("endTime") +", SQL = "+ sql
            +Constants.COMMENT_PREFIX+ ", Reports = "+reports);
        return "showReport";
    }

    /**
     * Do House keep
     * @param param
     *  1: Environment Auto detection
     *  2: Normal House Keep
     *  3: Delete ALL Module Records
     *  4. 删除1个月前的log日志和Report记录 [TODO]
     *  5.
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/houseKeep/{param}")//{startTime}/{endTime}
    @ResponseBody
    public List houseKeep(@PathVariable String param, HttpServletRequest request) throws Exception {
        boolean isSuccess = true;
        String message = "";
        if("1".equals(param)){
            List list = ModuleManager.envAutoChecking();
            int result = StringUtils.nullToInt(list.get(0));
            if(result != 0){
                isSuccess = false;
                message = StringUtils.nullToString(list.get(1));
            }

        }else if("2".equals(param)){
            try {
                ModuleManager.execAllModuleHouseKeep(false);
            }catch (Exception e){
                isSuccess = false;
                message = StringUtils.nullToString(e.getMessage());
            }

        }else if("3".equals(param)){
            try {
                ModuleManager.execAllModuleHouseKeep(true);
            }catch (Exception e){
                isSuccess = false;
                message = StringUtils.nullToString(e.getMessage());
            }
        }else if("4".equals(param)){ //first time load data
            try{
               List list =  ModuleManager.exeAllModuleSend();
               if(null != list && list.size() > 0){
                   isSuccess = Boolean.valueOf(StringUtils.nullToString(list.get(0)));
                    if(!isSuccess){
                        message = StringUtils.nullToString(list.get(1));
                   }
                }
            }catch (Exception e){
                isSuccess = false;
                message = StringUtils.nullToString(e.getMessage());
            }

        }
        else if("5".equals(param)){
            //    发送所有表数据到我的邮箱
        }

        request.setAttribute("success",isSuccess);
        request.setAttribute("message",message);
        List ret = new ArrayList();
        ret.add(isSuccess);
        ret.add(message);
        return ret;
    }
    /**
     * 单独run 某个module，这个功能主要为了处理没有跑成功的case，假设Accounts module没有run成功，那么这批数据需要手动去run
     * 主要条件为Module+Time+Erp IDs
     *  6. 必需增加一个手动导入某个功能，可以选择Module，选择时间，去导入数据  【TODO 20161130-201611】
     * 条件：Module+Time+ERP ID  -->这个周末完成,可以分批也可以分个个来做
     * @param  operation
     *          1： Search
     *          2:  Send
     *          3:  Reset
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/sepRun/{operation}")//{startTime}
    @ResponseBody
    public List sepRunModule(@PathVariable String operation, String module, String latestEditTime,String erpIDs,HttpServletRequest request) throws Exception {
        logger.debug("[sepRunModule], operation="+operation+", module="+module+", latestEditTime="+latestEditTime+", erpIDs="+erpIDs );
        boolean isSuccess = true;
        String errMsg = "";
        StringBuilder sb = new StringBuilder(1024);
        List list = new ArrayList();
        try{
            String sql = getDetailSql(module,erpIDs,latestEditTime);
            //1. start time 必填, module 必填，erpIDs可填
            if("1".equalsIgnoreCase(operation)){
                //查询操作
                if(!StringUtils.isEmptyString(latestEditTime)){
                    list.add(ThreadLocalDateUtil.parse(latestEditTime));
                }
                ResultSet rs = DBUtils.exeQuery(sql, list);
                while (rs.next()){
                    sb.append( rs.getString("ERPID")).append(",");
                }
            }else {
                //Send 操作
                //if exist erp id , execute update , else execute add operation
                //MODULE + ERP ID ==》 return request xml and response xml

                //2. get all data from ZOHO about special module
                //buildSkeletonFromZohoList
                //Ⅱ：这里组装db中的AcctObjList
                //buildDBObjList
                //build2ZohoObjSkeletonList
                //Ⅳ 由发送到ZOHO的骨架对象，组装发送到ZOHO 的XML，分别为添加、更新、删除三个对象集合
                //这里唯一需要注意的是把startTime作为lastestEditTime
                //build2ZohoXmlSkeleton
                sb.append(getResultBysql(sql,module));
                logger.info("最后结果：：：：：：：" + sb);
            }
        }catch (Exception e){
            logger.error("sepRunModule occurs error:", e);
            isSuccess = false;
            errMsg = e.getMessage();
        }
        List ret = new ArrayList();
        ret.add(isSuccess);
        ret.add(errMsg);
        ret.add(sb.toString().substring(0,sb.length()-1));

        return ret;

    }

    private static List getResultBysql(String sql ,String module) throws Exception {
        List ret = null;

        if(ModuleNameKeys.Accounts.toString() .equals(module)){
            ret =  ModuleManager.exeAccounts(true,sql);
        }else if(ModuleNameKeys.Products.toString() .equals(module)){
            ret =  ModuleManager.exeProducts(true,sql);
        }else if(ModuleNameKeys.Quotes.toString() .equals(module)){
            ret =  ModuleManager.exeQuotes(true,sql);
        }else if(ModuleNameKeys.SalesOrders.toString() .equals(module)){
            ret =  ModuleManager.exeSO(true,sql);
        }else if(ModuleNameKeys.Invoices.toString() .equals(module)){
            ret =  ModuleManager.exeInvoice(true,sql);
        }
        return  ret;
    }

    /**
     * 获取详细的SQL
     * @param module
     *  Accounts: Customer
     *  Products: Item
     *  3: Quotes
     *  4: SO
     *  5: Invoice
     *  @param  erpIDs
     *  @param  latestEditTime
     * @return
     */
    public static String getDetailSql(String module,String erpIDs,String latestEditTime) throws IOException, ConfigurationException {
        String sql = "";
        String erpIDName = "";
        if(ModuleNameKeys.Accounts.toString() .equals(module)){
            sql = DBUtils.getWholeSqlWithFilterUser(DBUtils.ACCT_SQL, ModuleNameKeys.Accounts.toString(), "");
            erpIDName = "CUSTOMERID";
            //orderby = " ORDER BY zoho.customerID DESC";
        }else if(ModuleNameKeys.Products.toString() .equals(module)){
            sql = DBUtils.getWholeSqlWithFilterUser(DBUtils.PROD_SQL, ModuleNameKeys.Accounts.toString(), "");
            erpIDName = "ITEMID";
        }else if(ModuleNameKeys.Quotes.toString() .equals(module)){
            sql = DBUtils.getWholeSqlWithFilterUser(DBUtils.QUOTES_SQL, ModuleNameKeys.Accounts.toString(), "");
            erpIDName = "QUOTEID";
        }else if(ModuleNameKeys.SalesOrders.toString() .equals(module)){
            sql = DBUtils.getWholeSqlWithFilterUser(DBUtils.SO_SQL, ModuleNameKeys.Accounts.toString(), "");
            erpIDName = "SOID";
        }else if(ModuleNameKeys.Invoices.toString() .equals(module)){
            sql = DBUtils.getWholeSqlWithFilterUser(DBUtils.INVOICE_SQL, ModuleNameKeys.Accounts.toString(), "");
            erpIDName = "INVOICEID";
        }
        if(!StringUtils.isEmptyString(latestEditTime)){
            sql += " AND zoho.LATESTEDITTIME >= ? ";
        }
        if(!StringUtils.isEmptyString(erpIDs)){
            sql += " AND zoho."+erpIDName + " IN (" + erpIDs+") ";
        }
        sql += " ORDER BY zoho.LATESTEDITTIME DESC";;
        logger.info("#[getDetailSql], sql =" +sql);

        return sql;
    }



}
