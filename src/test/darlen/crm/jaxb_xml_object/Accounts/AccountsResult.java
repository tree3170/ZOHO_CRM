/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Result.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb_xml_object.Accounts;

import darlen.crm.jaxb_xml_object.commjaxb.FieldRows;
import darlen.crm.jaxb_xml_object.commjaxb.Result;

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
public class AccountsResult extends Result{
    @XmlElement(name="Accounts",type = AccountsResult.class)
    private FieldRows fieldRows;

    public FieldRows getFieldRows() {
        return fieldRows;
    }

    public void setFieldRows(FieldRows fieldRows) {
        this.fieldRows = fieldRows;
    }

    @Override
    public String toString() {
        return "Result{" +
                "Accounts=" + fieldRows +
                '}';
    }
}
