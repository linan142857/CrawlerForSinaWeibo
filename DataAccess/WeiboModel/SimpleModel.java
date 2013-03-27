/**
 * @author Yaoxu
 * */
package WeiboModel;

import java.io.Serializable;

/**
 * @author Yaoxu
 * The simple model of weibo status, contains only status and its topic,
 *  the topic can be null
 * */
public class SimpleModel implements Serializable{
	protected String topic;
	protected String text;
	public SimpleModel(String topic, String text) {
		super();
		this.topic = topic;
		this.text = text;
	}
	public SimpleModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	
	
}
