/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Fields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.fields.common;

/**
 * darlen.crm.model
 * Description：ZOHO_CRM
 * Created on  2016/08/17 22：28
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：28   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Fields {
    /**
     * dv": "线索所有者"  --> field name
     */
    private String dv;
    /**
     * "customfield": "false" -->是否是自定义字段
      */
    private String customfield;
    /**
     * "maxlength": "120"  -->字段最大长度
     */
    private String maxlength;
    /**
     * "isreadonly": "false" --> 是否是只读字段
     */
    private String isreadonly;
    /**
     * "label": "Lead Owner" -->
     */
    private String label;
    /**
     * "type": "Lookup"，"Text","Email","Phone","Website"  -- >类型：查找
     */
    private String type;
    /**
     *  "req": "false"   -->是否是必填字段
     */
    private String req;

    /**
     * option values
     */
    private String val;

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCustomfield() {
        return customfield;
    }

    public void setCustomfield(String customfield) {
        this.customfield = customfield;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getIsreadonly() {
        return isreadonly;
    }

    public void setIsreadonly(String isreadonly) {
        this.isreadonly = isreadonly;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
