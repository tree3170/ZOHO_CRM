/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Product.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

/**
 * darlen.crm.model.result  产品Module
 * Description：ZOHO_CRM
 * Created on  2016/09/25 11：21
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        11：21   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Products {
    private String erpID;
    /**Product Name 产品名称*/
    private String prodName;
    /**Product Code产品编码,暂时用不到*/
    private String prodCode;
//    private String owerID;
//    /**Product Owner产品所有者
//     * 就是DB中的lastEditBy
//     * */
//    private String owner;
    /**
    * Product Owner产品所有者
    * 就是DB中的lastEditBy
    * */
    private User user;
    /**Enabled*/
    private String enabled;
    /**Catagory 产品分类     */
    private String catagory;
    /**ItemDesc产品描述*/
    private String itemDesc;
    /**Unit单位*/
    private String unit;
    /**SubCategory 产品子分类*/
    private String subCategory;
    /**Barcode*/
    private String barcode;
    /**LatestEditTime*/
    private String latestEditTime;
    /**LatestEditBy*/
    private String latestEditBy;
    /**Remark*/
    private String remark;

    public String getErpID() {
        return erpID;
    }

    public void setErpID(String erpID) {
        this.erpID = erpID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLatestEditTime() {
        return latestEditTime;
    }

    public void setLatestEditTime(String latestEditTime) {
        this.latestEditTime = latestEditTime;
    }

    public String getLatestEditBy() {
        return latestEditBy;
    }

    public void setLatestEditBy(String latestEditBy) {
        this.latestEditBy = latestEditBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Products{" +
                "erpID='" + erpID + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodCode='" + prodCode + '\'' +
                ", user=" + user +
                ", enabled='" + enabled + '\'' +
                ", catagory='" + catagory + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", unit='" + unit + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", barcode='" + barcode + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
