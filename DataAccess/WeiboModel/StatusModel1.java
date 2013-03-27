package WeiboModel;

import java.io.Serializable;

/**
 * stores the text, most-likely topic, and the ID of a status
 * @author Yaoxu
 * */
public class StatusModel1 extends SimpleModel implements Serializable{
	protected String ID;
	public StatusModel1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StatusModel1(String topic, String text, String iD) {
		super(topic, text);
		ID = iD;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	
}
