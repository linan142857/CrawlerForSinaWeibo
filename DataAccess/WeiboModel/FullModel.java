package WeiboModel;

import java.io.Serializable;
import java.util.Date;

public class FullModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3774146905857315263L;

	protected String statusID;
	protected String userID;
	protected String username;
	protected String URL;
	protected Date createTime;
	protected String text;
	protected int commentNum;
	protected int retweetNum;
	protected String sourcetext;
	protected String sourceID;
	protected String sourceuserID;
	protected String sourceusername;
	protected String sourceURL;
	protected String status_bmiddle_pic;
	protected String status_original_pic;
	protected String thumnail_pic;
	
	
	public FullModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public FullModel(String statusID, String userID, Date createTime,
			String text, int commentNum, int retweetNum) {
		super();
		this.statusID = statusID;
		this.userID = userID;
		this.createTime = createTime;
		this.text = text;
		this.commentNum = commentNum;
		this.retweetNum = retweetNum;
	}


	public FullModel(String statusID, String userID, String username, Date createTime,
			String text, int commentNum, int retweetNum,
			String status_bmiddle_pic, String status_original_pic,
			String status_source, String thumnail_pic, String URL, String sourcetext, String sourceid, String sourceuserid, String sourceusername, String sourceurl)
	{
		super();
		this.statusID = statusID;
		this.userID = userID;
		this.username = username;
		this.createTime = createTime;
		this.text = text;
		this.commentNum = commentNum;
		this.retweetNum = retweetNum;
		this.status_bmiddle_pic = status_bmiddle_pic;
		this.status_original_pic = status_original_pic;
		this.thumnail_pic = thumnail_pic;
		this.sourceID = sourceid;
		this.sourcetext = sourcetext;
		this.sourceURL = sourceurl;
		this.URL = URL;
	}
	
	

	public String getUsername()
	{
		return username;
	}


	public void setUsername(String username)
	{
		this.username = username;
	}


	public String getURL()
	{
		return URL;
	}


	public void setURL(String uRL)
	{
		URL = uRL;
	}


	public String getSourcetext()
	{
		return sourcetext;
	}


	public void setSourcetext(String sourcetext)
	{
		this.sourcetext = sourcetext;
	}


	public String getSourceID()
	{
		return sourceID;
	}


	public void setSourceID(String sourceID)
	{
		this.sourceID = sourceID;
	}


	public String getSourceuserID()
	{
		return sourceuserID;
	}


	public void setSourceuserID(String sourceuserID)
	{
		this.sourceuserID = sourceuserID;
	}


	public String getSourceusername()
	{
		return sourceusername;
	}


	public void setSourceusername(String sourceusername)
	{
		this.sourceusername = sourceusername;
	}


	public String getSourceURL()
	{
		return sourceURL;
	}


	public void setSourceURL(String sourceURL)
	{
		this.sourceURL = sourceURL;
	}


	public String getStatusID() {
		return statusID;
	}


	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getCommentNum() {
		return commentNum;
	}


	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}


	public int getRetweetNum() {
		return retweetNum;
	}


	public void setRetweetNum(int retweetNum) {
		this.retweetNum = retweetNum;
	}


	public String getStatus_bmiddle_pic() {
		return status_bmiddle_pic;
	}


	public void setStatus_bmiddle_pic(String status_bmiddle_pic) {
		this.status_bmiddle_pic = status_bmiddle_pic;
	}


	public String getStatus_original_pic() {
		return status_original_pic;
	}


	public void setStatus_original_pic(String status_original_pic) {
		this.status_original_pic = status_original_pic;
	}



	public String getThumnail_pic() {
		return thumnail_pic;
	}


	public void setThumnail_pic(String thumnail_pic) {
		this.thumnail_pic = thumnail_pic;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
