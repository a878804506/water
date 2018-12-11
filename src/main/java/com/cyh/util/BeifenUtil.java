package com.cyh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.cyh.exception.MyException;

public class BeifenUtil {
		//mysql数据库的备份操作
		public static void beifenMySQL(String path,String pathMysqldump,String name) throws MyException,IOException{
		    Runtime runtime = Runtime.getRuntime();
			Properties pro = System.getProperties();
			String osName = pro.getProperty("os.name");//获得当前操作系统的名称
		    if("linux".equalsIgnoreCase(osName)){
				try{
				    //String cmd=  pathMysqldump+"/mysqldump -uroot  -proot water >"+path+"/"+name+".sql";
					String[] cmd=new String[]{"/bin/sh","-c",pathMysqldump+"/mysqldump -uroot  -proot water >"+path+"/"+name+".sql"};
					runtime.exec(cmd);
				}catch(Exception e){
					e.printStackTrace();
					throw new MyException("linux下备份出错！");
				}
			}else{
				//-u后面是用户名，-p是密码-p后面最好不要有空格，-water是数据库的名字
				Process process;
				try {
					process = runtime.exec("cmd /c "+pathMysqldump+"mysqldump -uroot -proot water");
				} catch (IOException e1) {
					throw new MyException("执行备份的cmd语句时出错！");
				}
				InputStream inputStream = process.getInputStream();//得到输入流，写成.sql文件
				InputStreamReader reader = new InputStreamReader(inputStream,"utf8");

				String inStr;
				StringBuffer sb = new StringBuffer("");
				String outStr;
				BufferedReader br = null;
				OutputStreamWriter writer = null;
				FileOutputStream fout = null;
				try {
					br = new BufferedReader(reader);
					while ((inStr = br.readLine()) != null) {
						sb.append(inStr + "\r\n");
					}
					outStr = sb.toString();
					fout = new FileOutputStream(path+name);
					writer = new OutputStreamWriter(fout, "utf8");
					writer.write(outStr);
					writer.flush();

					inputStream.close();
					reader.close();
					br.close();
					writer.close();
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(inputStream != null){
						inputStream.close();
					}
					if(reader != null){
						reader.close();
					}
					if(br != null){
						br.close();
					}
					if(writer != null){
						writer.close();
					}
					if(fout != null){
						fout.close();
					}
				}
			}
		}
		//发送邮件
		/*
		 *     beifenTime    备份操作时间
		 *     file					文件名称
		 *     path					文件所在的物理地址
		 */
		public static void sendMail(String beifenTime,String file,String path) throws MyException, MessagingException{  
			Transport transport = null;
	        try {  
	            String smtpFromMail = "878804506@qq.com";  //账号  
	            String pwd = "pcuogfplvpeubbah"; //密码  
	            int port = 25; //端口  
	            String host = "smtp.qq.com"; //端口  
	            Properties props = new Properties();  
	            props.put("mail.smtp.host", host);  
	            props.put("mail.smtp.auth", "true");  
	            props.setProperty("mail.smtp.starttls.enable", "true");
	            Session session = Session.getDefaultInstance(props);  
	            session.setDebug(false);  
	            MimeMessage message = new MimeMessage(session);  
	            message.setFrom(new InternetAddress(smtpFromMail, "QQ邮件测试"));  
	            message.addRecipient(Message.RecipientType.TO,new InternetAddress("462375001@qq.com"));  
	            message.setSubject("MySQL网络备份");  //标题
	            message.addHeader("charset", "UTF-8");  
	            /*添加正文内容*/  
	            Multipart multipart = new MimeMultipart();  
	            BodyPart contentPart = new MimeBodyPart();  
	            contentPart.setText("备份操作时间：" +beifenTime);  //正文
	            contentPart.setHeader("Content-Type", "text/html; charset=utf-8");  
	            multipart.addBodyPart(contentPart);  
	            /*添加附件  */
	            File usFile = new File(path + file);  
	            MimeBodyPart fileBody = new MimeBodyPart();  
	            DataSource source = new FileDataSource(path + file);  
	            fileBody.setDataHandler(new DataHandler(source));  
//	            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
//	            fileBody.setFileName("=?GBK?B?"  + enc.encode(usFile.getName().getBytes()) + "?=");  
	            multipart.addBodyPart(fileBody);  
	            message.setContent(multipart);  
	            message.setSentDate(new Date());  
	            message.saveChanges();  
	            transport = session.getTransport("smtp");  
	            transport.connect(host, port, smtpFromMail, pwd);  
	            transport.sendMessage(message, message.getAllRecipients());  
	        } catch (Exception e) {  
	        	e.printStackTrace();
	            throw new MyException("邮件发送失败，请注意！");
	        }finally{
	        	if(transport != null){
	        		transport.close();
	        	}
            }
	    }  
}
