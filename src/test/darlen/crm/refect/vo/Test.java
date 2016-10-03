/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Test.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.refect.vo;

/**
 * darlen.crm.refect.vo
 * Description：ZOHO_CRM
 * Created on  2016/10/03 15：22
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        15：22   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
import java.util.Date;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {

        User user = new User();
        user.setUsername("user109");
        user.setPassword("pwd109");
        user.seteBlog("http://www.cnblogs.com/nick-huang/");
        user.setRegistrationDate(new Date());

        Map<String, FieldEntity> map = FieldsCollector.getFileds(user);
        System.out.println(map);

    }

}

/**
 *{
 username=FieldEntity[fieldname=username,value=user109,clazz=class java.lang.String,errorMsg=[]],
 registrationDate=FieldEntity[fieldname=registrationDate,value=MonOct0315: 22: 51CST2016,clazz=class java.util.Date,errorMsg=[]],
 eBlog=FieldEntity[fieldname=eBlog,value=http: //www.cnblogs.com/nick-huang/,clazz=class java.lang.String,errorMsg=[]],
 password=FieldEntity[fieldname=password,value=pwd109,clazz=class java.lang.String,errorMsg=[]]
 }
 */