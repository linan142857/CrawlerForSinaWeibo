package WeiboModel;


import java.io.Serializable;

public class FreqItemModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7201690842522053422L;
	private String wordText;
	private int value;
	public FreqItemModel(String wordText, int value) {
		super();
		this.wordText = wordText;
		this.value = value;
	}
	public FreqItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getWordText() {
		return wordText;
	}
	public void setWordText(String wordText) {
		this.wordText = wordText;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
