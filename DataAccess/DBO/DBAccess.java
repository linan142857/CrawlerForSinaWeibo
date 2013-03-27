package DBO;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import mvs.weibo.page.service.ServiceFlag;
import mvs.weibo.page.vo.status.HibernateStatus;
import mvs.weibo.page.vo.user.HibernateUser;
import mvs.weibo.page.vo.user.HibernateUser.VeriReason;

import WeiboModel.FullModel;

public class DBAccess
{
	public static String DB_table = "weiboid";
	public static String DB="microblog";
	public static String DB_username = "hadoop";
	public static String DB_password = "";
	public static String DB_port = "3306";
	public static String DB_path = "localhost";
	public static String DB_userinfo_table = "userinfos";
	public Connection conn;
	public Statement stmt;
	public PreparedStatement pstmt;
	public ResultSet rs;

	public DBAccess()
	{
		try
		{
			Properties prop = new Properties();
			
			try {
				InputStream in = new BufferedInputStream (new FileInputStream("config.properties"));
				prop.load(in);
				DBAccess.DB_table = prop.getProperty("DB_table");
				DBAccess.DB = prop.getProperty("DB");
				DBAccess.DB_username = prop.getProperty("DB_username");
				DBAccess.DB_password = prop.getProperty("DB_password");
				DBAccess.DB_port = prop.getProperty("DB_port");
				DBAccess.DB_path = prop.getProperty("DB_path");
				DBAccess.DB_userinfo_table = prop.getProperty("DB_userinfo_table");
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = java.sql.DriverManager.getConnection(
					"jdbc:mysql://"+DB_path+":"+DB_port+"/"+ DB +"?useUnicode=true&characterEncoding=utf-8", DBAccess.DB_username, DBAccess.DB_password);
			if (!conn.isClosed()) System.out.println("Succeeded connecting to the Database!");
			stmt = conn.createStatement();

		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkExists(String statusID){
		if((this.getConstraintStatuses(" where status_id ='"+ statusID +"'")).size() != 0){
			return true;
		}else return false;
	}
	
	public List<FullModel> getAllStatuses()
	{
		return getConstraintStatuses("");
	}

	public List<FullModel> getConstraintStatuses(String statement)
	{
		List<FullModel> allStatuses = new ArrayList<FullModel>();
		try
		{
			rs = stmt.executeQuery("SELECT * FROM " + DB_table + " " + statement);
			while (rs.next())
			{
				FullModel status = new FullModel();
				status.setStatusID(rs.getString("status_id"));
				status.setUserID(rs.getString("user_id"));
				status.setCreateTime(rs.getDate("status_created_at"));
				status.setText(rs.getString("status_text"));
				status.setCommentNum(rs.getInt("status_commentsCount"));
				status.setRetweetNum(rs.getInt("status_repostsCount"));
				allStatuses.add(status);
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allStatuses;
	}

	/**
	 * date string format: 2009-06-01
	 * */
	public List<FullModel> getPeriodicStatuses_1(String date1, String date2)
	{
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date myDate1 = dateFormat1.parse(date1);
			Date myDate2 = dateFormat1.parse(date2);
			return this.getPeriodicStatuses(myDate1, myDate2);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String dateToString(Date time)
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		String ctime = formatter.format(time);

		return ctime;
	}

	public List<FullModel> getPeriodicStatuses(Date date1, Date date2)
	{
		String statement = " WHERE  status_created_at > '" + new Timestamp(date1.getTime())
				+ "' AND status_created_at < '" + new Timestamp(date2.getTime()) + "'";
		return this.getConstraintStatuses(statement);
	}

	public void userinfoCrawlerForParticularID(HibernateUser hibernateUser){
		try {
			pstmt = conn.prepareStatement("replace into "+ DB_userinfo_table +
					" (userID, screenName, province, city, location, description, url, profileImageUrl," +
					" userDomain, gender,  followersCount, friendsCount, statusesCount, favouritesCount, createdAt," +
					" createdAtStr, verified, verified_reason, biFollowersCount, remark, inDBTime) " + 
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			pstmt.setString(1, String.valueOf(hibernateUser.getUserID()));
			pstmt.setString(2, String.valueOf(hibernateUser.getScreenName()));
			pstmt.setString(3, hibernateUser.getProvince());
			pstmt.setString(4, hibernateUser.getCity());
			pstmt.setString(5, String.valueOf(hibernateUser.getLocation()));
			pstmt.setString(6, String.valueOf(hibernateUser.getDescription()));
			pstmt.setString(7, String.valueOf(hibernateUser.getUrl()));
			pstmt.setString(8, String.valueOf(hibernateUser.getProfileImageUrl()));
			pstmt.setString(9, String.valueOf(hibernateUser.getUserDomain()));
			pstmt.setString(10, String.valueOf(hibernateUser.getGender()));
			pstmt.setInt(11, hibernateUser.getFollowersCount());
			pstmt.setInt(12, hibernateUser.getFriendsCount());
			pstmt.setInt(13, hibernateUser.getStatusesCount());
			pstmt.setInt(14, hibernateUser.getFavouritesCount());
			pstmt.setTimestamp(15, hibernateUser.getCreatedAt());
			pstmt.setString(16, String.valueOf(hibernateUser.getCreatedAtStr()));
			pstmt.setInt(17, hibernateUser.getVerified());
			pstmt.setInt(18, (hibernateUser.getVerified_reason() == HibernateUser.VeriReason.GOVERNMENT)?
					0 : ((hibernateUser.getVerified_reason() == HibernateUser.VeriReason.ENTERPRISE)? 1 :2));
			pstmt.setInt(19,  hibernateUser.getBiFollowersCount());
			pstmt.setString(20, String.valueOf(hibernateUser.getRemark()));
			pstmt.setTimestamp(21, new Timestamp(new Date().getTime()));
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void statusCrawlerForParticularID(HibernateStatus hibernateStatus){
			try {
				pstmt = conn.prepareStatement("replace into "+DB_table+" (status_id,status_created_at,status_user_id,status_user_name,status_text,status_url,status_bmiddle_pic,status_original_pic,status_thumnail_pic,status_commentsCount,status_repostsCount,source_status_id,source_status_text,source_status_user_id,source_status_user_name,source_status_url) " +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setString(1, String.valueOf(hibernateStatus.getId()));
				pstmt.setTimestamp(2, hibernateStatus.getCreatedAt());
				pstmt.setString(3, String.valueOf(hibernateStatus.getUserId()));
				pstmt.setString(4, hibernateStatus.getUserName());
				pstmt.setString(5, hibernateStatus.getText());
				pstmt.setString(6, hibernateStatus.getSourceURL());
				pstmt.setString(7, hibernateStatus.getPicURL());
				pstmt.setString(8, hibernateStatus.getForwardpicURL());
				pstmt.setString(9, null);
				pstmt.setInt(10, hibernateStatus.getCommentsCount());
				pstmt.setInt(11, hibernateStatus.getRepostsCount());
				pstmt.setString(12, hibernateStatus.getForwardStatusID() == 0? null : String.valueOf(hibernateStatus.getForwardStatusID()));
				pstmt.setString(13, hibernateStatus.getForwardText());
				pstmt.setString(14,  hibernateStatus.getForwardUserID() == 0? null : String.valueOf(hibernateStatus.getForwardUserID()));
				pstmt.setString(15, hibernateStatus.getForwardUserName());
				pstmt.setString(16, hibernateStatus.getForwardSourceURL());
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.executeUpdate();
			} catch (SQLException e) {
				ServiceFlag.flag.put(String.valueOf(hibernateStatus.getUserId()), 0);
				e.printStackTrace();
				System.out.println("state+"+ServiceFlag.flag.get(String.valueOf(hibernateStatus.getUserId())));
			}
		
	}
	
	public static void main(String[] args)
	{
		DBAccess db = new DBAccess();
		List<FullModel> all = db.getPeriodicStatuses_1("2013-01-25", "2013-01-26");
		System.out.println(all.size());
	}
}
