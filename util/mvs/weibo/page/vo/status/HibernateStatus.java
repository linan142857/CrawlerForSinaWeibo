package mvs.weibo.page.vo.status;

import java.io.Serializable;
import java.sql.Timestamp;

public class HibernateStatus implements Serializable, Comparable<HibernateStatus>
{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long userId;
	private String userName;
	private Timestamp createdAt;
	private String text;
	private int repostsCount;
	private int commentsCount;
	private String sourceURL;
	private String picURL = null;
	private String forwardpicURL = null;
	private String forwardText = null;
	private String forwardUserName = null;
	private long forwardStatusID;
	private String forwardSourceURL = null;
	private long ForwardUserID;

	
	public long getForwardUserID()
	{
		return ForwardUserID;
	}


	public void setForwardUserID(long setForwardUserID)
	{
		this.ForwardUserID = setForwardUserID;
	}


	public String getForwardSourceURL()
	{
		return forwardSourceURL;
	}


	public void setForwardSourceURL(String forwardSourceURL)
	{
		this.forwardSourceURL = forwardSourceURL;
	}


	public long getId()
	{
		return id;
	}


	public void setId(long id)
	{
		this.id = id;
	}


	public long getUserId()
	{
		return userId;
	}


	public void setUserId(long userId)
	{
		this.userId = userId;
	}


	public String getUserName()
	{
		return userName;
	}


	public void setUserName(String userName)
	{
		this.userName = userName;
	}


	public Timestamp getCreatedAt()
	{
		return createdAt;
	}


	public void setCreatedAt(Timestamp createdAt)
	{
		this.createdAt = createdAt;
	}


	public String getText()
	{
		return text;
	}


	public void setText(String text)
	{
		this.text = text;
	}


	public int getRepostsCount()
	{
		return repostsCount;
	}


	public void setRepostsCount(int repostsCount)
	{
		this.repostsCount = repostsCount;
	}


	public int getCommentsCount()
	{
		return commentsCount;
	}


	public void setCommentsCount(int commentsCount)
	{
		this.commentsCount = commentsCount;
	}


	public String getSourceURL()
	{
		return sourceURL;
	}


	public void setSourceURL(String sourceURL)
	{
		this.sourceURL = sourceURL;
	}


	public String getPicURL()
	{
		return picURL;
	}


	public void setPicURL(String picURL)
	{
		this.picURL = picURL;
	}


	public String getForwardpicURL()
	{
		return forwardpicURL;
	}


	public void setForwardpicURL(String forwardpicURL)
	{
		this.forwardpicURL = forwardpicURL;
	}


	public String getForwardText()
	{
		return forwardText;
	}


	public void setForwardText(String forwardText)
	{
		this.forwardText = forwardText;
	}


	public String getForwardUserName()
	{
		return forwardUserName;
	}


	public void setForwardUserName(String forwardUserName)
	{
		this.forwardUserName = forwardUserName;
	}


	public long getForwardStatusID()
	{
		return forwardStatusID;
	}


	public void setForwardStatusID(long forwardStatusID)
	{
		this.forwardStatusID = forwardStatusID;
	}


	public int compareTo(HibernateStatus o)
	{
		if (this.id > o.id)
			return -1;
		if (this.id < o.id) { return 1; }
		return 0;
	}
}
