/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Fields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model;

import java.util.List;

/**
 * darlen.crm.model
 * Description：ZOHO_CRM
 * Created on  2016/08/17 08：22
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：22   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class SectionFields {
    private String dv;
    private List<Fields> fl;
    private String name;

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public List<Fields> getFl() {
        return fl;
    }

    public void setFl(List<Fields> fl) {
        this.fl = fl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
