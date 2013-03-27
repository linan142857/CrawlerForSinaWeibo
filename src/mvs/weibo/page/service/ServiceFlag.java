package mvs.weibo.page.service;

import java.util.HashMap;

public class ServiceFlag {
	public static HashMap<String, Integer> flag = new HashMap<String, Integer>();//1表示用户还没爬，0表示爬过了
	public static boolean mode = false;//是否增量式
	public static boolean running = true;//true表示程序可以运行，false表示程序将在一定时间后结束
	
	public static void closeCrawler(){
		ServiceFlag.running = false;
		System.out.println("爬虫即将结束，请等待。");
	}
}
