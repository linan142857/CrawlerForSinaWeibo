/*    */ package mvs.weibo.page.spring;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*    */ 
/*    */ public class SpringStart
/*    */ {
/* 15 */   private static ApplicationContext appContext = null;
/*    */ 
/*    */   public static void start()
/*    */   {
/* 22 */     getApplicationContext();
/*    */   }
/*    */ 
/*    */   public static ApplicationContext getApplicationContext() {
/* 26 */     if (appContext == null) {
/* 27 */       appContext = new ClassPathXmlApplicationContext("applicationcontext.xml");
/*    */     }
/* 29 */     return appContext;
/*    */   }
/*    */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.spring.SpringStart
 * JD-Core Version:    0.6.1
 */