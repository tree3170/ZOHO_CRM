/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   SingleMailSend.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package mail;

/**
 * 参考：http://blog.csdn.net/is_zhoufeng/article/details/12004073
 * Description：ZOHO_CRM
 * Created on  2016/10/09 11：19
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        11：19   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 本类测试简单邮件 直接用邮件发送
 *
 * @author Administrator
 *
 */
public class SingleMailSend
{
    public static void main(String args[])
    {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server
        //163
        senderImpl.setHost("smtp.163.com");
        senderImpl.setPort(25);
        //google
        //senderImpl.setHost("smtp.gmail.com");
        //senderImpl.setPort(587);
        // 建立邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo("317035791@qq.com");
        mailMessage.setFrom("tree317035791@163.com");
        //mailMessage.setFrom("tree3170@gmail.com");
        mailMessage.setSubject(" 测试简单文本邮件发送！ ");
        mailMessage.setText(" 测试我的简单邮件发送机制！！ ");

        senderImpl.setUsername("tree317035791@163.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("zaq1@WSX_163"); // 根据自己的情况, 设置password

        //senderImpl.setUsername("tree3170@gmail.com"); // 根据自己的情况,设置username
        //senderImpl.setPassword("a283287=Gmail"); // 根据自己的情况, 设置password

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", " true "); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", " 25000 ");
        ////for gmail,如果不设置这个，会抛出com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first. y126sm24456304pfy.41 - gsmtp
        prop.put("mail.smtp.starttls.enable","true");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println(" 邮件发送成功.. ");
    }
}
