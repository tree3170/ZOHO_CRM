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
import darlen.crm.util.Constants;
import darlen.crm.util.DBUtils;
import darlen.crm.util.ModuleNameKeys;
import darlen.crm.util.StringUtils;
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

    // 删除所有数据d
    // 删除某条数据
    // 查看Report
    // SQL查询
    // HouseKeep：1.删除无用数据2.删除Report的table数据3.删除本地log的文件
    // 尽量做一个登陆页面，登陆后才能做这些功能



    public static void main(String[] args) throws Exception {
        //getDBFieldNameValueMap(SysUser.class.getName(),null);
        //getConnection();
        Map<String,?> accts = (Map<String,?>)DBUtils.getAccountMap().get(0);
        Map<String,?> so = (Map<String,?>)DBUtils.getSOMap().get(0);
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
                erpModuleMap = (Map<String,?>)DBUtils.getAccountMap().get(0);
            }else if(ModuleNameKeys.Products.toString().equals(moduleName)){
                className = Products.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getProductMap().get(0);
            }else if(ModuleNameKeys.Quotes.toString().equals(moduleName)){
                className = Quotes.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getQuotesList().get(0);
            }else if(ModuleNameKeys.SalesOrders.toString().equals(moduleName)){
                className = SO.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getSOMap().get(0);
            }else if(ModuleNameKeys.Invoices.toString().equals(moduleName)){
                className = Invoices.class.getName();
                erpModuleMap = (Map<String,?>)DBUtils.getInvoiceMap().get(0);
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
     *  4. 删除1个月前的log日志和Report记录【TODO】
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



}
