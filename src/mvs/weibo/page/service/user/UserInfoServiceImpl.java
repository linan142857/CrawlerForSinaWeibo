package mvs.weibo.page.service.user;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.sound.sampled.AudioFormat.Encoding;

import mvs.weibo.page.parser.UserInfoParser;
import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.util.html.HtmlParserUtil;
import mvs.weibo.page.util.html.UnicodeUtf8;
import mvs.weibo.page.vo.user.HibernateUser;

import org.apache.log4j.Logger;

public class UserInfoServiceImpl extends BaseService implements
		IUserInfoService {
	private static Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

	private static UserInfoParser userInfoParser = null;

	public UserInfoParser getUserInfoParser() {
		return this.userInfoParser;
	}

	public HibernateUser getUserInfoById(String uid) {
		String url = "";
		// try {
		// url = "http://weibo.com/u/" + URLEncoder.encode(uid, "utf-8");
		url = "http://www.weibo.com/" + uid + "/info";
		// } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return getUserInfoByUrl(url);
	}

	public void setUserInfoParser(UserInfoParser userInfoParser) {
		this.userInfoParser = userInfoParser;
	}

	public HibernateUser getUserInfoByUrl(String url) {
		String pageContent = getPageContentFromUrl(url, "");
//		 try {
//		 PrintWriter writer = new PrintWriter("Content3.txt");
//		 writer.println(pageContent);
//		 writer.close();
//		 } catch (FileNotFoundException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
		HibernateUser hu = getUserInfoByHtml(pageContent);
		return hu;
	}
	
	public String getUserInfoByUrl2(String url) {
		String pageContent = getPageContentFromUrl(url, "");
		return pageContent;
	}

	private static HibernateUser getUserInfoByHtml(String text) {
		HibernateUser hu = null;
		try {
			if (text.contains("<iframe onload=\"clearLoading()\"")) {
				String url = HtmlParserUtil.getSpecSubStrFromStr(text,
						"<iframe onload=\"clearLoading()\" src=\"", "\"");
				text = getPageContentFromUrl(url, "");
			}
			hu = userInfoParser.getUserInfo(text);
		} catch (Exception e) {
			System.out.println("获得用户详细信息产生错误");
		}
		return hu;
	}
}
