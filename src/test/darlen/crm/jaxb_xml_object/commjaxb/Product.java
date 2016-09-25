/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ProdDetails.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb_xml_object.commjaxb;


import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * darlen.crm.jaxb_xml_object.SO
 * Description：ZOHO_CRM
 * Created on  2016/08/27 22：19
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：19   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Product {
    @XmlAttribute(name="no")
    private String no;

    @XmlElement(name = "FL")
    private List<FL> fls;

    public List<FL> getFls() {
        return fls;
    }

    public void setFls(List<FL> fls) {
        this.fls = fls;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "Product{" +
                "no='" + no + '\'' +
                ", fls=" + fls +
                '}';
    }
}
