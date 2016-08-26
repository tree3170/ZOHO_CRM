/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ProductDetails.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/08/26 08：21
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：21   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class ProductDetails {
    /**产品IDProduct Id*/
    private String Product_Id;
    /**产品名字Product Name*/
    private String Product_Name;
    //TODO Confirm Unit Price and List Price 区别
    /**定价 (￥)：单价Unit Price*/
    private String Unit_Price;
    /**List Price*/
    private String List_Price;
    /**数量Quantity*/
    private String Quantity;
    /**库存量：Quantity in Stock*/
    private String Quantity_in_Stock;

    /**折扣 (￥)Discount*/
    private String Discount;



    /**累计Total After Discount*/
    private String Total_After_Discount;
    /**总计Total*/
    private String Total;
    /**小计Net Total*/
    private String Net_Total;
    /**税	Tax*/
    private String Tax;
    /**Product Description*/
    private String Product_Description;

}
