/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Reponse.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb.error;

import javax.xml.bind.annotation.*;

/**
 * darlen.crm.jaxb_xml_object.leads
 * Description：ZOHO_CRM
 * Created on  2016/08/27 08：22
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：22   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@XmlType(propOrder = {})
public class Response {
    @XmlElement(name="error")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "error=" + error +
                '}';
    }
}
