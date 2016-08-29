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
    /**id*/
    private String SALESORDERID;
    /**销售订单所有者SMOWNERID*/
    private String SMOWNERID;
    /**销售订单所有者Sales Order Owner*/
    private String Sales_Order_Owner;
    /** 销售订单编号SO Number*/
    private String SO_Number;
    /**主题Subject:必填*/
    private String Subject;
    /**商机id ： POTENTIALID*/
    private String POTENTIALID;
    /**商机名Potential Name*/
    private String Potential_Name;
    /**客户编号Customer No*/
    private String Customer_No;
    /**采购订单Purchase Order*/
    private String Purchase_Order;
    /**报价名称QUOTEID*/
    private String QUOTEID;
    /**报价名称Quote Name*/
    private String Quote_Name;
    /**到期日期Due Date*/
    private String Due_Date;
    /**待解决Pending*/
    private String Pending;
    /**联系人IDCONTACTID*/
    private String CONTACTID;
    /**联系人名Contact Name*/
    private String Contact_Name;
    /**Carrier*/
    private String Carrier;
    /**消费税Excise Duty*/
    private String Excise_Duty;
    /**销售佣金Sales Commission*/
    private String Sales_Commission;
    /**状态Status*/
    private String Status;
    /**客户IDACCOUNTID*/
    private String ACCOUNTID;
    /**客户名Account Name*/
    private String Account_Name;


    /**SMCREATORID*/
    private String SMCREATORID;
    /**Created By*/
    private String Created_By;
    /**MODIFIEDBY*/
    private String MODIFIEDBY;
    /**Modified By*/
    private String Modified_By;
    /**Created Time*/
    private String Created_Time;
    /**Modified Time*/
    private String Modified_Time;
    /**小计	     Sub Total*/
    private String Sub_Total;
    /**折扣	     Discount*/
    private String Discount;
    /**税	     Tax*/
    private String Tax;
    /**调整 Adjustment*/
    private String Adjustment;
    /**Grand Total*/
    private String Grand_Total;
    /**Billing Street*/
    private String Billing_Street;
    /**Shipping Street*/
    private String Shipping_Street;
    /**Billing City*/
    private String Billing_City;
    /**Shipping City*/
    private String Shipping_City;
    /**Billing State*/
    private String Billing_State;
    /** Shipping State*/
    private String Shipping_State;
    /**Billing Code*/
    private String Billing_Code;
    /**Shipping Code*/
    private String Shipping_Code;
    /**Billing Country*/
    private String Billing_Country;
    /**Shipping Country*/
    private String Shipping_Country;
    /**条款及条件Terms and Conditions*/
    private String Terms_and_Conditions;
    /**Description*/
    private String Description;
    /**pds*/
    private List<ProductDetails> pds;

    public String getSALESORDERID() {
        return SALESORDERID;
    }

    public void setSALESORDERID(String SALESORDERID) {
        this.SALESORDERID = SALESORDERID;
    }

    public String getSMOWNERID() {
        return SMOWNERID;
    }

    public void setSMOWNERID(String SMOWNERID) {
        this.SMOWNERID = SMOWNERID;
    }

    public String getSales_Order_Owner() {
        return Sales_Order_Owner;
    }

    public void setSales_Order_Owner(String sales_Order_Owner) {
        Sales_Order_Owner = sales_Order_Owner;
    }

    public String getSO_Number() {
        return SO_Number;
    }

    public void setSO_Number(String SO_Number) {
        this.SO_Number = SO_Number;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getPOTENTIALID() {
        return POTENTIALID;
    }

    public void setPOTENTIALID(String POTENTIALID) {
        this.POTENTIALID = POTENTIALID;
    }

    public String getPotential_Name() {
        return Potential_Name;
    }

    public void setPotential_Name(String potential_Name) {
        Potential_Name = potential_Name;
    }

    public String getCustomer_No() {
        return Customer_No;
    }

    public void setCustomer_No(String customer_No) {
        Customer_No = customer_No;
    }

    public String getPurchase_Order() {
        return Purchase_Order;
    }

    public void setPurchase_Order(String purchase_Order) {
        Purchase_Order = purchase_Order;
    }

    public String getQUOTEID() {
        return QUOTEID;
    }

    public void setQUOTEID(String QUOTEID) {
        this.QUOTEID = QUOTEID;
    }

    public String getQuote_Name() {
        return Quote_Name;
    }

    public void setQuote_Name(String quote_Name) {
        Quote_Name = quote_Name;
    }

    public String getDue_Date() {
        return Due_Date;
    }

    public void setDue_Date(String due_Date) {
        Due_Date = due_Date;
    }

    public String getPending() {
        return Pending;
    }

    public void setPending(String pending) {
        Pending = pending;
    }

    public String getCONTACTID() {
        return CONTACTID;
    }

    public void setCONTACTID(String CONTACTID) {
        this.CONTACTID = CONTACTID;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

    public String getExcise_Duty() {
        return Excise_Duty;
    }

    public void setExcise_Duty(String excise_Duty) {
        Excise_Duty = excise_Duty;
    }

    public String getSales_Commission() {
        return Sales_Commission;
    }

    public void setSales_Commission(String sales_Commission) {
        Sales_Commission = sales_Commission;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getACCOUNTID() {
        return ACCOUNTID;
    }

    public void setACCOUNTID(String ACCOUNTID) {
        this.ACCOUNTID = ACCOUNTID;
    }

    public String getAccount_Name() {
        return Account_Name;
    }

    public void setAccount_Name(String account_Name) {
        Account_Name = account_Name;
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

    public String getSub_Total() {
        return Sub_Total;
    }

    public void setSub_Total(String sub_Total) {
        Sub_Total = sub_Total;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getAdjustment() {
        return Adjustment;
    }

    public void setAdjustment(String adjustment) {
        Adjustment = adjustment;
    }

    public String getGrand_Total() {
        return Grand_Total;
    }

    public void setGrand_Total(String grand_Total) {
        Grand_Total = grand_Total;
    }

    public String getBilling_Street() {
        return Billing_Street;
    }

    public void setBilling_Street(String billing_Street) {
        Billing_Street = billing_Street;
    }

    public String getShipping_Street() {
        return Shipping_Street;
    }

    public void setShipping_Street(String shipping_Street) {
        Shipping_Street = shipping_Street;
    }

    public String getBilling_City() {
        return Billing_City;
    }

    public void setBilling_City(String billing_City) {
        Billing_City = billing_City;
    }

    public String getShipping_City() {
        return Shipping_City;
    }

    public void setShipping_City(String shipping_City) {
        Shipping_City = shipping_City;
    }

    public String getBilling_State() {
        return Billing_State;
    }

    public void setBilling_State(String billing_State) {
        Billing_State = billing_State;
    }

    public String getShipping_State() {
        return Shipping_State;
    }

    public void setShipping_State(String shipping_State) {
        Shipping_State = shipping_State;
    }

    public String getBilling_Code() {
        return Billing_Code;
    }

    public void setBilling_Code(String billing_Code) {
        Billing_Code = billing_Code;
    }

    public String getShipping_Code() {
        return Shipping_Code;
    }

    public void setShipping_Code(String shipping_Code) {
        Shipping_Code = shipping_Code;
    }

    public String getBilling_Country() {
        return Billing_Country;
    }

    public void setBilling_Country(String billing_Country) {
        Billing_Country = billing_Country;
    }

    public String getShipping_Country() {
        return Shipping_Country;
    }

    public void setShipping_Country(String shipping_Country) {
        Shipping_Country = shipping_Country;
    }

    public String getTerms_and_Conditions() {
        return Terms_and_Conditions;
    }

    public void setTerms_and_Conditions(String terms_and_Conditions) {
        Terms_and_Conditions = terms_and_Conditions;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<ProductDetails> getPds() {
        return pds;
    }

    public void setPds(List<ProductDetails> pds) {
        this.pds = pds;
    }
}
