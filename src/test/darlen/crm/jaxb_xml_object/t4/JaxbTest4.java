package darlen.crm.jaxb_xml_object.t4;

import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JaxbTest4 {

	@Test
	public void showMarshaller() {

        School school = new School();

		Student student = new Student();
		student.setId(1);
		student.setName("学生1");

		Role role = new Role();
		role.setDesc("管理");
		role.setName("班长");
		student.setRole(role);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("学生2");
        role = new Role();
        role.setDesc("管理");
        role.setName("学习委员");
        student2.setRole(role);

        List<Student> students = new ArrayList<Student>();
        students.add(student);
        students.add(student2);
        school.setStudents(students);

		String str = JaxbUtil.convertToXml(school);
		System.out.println(str);
        //System.out.println("123123"+JaxbUtil.converyToJavaBean(str,Student.class));
	}

	@Test
	public void showUnMarshaller() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<school>\n" +
                "    <students>\n" +
                "        <student id=\"1\">\n" +
                "            <name>学生1</name>\n" +
                "            <role>\n" +
                "                <name>班长</name>\n" +
                "                <desc>管理</desc>\n" +
                "            </role>\n" +
                "        </student>\n" +
                "        <student id=\"2\">\n" +
                "            <name>学生2</name>\n" +
                "            <role>\n" +
                "                <name>学习委员</name>\n" +
                "                <desc>管理</desc>\n" +
                "            </role>\n" +
                "        </student>\n" +
                "    </students>\n" +
                "</school>\n";
        School student = JaxbUtil.converyToJavaBean(str, School.class);
		System.out.println(student);
	}
	
}
