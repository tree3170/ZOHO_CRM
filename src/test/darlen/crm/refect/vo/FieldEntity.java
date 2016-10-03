/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   FieldEntity.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.refect.vo;
import java.util.ArrayList;
import java.util.List;

/**
 * darlen.crm.refect.vo
 * Description：ZOHO_CRM
 * Created on  2016/10/03 15：18
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        15：18   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class FieldEntity {




        // field name
        private String fieldname;

        // field value
        private Object value;

        // field value's class type
        private Class clazz;

        private List<String> errorMsg = new ArrayList<String> ();

        public String getFieldname() {
            return fieldname;
        }

        public void setFieldname(String fieldname) {
            this.fieldname = fieldname;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public List<String> getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(List<String> errorMsg) {
            this.errorMsg = errorMsg;
        }

        public FieldEntity() {
            super();
        }

        public FieldEntity(String fieldname, Object value, Class clazz) {
            super();
            this.fieldname = fieldname;
            this.value = value;
            this.clazz = clazz;
        }

        private FieldEntity(String fieldname, List<String> errorMsg) {
            super();
            this.fieldname = fieldname;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "FieldEntity [fieldname=" + fieldname + ", value=" + value
                    + ", clazz=" + clazz + ", errorMsg=" + errorMsg + "]";
        }


}
