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
    private String Last_Name;
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
    private String Lead_Source;
    /**线索状态*/
    private String Lead_Status;
    /**行业*/
    private String Industry;
    /**员工数No of Employees*/
    private String No_of_Employees;
    /**年收入Annual Revenue*/
    private String Annual_Revenue;
    /**等级*/
    private String Rating;
    /**？*/
    private String CAMPAIGNID;
    /**？Campaign Source*/
    private String Campaign_Source;
    /**不发营销邮件Email Opt Out*/
    private String Email_Opt_Out;
    /**Skype ID*/
    private String Skype_ID;
    /**创建人id:SMCREATORID*/
    private String SMCREATORID;
    /**创建人Created By*/
    private String Created_By;
    /**修改人id:MODIFIEDBY*/
    private String MODIFIEDBY;
    /**Modified_By*/
    private String Modified_By;
    /**Created Time*/
    private String Created_Time;
    /**Modified Time*/
    private String Modified_Time;
    private String Street;
    private String City;
    /**省*/
    private String State;
    /**邮编Zip Code*/
    private String Zip_Code;
    private String Country;
    private String Description;
    /**备用邮件 Secondary Email*/
    private String Secondary_Email;
    /**与modified time有什么区别:Last Activity Time*/
    private String Last_Activity_Time;

    public String getLEADID() {
        return LEADID;
    }

    public void setLEADID(String LEADID) {
        this.LEADID = LEADID;
    }

    public String getSMOWNERID() {
        return SMOWNERID;
    }

    public void setSMOWNERID(String SMOWNERID) {
        this.SMOWNERID = SMOWNERID;
    }

    public String getLead_Owner() {
        return Lead_Owner;
    }

    public void setLead_Owner(String lead_Owner) {
        Lead_Owner = lead_Owner;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getSalutation() {
        return Salutation;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getLead_Source() {
        return Lead_Source;
    }

    public void setLead_Source(String lead_Source) {
        Lead_Source = lead_Source;
    }

    public String getLead_Status() {
        return Lead_Status;
    }

    public void setLead_Status(String lead_Status) {
        Lead_Status = lead_Status;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getNo_of_Employees() {
        return No_of_Employees;
    }

    public void setNo_of_Employees(String no_of_Employees) {
        No_of_Employees = no_of_Employees;
    }

    public String getAnnual_Revenue() {
        return Annual_Revenue;
    }

    public void setAnnual_Revenue(String annual_Revenue) {
        Annual_Revenue = annual_Revenue;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getCAMPAIGNID() {
        return CAMPAIGNID;
    }

    public void setCAMPAIGNID(String CAMPAIGNID) {
        this.CAMPAIGNID = CAMPAIGNID;
    }

    public String getCampaign_Source() {
        return Campaign_Source;
    }

    public void setCampaign_Source(String campaign_Source) {
        Campaign_Source = campaign_Source;
    }

    public String getEmail_Opt_Out() {
        return Email_Opt_Out;
    }

    public void setEmail_Opt_Out(String email_Opt_Out) {
        Email_Opt_Out = email_Opt_Out;
    }

    public String getSkype_ID() {
        return Skype_ID;
    }

    public void setSkype_ID(String skype_ID) {
        Skype_ID = skype_ID;
    }

    public String getSMCREATORID() {
        return SMCREATORID;
    }

    public void setSMCREATORID(String SMCREATORID) {
        this.SMCREATORID = SMCREATORID;
    }

    public String getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    public String getMODIFIEDBY() {
        return MODIFIEDBY;
    }

    public void setMODIFIEDBY(String MODIFIEDBY) {
        this.MODIFIEDBY = MODIFIEDBY;
    }

    public String getModified_By() {
        return Modified_By;
    }

    public void setModified_By(String modified_By) {
        Modified_By = modified_By;
    }

    public String getCreated_Time() {
        return Created_Time;
    }

    public void setCreated_Time(String created_Time) {
        Created_Time = created_Time;
    }

    public String getModified_Time() {
        return Modified_Time;
    }

    public void setModified_Time(String modified_Time) {
        Modified_Time = modified_Time;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip_Code() {
        return Zip_Code;
    }

    public void setZip_Code(String zip_Code) {
        Zip_Code = zip_Code;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSecondary_Email() {
        return Secondary_Email;
    }

    public void setSecondary_Email(String secondary_Email) {
        Secondary_Email = secondary_Email;
    }

    public String getLast_Activity_Time() {
        return Last_Activity_Time;
    }

    public void setLast_Activity_Time(String last_Activity_Time) {
        Last_Activity_Time = last_Activity_Time;
    }
}
