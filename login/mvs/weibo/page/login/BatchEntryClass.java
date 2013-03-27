package mvs.weibo.page.login;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import DBO.DBAccess;
import File.UserIdBatch;

import mvs.weibo.page.service.ServiceFlag;

public class BatchEntryClass {
	
	
	public static void main(String[] args){
		UserIdBatch userIdBatch = new UserIdBatch("allUsersFreq.txt");//填写用户id文件位置，文件格式为每行一个id
		
		List<String> listUserID= userIdBatch.getAllUserID();
		System.out.println("读取用户id文件成功");
		LoginEntryClass lclass = new LoginEntryClass(listUserID);
		lclass.loginEntry();
		lclass.crawStatus();
	}
	
}
