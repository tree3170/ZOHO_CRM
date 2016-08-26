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
}
