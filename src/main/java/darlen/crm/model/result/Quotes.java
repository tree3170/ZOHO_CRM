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
 * Total:金额 --> 金额(Total) = 定价(Unit Price) x 数量(Quantity)
 * Net Total:总计 --> 总计 = 金额 - 产品折扣(Discount) - 产品税（Tax）
 * Sub Total:订单小计 --> 订单小计 = 总计和
 * Grand Total:订单累计 -->订单累计 = 订单小计(Sub Total) - 订单折扣(Discount) - 订单税（Tax） - 订单调整(Adjustment)
 * Total After Discount:这是个什么字段，没有在页面显示？
 *
 *
 * Description：ZOHO_CRM  SO=Quotation +Potential+Customer+product
 * Created on  2016/08/26 08：20
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：20   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Quotes {
    /**DB ID*/
    private String erpID;
    /**主题Subject:必填,就是DB中的SORef 编号*/
    private String subject;
    /** 报价编号quotesNO  -- */
    private String quotesNo;
    /**报价所有者*/
    private  User user;
    /**銷售單日期*/
    private  String QuotesDate;
    /**客户编号custNO*/
    private String custNO;
    /**客户名字custName*/
    private String custName;
    /**公司联络人Contact*/
    private String contact;
    /**PaymentTerm*/
    private String paymentTerm;
    /**PayMethod*/
    private String payMethod;
    private String remark;

    /**=========================隐藏字段*/
    /**ERP_ExchangeRate*/
    private String erpExchangeRate;
    /**Customer Discount*/
    private String cusDiscount;//from ERP
    /**客户名ERP_Currency,DB中用CurrencyName表示     */
    private String erpCurrency;
    /**Discount来自销售订单中的“折扣” 实际上是与cusDiscount相等的*/
    private String discount;
    /**Sub Total 来自销售订单中的“小计”*/
    private String subTotal;
    /**Grand Total来自销售订单中的“累计”*/
    private String grandTotal;
    /**LatestEditBy*/
    private String latestEditBy;
    /**LatestEditTime*/
    private String latestEditTime;

    public String getErpID() {
        return erpID;
    }

    public void setErpID(String erpID) {
        this.erpID = erpID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuotesNo() {
        return quotesNo;
    }

    public void setQuotesNo(String quotesNo) {
        this.quotesNo = quotesNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuotesDate() {
        return QuotesDate;
    }

    public void setQuotesDate(String quotesDate) {
        QuotesDate = quotesDate;
    }

    public String getCustNO() {
        return custNO;
    }

    public void setCustNO(String custNO) {
        this.custNO = custNO;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getErpExchangeRate() {
        return erpExchangeRate;
    }

    public void setErpExchangeRate(String erpExchangeRate) {
        this.erpExchangeRate = erpExchangeRate;
    }

    public String getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(String cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public String getErpCurrency() {
        return erpCurrency;
    }

    public void setErpCurrency(String erpCurrency) {
        this.erpCurrency = erpCurrency;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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
        return "Quotes{" +
                "erpID='" + erpID + '\'' +
                ", subject='" + subject + '\'' +
                ", quotesNo='" + quotesNo + '\'' +
                ", user=" + user +
                ", QuotesDate='" + QuotesDate + '\'' +
                ", custNO='" + custNO + '\'' +
                ", custName='" + custName + '\'' +
                ", contact='" + contact + '\'' +
                ", paymentTerm='" + paymentTerm + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", remark='" + remark + '\'' +
                ", erpExchangeRate='" + erpExchangeRate + '\'' +
                ", cusDiscount='" + cusDiscount + '\'' +
                ", erpCurrency='" + erpCurrency + '\'' +
                ", discount='" + discount + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", grandTotal='" + grandTotal + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                '}';
    }
}
