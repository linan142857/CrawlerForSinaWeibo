package WeiboModel;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * stores the text, most-likely topic, createdTime, insertTime and the ID of a status
 * can be used particularly with NLPIR 23W
 * @author Yaoxu
 * */
public class StatusModel2 extends StatusModel1 implements Serializable{

	protected Date createdTime;
	protected Date insertTime;
	protected double topicPossibility;
	public StatusModel2(String topic, String text, String iD,
			Date createdTime, Date insertTime) {
		super(topic, text, iD);
		this.createdTime = createdTime;
		this.insertTime = insertTime;
	}
	public StatusModel2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	public double getTopicPossibility(){
		return this.topicPossibility;
	}
	
	public void setTopicPossibility(double possibility){
		this.topicPossibility = possibility;
	}
}
