/*    */ package mvs.weibo.page.util.pool;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.Executors;
/*    */ 
/*    */ public class ThreadPoolManager
/*    */ {
/* 15 */   private Executor pool = null;
/*    */ 
/*    */   public ThreadPoolManager(int nThreads) {
/* 18 */     this.pool = Executors.newFixedThreadPool(nThreads);
/*    */   }
/*    */ 
/*    */   public void execute(Runnable r) {
/* 22 */     this.pool.execute(r);
/*    */   }
/*    */ }