/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   school.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.jaxb_xml_object.t4;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * darlen.crm.jaxb.t4
 * Description：ZOHO_CRM
 * Created on  2016/08/26 23：33
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：33   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "school")
@XmlType(propOrder = {})
public class School {
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "School{" +
                "students=" + students +
                '}';
    }
}
