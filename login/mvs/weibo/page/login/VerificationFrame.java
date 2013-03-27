/*     */ package mvs.weibo.page.login;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ class VerificationFrame extends JFrame
/*     */ {
/*     */   private GetVerificationCodeImageBytes getVerificationCodeMethod;
/*     */   private LoginEntryClass loginEntryClass;
/*     */   private JPanel jPanel;
/*     */   private JButton bntConfirm;
/*     */   private JButton bntCannel;
/*     */   private JLabel verificationCodeImage;
/*     */   private JTextField input;
/*     */   private JLabel jLabel2;
/*     */ 
/*     */   public VerificationFrame(LoginEntryClass loginEntryClass)
/*     */   {
/*  39 */     this.getVerificationCodeMethod = new GetVerificationCodeImageBytes(loginEntryClass);
/*  40 */     this.loginEntryClass = loginEntryClass;
/*  41 */     initGUI();
/*  42 */     setVisible(true);
/*     */   }
/*     */ 
/*     */   private void initGUI()
/*     */   {
/*  55 */     setDefaultCloseOperation(2);
/*  56 */     setTitle("请输入验证码");
/*  57 */     setResizable(false);
/*     */ 
/*  59 */     this.jPanel = new JPanel();
/*  60 */     this.jPanel.setBackground(Color.WHITE);
/*  61 */     getContentPane().add(this.jPanel, "Center");
/*  62 */     this.jPanel.setLayout(null);
/*     */ 
/*  64 */     this.jLabel2 = new JLabel();
/*  65 */     this.jPanel.add(this.jLabel2);
/*  66 */     this.jLabel2.setText("请输入：");
/*  67 */     this.jLabel2.setBounds(45, 75, 55, 15);
/*     */ 
/*  69 */     byte[] bytePng = this.getVerificationCodeMethod.getVerificationImageBytes();
/*     */ 
/*  71 */     ImageIcon icon = new ImageIcon(bytePng);
/*     */ 
/*  73 */     this.verificationCodeImage = new JLabel();
/*  74 */     this.verificationCodeImage.setIcon(icon);
/*  75 */     this.jPanel.add(this.verificationCodeImage);
/*  76 */     this.verificationCodeImage.setBounds(60, 15, 100, 40);
/*     */ 
/*  78 */     this.input = new JTextField();
/*  79 */     this.jPanel.add(this.input);
/*  80 */     this.input.setBounds(115, 70, 60, 25);
/*  81 */     this.input.addKeyListener(new KeyListener()
/*     */     {
/*     */       public void keyTyped(KeyEvent arg0)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void keyReleased(KeyEvent arg0)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void keyPressed(KeyEvent arg0) {
/*  92 */         if (arg0.getKeyCode() == 10)
/*  93 */           VerificationFrame.this.bntConfirm.doClick();
/*     */       }
/*     */     });
/*  97 */     this.bntConfirm = new JButton();
/*  98 */     this.jPanel.add(this.bntConfirm);
/*  99 */     this.bntConfirm.setText("确定");
/* 100 */     this.bntConfirm.setBounds(50, 110, 50, 30);
/* 101 */     this.bntConfirm.setMargin(new Insets(0, 0, 0, 0));
/* 102 */     this.bntConfirm.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 105 */         VerificationFrame.this.bntConfirm.setEnabled(false);
/* 106 */         String verificationCodeString = VerificationFrame.this.input.getText().trim();
/* 107 */         if ("".equals(verificationCodeString)) {
/* 108 */           JOptionPane.showMessageDialog(VerificationFrame.this, 
/* 109 */             "请输入验证码");
/* 110 */           VerificationFrame.this.verificationCodeImage.requestFocusInWindow();
/* 111 */           VerificationFrame.this.bntConfirm.setEnabled(true);
/* 112 */           return;
/*     */         }
/* 114 */         VerificationFrame.this.loginEntryClass.setVerificationString(verificationCodeString);
/* 115 */         VerificationFrame.this.loginEntryClass.loginEntry();
/*     */       }
/*     */     });
/* 119 */     this.bntCannel = new JButton();
/* 120 */     this.jPanel.add(this.bntCannel);
/* 121 */     this.bntCannel.setBounds(150, 110, 50, 30);
/* 122 */     this.bntCannel.setText("取消");
/* 123 */     this.bntCannel.setMargin(new Insets(0, 0, 0, 0));
/* 124 */     this.bntCannel.addMouseListener(new MouseAdapter() {
/*     */       public void mouseClicked(MouseEvent e) {
/* 126 */         System.exit(0);
/*     */       }
/*     */     });
/* 130 */     addWindowListener(new WindowAdapter() {
/*     */       public void windowClosing(WindowEvent e) {
/* 132 */         System.exit(0);
/*     */       }
/*     */     });
/* 136 */     pack();
/* 137 */     setLocationRelativeTo(null);
/* 138 */     setSize(240, 175);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.login.VerificationFrame
 * JD-Core Version:    0.6.1
 */