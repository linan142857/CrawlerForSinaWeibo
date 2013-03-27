package mvs.weibo.page.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.util.html.UnicodeUtf8;
import mvs.weibo.page.vo.status.HibernateStatus;

public class IdParser {
	public IdParser() {

	}

	public static List<String> getIDFromText(String text) {
		List<String> rtn = new ArrayList();
		List<String> subtext = new ArrayList();
		String regEx = "action-data=\\\\\"uid=[(\\d)*?]+&fnick";
		/** 根据URl从文本中抽取和UID有关的子文本**/
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		while (m.find()) {
			// System.out.println("Mathc:" + m.group());
			subtext.add(m.group());
		}
		
//		System.out.println(subtext);

		/** 获得UID**/
		CharSequence[] cs = subtext.toArray(new CharSequence[subtext.size()]);
		for (int i = 0; i < cs.length; i++) {
//			System.out.println(cs[i]);
			String uids = "\\d+";
			Pattern p1 = Pattern.compile(uids);
			Matcher mat = p1.matcher(cs[i]);
			while (mat.find()) {
//				System.out.println("Mathc:" + mat.group());
				rtn.add(mat.group());
			}
		}
		return rtn;
	}

	public static String getPageFromText(String text) {
		List<String> lastPage = new ArrayList<String>();
		List<String> subtext = new ArrayList<String>();
		int index=0;
		//follow?page=100">100</a>
		String regEx = "follow\\?page=\\d+\\\\\">";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
//		System.out.println(text);
		while (m.find()) {
//			System.out.println("Mathc:" + m.group());
			subtext.add(m.group());
		}
		
		CharSequence[] cs = subtext.toArray(new CharSequence[subtext.size()]);
		for (int i = 0; i < cs.length; i++) {
//			System.out.println(cs[i]);
			String uids = "\\d+";
			Pattern p1 = Pattern.compile(uids);
			Matcher mat = p1.matcher(cs[i]);
			while (mat.find()) {
//				System.out.println("Mathc:" + mat.group());
				lastPage.add(mat.group());
			}
		}
		String maxPage = lastPage.get(0);
		int maximum = Integer.parseInt(maxPage) ;
		while(index < lastPage.size()) {
			if(Integer.parseInt(lastPage.get(index)) > maximum) {
				maxPage = lastPage.get(index);
			}
			index++;
		}
		System.out.println();
		return maxPage;
		
	}
	
	public static void main(String[] args) {
		/**
		String s = "<li class=\\\"clearfix W_linecolor\\\" action-type=\\\"itmeClick\\\" action-data=\\\"uid=1713458220&fnick=\\\"itmeClick\\\" action-data=\\\"uid=1713458220&fnick";
		String regEx = "action-data=\\\\\"uid=[(\\d)*?]+&fnick";
		String uids = "\\d+";

		// Pattern p = Pattern.compile(regEx);
		// Matcher m = p.matcher(s);

		Pattern p1 = Pattern.compile(uids);
		// System.out.println(uids);
		Matcher m1 = p1.matcher("action-data=\"uid=1713458220&fnick");
		while (m1.find()) {
			System.out.println("Mathc:" + m1.group());
		}
		*/
		String regEx = "follow\\?page=\\d+\\\\\">";
		String text="<a action-data=\\\"\\/1191979833\\/follow?page=&page=3\\\" href=\\\"\\/1191979833\\/follow?page=3\\\">3<\\/a>";
	
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		while (m.find()) {
			System.out.println("Mathc:"+m.group());
		}
	}
	
}
