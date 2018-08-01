package com.iwork.core.server.email.tools;

import com.iwork.app.conf.SystemConfig;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;
public class SendMail {
	private static Logger logger = Logger.getLogger(SendMail.class);
	  public SendMail()
	    {
	        mailAccount = "";
	        password = "";
	        props = new Properties();
	        props.put("mail.smtp.host", SystemConfig._mailServerConf.getSmtp_host());
	        props.put("mail.smtp.auth", SystemConfig._mailServerConf.getSmtp_auth());
	        props.put("mail.smtp.port", SystemConfig._mailServerConf.getSmtp_port());
	        props.put("mail.smtp.ssl", SystemConfig._mailServerConf.getSmtp_ssl());
	        mailAccount = SystemConfig._mailServerConf.getMailuser();
	        password = SystemConfig._mailServerConf.getPassword();
	        createMimeMessage();
	    }

	    private boolean createMimeMessage()
	    {
	        try
	        {
	            javax.mail.Authenticator auth = new PopupAuthenticator(mailAccount, password);
	            session = Session.getInstance(props, auth);
	        }
	        catch(Exception e)
	        {
	            System.err.println((new StringBuilder("\u83B7\u53D6\u90AE\u4EF6\u4F1A\u8BDD\u5BF9\u8C61\u65F6\u53D1\u751F\u9519\u8BEF!")).append(e).toString());
	            return false;
	        }
	        try
	        {
	            mimeMsg = new MimeMessage(session);
	            mp = new MimeMultipart();
	        }
	        catch(Exception e)
	        {
	            System.err.println((new StringBuilder("\u521B\u5EFAMIME\u90AE\u4EF6\u5BF9\u8C61\u5931\u8D25!")).append(e).toString());
	            return false;
	        }
	        return true;
	    }

	    public boolean setSubject(String mailSubject)
	    {
	        try
	        {
	            mimeMsg.setSubject(MimeUtility.encodeText(mailSubject, "gb2312", "B"));
	        }
	        catch(Exception e)
	        {
	            System.err.println((new StringBuilder("\u8BBE\u7F6E\u90AE\u4EF6\u4E3B\u9898\u53D1\u751F\u9519\u8BEF!")).append(e).toString());
	            return false;
	        }
	        return true;
	    }

	    public boolean setBody(String mailBody)
	    {
	        try
	        {
	            MimeBodyPart bp = new MimeBodyPart();
	            bp.setContent((new StringBuilder("<meta http-equiv=Content-Type content=text/html;charset=UTF-8>")).append(mailBody).toString(), "text/html;charset=UTF-8");
	            mp.addBodyPart(bp);
	        }
	        catch(Exception e)
	        {
	            System.err.println((new StringBuilder("\u8BBE\u7F6E\u90AE\u4EF6\u6B63\u6587\u65F6\u53D1\u751F\u9519\u8BEF!")).append(e).toString());
	            return false;
	        }
	        return true;
	    }

	    public boolean addFileAffix(String filename)
	    {
	        try
	        {
	            MimeBodyPart bp = new MimeBodyPart();
	            FileDataSource fields = new FileDataSource(filename);
	            bp.setDataHandler(new DataHandler(fields));
	            bp.setFileName(MimeUtility.encodeText(fields.getName(), "GB2312", "B"));
	            mp.addBodyPart(bp);
	        }
	        catch(Exception e)
	        {
	            System.err.println((new StringBuilder("\u589E\u52A0\u90AE\u4EF6\u9644\u4EF6:")).append(filename).append("\u53D1\u751F\u9519\u8BEF").append(e).toString());
	            return false;
	        }
	        return true;
	    }

	    public boolean setFrom(String from)
	    {
	        try
	        {
	            mimeMsg.setFrom(new InternetAddress(from));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setFrom(InternetAddress from)
	    {
	        try
	        {
	            mimeMsg.setFrom(from);
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setFrom(String title, String from)
	    {
	        try
	        {
	            InternetAddress internetaddress = new InternetAddress();
	            internetaddress.setAddress(from);
	            internetaddress.setPersonal(MimeUtility.encodeText(title, "gb2312", "B"));
	            mimeMsg.setFrom(internetaddress);
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setTo(String to)
	    {
	        if(to == null)
	            return false;
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setTo(String toList[])
	    {
	        if(toList == null)
	            return false;
	        StringBuffer strbuf = new StringBuffer();
	        String as[];
	        int j = (as = toList).length;
	        for(int i = 0; i < j; i++)
	        {
	            String str = as[i];
	            strbuf.append(", ").append(str);
	        }

	        String to_list = strbuf.deleteCharAt(0).toString();
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to_list));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setCC(String ccto)
	    {
	        if(ccto == null)
	            return false;
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(ccto));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setCC(String ccto[])
	    {
	        if(ccto == null)
	            return false;
	        StringBuffer strbuf = new StringBuffer();
	        String as[];
	        int j = (as = ccto).length;
	        for(int i = 0; i < j; i++)
	        {
	            String str = as[i];
	            strbuf.append(", ").append(str);
	        }

	        String to_list = strbuf.deleteCharAt(0).toString();
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(to_list));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setBCC(String copyto)
	    {
	        if(copyto == null)
	            return false;
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(copyto));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setBCC(String ccto[])
	    {
	        if(ccto == null)
	            return false;
	        StringBuffer strbuf = new StringBuffer();
	        String as[];
	        int j = (as = ccto).length;
	        for(int i = 0; i < j; i++)
	        {
	            String str = as[i];
	            strbuf.append(", ").append(str);
	        }

	        String to_list = strbuf.deleteCharAt(0).toString();
	        try
	        {
	            mimeMsg.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(to_list));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setReplyTo(String replyTo)
	    {
	        if(replyTo == null)
	            return false;
	        try
	        {
	            mimeMsg.setReplyTo(InternetAddress.parse(replyTo));
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setNotification(String notification)
	    {
	        if(notification == null)
	            return false;
	        try
	        {
	            mimeMsg.addHeader("Disposition-Notification-To", notification);
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean setPriority(String priority)
	    {
	        if(priority == null)
	            return false;
	        try
	        {
	            mimeMsg.addHeader("X-Priority", priority);
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	        return true;
	    }

	    public boolean send()
	    {
	        boolean flag = false;
	        try
	        {
	            mimeMsg.setContent(mp);
	            mimeMsg.saveChanges();
	            mimeMsg.setSentDate(new Date());
	            mimeMsg.addHeader("X-Mailer", ",Core Version=HiBPM");
	            String language[] = {
	                "GBK", "GB2312", "UTF-8"
	            };
	            mimeMsg.setContentLanguage(language);
	            Transport transport = session.getTransport("smtp");
	            String smtp = (String)props.get("mail.smtp.host");
	            transport.connect(smtp, mailAccount, password);
	            transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
	            transport.close();
	            flag = true;
	        }
	        catch(Exception e)
	        {
	            flag = false;
	        }
	        return flag;
	    }

	    private MimeMessage mimeMsg;
	    private Properties props;
	    private String mailAccount;
	    private String password;
	    private Multipart mp;
	    private Session session;
	}
