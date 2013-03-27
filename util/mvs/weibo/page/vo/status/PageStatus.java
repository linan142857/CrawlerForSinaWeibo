/*    */ package mvs.weibo.page.vo.status;
/*    */ 
/*    */ import java.sql.Timestamp;
/*    */ 
/*    */ public class PageStatus
/*    */ {
/* 13 */   private long statusID = 0L;
/* 14 */   private String statusText = "";
/* 15 */   private long userID = 0L;
/* 16 */   private String userName = "";
/*    */   private Timestamp inDBat;
/*    */ 
/*    */   public Timestamp getInDBat()
/*    */   {
/* 20 */     return this.inDBat;
/*    */   }
/*    */   public void setInDBat(Timestamp inDBat) {
/* 23 */     this.inDBat = inDBat;
/*    */   }
/*    */   public long getStatusID() {
/* 26 */     return this.statusID;
/*    */   }
/*    */   public void setStatusID(long statusID) {
/* 29 */     this.statusID = statusID;
/*    */   }
/*    */   public String getStatusText() {
/* 32 */     return this.statusText;
/*    */   }
/*    */   public void setStatusText(String statusText) {
/* 35 */     this.statusText = statusText;
/*    */   }
/*    */   public long getUserID() {
/* 38 */     return this.userID;
/*    */   }
/*    */   public void setUserID(long userID) {
/* 41 */     this.userID = userID;
/*    */   }
/*    */   public String getUserName() {
/* 44 */     return this.userName;
/*    */   }
/*    */   public void setUserName(String userName) {
/* 47 */     this.userName = userName;
/*    */   }
/*    */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.vo.status.PageStatus
 * JD-Core Version:    0.6.1
 */