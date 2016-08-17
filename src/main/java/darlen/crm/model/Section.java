/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Section.java 
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
 * Created on  2016/08/17 08：15
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：15   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Section {
    private List<Fields> filedss;

    public List<Fields> getFiledss() {
        return filedss;
    }

    public void setFiledss(List<Fields> filedss) {
        this.filedss = filedss;
    }
}
