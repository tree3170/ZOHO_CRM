/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   MailMail.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.mail;

/**
 * darlen.crm.model.mail
 * Description：ZOHO_CRM
 * Created on  2016/10/09 12：30
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        12：30   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public class MailMail
{

    //@Autowired
    //@Qualifier("gmailSender")
    private JavaMailSender mailSender;

    //@Autowired
    //@Qualifier("mail163Sender")
    //private JavaMailSender mail163Sender;
    //@Autowired
    //private MailSender mailSender2;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 发送简单文本邮件
     * @param from
     * @param to
     * @param subject
     * @param msg
     */
    public void sendSimpleMail(String from, String to, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     * @param from
     * @param to
     * @param subject
     * @param msg
     * @throws MessagingException
     */
    public void sendHTMLMail(String from, String to, String subject, String msg) throws MessagingException {
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

        // 设置收件人，寄件人
        //messageHelper.setTo("Mailto@sina.com");
        //messageHelper.setFrom("username@163.com");
        messageHelper.setTo(to);
        //mailMessage.setFrom("tree317035791@163.com");
        messageHelper.setFrom(from);
        messageHelper.setSubject("测试HTML邮件！");
        // true 表示启动HTML格式的邮件
        messageHelper
                .setText(
                        "<html><head></head><body><h1>hello!!spring html Mail</h1></body></html>",
                        true);

        // 发送邮件
        mailSender.send(mailMessage);
        System.out.println("邮件发送成功..");
    }

    /**
     * 邮件带图片
     * @param from
     * @param to
     * @param subject
     * @param msg
     * @throws MessagingException
     */
    public void sendImageMail(String from, String to, String subject, String msg) throws MessagingException {
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = mailSender.createMimeMessage();
        // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
        // multipart模式
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);

        // 设置收件人，寄件人
        messageHelper.setTo(to);
        messageHelper.setFrom(from);
        messageHelper.setSubject("测试邮件中嵌套图片!！");
        // true 表示启动HTML格式的邮件，
        // 注意：嵌入图片<img src=\"cid:aaa\"/>，其中cid:是固定的写法，而aaa是一个contentId。
        messageHelper.setText(
                "<html><head></head><body><h1>hello!!spring image html mail</h1>"
                        + "<img src=\"cid:aaa\"/></body></html>",
                true);

        FileSystemResource img = new FileSystemResource(new File("F:\\java_workspace\\ZOHO_CRM\\src\\main\\webapp\\pic\\My-Learning-Way.jpg"));

        messageHelper.addInline("aaa", img);
        // 发送邮件
        mailSender.send(mailMessage);
        System.out.println("邮件发送成功..");
    }

    /**
     * 发送附件
     * @param from
     * @param to
     * @param subject
     * @param msg
     * @throws MessagingException
     */
    public void sendMailWihAttachment(String from, String to, String subject, String msg) throws MessagingException {
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = mailSender.createMimeMessage();
        // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
        // multipart模式 为true时发送附件 可以设置html格式
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                true, "utf-8");
        // 设置收件人，寄件人
        messageHelper.setTo(to);
        messageHelper.setFrom(from);
        messageHelper.setSubject("测试邮件中上传附件!！");
        // true 表示启动HTML格式的邮件
        messageHelper.setText(
                "<html><head></head><body><h1>你好：附件中有学习资料！</h1></body></html>",true);

        //TODO 如何获取需要发送的文件
        FileSystemResource file = new FileSystemResource(
                new File("F:\\java_workspace\\ZOHO_CRM\\src\\main\\webapp\\pic\\My-Learning-Way.jpg"));
        // 这里的方法调用和插入图片是不同的。
        messageHelper.addAttachment("My-Learning-Way.jpg", file);

        // 发送邮件
        mailSender.send(mailMessage);
    }
}
