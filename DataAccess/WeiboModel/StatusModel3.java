package WeiboModel;

import WeiboModel.StatusModel2;

import java.io.Serializable;
import java.util.Date;

/**
 * statusModel2 with discuss and transmit number
 * @author Yaoxu
 * */
public class StatusModel3 extends StatusModel2 implements Serializable{
	protected int discuss;
	protected int transmit;
	
	public StatusModel3() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatusModel3(String topic, String text, String iD,
			Date createdTime, Date insertTime, int discuss, int transmit) {
		super(topic,text,iD, createdTime, insertTime);
		this.discuss = discuss;
		this.transmit = transmit;
	}

	public int getDiscuss() {
		return discuss;
	}

	public void setDiscuss(int discuss) {
		this.discuss = discuss;
	}

	public int getTransmit() {
		return transmit;
	}

	public void setTransmit(int transmit) {
		this.transmit = transmit;
	}
	
	
}
