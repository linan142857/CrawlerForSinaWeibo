import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import mvs.weibo.page.login.LoginEntryClass;


public class CrawStatus {
	public static void main(String[] args) throws ParseException{
		System.out.println("MVS微博爬虫");
		List<String> listUserID=new ArrayList<String>(1);
		LoginEntryClass lclass = new LoginEntryClass(listUserID);//listUserID无用  因为爬的是登陆账户的主页
		lclass.loginEntry();//登陆
		lclass.crawStatus();//爬取... MainCaller中循环
	}
	
	
}
