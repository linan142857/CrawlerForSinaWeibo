package rsaLogin;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import mvs.weibo.page.spring.SpringStart;
import mvs.weibo.page.util.pool.WeiboCookiesManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class LoginRSA {

	static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
			+ "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
			+ "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
			+ "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";
	private Map<String, String> cookie = null;
	private WeiboCookiesManager weiboCookiesManager;
	public List<NameValuePair> nvps;
	private static final Log logger = LogFactory.getLog(LoginRSA.class);

	
	private static String generateNonce() {
		String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String nonce = "";
		for (int i = 0; i < 6; i++) {
			nonce = nonce
					+ x.charAt((int) Math.ceil(Math.random() * 1000000.0D)
							% x.length());
		}
		return nonce;
	}

	public LoginRSA(){
		this.cookie = new HashMap();
		this.weiboCookiesManager = ((WeiboCookiesManager) SpringStart
				.getApplicationContext().getBean("weiboCookiesManager"));
	}
	
	public int post(String username, String passwd) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 5000);

		HttpPost post = new HttpPost(
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)");

		post.setHeader("cookie",this.weiboCookiesManager.getCookie());
		String servertime = getCurrentTime();
		String nonce = generateNonce();

	
	//	long servertime = info.servertime;
	//	String nonce = info.nonce;

		String pwdString = servertime + "\t" + nonce + "\n" + passwd;
		String sp;
		int rtn = -1;
		try {
			sp = new BigIntegerRSA().rsaCrypt(SINA_PK, "10001", pwdString);
			nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("entry", "weibo"));
			nvps.add(new BasicNameValuePair("gateway", "1"));
			nvps.add(new BasicNameValuePair("from", ""));
			nvps.add(new BasicNameValuePair("savestate", "7"));
			nvps.add(new BasicNameValuePair("useticket", "1"));
			nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
			nvps.add(new BasicNameValuePair("vsnf", "1"));
			// new NameValuePair("vsnval", ""),
			nvps.add(new BasicNameValuePair("su", encodeUserName(username)));
			nvps.add(new BasicNameValuePair("service", "miniblog"));
			nvps.add(new BasicNameValuePair("servertime", servertime + ""));
			nvps.add(new BasicNameValuePair("nonce", nonce));
			nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
			nvps.add(new BasicNameValuePair("rsakv", "1330428213"));
			nvps.add(new BasicNameValuePair("sp", sp));
			nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
			nvps.add(new BasicNameValuePair("prelt", "115"));
			nvps.add(new BasicNameValuePair("returntype", "META"));
			nvps.add(new BasicNameValuePair(
					"url",
					"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));

			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			HttpResponse response = client.execute(post);  
			
			
			if(response.getStatusLine().getStatusCode() == 200){
				String pageContent = EntityUtils.toString(response.getEntity());  
				
				 
				if (pageContent.contains("retcode=4049")){
					return 4049;
				}else if (pageContent.contains("retcode=101")){
					return 101;
				}else if (pageContent.contains("retcode=2070")){
					return 2070;
				}else if (pageContent.contains("retcode=0")){
					rtn = 0;
					 String url = pageContent.substring(pageContent  
				                .indexOf("http://weibo.com/ajaxlogin.php?"), pageContent  
				                .indexOf("code=0") + 6);
					 HttpGet getMethod = new HttpGet(url);
					 response = client.execute(getMethod);
					   makeCookies2(response);
				        String cookie = methodWithCookie();
						this.weiboCookiesManager.addCookie(cookie);
						
				}else if (pageContent.contains("retcode=4403")){
					rtn = 4403;
				}else {
					System.out.println("error"+pageContent);
					return rtn;
				}
			}
			
//	        String entity = EntityUtils.toString(response.getEntity());  
//
//	        String url = entity.substring(entity  
//	                .indexOf("http://weibo.com/ajaxlogin.php?"), entity  
//	                .indexOf("code=0") + 6); 
//
//	        logger.debug("url:" + url);
//
//	     // 获取到实际url进行连接  
//	        HttpGet getMethod = new HttpGet(url);  
//
//	        response = client.execute(getMethod);  
	     
	     
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtn;
		
	}
	
	private void makeCookies2(HttpResponse response){
		  org.apache.http.Header[] headers =  response.getHeaders("Set-Cookie");
		  for (org.apache.http.Header header : headers) {
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
	
	public static void main(String[] args) throws HttpException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 5000);

		HttpPost post = new HttpPost(
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)");


		String servertime = getCurrentTime();
		String nonce = generateNonce();

	
	//	long servertime = info.servertime;
	//	String nonce = info.nonce;

		String pwdString = servertime + "\t" + nonce + "\n" + "buptsse";
		String sp = new BigIntegerRSA().rsaCrypt(SINA_PK, "10001", pwdString);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("entry", "weibo"));
		nvps.add(new BasicNameValuePair("gateway", "1"));
		nvps.add(new BasicNameValuePair("from", ""));
		nvps.add(new BasicNameValuePair("savestate", "7"));
		nvps.add(new BasicNameValuePair("useticket", "1"));
		nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
		nvps.add(new BasicNameValuePair("vsnf", "1"));
		// new NameValuePair("vsnval", ""),
		nvps.add(new BasicNameValuePair("su", encodeUserName("buptsselab110@sina.com")));
		nvps.add(new BasicNameValuePair("service", "miniblog"));
		nvps.add(new BasicNameValuePair("servertime", servertime + ""));
		nvps.add(new BasicNameValuePair("nonce", nonce));
		nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
		nvps.add(new BasicNameValuePair("rsakv", "1330428213"));
		nvps.add(new BasicNameValuePair("sp", sp));
		nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
		nvps.add(new BasicNameValuePair("prelt", "115"));
		nvps.add(new BasicNameValuePair("returntype", "META"));
		nvps.add(new BasicNameValuePair(
				"url",
				"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));

		post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = client.execute(post);  
		
		
		
        String entity = EntityUtils.toString(response.getEntity());  
        
        System.out.println("ASDFGHJK"+entity);
//
//        String url = entity.substring(entity  
//                .indexOf("http://weibo.com/ajaxlogin.php?"), entity  
//                .indexOf("code=0") + 6); 
//
//        logger.debug("url:" + url);
//
//     // 获取到实际url进行连接  
//        HttpGet getMethod = new HttpGet(url);  
//
//        response = client.execute(getMethod);  
//        entity = EntityUtils.toString(response.getEntity());  
        
       org.apache.http.Header[] headers =  response.getHeaders("Set-Cookie");
       for ( org.apache.http.Header header : headers) {
			String value = header.getValue();
			String[] params = value.split(";");
			if (params.length > 1) {
				int begin = params[0].indexOf('=');
				String keyString = params[0].substring(0, begin);
				String valueString = params[0].trim().substring(begin + 1,
						params[0].trim().length());
				System.out.println(keyString+"\t"+ valueString);
			}
		}
        
//        entity = entity.substring(entity.indexOf("userdomain") + 13, entity  
//                .lastIndexOf("\""));  
//        logger.debug(entity);  
//
//        getMethod = new HttpGet("http://weibo.com/humingchun?wvr=5&lf=reg");  
//        response = client.execute(getMethod);  
//        entity = EntityUtils.toString(response.getEntity());  
//        // Document doc =  
//        // Jsoup.parse(EntityUtils.toString(response.getEntity()));  
//        System.out.println("@#$%^&*("+entity);
//        logger.debug(entity);
	}


	public static String getPreLoginInfo(HttpClient client)
			throws ParseException, IOException {
		String preloginurl = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su="
				+ "dW5kZWZpbmVk"
				+ "&rsakt=mod&client=ssologin.js(v1.4.2)"
				+ "&_=" + getCurrentTime();
		HttpGet get = new HttpGet(preloginurl);

		HttpResponse response = client.execute(get);

		String getResp = EntityUtils.toString(response.getEntity());

		int firstLeftBracket = getResp.indexOf("(");
		int lastRightBracket = getResp.lastIndexOf(")");

		String jsonBody = getResp.substring(firstLeftBracket + 1,
				lastRightBracket);
		System.out.println(jsonBody);
		return jsonBody;

	}

	private static String getCurrentTime() {
		long servertime = new Date().getTime() / 1000;
		return String.valueOf(servertime);
	}

	private static String encodeUserName(String email) {
		email = email.replaceFirst("@", "%40");// MzM3MjQwNTUyJTQwcXEuY29t
		email = Base64.encodeBase64String(email.getBytes());
		return email;
	}

}