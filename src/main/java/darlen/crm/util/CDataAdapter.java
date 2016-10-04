/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   CDataAdapter.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

/**
 * darlen.crm.jaxb_xml_object.t1
 * Description：ZOHO_CRM
 * Created on  2016/10/05 00：33
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        00：33   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
import javax.xml.bind.annotation.adapters.XmlAdapter;

//有时候 Java 类不能自然映射到自己所需的 XML 形式，
//这时需要编写自己的适配器类，通过注解绑定到javabean的成员变量上，
//在运行的时候jaxb框架自动会适配你所编写的适配器类的方法，
//CDataAdapter.marshal(String str)，将javabean的成员变量的value值
//转变成你想要的形式。
public class CDataAdapter extends XmlAdapter<String, String> {
    //从javabean到xml的适配方法
    @Override
    public String marshal(String str) throws Exception {
        return "<![CDATA[" + str+ "]]>";
    }

    //从xml到javabean的适配方法
    @Override
    public String unmarshal(String str) throws Exception {
        return str;
    }
}