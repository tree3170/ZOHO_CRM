/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   MailManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.model.mail.MailMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;

/**
 * darlen.crm.manager
 * Description：ZOHO_CRM
 * Created on  2016/10/09 12：33
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        12：33   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class MailManager {

    @Autowired
    private MailMail mail;

    public static void main( String[] args ) throws MessagingException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring/Spring-Mail.xml");

        MailMail mm = (MailMail) context.getBean("mailMail");
        mm.sendImageMail(
                //"tree3170@gmail.com",
                "tree317035791@163.com",
                "317035791@qq.com",
                "Testing123",
                "Testing only \n\n Hello Spring Email Sender");

    }
}
