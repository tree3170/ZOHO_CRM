/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Leads.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/08/26 08：20
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：20   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Leads {
    /**Leads 编号 id*/
    private String LEADID;
    /**线索所有者：某个账户的id*/
    private String SMOWNERID;
    /**线索所有者：某个账户的名字*/
    private String Lead_Owner;
    /**公司名*/
    private String Company;
    /**Salutation称呼（先生或者女生）：在first name前面*/
    private String Salutation;
    /**名*/
    private String First_Name;
    /**姓*/
    private String Last Name;
    /**职位*/
    private String Designation;
    private String Email;
    /**电话*/
    private String Phone;
    /**传真*/
    private String Fax;
    /**手机*/
    private String Mobile;
    private String Website;
    /**线索来源*/
    private String Lead Source;
    /**线索状态*/
    private String Lead Status;
    /**行业*/
    private String Industry;
    /**员工数*/
    private String No of Employees;
    /**年收入*/
    private String Annual Revenue;
    /**等级*/
    private String Rating;
    /**？*/
    private String CAMPAIGNID;
    /**？*/
    private String Campaign Source;
    /**不发营销邮件*/
    private String Email Opt Out;
    private String Skype ID;
    /**创建人id*/
    private String SMCREATORID;
    /**创建人*/
    private String Created By;
    /**修改人id*/
    private String MODIFIEDBY;
    private String Modified By;
    private String Created Time;
    private String Modified Time;

    private String Street;
    private String City;
    /**省*/
    private String State;
    /**邮编*/
    private String Zip Code;
    private String Country;
    private String Description;
    /**备用邮件*/
    private String Secondary Email;
    /**与modified time有什么区别*/
    private String Last Activity Time;


}
