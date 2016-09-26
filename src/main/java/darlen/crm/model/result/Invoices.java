/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Invoices.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

import java.util.List;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/09/25 23：42
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：42   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Invoices {
/**DB ID*/
    private String erpID;
/**Subject主题*/
    private String invoiceSubject;
/**发票拥有者Invoice Owner*/
    private User user;
/**Invoice Date发货单日期*/
    private String invoiceDate;
/**Sales Order销售订单,查看数据是用SOREF*/
//    private SO so;
    private String soName;
    private String SALESORDERID;
/**ERP_ExchangeRate*/
    private String erp_ExchangeRate;
/**PaymentTerm*/
    private String paymentTerm;
/**Due Date到期日期*/
    private String dueDate;
    //TODO 改写成Account对象
/**ACCOUNTID 客户ID*/
    private String ACCOUNTID;
/**Account Name客户名*/
    private String acctName;
/**CustomerNo客戶編號*/
    private String customerNo;
/**Contact公司聯絡人 */
    private String contact;
/**Email*/
    private String email;
/**DeliveryAddress客户发货地址 */
    private String deliveryAddress;
/**MailAddress 客户邮寄地址 */
    private String mailAddress;
/**Fax */
    private String fax;
/**Tel */
    private String tel;
/**ERP_Currency */
    private String erp_Currency;
/**PayMethod付款方式 */
    private String payMethod;
/**DeliveryMethod配送方式 */
    private String deliveryMethod;

/**客户PONo */
    private String poNO;
    /**DNNo 送貨單編號 */
    private String dnNo;
/**Deposit訂金
 * 匯入時x ExchangeRate換成港幣
 * */
    private String deposit;
/**其他费用
 *   匯入時x ExchangeRate換成港幣
 * */
    private String otherCharge;
/**FreightAmount 运费
 * 匯入時x ExchangeRate換成港幣
 * */
    private String freightAmount;
    private String total;
    private String latestEditBy;
    private String creationTime;
    private String latestEditTime;

    /**Customer Discount来自Customer的折扣*/
    private String cusDiscount;//from ERP
    /**Discount来自销售订单中的“折扣”*/
//    private String discount;
    /**Sub Total 来自销售订单中的“小计”*/
    private String subTotal;
    /**Grand Total来自销售订单中的“累计”*/
    private String grandTotal;

    /**pds*/
    private List<ProductDetails> pds;

    public String getErpID() {
        return erpID;
    }

    public void setErpID(String erpID) {
        this.erpID = erpID;
    }


    public String getInvoiceSubject() {
        return invoiceSubject;
    }

    public void setInvoiceSubject(String invoiceSubject) {
        this.invoiceSubject = invoiceSubject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    public String getSALESORDERID() {
        return SALESORDERID;
    }

    public void setSALESORDERID(String SALESORDERID) {
        this.SALESORDERID = SALESORDERID;
    }

    public String getErp_ExchangeRate() {
        return erp_ExchangeRate;
    }

    public void setErp_ExchangeRate(String erp_ExchangeRate) {
        this.erp_ExchangeRate = erp_ExchangeRate;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getErp_Currency() {
        return erp_Currency;
    }

    public void setErp_Currency(String erp_Currency) {
        this.erp_Currency = erp_Currency;
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

    public String getPoNO() {
        return poNO;
    }

    public void setPoNO(String poNO) {
        this.poNO = poNO;
    }

    public String getDnNo() {
        return dnNo;
    }

    public void setDnNo(String dnNo) {
        this.dnNo = dnNo;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(String otherCharge) {
        this.otherCharge = otherCharge;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getLatestEditBy() {
        return latestEditBy;
    }

    public void setLatestEditBy(String latestEditBy) {
        this.latestEditBy = latestEditBy;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLatestEditTime() {
        return latestEditTime;
    }

    public void setLatestEditTime(String latestEditTime) {
        this.latestEditTime = latestEditTime;
    }

    public String getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(String cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<ProductDetails> getPds() {
        return pds;
    }

    public void setPds(List<ProductDetails> pds) {
        this.pds = pds;
    }

    @Override
    public String toString() {
        return "Invoices{" +
                "erpID='" + erpID + '\'' +
                ", subject='" + invoiceSubject + '\'' +
                ", user=" + user +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", Sales Order=" + soName +
                ", Sales Order ID=" + SALESORDERID +
                ", erp_ExchangeRate='" + erp_ExchangeRate + '\'' +
                ", paymentTerm='" + paymentTerm + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", ACCOUNTID='" + ACCOUNTID + '\'' +
                ", acctName='" + acctName + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", fax='" + fax + '\'' +
                ", tel='" + tel + '\'' +
                ", erp_Currency='" + erp_Currency + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", poNO='" + poNO + '\'' +
                ", dnNo='" + dnNo + '\'' +
                ", deposit='" + deposit + '\'' +
                ", otherCharge='" + otherCharge + '\'' +
                ", freightAmount='" + freightAmount + '\'' +
                ", Total='" + total + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                ", Customer Discount='" + cusDiscount + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", grandTotal='" + grandTotal + '\'' +
                ", pds=" + pds +
                '}';
    }
}
