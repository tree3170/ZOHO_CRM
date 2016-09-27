/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Result.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb.SO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * darlen.crm.jaxb_xml_object.leads
 * Description：ZOHO_CRM
 * Created on  2016/08/27 08：23
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：23   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Result {
    @XmlElement(name="SalesOrders")
    private SO so;

    public SO getSo() {
        return so;
    }

    public void setSo(SO so) {
        this.so = so;
    }

    @Override
    public String toString() {
        return "Result{" +
                "so=" + so +
                '}';
    }
}