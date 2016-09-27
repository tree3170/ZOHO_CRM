/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ProdDetails.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb.common;



import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * darlen.crm.jaxb_xml_object.SO
 * Description：ZOHO_CRM
 * Created on  2016/08/27 22：19
 * sample:
 * <FL val="Product Details">
 *    <product no="1">
 *      <FL val="Product Id">85333000000089011</FL>
 *   </product>
 * </FL>
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：19   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class ProdDetails {

    /**sample:<FL val="Product Details">*/
    @XmlAttribute(name="val")
    private String val;

    /**
     * <FL val="Product Details">
     *    <product no="1">
     *      <FL val="Product Id">85333000000089011</FL>
     *   </product>
     * </FL>
     * */
    @XmlElement(name = "product")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "ProdDetails{" +
                "val='" + val + '\'' +
                ", products=" + products +
                '}';
    }
}
