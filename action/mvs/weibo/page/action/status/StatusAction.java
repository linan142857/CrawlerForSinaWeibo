/*    */ package mvs.weibo.page.action.status;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JTextArea;

import mvs.weibo.page.service.status.IStatusService;
import mvs.weibo.page.vo.status.HibernateStatus;

/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class StatusAction
/*    */ {
/* 22 */   private static Logger logger = Logger.getLogger(StatusAction.class);
/*    */ 
/* 24 */   private IStatusService statusService = null;
/*    */ 
/*    */   public IStatusService getStatusService() {
/* 27 */     return this.statusService;
/*    */   }
/*    */ 
/*    */   public void setStatusService(IStatusService statusService) {
/* 31 */     this.statusService = statusService;
/*    */   }
/*    */ 
/*    */   public List<HibernateStatus> getStatusListByUid(String name) {
/* 35 */     List rtn = null;
/*    */     try {
/* 37 */       rtn = this.statusService.getStatusListByUid(name);

/* 38 */       if ((rtn != null) && (!rtn.isEmpty()))
/* 39 */         Collections.sort(rtn);
/*    */     }
/*    */     catch (Exception e) {
/* 42 */       logger.error("爬取微博列表出错uid=[ " + name + " ]", e);
/*    */     } finally {
/* 44 */       
/*    */     }
/* 46 */     return rtn;
/*    */   }
/*    */ 
/*    */   public int getProcessNum() {
/* 50 */     return this.statusService.getProcessNum();
/*    */   }
/*    */ }
