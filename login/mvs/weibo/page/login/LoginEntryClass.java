//重要！
package mvs.weibo.page.login;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpClient;

import rsaLogin.LoginRSA;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.IDDatatypeValidator;

import DBO.DBAccess;

import mvs.weibo.page.parser.IdParser;
import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.service.ServiceFlag;
import mvs.weibo.page.service.id.IDService;
import mvs.weibo.page.service.status.StatusServiceImpl;
import mvs.weibo.page.spring.SpringStart;
import mvs.weibo.page.util.pool.WeiboCookiesManager;
import mvs.weibo.page.view.MainCaller;
import mvs.weibo.page.vo.status.HibernateStatus;
import mvs.weibo.page.vo.user.HibernateUser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginEntryClass {
	private WeiboCookiesManager weiboCookiesManager = null;
	private LoginRSA loginPostMethod;
	private VerificationFrame verificationFrame;
	private static String usernameString =  "buptsselab110@sina.com".trim();
	private static String passwordString =  "buptsse".trim();
	private String verificationString;
	private String pcid;
	private String nonce;
	private String servertime;
	public static List<String> listId;
	public static int PageCount = 10;
	public static int sleepTime = 60;
	
	public LoginEntryClass(List<String> ls) {
		this.setMode();
		listId = ls;
		System.out.println("模式："+ServiceFlag.mode);
		this.loginPostMethod = new LoginPostWithoutVerification();
		
		System.out.println("读取application context...");
		this.weiboCookiesManager = ((WeiboCookiesManager) SpringStart
				.getApplicationContext().getBean("weiboCookiesManager"));
		System.out.println("application context读取完成！");
		for(String id : ls){
			ServiceFlag.flag.put(id, 1);
		}
	}
	
	public void setMode(){
		Properties prop = new Properties();
		
		try {
			InputStream in = new BufferedInputStream (new FileInputStream("config.properties"));
			prop.load(in);
			if(prop.getProperty("mode").equals("true")){
				ServiceFlag.mode = true;
			}else{
				ServiceFlag.mode = false;
			}
			
			this.usernameString = prop.getProperty("username").trim();
			this.passwordString = prop.getProperty("password").trim();
			sleepTime = Integer.parseInt(prop.getProperty("sleepTime"));//提取间隔时间
			PageCount =  Integer.parseInt(prop.getProperty("PageCount"));//提取爬去主页页数
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeCrawler(){
		ServiceFlag.closeCrawler();
	}
	public String getNonce() {
		return this.nonce;
	}

	public String getPcid() {
		return this.pcid;
	}
	
	public HibernateUser getUserInfoById(String uid){
		return IDService.userInfoService.getUserInfoById(uid);
	}
	
	public List<String> getUserFriends(String uid){
		return IDService.getUserFriends(uid);
	}

	public String getServertime() {
		return this.servertime;
	}

	public String getUsernameString() {
		return this.usernameString;
	}

	public String getVerificationString() {
		return this.verificationString;
	}

	public void crawStatus(){
		sleepTime *= 1000;//毫秒扩展成秒
		new MainCaller(this.usernameString, listId);
	}
	
	public void crawUserInfos(){
		 ExecutorService service = Executors.newFixedThreadPool(20);
		 final DBAccess dba = new DBAccess();
		 
		 for(int i = 0; i< listId.size(); i++){
			 final int fi = i;
			 Runnable writeUserInfo  = new Runnable(){

				@Override
				public void run() {
					HibernateUser hu=getUserInfoById(listId.get(fi));
					System.out.println(hu.getScreenName());
					//write to db
					dba.userinfoCrawlerForParticularID(hu);
				}
				 
			 };
			 service.execute(writeUserInfo);
		 }
		 
		 service.shutdown();
		 
		
//		int maxThreadNum = 20;
//		int listLength = listId.size();
//		int i = -1;
//		while(i < listLength-1){
//			if(maxThreadNum > 0){
//				i ++;
//				maxThreadNum --;
//				
//				
//				
//				maxThreadNum ++;
//			}else{
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
	}
	
	public void loginEntry() {
		int retcode = -1;

		System.out.println("验证用户...");
		retcode = this.loginPostMethod.post(this.usernameString,
				this.passwordString);
		if (retcode == 101) {
			System.out.println("用户名密码错误，请重新输入。");
		} else if (retcode == 4049) {
			System.out.println("需要验证码");
			this.loginPostMethod = new LoginPostWithVerification(this);
			this.verificationFrame = new VerificationFrame(this);
		} else if (retcode == 0) {
			System.out.println("登陆成功");

	//		new BaseService();
		//	String text = BaseService.getPageContentFromUrl("http://weibo.com/1191979833/follow", "");
			
//			new MainCaller(this.usernameString,listId);// 打开主页面，传送参数用户名
//			System.out.println(new IdParser().getIDFromText(text));
//			System.out.println(text);
//			System.out.println(new IdParser().getPageFromText(text));
			
		} else if (retcode == 2070) {
			System.out.println("验证码错误");
			this.verificationFrame = new VerificationFrame(this);
			System.out.println("验证码错误，重新输入");
			JOptionPane.showMessageDialog(this.verificationFrame,
					"验证码输入错误，请重新输入");
		}else if(retcode == 4403){
			System.out.println("发生了一个未经处理的错误。");
		} else {
			System.out.println("未知错误,请检查网络" + retcode);
		}
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public void setPcid(String pcid) {
		this.pcid = pcid;
	}

	public void setServertime(String servertime) {
		this.servertime = servertime;
	}

	public void setVerificationString(String verificationString) {
		this.verificationString = verificationString;
	}
	


	public static void main(String[] args) {
		System.out.println("MVS微博爬虫");
		List<String> listUserID=new ArrayList<String>();
		listUserID.add("2360287147");
		LoginEntryClass lclass = new LoginEntryClass(listUserID);
		lclass.loginEntry();
		lclass.crawStatus();
		
		//System.out.println(IDService.getUserFriends("1687009790").size()); 
	//	ServiceFlag.running = false;
	}
}
