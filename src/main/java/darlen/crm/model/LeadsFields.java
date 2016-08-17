/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   LeadsFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model;

/**
 * darlen.crm.model
 * Description：ZOHO_CRM
 * Created on  2016/08/16 23：34
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：34   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class LeadsFields {
//    private String leads;
    private LeadsSection leads;

    public LeadsSection getLeads() {
        return leads;
    }

    public void setLeads(LeadsSection leads) {
        this.leads = leads;
    }


}
