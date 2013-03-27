/*     */package mvs.weibo.page.util.pool;

/*     */
/*     */import java.io.BufferedReader;
/*     */
import java.io.File;
/*     */
import java.io.FileReader;
import java.io.PrintWriter;
/*     */
import java.util.ArrayList;
/*     */
import java.util.List;
/*     */
import java.util.Properties;

import mvs.weibo.page.service.ServiceFlag;

/*     */
import org.apache.commons.httpclient.HttpClient;
/*     */
import org.apache.commons.httpclient.HttpConnectionManager;
/*     */
import org.apache.commons.httpclient.HttpMethod;
/*     */
import org.apache.commons.httpclient.methods.GetMethod;
/*     */
import org.apache.commons.httpclient.params.HttpClientParams;
/*     */
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

/*     */
/*     */public class WeiboCookiesManager
/*     */{
	/* 27 */private static Logger logger = Logger
			.getLogger(WeiboCookiesManager.class);
	/* 28 */private boolean[] cookieListLock = new boolean[0];
	/*     */
	/* 71 */private List<String> cookieList = new ArrayList();
	/*     */
	/* 87 */private int count = 0;

	/*     */
			public WeiboCookiesManager(String filePath)
			{
		/* 31 */String user_dir = System.getProperties().getProperty("user.dir");
		System.out.println(user_dir + "");
		/* 32 */logger.debug("文件工作路径：" + user_dir);
		/* 33 */filePath = user_dir + "\\" + filePath;
		/*     */
		/* 37 */logger.debug(filePath);
		/*     */
		/* 39 */File f = new File(filePath);
		/* 40 */if (!f.exists()) {
			/* 41 */logger.debug("文件不存在");
			System.out.println("文件不存在");
			/* 42 */keepAlive();
			/* 43 */return;
			/*     */}
		/* 45 */logger.debug("文件存在");
		/*     */System.out.println("文件存在");
		/* 47 */FileReader file = null;
		/*     */try {
			/* 49 */file = new FileReader(filePath);
			/* 50 */BufferedReader reader = new BufferedReader(file);
			/*     */String line;
			/* 52 */while ((line = reader.readLine()) != null)
			/*     */{
				/* 53 */if (!"".equals(line.trim())) {
					/* 54 */addCookie(line);
					/* 55 */logger.debug("读入cookie:" + line);
					System.out.println("读入cookie:" + line);
					/*     */}
				/*     */}
			/* 58 */logger.debug("cookies文件读取成功，当前有cookie个数"
					+ this.cookieList.size());
			System.out.println("cookies文件读取成功，当前有cookie个数"
					+ this.cookieList.size());
			/*     */} catch (Exception e) {
			/* 60 */logger.error("读取cookies文件出错", e);
			System.out.println("读取coolies文件出错");
			/*     */try
			/*     */{
				/* 63 */file.close();
				/*     */} catch (Exception ex) {
				/* 65 */logger.error("关闭cookies文件出错", e);
				/*     */}
			/*     */}
		/*     */finally
		/*     */{
			/*     */try
			/*     */{
				/* 63 */file.close();
				/*     */} catch (Exception e) {
				/* 65 */logger.error("关闭cookies文件出错", e);
				/*     */}
			/*     */}
		/* 68 */keepAlive();
		/*     */}

	/*     */
	/*     */public List<String> getCookieList()
	/*     */{
		/* 74 */return this.cookieList;
		/*     */}

	/*     */
	/*     */public void setCookieList(List<String> cookieList) {
		/* 78 */this.cookieList = cookieList;
		/*     */}

	/*     */
	/*     */public void addCookie(String cookie) {
		/* 82 */synchronized (this.cookieListLock) {

			/* 83 */this.cookieList.add(cookie);
			System.out.println("添加cookie完成");
			/*     */}
		/*     */}

	/*     */
	/*     */public synchronized String getCookie()
	/*     */{
		/* 95 */String rtn = "";
		/* 96 */synchronized (this.cookieListLock) {
			/* 97 */if (this.cookieList.isEmpty()) {
				/* 98 */return "";
				/*     */}
			/* 100 */rtn = (String) this.cookieList.get(this.count);
			/* 101 */this.count += 1;
			/* 102 */if (this.count == this.cookieList.size()) {
				/* 103 */this.count = 0;
				/*     */try {
					/* 105 */Thread.sleep(100L);
					/*     */} catch (Exception e) {
					/* 107 */logger.debug(e);
					/*     */}
				/*     */}
			/*     */}
		/* 111 */return rtn;
		/*     */}

	/*     */
	/*     */public HttpMethod getMethodReqquest(String url, String params)
	/*     */{
		/* 121 */HttpMethod getMethod = new GetMethod(url);
		/* 122 */getMethod
				/* 123 */.setRequestHeader(
						/* 124 */"Accept",
						/* 125 */"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		/*     */
		/* 129 */getMethod.setRequestHeader("Referer", url);
		/* 130 */getMethod.setRequestHeader("Accept-Language", "zh-cn");
		/* 131 */getMethod
				/* 132 */.setRequestHeader(
						/* 133 */"User-Agent",
						/* 134 */"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; InfoPath.2)");
		/* 135 */getMethod.setRequestHeader("Connection", "Keep-Alive");
		/* 136 */getMethod.setRequestHeader("cookie", getCookie());
		/* 137 */if ((params != null) && (!"".equals(params))) {
			/* 138 */getMethod.setQueryString(params);
			/*     */}
		/* 140 */return getMethod;
		/*     */}

	/*     */
	/*     */private void keepAlive()
	/*     */{
		/* 147 */RefreshAliveThread rwt = new RefreshAliveThread();
		/* 148 */rwt.start();
		/*     */}

	/*     */class RefreshAliveThread extends Thread {
		/*     */RefreshAliveThread() {
			/*     */}

		/*     */public void run() {
			System.out.println("dasfsad"+ServiceFlag.running);
			/*     */while (ServiceFlag.running) {
				WeiboCookiesManager.logger.debug("保持cookie为alive状态进行的请求");
				System.out.println("保持cookie为alive状态进行的请求");
				/* 155 */HttpMethod method = null;
				/* 156 */String url = "http://weibo.com/u/2395784474";
				/*     */try {
					/* 158 */String cookies = WeiboCookiesManager.this
							.getCookie();
					/*     */
					/* 160 */method = WeiboCookiesManager.this
							.getMethodReqquest(url, "");
					/* 161 */method.setRequestHeader("Cookie", cookies);
					/* 162 */HttpClient client = new HttpClient();
					/* 163 */client.getParams().setCookiePolicy("rfc2965");
					/*     */
					/* 165 */client.getParams().setParameter(
					/* 166 */"http.protocol.content-charset", "UTF-8");
					/* 167 */client.getHttpConnectionManager().getParams()
					/* 168 */.setConnectionTimeout(20000);
					/* 169 */client.getHttpConnectionManager().getParams()
					/* 170 */.setSoTimeout(20000);
					/* 171 */int returnCode = client.executeMethod(method);
					/* 172 */if (returnCode == 200) {
						/* 173 */method.getResponseBodyAsString();
						/*     */}
					/* 175 */Thread.sleep(120000L);
					/*     */} catch (Exception e) {
					/* 177 */e.printStackTrace();
					/*     */
					/* 179 */method.releaseConnection();
					/* 180 */method = null;
					/*     */}
				/*     */finally
				/*     */{
					/* 179 */method.releaseConnection();
					/* 180 */method = null;
					/*     */}
				/*     */}
			/*     */}
		/*     */
	}
	/*     */
}
