/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Leads.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb.Quotes;

import darlen.crm.jaxb.common.ProdRow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

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
public class Quotes {
    //@XmlElementWrapper(name = "rows")
    @XmlElement(name = "row")
    private List<ProdRow> rows;

    public List<ProdRow> getRows() {
        return rows;
    }

    public void setRows(List<ProdRow> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Quotes{" +
                "rows=" + rows +
                '}';
    }
}
