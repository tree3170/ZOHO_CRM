/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   HTMLMailDemo.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package mail;

/**
 * mail
 * Description：ZOHO_CRM
 * Created on  2016/10/09 12：05
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        12：05   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 本类测试html邮件
 *
 * @author sunny
 *
 */
public class HTMLMailDemo
{
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        // 设定mail server
        //senderImpl.setHost("smtp.163.com");
        //senderImpl.setPort(25);
        //google
        senderImpl.setHost("smtp.gmail.com");
        senderImpl.setPort(587);

        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

        // 设置收件人，寄件人
        //messageHelper.setTo("Mailto@sina.com");
        //messageHelper.setFrom("username@163.com");
        messageHelper.setTo("317035791@qq.com");
        //mailMessage.setFrom("tree317035791@163.com");
        messageHelper.setFrom("tree3170@gmail.com");
        messageHelper.setSubject("测试HTML邮件！");
        // true 表示启动HTML格式的邮件
        messageHelper
                .setText(
                        "<html><head></head><body><h1>hello!!spring html Mail</h1></body></html>",
                        true);

        senderImpl.setUsername("tree317035791@163.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("zaq1@WSX_163"); // 根据自己的情况, 设置password

        senderImpl.setUsername("tree3170@gmail.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("a283287=Gmail"); // 根据自己的情况, 设置password
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        ////for gmail,如果不设置这个，会抛出com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first. y126sm24456304pfy.41 - gsmtp
        prop.put("mail.smtp.starttls.enable","true");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println("邮件发送成功..");
    }
}
