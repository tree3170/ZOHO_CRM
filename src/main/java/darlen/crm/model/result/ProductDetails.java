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
    private String pd_Product_Id;
    /**产品名字Product Name*/
    private String pd_Product_Name;
    //TODO Confirm Unit Price and List Price 区别
    /**定价 (￥)：单价Unit Price*/
    private String pd_Unit_Price;
    /**List Price*/
    private String pd_List_Price;
    /**数量Quantity*/
    private String pd_Quantity;
    /**库存量：Quantity in Stock*/
    private String pd_Quantity_in_Stock;

    /**折扣 (￥)Discount*/
    private String pd_Discount;

    /**累计Total After Discount*/
    private String pd_Total_After_Discount;
    /**总计Total*/
    private String pd_Total;
    /**小计Net Total*/
    private String pd_Net_Total;
    /**税	Tax*/
    private String pd_Tax;
    /**Product Description*/
    private String pd_Product_Description;

    public String getPd_Product_Id() {
        return pd_Product_Id;
    }

    public void setPd_Product_Id(String pd_Product_Id) {
        this.pd_Product_Id = pd_Product_Id;
    }

    public String getPd_Product_Name() {
        return pd_Product_Name;
    }

    public void setPd_Product_Name(String pd_Product_Name) {
        this.pd_Product_Name = pd_Product_Name;
    }

    public String getPd_Unit_Price() {
        return pd_Unit_Price;
    }

    public void setPd_Unit_Price(String pd_Unit_Price) {
        this.pd_Unit_Price = pd_Unit_Price;
    }

    public String getPd_List_Price() {
        return pd_List_Price;
    }

    public void setPd_List_Price(String pd_List_Price) {
        this.pd_List_Price = pd_List_Price;
    }

    public String getPd_Quantity() {
        return pd_Quantity;
    }

    public void setPd_Quantity(String pd_Quantity) {
        this.pd_Quantity = pd_Quantity;
    }

    public String getPd_Quantity_in_Stock() {
        return pd_Quantity_in_Stock;
    }

    public void setPd_Quantity_in_Stock(String pd_Quantity_in_Stock) {
        this.pd_Quantity_in_Stock = pd_Quantity_in_Stock;
    }

    public String getPd_Discount() {
        return pd_Discount;
    }

    public void setPd_Discount(String pd_Discount) {
        this.pd_Discount = pd_Discount;
    }

    public String getPd_Total_After_Discount() {
        return pd_Total_After_Discount;
    }

    public void setPd_Total_After_Discount(String pd_Total_After_Discount) {
        this.pd_Total_After_Discount = pd_Total_After_Discount;
    }

    public String getPd_Total() {
        return pd_Total;
    }

    public void setPd_Total(String pd_Total) {
        this.pd_Total = pd_Total;
    }

    public String getPd_Net_Total() {
        return pd_Net_Total;
    }

    public void setPd_Net_Total(String pd_Net_Total) {
        this.pd_Net_Total = pd_Net_Total;
    }

    public String getPd_Tax() {
        return pd_Tax;
    }

    public void setPd_Tax(String pd_Tax) {
        this.pd_Tax = pd_Tax;
    }

    public String getPd_Product_Description() {
        return pd_Product_Description;
    }

    public void setPd_Product_Description(String pd_Product_Description) {
        this.pd_Product_Description = pd_Product_Description;
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "pd_Product_Id='" + pd_Product_Id + '\'' +
                ", pd_Product_Name='" + pd_Product_Name + '\'' +
                ", pd_Unit_Price='" + pd_Unit_Price + '\'' +
                ", pd_List_Price='" + pd_List_Price + '\'' +
                ", pd_Quantity='" + pd_Quantity + '\'' +
                ", pd_Quantity_in_Stock='" + pd_Quantity_in_Stock + '\'' +
                ", pd_Discount='" + pd_Discount + '\'' +
                ", pd_Total_After_Discount='" + pd_Total_After_Discount + '\'' +
                ", pd_Total='" + pd_Total + '\'' +
                ", pd_Net_Total='" + pd_Net_Total + '\'' +
                ", pd_Tax='" + pd_Tax + '\'' +
                ", pd_Product_Description='" + pd_Product_Description + '\'' +
                '}';
    }
}
