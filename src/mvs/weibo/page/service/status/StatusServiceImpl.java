package mvs.weibo.page.service.status;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import mvs.weibo.page.login.LoginEntryClass;
import mvs.weibo.page.parser.StatusParser;
import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.service.ServiceFlag;
import mvs.weibo.page.service.user.IUserInfoService;
import mvs.weibo.page.vo.status.HibernateStatus;
import mvs.weibo.page.vo.user.HibernateUser;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import DBO.DBAccess;

import java.sql.*;

public class StatusServiceImpl extends BaseService implements IStatusService
{
	private static Logger logger = Logger.getLogger(StatusServiceImpl.class);
	private DBAccess db;
	private IUserInfoService userInfoService = null;
	private StatusParser statusParser = null;
	private int total = 0;
	private volatile int current = 0;
	private int currentShowPage = 1;


	public StatusServiceImpl()
	{
		super();
		db = new DBAccess();
	}

	public int getProcessNum()
	{
		if (this.total == 0)
		{
			return 1;
		}
		int rtn = this.current * 1000 / this.total;
		return rtn;
	}

	public List<HibernateStatus> getStatusListByUid(String name)
	{
		this.total = 0;
		this.current = 0;
		String url = "";
		try
		{
			url = "http://weibo.com/u/" + URLEncoder.encode(name, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		List<HibernateStatus> rtn = getStatusListByUrl(url);
		return rtn;
	}

	private List<HibernateStatus> getStatusListByUrl(String url)
	{

		HibernateUser hUser = this.userInfoService.getUserInfoByUrl(url);
//		System.out.println("user id为" + hUser.getScreenName() + " " + hUser.getUserID());

		// 开始爬取
		List<HibernateStatus> rtn;
		rtn = getAllStatusListAlternative(null);
//		final List finalHibernateStatus = rtn;
//		final long end = System.currentTimeMillis();

		return rtn;
	}

	private List<HibernateStatus> getStatusList(int fi, String urlString)
	{
		String resourceString = StatusServiceImpl.getPageContentFromUrl(
				urlString, "");
		List<HibernateStatus> statusList;
		if ("".equals(resourceString))
		{
			statusList = new ArrayList<HibernateStatus>();
		}
		else
		{
			statusList = StatusServiceImpl.this
					.getStatusListFromJson(resourceString);
		}
		return statusList;
	}

	private String generateURL(int fi, long userID)
	{
		int page = (fi + 2) / 3;
		int pre_page = (fi + 1) / 3;
		int pagebar = fi % 3 == 0 ? 1 : 0;
		String urlString = "http://weibo.com/aj/mblog/mbloglist?page=" + page
				+ "&count=15&pre_page=" + pre_page + "&pagebar=" + pagebar
				+ "&uid=" + userID + "&_t=0";
//		String urlString = "http://weibo.com/u/2360287147?wvr=5&sudaref=login.sina.com.cn&page=4&pre_page=3&end_id=3556226358950392&end_msign=-1";
		return urlString;
	}

	private List<HibernateStatus> getAllStatusListAlternative(
			final HibernateUser hUser)
	{
		int pageTotal = LoginEntryClass.PageCount;
		total = pageTotal;
		@SuppressWarnings("rawtypes")
		final List<List> statusIndexList = Collections.synchronizedList(new ArrayList<List>(pageTotal));
		for (int i = 0; i < pageTotal; i++)
		{
			statusIndexList.add(null);
		}
		
		List<HibernateStatus> allStatusList = new ArrayList<HibernateStatus>();
		final CountDownLatch threadsSignal = new CountDownLatch(pageTotal);
		String url = "";
		List<HibernateStatus> currentCrawlList = new ArrayList<HibernateStatus>(45);
		for (int i = 6; i <= pageTotal; i++)
		{
			currentCrawlList.clear();
			for(int j = 1; j <= 3; j++)
			{
				switch(j)
				{
					case 1 : url = "http://weibo.com/aj/mblog/fsearch?_wv=5&page=" + i + "&count=15&pre_page=" + (i-1) + "&pagebar=0";break;
					case 2 : url = "http://weibo.com/aj/mblog/fsearch?_wv=5&page=" + i + "&count=15&pre_page=" + i + "&pagebar=0";break;
					case 3 : url = "http://weibo.com/aj/mblog/fsearch?_wv=5&page=" + i + "&count=15&pre_page=" + i + "&pagebar=1";break;
				}
				currentCrawlList.addAll(getStatusList(i, url));// 这个是通过userId爬去微博的入口
			}
			statusIndexList.set(i - 1, currentCrawlList);
			StatusServiceImpl.this.showStatusListByPage(i,statusIndexList,Long.valueOf("1638306795"));
			threadsSignal.countDown();
			current++;
			System.out.println("第" + i +"页导出-------------");
		}
		try
		{
			threadsSignal.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		this.currentShowPage = 1;
		this.current = 0;
		List<HibernateStatus> hibernateStatusList;
		for (@SuppressWarnings("rawtypes")
		Iterator<List> iterator = statusIndexList.iterator(); iterator.hasNext(); allStatusList
				.addAll(hibernateStatusList))
		{
			hibernateStatusList = (List<HibernateStatus>) iterator.next();
		}
		return allStatusList;
	}

	private List<HibernateStatus> getAllStatusList(final HibernateUser hUser)
	{
		int pageTotal = (int) Math
				.ceil((double) hUser.getStatusesCount() / 15D);
		total = pageTotal;
		final List<List> statusIndexList = Collections
				.synchronizedList(new ArrayList<List>(pageTotal));
		for (int i = 0; i < pageTotal; i++)
		{
			statusIndexList.add(null);
		}
		List<HibernateStatus> allStatusList = new ArrayList<HibernateStatus>();

		final CountDownLatch threadsSignal = new CountDownLatch(pageTotal);
		for (int i = 1; i <= pageTotal; i++)
		{

			final int fi = i;
			Runnable r = new Runnable()
			{

				public void run()
				{
					crawl();
					threadsSignal.countDown();
					current++;
				}

				private void crawl()
				{
					System.out.println("in crawler for page " + fi
							+ ", for uid:" + hUser.getUserID());
					List<?> currentCrawlList = getStatusList(fi, hUser.getUserID());// 这个是通过userId爬去微博的入口
					// List currentCrawlList = getStatusList(fi,1291352405);
					statusIndexList.set(fi - 1, currentCrawlList);
					StatusServiceImpl.this.showStatusListByPage(fi,
							statusIndexList, hUser.getUserID());
				}

				private List<HibernateStatus> getStatusList(int fi, long userID)
				{
					String urlString = generateURL(fi, userID);
					String resourceString = StatusServiceImpl
							.getPageContentFromUrl(urlString, "");
					List<HibernateStatus> statusList;
					if ("".equals(resourceString))
					{
						statusList = new ArrayList<HibernateStatus>();
					}
					else
					{
						statusList = StatusServiceImpl.this
								.getStatusListFromJson(resourceString);
					}
					return statusList;
				}

				private String generateURL(int fi, long userID)
				{
					int page = (fi + 2) / 3;
					int pre_page = (fi + 1) / 3;
					int pagebar = fi % 3 == 0 ? 1 : 0;
					String urlString = "http://weibo.com/aj/mblog/mbloglist?page="
							+ page
							+ "&count=15&pre_page="
							+ pre_page
							+ "&pagebar="
							+ pagebar
							+ "&uid="
							+ userID
							+ "&_t=0";
					return urlString;
				}
			};
			if (ServiceFlag.flag.get(String.valueOf(hUser.getUserID())) == 1)
			{
				execute(r);
				// try {
				// Thread.sleep(10000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }//通过等待的方法
			}
			else
			{
				System.out.println("有重复，跳出");
				break;
			}
		}
		try
		{
			threadsSignal.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		this.currentShowPage = 1;
		this.current = 0;
		List<HibernateStatus> hibernateStatusList;
		for (Iterator<List> iterator = statusIndexList.iterator(); iterator.hasNext(); allStatusList
				.addAll(hibernateStatusList))
		{
			hibernateStatusList = (List<HibernateStatus>) iterator.next();
		}
		return allStatusList;
	}

	private synchronized void showStatusListByPage(int fi, List<List> statusIndexList, long userID)
	{
		try
		{

			if (fi == this.currentShowPage)
				do
				{
					final StringBuilder stringBuilder = new StringBuilder();

					Iterator<?> localIterator = ((List<?>) statusIndexList
							.get(this.currentShowPage - 1)).iterator();

					while (localIterator.hasNext())
					{
						HibernateStatus hibernateStatus = (HibernateStatus) localIterator
								.next();

//						System.out.println(hibernateStatus.getCreatedAtStr());
//						System.out.println(hibernateStatus.getRepostsCount());// 转发
//						System.out.println(hibernateStatus.getCommentsCount());// 评论
//						System.out.println(hibernateStatus.getId() + "  "
//								+ hibernateStatus.getText());
						db.statusCrawlerForParticularID(hibernateStatus);

					}
					if (this.currentShowPage % 3 == 0)
						stringBuilder.append("第 [")
								.append(this.currentShowPage / 3)
								.append("] 页\r\n");
					else if (this.currentShowPage == statusIndexList.size())
					{
						stringBuilder.append("第 [")
								.append(this.currentShowPage / 3 + 1)
								.append("] 页\r\n");
					}

					this.currentShowPage += 1;
				} while ((this.currentShowPage <= statusIndexList.size())
						&& (statusIndexList.get(this.currentShowPage - 1) != null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private List<HibernateStatus> getStatusListFromJson(String json)
	{
		List<HibernateStatus> rtn = this.statusParser.getListFromText(json);
		return rtn;
	}
	
	public IUserInfoService getUserInfoService()
	{
		return this.userInfoService;
	}

	public void setUserInfoService(IUserInfoService userInfoService)
	{
		this.userInfoService = userInfoService;
	}

	public StatusParser getStatusParser()
	{
		return this.statusParser;
	}

	public void setStatusParser(StatusParser statusParser)
	{
		this.statusParser = statusParser;
	}
}
