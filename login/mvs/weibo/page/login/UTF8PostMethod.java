/*    */ package mvs.weibo.page.login;
/*    */ 
/*    */ import org.apache.commons.httpclient.methods.PostMethod;
/*    */ 
/*    */ class UTF8PostMethod extends PostMethod
/*    */ {
/*    */   public UTF8PostMethod(String url)
/*    */   {
/* 12 */     super(url);
/*    */   }
/*    */ 
/*    */   public String getRequestCharSet()
/*    */   {
/* 17 */     return "UTF-8";
/*    */   }
/*    */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.login.UTF8PostMethod
 * JD-Core Version:    0.6.1
 */