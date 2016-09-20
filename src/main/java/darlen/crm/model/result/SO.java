/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   SO.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

import java.util.List;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM  SO=Quotation +Potential+Customer+product
 * Created on  2016/08/26 08：20
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：20   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class SO {
    /**ZOHO ID*/
    private String SALESORDERID;
    /**销售订单所有者SMOWNERID*/
    /**主题Subject:必填,就是DB中的SORef 编号*/
    private String subject;
    /** 销售订单编号SO Number*/
    private String soNumber;
    private String owerID;
    /**销售订单所有者Sales Order Owner
     * 就是DB中的lastEditBy
     * */
    private String owner;
    /**客户IDACCOUNTID*/
    private String ACCOUNTID;
    /**QuoteNO*/
    private String quoteNO;
    /**客户名Account Name*/
    private String acctName;
    /**客户名ERP_Currency,DB中用CurrencyName表示     */
    private String erpCurrency;
    /**CustomerNO*/
    private String customerNO;
    /**MailAddress*/
    private String mailAddress;
    /**DeliveryAddress*/
    private String deliveryAddress;
    /**PONO*/
    private String poNO;
    /**Email*/
    private String email;
    /**Tel*/
    private String tel;
    /**Fax*/
    private String fax;
    /**ERP_ExchangeRate*/
    private String erpExchangeRate;
    /**PaymentTerm*/
    private String paymentTerm;
    /**PayMethod*/
    private String payMethod;
    /**DeliveryMethod*/
    private String deliveryMethod;
    /**PaymentPeriod*/
    private String paymentPeriod;
    /**到期日期Due Date*/
    private String dueDate;
    /**LatestEditBy*/
    private String latestEditBy;
    /**LatestEditTime*/
    private String latestEditTime;
    /**CreationTime*/
    private String creationTime;
    private String SOID;
    /**pds*/
    private List<ProductDetails> pds;

    public String getSALESORDERID() {
        return SALESORDERID;
    }

    public void setSALESORDERID(String SALESORDERID) {
        this.SALESORDERID = SALESORDERID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public String getOwerID() {
        return owerID;
    }

    public void setOwerID(String owerID) {
        this.owerID = owerID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQuoteNO() {
        return quoteNO;
    }

    public void setQuoteNO(String quoteNO) {
        this.quoteNO = quoteNO;
    }

    public String getACCOUNTID() {
        return ACCOUNTID;
    }

    public void setACCOUNTID(String ACCOUNTID) {
        this.ACCOUNTID = ACCOUNTID;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getErpCurrency() {
        return erpCurrency;
    }

    public void setErpCurrency(String erpCurrency) {
        this.erpCurrency = erpCurrency;
    }

    public String getCustomerNO() {
        return customerNO;
    }

    public void setCustomerNO(String customerNO) {
        this.customerNO = customerNO;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPoNO() {
        return poNO;
    }

    public void setPoNO(String poNO) {
        this.poNO = poNO;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getErpExchangeRate() {
        return erpExchangeRate;
    }

    public void setErpExchangeRate(String erpExchangeRate) {
        this.erpExchangeRate = erpExchangeRate;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(String paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getSOID() {
        return SOID;
    }

    public void setSOID(String SOID) {
        this.SOID = SOID;
    }

    public List<ProductDetails> getPds() {
        return pds;
    }

    public void setPds(List<ProductDetails> pds) {
        this.pds = pds;
    }

    @Override
    public String toString() {
        return "SO{" +
                "SALESORDERID='" + SALESORDERID + '\'' +
                ", subject='" + subject + '\'' +
                ", soNumber='" + soNumber + '\'' +
                ", owerID='" + owerID + '\'' +
                ", owner='" + owner + '\'' +
                ", ACCOUNTID='" + ACCOUNTID + '\'' +
                ", acctName='" + acctName + '\'' +
                ", erpCurrency='" + erpCurrency + '\'' +
                ", customerNO='" + customerNO + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", poNO='" + poNO + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", erpExchangeRate='" + erpExchangeRate + '\'' +
                ", paymentTerm='" + paymentTerm + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", paymentPeriod='" + paymentPeriod + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", SOID='" + SOID + '\'' +
                ", pds=" + pds +
                '}';
    }
}
