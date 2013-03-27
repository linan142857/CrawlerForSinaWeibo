/*    */ package mvs.weibo.page.view;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JWindow;
/*    */ 
/*    */ public class LoadSystemWaitting
/*    */ {
/*    */   private static LoadSystemWaitting loadSystemWaitting;
/*    */   private Image image;
/*    */   private JWindow jw;
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 16 */     getInstance(true);
/*    */   }
/*    */   public static void instanceInit() {
/* 19 */     if (loadSystemWaitting == null)
/* 20 */       loadSystemWaitting = new LoadSystemWaitting();
/*    */   }
/*    */ 
/*    */   public static LoadSystemWaitting getInstance(boolean alwaysOnTop) {
/* 24 */     if (loadSystemWaitting == null) {
/* 25 */       loadSystemWaitting = new LoadSystemWaitting();
/*    */     }
/* 27 */     loadSystemWaitting.jw.setAlwaysOnTop(alwaysOnTop);
/* 28 */     loadSystemWaitting.setVisible(true);
/* 29 */     return loadSystemWaitting;
/*    */   }
/*    */ 
/*    */   private JPanel getJPanel() {
/* 33 */     JPanel jp = new JPanel()
/*    */     {
/*    */       protected void paintComponent(Graphics g)
/*    */       {
/* 38 */         super.paintComponent(g);
/* 39 */         g.drawImage(LoadSystemWaitting.this.image, 0, 0, this);
/*    */       }
/*    */     };
/* 43 */     return jp;
/*    */   }
/*    */ 
/*    */   public static void closeWindow() {
/* 47 */     if (loadSystemWaitting != null)
/* 48 */       loadSystemWaitting.setVisible(false);
/*    */   }
/*    */ 
/*    */   public void setVisible(boolean b) {
/* 52 */     this.jw.setVisible(b);
/*    */   }
/*    */ 
/*    */   private LoadSystemWaitting()
/*    */   {
/* 57 */     this.image = Toolkit.getDefaultToolkit().createImage(
/* 58 */       LoadSystemWaitting.class
/* 59 */       .getResource("/loading.gif"));
/* 60 */     this.jw = new JWindow();
/* 61 */     this.jw.setSize(145, 15);
/* 62 */     this.jw.setLayout(new BorderLayout());
/* 63 */     this.jw.add(getJPanel(), "Center");
/* 64 */     this.jw.setLocationRelativeTo(null);
/*    */   }
/*    */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.view.LoadSystemWaitting
 * JD-Core Version:    0.6.1
 */