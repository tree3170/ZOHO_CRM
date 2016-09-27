/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   IModule.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import java.text.ParseException;
import java.util.List;

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
public interface IModule {
    //1.
    public List buildSkeletonFromZohoList() throws Exception ;
    //2.
    public List buildDBObjList() throws ParseException;
    //3.
    public List build2ZohoObjSkeletonList() throws Exception;
    //4.
    public List build2ZohoXmlSkeleton() throws Exception;
}
