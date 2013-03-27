/*     */ package mvs.weibo.page.service;
/*     */ 
/*     */ import java.net.ConnectException;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;

import mvs.weibo.page.util.pool.ThreadPoolManager;
import mvs.weibo.page.util.pool.WeiboCookiesManager;

/*     */ import org.apache.commons.httpclient.ConnectTimeoutException;
/*     */ import org.apache.commons.httpclient.HttpClient;
/*     */ import org.apache.commons.httpclient.HttpConnectionManager;
/*     */ import org.apache.commons.httpclient.HttpMethod;
/*     */ import org.apache.commons.httpclient.params.HttpClientParams;
/*     */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class BaseService
/*     */ {
/*  25 */   private static Logger logger = Logger.getLogger(BaseService.class);
/*     */ 
/*  27 */   private static WeiboCookiesManager weiboCookiesManager = null;
/*     */ 
/*  37 */   private ThreadPoolManager threadPoolManager = null;
/*     */ 
/*     */   public WeiboCookiesManager getWeiboCookiesManager()
/*     */   {
/*  30 */     return weiboCookiesManager;
/*     */   }
/*     */ 
/*     */   public void setWeiboCookiesManager(WeiboCookiesManager weiboCookiesManager) {
/*  34 */     BaseService.weiboCookiesManager = weiboCookiesManager;
/*     */   }
/*     */ 
/*     */   public ThreadPoolManager getThreadPoolManager()
/*     */   {
/*  40 */     return this.threadPoolManager;
/*     */   }
/*     */ 
/*     */   public void setThreadPoolManager(ThreadPoolManager threadPoolManager) {
/*  44 */     this.threadPoolManager = threadPoolManager;
/*     */   }
/*     */ 
/*     */   public static String getPageContentFromUrl(String url, String params) {
				 
/*  48 */     return getPageContentFromUrl(url, params, 3);
/*     */   }
/*     */ 
/*     */   public static String getPageContentFromUrl(String url, String params, int count)
/*     */   {
/*  60 */     if (count < 0) {
/*  61 */       return "";
/*     */     }
/*  63 */     String pageContent = "";
/*  64 */     String cookies = weiboCookiesManager.getCookie();

/*  65 */     HttpMethod method = null;
/*     */     try {
/*  67 */       method = weiboCookiesManager.getMethodReqquest(url, params);
/*  68 */       method.setRequestHeader("Cookie", cookies);
/*  69 */       HttpClient client = new HttpClient();
/*  70 */       client.getParams().setCookiePolicy("rfc2965");
/*     */ 
/*  72 */       client.getParams().setParameter(
/*  73 */         "http.protocol.content-charset", "UTF-8");
/*  74 */       client.getHttpConnectionManager().getParams()
/*  75 */         .setConnectionTimeout(20000);
/*  76 */       client.getHttpConnectionManager().getParams()
/*  77 */         .setSoTimeout(20000);
/*  78 */       int returnCode = client.executeMethod(method);
/*  79 */       if (returnCode == 200)
/*  80 */         pageContent = method.getResponseBodyAsString();
	

/*     */     }
/*     */     catch (ConnectException ce)
/*     */     {
/*  84 */       logger.error("由于网络原因导致的问题要重新访问url=" + url + ",params=" + params + 
/*  85 */         ",cookies=" + cookies, ce);
/*  86 */       releaseConnection(method);
/*  87 */       pageContent = getPageContentFromUrl(url, params, --count);
/*     */     } catch (SocketException se) {
/*  89 */       logger.error("由于网络原因导致的问题要重新访问url=" + url + ",params=" + params + 
/*  90 */         ",cookies=" + cookies, se);
/*  91 */       releaseConnection(method);
/*  92 */       pageContent = getPageContentFromUrl(url, params, --count);
/*     */     } catch (SocketTimeoutException ste) {
/*  94 */       logger.error("由于网络原因导致的问题要重新访问url=" + url + ",params=" + params + 
/*  95 */         ",cookies=" + cookies, ste);
/*  96 */       releaseConnection(method);
/*  97 */       pageContent = getPageContentFromUrl(url, params, --count);
/*     */     } catch (ConnectTimeoutException cte) {
/*  99 */       logger.error("由于网络原因导致的问题要重新访问url=" + url + ",params=" + params + 
/* 100 */         ",cookies=" + cookies, cte);
/* 101 */       releaseConnection(method);
/* 102 */       pageContent = getPageContentFromUrl(url, params, --count);
/*     */     } catch (Exception e) {
/* 104 */       logger.error("抓取网页出错url=" + url + ",params=" + params + ",cookies=" + 
/* 105 */         cookies, e);
/*     */     } finally {
/* 107 */       releaseConnection(method);
/*     */     }
/* 109 */     return pageContent;
/*     */   }
/*     */ 
/*     */   private static void releaseConnection(HttpMethod method) {
/* 113 */     method.releaseConnection();
/* 114 */     method = null;
/*     */   }
/*     */ 
/*     */   protected void execute(Runnable r)
/*     */   {
/* 123 */     this.threadPoolManager.execute(r);
/*     */   }

			
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.service.BaseService
 * JD-Core Version:    0.6.1
 */