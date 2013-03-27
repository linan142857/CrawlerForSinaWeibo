package mvs.weibo.page.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.spring.SpringStart;
import mvs.weibo.page.util.pool.WeiboCookiesManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

abstract class LoginPostMethod {
	private EncodePassword encodePassword;
	private WeiboCookiesManager weiboCookiesManager;
	private HttpClient client;
	protected UTF8PostMethod postMethod;
	private Map<String, String> cookie = null;

	public static String getPreLoginInfo()
			throws ParseException, IOException {
		String preloginurl = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su="
				+ "dW5kZWZpbmVk"
				+ "&rsakt=mod&client=ssologin.js(v1.4.5)"
				+ "&_=" + getCurrentTime();
		String json = BaseService.getPageContentFromUrl(preloginurl, "");
	
		
		return json;

	}
	private static String getCurrentTime() {
		long servertime = new Date().getTime() / 1000;
		return String.valueOf(servertime);
	}
	public LoginPostMethod() {
		this.encodePassword = new EncodePassword();
		this.cookie = new HashMap();
		this.client = new HttpClient();
		this.weiboCookiesManager = ((WeiboCookiesManager) SpringStart
				.getApplicationContext().getBean("weiboCookiesManager"));
	}

	int post(String usernameString, String passwordString) {
		System.out.println("向服务器传递用户名密码进行登陆……");
		String serverTime = "";
		String nonce = "";

		try {
			String json = getPreLoginInfo();
			
			String regEx = "nonce\":\"\\w{6}\"";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(json);
			if (m.find()) {
				nonce =  m.group().replace("nonce\":\"", "").replace("\"", "");		
				System.out.println("nonce:" +nonce);
			}
			
			String regEx1 = "servertime\":\\d{10},";
			Pattern p1 = Pattern.compile(regEx1);
			Matcher m1 = p1.matcher(json);
			if (m1.find()) {
				serverTime =  m1.group().replace("servertime\":", "").replace(",", "");		
				System.out.println("serverTime:"+serverTime);
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// String encodePW = this.encodePassword.encodePW(passwordString,
		// serverTime, nonce);
		String encodePW = this.encodePassword.encodePW2(passwordString,serverTime, nonce);
		try {
			usernameString = usernameString.replaceFirst("@", "%40");//注意！
			usernameString = URLEncoder.encode(usernameString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("对name进行url编码错误");
		}
		byte[] userNameBytes = usernameString.getBytes();
		String encodeName = new String(Base64.encodeBase64(userNameBytes));

		String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)";
		this.postMethod = new UTF8PostMethod(url);
		this.postMethod.setRequestHeader("cookie",this.weiboCookiesManager.getCookie());
		
		this.postMethod.addParameter("encoding", "utf-8");
		this.postMethod.addParameter("entry", "weibo");
		this.postMethod.addParameter("from", "");
		this.postMethod.addParameter("gateway", "1");
		this.postMethod.addParameter("nonce", nonce);
		this.postMethod.addParameter("pagerefer", "");
		// this.postMethod.addParameter("pwencode", "wsse");
		this.postMethod.addParameter("prelt", "151");
		this.postMethod.addParameter("pwencode", "ras2");

		this.postMethod.addParameter("returntype", "META");
		this.postMethod.addParameter("rsakv", "1330428213");
		this.postMethod.addParameter("savestate", "7");
		this.postMethod.addParameter("servertime", serverTime);
		this.postMethod.addParameter("service", "miniblog");
		this.postMethod.addParameter("sp", encodePW);
	//	this.postMethod.addParameter("ssosimplelogin", "1");
		this.postMethod.addParameter("su", encodeName);
		this.postMethod
				.addParameter(
						"url",
						"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
		this.postMethod.addParameter("useticket", "1");
		this.postMethod.addParameter("vsnf", "1");
//s		this.postMethod.addParameter("vsnval", "");
		hookForAddingVerification();

		int rtn = -1;
		try {
			int returnCode = this.client.executeMethod(this.postMethod);
			if (returnCode == 200) {
				String pageContent = this.postMethod.getResponseBodyAsString();
				// logger.debug(pageContent);
				if (pageContent.contains("retcode=4049"))
					return 4049;
				if (pageContent.contains("retcode=101"))
					return 101;
				if (pageContent.contains("retcode=2070"))
					return 2070;
				if (pageContent.contains("retcode=0"))
					rtn = 0;
				if (pageContent.contains("retcode=4403"))
					rtn = 4403;
				else {
					System.out.println(pageContent);
					return rtn;
				}
				makeCookies(this.postMethod);
				String cookie = methodWithCookie();
				this.weiboCookiesManager.addCookie(cookie);
			}
		} catch (Exception e) {

			System.out.println("密码登陆出错");
		}
		return rtn;
	}

	void hookForAddingVerification() {
	}

	private String generateNonce() {
		String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String nonce = "";
		for (int i = 0; i < 6; i++) {
			nonce = nonce
					+ x.charAt((int) Math.ceil(Math.random() * 1000000.0D)
							% x.length());
		}
		return nonce;
	}

	private void makeCookies2(HttpPost httpPost){
		
	}
	
	private void makeCookies(HttpMethod method) {
		Header[] headers = method.getResponseHeaders("Set-Cookie");
		for (Header header : headers) {
			String value = header.getValue();
			String[] params = value.split(";");
			if (params.length > 1) {
				int begin = params[0].indexOf('=');
				String keyString = params[0].substring(0, begin);
				String valueString = params[0].trim().substring(begin + 1,
						params[0].trim().length());
				this.cookie.put(keyString, valueString);
			}
		}
	}

	private String methodWithCookie() {
		StringBuffer sbCookie = new StringBuffer();
		String strCookie = "";
		for (String key : this.cookie.keySet()) {
			sbCookie.append(" ").append(key).append("=")
					.append((String) this.cookie.get(key)).append(";");
		}
		sbCookie.deleteCharAt(sbCookie.length() - 1);
		strCookie = sbCookie.toString();
		return strCookie;
	}
}
