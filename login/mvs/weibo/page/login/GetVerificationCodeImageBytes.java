/*     */ package mvs.weibo.page.login;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;

import mvs.weibo.page.spring.SpringStart;
import mvs.weibo.page.util.pool.WeiboCookiesManager;

/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.httpclient.HttpClient;
/*     */ import org.apache.commons.httpclient.HttpConnectionManager;
/*     */ import org.apache.commons.httpclient.HttpException;
/*     */ import org.apache.commons.httpclient.HttpMethod;
/*     */ import org.apache.commons.httpclient.params.HttpClientParams;
/*     */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.springframework.context.ApplicationContext;
/*     */ 
/*     */ class GetVerificationCodeImageBytes
/*     */ {
/*     */   private HttpClient client;
/*     */   private LoginEntryClass loginEntryClass;
/*     */   private WeiboCookiesManager weiboCookiesManager;
/*     */   private Map<String, String> cookie;
/*     */ 
/*     */   GetVerificationCodeImageBytes(LoginEntryClass loginEntryClass)
/*     */   {
/*  33 */     this.client = new HttpClient();
/*  34 */     this.cookie = new HashMap();
/*  35 */     this.loginEntryClass = loginEntryClass;
/*  36 */     this.weiboCookiesManager = ((WeiboCookiesManager)
/*  37 */       SpringStart.getApplicationContext().getBean("weiboCookiesManager"));
/*     */   }
/*     */ 
/*     */   public byte[] getVerificationImageBytes()
/*     */   {
/*  46 */     byte[] imageBytes = (byte[])null;
/*     */ 
/*  48 */     if (this.loginEntryClass.getPcid() == null)
/*  49 */       generateParameter();
/*  50 */     String rString = generateR();
/*  51 */     String urlString = generateURL(rString, this.loginEntryClass.getPcid());
/*  52 */     HttpMethod method = this.weiboCookiesManager
/*  53 */       .getMethodReqquest(urlString, "");
/*     */     try {
/*  55 */       this.client.executeMethod(method);
/*  56 */       imageBytes = method.getResponseBody();
/*     */     }
/*     */     catch (HttpException e) {
/*  59 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/*  61 */       e.printStackTrace();
/*     */     }
/*  63 */     return imageBytes;
/*     */   }
/*     */ 
/*     */   private void generateParameter()
/*     */   {
/*  71 */     String usernameString = this.loginEntryClass.getUsernameString();
/*     */     try
/*     */     {
/*  74 */       usernameString = 
/*  75 */         URLEncoder.encode(usernameString, "UTF-8");
/*     */     } catch (UnsupportedEncodingException e) {
/*  77 */       e.printStackTrace();
/*     */     }
/*  79 */     byte[] userNameBytes = usernameString.getBytes();
/*  80 */     String encodeName = new String(Base64.encodeBase64(userNameBytes));
/*     */ 
/*  82 */     String url = " http://login.sina.com.cn/sso/prelogin.php";
/*  83 */     String params = "entry=sso&callback=sinaSSOController.preloginCallBack&su=" + 
/*  84 */       encodeName + 
/*  85 */       "&rsakt=mod&client=ssologin.js(v1.4.1)&_=1345013422871";
/*     */ 
/*  87 */     HttpMethod method = this.weiboCookiesManager.getMethodReqquest(url, params);
/*     */ 
/*  89 */     HttpClient client = new HttpClient();
/*  90 */     client.getParams().setCookiePolicy("rfc2965");
/*     */ 
/*  92 */     client.getParams().setParameter("http.protocol.content-charset", 
/*  93 */       "UTF-8");
/*  94 */     client.getHttpConnectionManager().getParams()
/*  95 */       .setConnectionTimeout(20000);
/*  96 */     client.getHttpConnectionManager().getParams().setSoTimeout(20000);
/*     */     try
/*     */     {
/*  99 */       int returnCode = client.executeMethod(method);
/* 100 */       if (returnCode == 200) {
/* 101 */         String content = method.getResponseBodyAsString();
/* 102 */         System.out.println("预登陆返回数据" + content);
/* 103 */         int begin = content.indexOf("pcid\":\"") + 7;
/* 104 */         int end = content.indexOf("\"", begin);
/* 105 */         String pcid = content.substring(begin, end);
/* 106 */         this.loginEntryClass.setPcid(pcid);
/* 107 */         System.out.println("pcid为" + pcid);
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private String generateURL(String rString, String pString)
/*     */   {
/* 134 */     String urlString = "http://login.sina.com.cn/cgi/pin.php?r=" + rString + 
/* 135 */       "&s=0&p=" + pString;
/* 136 */     return urlString;
/*     */   }
/*     */ 
/*     */   private String generateR()
/*     */   {
/* 145 */     String rString = "0123456789";
/* 146 */     StringBuilder stringBuilder = new StringBuilder();
/* 147 */     stringBuilder
/* 148 */       .append(rString.charAt((int)Math.floor(Math.random() * 9.0D) + 1));
/* 149 */     for (int i = 1; i < 8; i++) {
/* 150 */       stringBuilder
/* 151 */         .append(rString.charAt((int)Math.floor(Math.random() * 10.0D)));
/*     */     }
/*     */ 
/* 154 */     return stringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.login.GetVerificationCodeImageBytes
 * JD-Core Version:    0.6.1
 */