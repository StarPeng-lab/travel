package cn.itcast.travel.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 发邮件工具类
 * 这里用到的电子邮件协议是 SMTP ，即 Simple Mail Transfer Protocol 简单邮件传输协议
 */
public final class MailUtils {
    private static final String USER = ""; // 发件人称号，同邮箱地址
    private static final String PASSWORD = ""; // 如果是qq邮箱可以使用登录密码，如果是163邮箱需要开启客户端授权码

    /**
     *
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text, String title){
        try {
            //1、创建一封邮件
            //1.1、用于连接邮件服务器的参数配置（发送邮件时才需要用到）
            final Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
            props.put("mail.smtp.auth", "true"); // 需要请求认证
            props.put("mail.smtp.host", "smtp.qq.com"); //设置协议主机，qq邮箱的 SMTP 服务器地址为: smtp.qq.com

            props.put("mail.user", USER); // 发件人的账号
            props.put("mail.password", PASSWORD); //发件人的密码

            //1.2、构建授权信息，用于进行SMTP进行身份验证
            //在真正使用创建的过程中，往往会让我们验证密码，这是我们要写一个密码验证类。javax.mail.Authenticator是一个抽象类，我们要写MyAuthenticator的密码验证类，该类继承Authenticator实现：
            //这里直接用匿名内部类实现
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            //1.3、使用环境属性和授权信息，创建邮件会话对象
            Session mailSession = Session.getInstance(props, authenticator);
            //1.4、创建邮件消息--创建一个邮件对象（MimeMessage）
            MimeMessage message = new MimeMessage(mailSession);

            //或者根据已有的email邮件文件创建 MimeMessage 对象
            //MimeMessage message = new MimeMessage(session, new FileInputStream("myEmail.eml"));

            //2、设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress from = new InternetAddress(username); //new InternetAddress("email@send.com", "显示昵称", "昵称的编码UTF-8")
            message.setFrom(from);

            //3、设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress); //To: 增加收件人（可选）；CC: 抄送（可选）；Bcc: 密送（可选）

            //4、设置邮件标题
            message.setSubject(title); //message.setSubject("邮件主题", "UTF-8");

            //5、设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");

            //6、设置显示的发件时间
            message.setSentDate(new Date());

            //7、保存设置
            message.saveChanges();

            //8、发送邮件
            Transport.send(message);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception { // 做测试用
        MailUtils.sendMail("","你好，这是一封测试邮件，无需回复。","测试邮件");
        System.out.println("发送成功");
    }



}
