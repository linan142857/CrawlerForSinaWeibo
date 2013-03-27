/*    */ package mvs.weibo.page.login;

import org.apache.http.message.BasicNameValuePair;

import rsaLogin.LoginRSA;

/*    */ 
/*    */ class LoginPostWithVerification extends LoginRSA
/*    */ {
/*    */   private LoginEntryClass loginEntryClass;
/*    */ 
/*    */   public LoginPostWithVerification(LoginEntryClass loginEntryClass)
/*    */   {
/*  9 */     this.loginEntryClass = loginEntryClass;
/*    */   }
/*    */ 
/*    */   void hookForAddingVerification()
/*    */   {
	nvps.add(new BasicNameValuePair("door", this.loginEntryClass.getVerificationString()));
	nvps.add(new BasicNameValuePair("pcid", this.loginEntryClass.getPcid()));
/*    */   }
/*    */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.login.LoginPostWithVerification
 * JD-Core Version:    0.6.1
 */