/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   IModule.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.jaxb.common.ProdRow;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * darlen.crm.manager
 * Description：ZOHO_CRM
 * Created on  2016/09/27 23：02
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：02   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public interface IModuleHandler {
    public  String retrieveZohoRecordByID(String moduleName,String zohoID) throws Exception;
//    1.这里仅仅只是组装zohoAcctObjList
    public List buildSkeletonFromZohoList() throws Exception ;
//    1.1 从ZOHO获取有效的xml
    public  String retrieveZohoRecords(String moduleName, int fromIndex,int toIndex) throws Exception;
//    1.2 xml 转 java bean
//    1.3 组装 zohoModuleList
    public List buildZohoComponentList(List<ProdRow> rows, String zohoIDName, String erpIDName,String moduleName) throws Exception;
//  2.组装DB 对象List
    public List buildDBObjList() throws Exception;
//    3.由获得的ZOHO所有对象集合和从DB获取的对象集合，经过过滤，获取的组装需要***发送到ZOHO的对象集合骨架***
    public List build2ZohoObjSkeletonList() throws Exception;
//    3.1  获取ZOHO对象的骨架集合
//    3.2 组装DB 对象List
//    3.3 经过过滤，组装发送到ZOHO的三大对象并放入到List中:addMap、updateMap、delZohoIDList
    public List build2Zoho3PartObj(Map<String,String> erpZohoIDMap,Map<String,String> erpIDTimeMap,
                                List<String> delZohoIDList,List dbModuleList) throws Exception;
//    4.组装addZOHOXml，updateZOHOXml，deleteZOHOIDsList,放进zohoXMLList集合对象中
    public List build2ZohoXmlSkeleton() throws Exception;
//    4.1. 获取发送到ZOHO的三大对象集合骨架<String, Object>
//    public List<String>  buildAdd2ZohoXml(Map accountMap) throws Exception;
//    4.2. 添加
//    4.3. 更新
//    4.4. 删除
    public int addRecords(String moduleName,int curdKey,List<String> addZohoXMLList);
    public int updateRecords(String moduleName,int curdKey,Map<String,String> updZohoXMLMap);
    /**
     * 返回第一个是失败的次数，第二个是需要删除的ZOHO IDlist
     * 因为删除的顺序必需是倒序：Invoices/SO/Quotes/Products/Accounts
     * @return
     * @throws Exception
     */
    public int delRecords(String moduleName,int curdKey,List<String> deleteZOHOIDsList);


    public List execSend() throws Exception;
}
