 package mvs.weibo.page.view;

import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.util.List;  
import javax.swing.ButtonGroup;  
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JProgressBar;  
import javax.swing.JRadioButton;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;  
import javax.swing.Timer;  

import mvs.weibo.page.action.status.StatusAction;
import mvs.weibo.page.login.LoginEntryClass;
import mvs.weibo.page.service.ServiceFlag;
import mvs.weibo.page.spring.SpringStart;
import mvs.weibo.page.vo.status.HibernateStatus;

import org.apache.log4j.Logger;  


 public class MainCaller 
 {
	 private static final long serialVersionUID = -6573152289003229112L;
	 private static Logger logger = Logger.getLogger(MainCaller.class);
	 
	 private List<HibernateStatus> lshs = null;
	 private String name = null;
	 private int wordFlag = 0;
	 public JTextArea consoleTextArea;
	 private StatusAction userStatusAction = null;
	 public JTextField JTFUrlForStatus;
	 public JTextArea JTAConsoleForStatus = null;
	 private List<String> listId;
	 
	 public MainCaller(String name,List<String> ls)
	 {
			this.listId=ls;
		 initPanelStatus();

	 }

	

	 
	 private void initPanelStatus()
	 {

		this.crawlerPerformed();
	}

	public void crawlerPerformed() {
		MainCaller.this.wordFlag = 0;
		MainCaller.this.crawlStatus();
	}

	 private void crawlStatus()
	 {
		 try
		 {
			 new Thread()
			 {
				 public void run()
				 {
					
					 if (MainCaller.this.userStatusAction == null) {
						 MainCaller.this.userStatusAction = ((StatusAction)
						 SpringStart.getApplicationContext()
						 .getBean("statusAction"));
						 }
					// Timer timer = new Timer(300, new ActionListener() {
					// public void actionPerformed(ActionEvent e) {
					// System.out
					// .println(userStatusAction.getProcessNum());
					// }
					// });
					// timer.start();
				//	MainFrame.this.name = "1291352405".trim();// 用户昵称
					//MainFrame.this.lshs = MainFrame.this.userStatusAction
				//			.getStatusListByUid(MainFrame.this.name,listId);
					//
					while(true)//不断循环
					{
						MainCaller.this.lshs = MainCaller.this.userStatusAction.getStatusListByUid("1638306795");
						try
						{
							Thread.sleep(LoginEntryClass.sleepTime);//禁止
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						
					// MainFrame.this.bar.setValue(1000);
					// timer.stop();
					//System.exit(0);
				}

			}
			 
			 .start();
			 } catch (Exception e) {
			 logger.error("爬取微博列表出错：", e);
			 System.out.println("爬取微博列表出错");
			 }
		 }


	 
}

