/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   AttachedImageMail.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package mail;

/**
 * mail
 * Description：ZOHO_CRM
 * Created on  2016/10/09 12：12
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        12：12   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 本类测试邮件中嵌套图片
 *
 * @author sunny
 *
 */
public class AttachedImageMail
{
    public static void main(String[] args) throws Exception
    {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        // 设定mail server
        senderImpl.setHost("smtp.163.com");
        senderImpl.setPort(25);

        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
        // multipart模式
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                true);

        // 设置收件人，寄件人
        messageHelper.setTo("317035791@qq.com");
        messageHelper.setFrom("tree317035791@163.com");
        messageHelper.setSubject("测试邮件中嵌套图片!！");
        // true 表示启动HTML格式的邮件，
        // 注意：嵌入图片<img src=\"cid:aaa\"/>，其中cid:是固定的写法，而aaa是一个contentId。
        messageHelper.setText(
                "<html><head></head><body><h1>hello!!spring image html mail</h1>"
                        + "<img src=\"cid:aaa\"/></body></html>", true);

        FileSystemResource img = new FileSystemResource(new File("F:\\java_workspace\\ZOHO_CRM\\src\\main\\webapp\\pic\\My-Learning-Way.jpg"));

        messageHelper.addInline("aaa", img);

        senderImpl.setUsername("tree317035791@163.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("zaq1@WSX_163"); // 根据自己的情况, 设置password
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);

        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println("邮件发送成功..");
    }
}
