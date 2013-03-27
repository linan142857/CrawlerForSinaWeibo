package mvs.weibo.page.service.id;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import mvs.weibo.page.login.LoginEntryClass;
import mvs.weibo.page.parser.IdParser;
import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.service.user.IUserInfoService;
import mvs.weibo.page.service.user.UserInfoServiceImpl;
import mvs.weibo.page.vo.user.HibernateUser;


public class IDService extends BaseService {
	public static UserInfoServiceImpl userInfoService = new UserInfoServiceImpl();
	/**
	 * 返回用户id，要求手工登录并退出
	 * 
	 * 
	 * LoginEntryClass lclass = new LoginEntryClass(null);
		lclass.loginEntry();
		....
		lclass.closeCrawler();
	 * */
	public static List<String> getUserFriends(final String uid) {
		userInfoService = new UserInfoServiceImpl();
		String url;
		try {
			url = "http://weibo.com/u/" + URLEncoder.encode(uid, "utf-8");
			HibernateUser hUser = userInfoService.getUserInfoByUrl(url);
			System.out.println("user id为" + hUser.getUserID());

			if (hUser.getUserID() == 0L) {
				final long end = System.currentTimeMillis();

				System.out.println("用户基本信息获取失败，请检查用户名是否输入正确，或者网络环境是否正常，并重新爬取。");
				return null;
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		final List<String> userIDs= new ArrayList<String>();
		String text = BaseService.getPageContentFromUrl("http://weibo.com/"+uid+"/follow", "");
	//	System.out.println();
		userIDs.addAll(IdParser.getIDFromText(text));
		
		int numOfPage=0;
		
		numOfPage = Integer.parseInt(IdParser.getPageFromText(text));
		
		final CountDownLatch threadsSignal = new CountDownLatch(numOfPage - 1);
		List<Thread> ts = new ArrayList<Thread>();
		for(int i=2;i<=numOfPage;i++)
		{
			final int fi = i;
			Runnable r = new Runnable() {

				public void run() {
					crawl();
					threadsSignal.countDown();
				}

				private void crawl() {
					String text = BaseService.getPageContentFromUrl("http://weibo.com/"+uid+"/follow?page="+fi, "");
					userIDs.addAll(IdParser.getIDFromText(text));
			//		System.out.println("page"+fi);
				}
		
			};
			
			Thread t = new Thread(r);
			ts.add(t);
			t.start();
		}
		
		for(Thread t : ts){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userIDs;
	}
	
	
	
}
