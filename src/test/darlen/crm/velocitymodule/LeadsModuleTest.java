/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   LeadsModuleTest.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.velocitymodule;

import darlen.crm.util.CommonUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * darlen.crm.velocitymodule
 * Description：ZOHO_CRM  使用Velocity模板引擎来生成xml的例子
 * Created on  2016/08/22 22：21
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：21   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class LeadsModuleTest {
    public static void main( String[] args )
            throws Exception
    {
        /*
         *   first, get and initialize an engine
         */
        Properties prop = new Properties();
        prop.load(LeadsModuleTest.class.getResourceAsStream("/velocity.properties"));
        VelocityEngine ve = new VelocityEngine();
        ve.init(prop);

        /*
         *   organize our data
         */

        ArrayList list = new ArrayList();
        Map map = new HashMap();

        map.put("name",null);
        map.put("price", "$100.00");
        list.add( map );

        map = new HashMap();
        map.put("name", "Eagle");
        map.put("price", "$59.99");
        list.add( map );

        map = new HashMap();
        map.put("name", "Shark");
        map.put("price", "$3.99");
        list.add( map );

        /*
         *  add that list to a VelocityContext
         */

        VelocityContext context = new VelocityContext();
        context.put("petList", list);

        /*
         *   get the Template
         */

//        Template t = ve.getTemplate(CommonUtils.getFileNamePath("module/","leads.vm" ));
        Template t = ve.getTemplate("module/leads.vm" );

        /*
         *  now render the template into a Writer, here
         *  a StringWriter
         */

        StringWriter writer = new StringWriter();

        t.merge( context, writer );

        /*
         *  use the output in the body of your emails
         */

        System.out.println( writer.toString() );
    }
}
