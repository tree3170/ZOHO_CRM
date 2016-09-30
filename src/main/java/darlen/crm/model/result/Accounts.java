/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Accounts.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/09/18 18：13
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        18：13   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Accounts {
    /**db-->CustomerID*/
    private String erpID;
    /**客户拥有者*/
    private User user;
//    /**客户拥有者ID*/
//    private String SMOWNERID;
//    /**客户拥有者Account Owner*/
//    private String acctOwner;
    /**客户名称Account Name*/
    private String acctName;
    /**CustomerNO客户编号*/
    private String customerNO;
    private String enabled;
    private String phone;
    private String fax;
    private String contact;
    private String direct;
    private String deliveryAddress;
    private String email;
    private String mailAddress;
    private String website;
    private String countryID;
    private String postNo;
    private String state;
    private String city;
    private String deliveryMethod;
    private String remark;
    private String creationTime;
    private String latestEditBy;
    private String latestEditTime;

    public String getErpID() {
        return erpID;
    }

    public void setErpID(String erpID) {
        this.erpID = erpID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public String getSMOWNERID() {
//        return SMOWNERID;
//    }
//
//    public void setSMOWNERID(String SMOWNERID) {
//        this.SMOWNERID = SMOWNERID;
//    }
//
//    public String getAcctOwner() {
//        return acctOwner;
//    }
//
//    public void setAcctOwner(String acctOwner) {
//        this.acctOwner = acctOwner;
//    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getCustomerNO() {
        return customerNO;
    }

    public void setCustomerNO(String customerNO) {
        this.customerNO = customerNO;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLatestEditBy() {
        return latestEditBy;
    }

    public void setLatestEditBy(String latestEditBy) {
        this.latestEditBy = latestEditBy;
    }

    public String getLatestEditTime() {
        return latestEditTime;
    }

    public void setLatestEditTime(String latestEditTime) {
        this.latestEditTime = latestEditTime;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "ERP ID='" + erpID + '\'' +
                ", User='" + user + '\'' +
                ", acctName='" + acctName + '\'' +
                ", customerNO='" + customerNO + '\'' +
                ", enabled='" + enabled + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", contact='" + contact + '\'' +
                ", direct='" + direct + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", email='" + email + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", website='" + website + '\'' +
                ", countryID='" + countryID + '\'' +
                ", postNo='" + postNo + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", remark='" + remark + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                '}';
    }
}
