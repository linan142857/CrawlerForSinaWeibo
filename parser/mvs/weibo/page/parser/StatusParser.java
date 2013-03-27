package mvs.weibo.page.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/*     */ import java.util.List;

import mvs.weibo.page.login.LoginEntryClass;
import mvs.weibo.page.service.status.StatusServiceImpl;
import mvs.weibo.page.util.html.HtmlParserUtil;
import mvs.weibo.page.util.html.TimeUtil;
import mvs.weibo.page.util.html.UnicodeUtf8;
import mvs.weibo.page.vo.status.HibernateStatus;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class StatusParser
{
	private static Logger logger = Logger.getLogger(StatusParser.class);
    static int k = 0;
	public List<HibernateStatus> getListFromText(String text)
	{
		List<HibernateStatus> rtn = new ArrayList<HibernateStatus>();
		int begin = text.indexOf("\"data\":") + 8;
		int end = text.lastIndexOf("\"");
		text = text.substring(begin, end);
		text = UnicodeUtf8.unicode2utf8(text);
		try
		{
			BufferedWriter w = new BufferedWriter(new FileWriter( (k++)  + ".html"));
			w.write("\n" + text);
			w.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> blockList = HtmlParserUtil.getCodeBlockList(text, "div",  "diss-data=\"group_source=group_all\"");//获得含有该串的所有text的列表
		k++;
		for (int i = 0; i < blockList.size(); i++)
		{
			if(blockList.get(i) != null)
			{	String str = (String)blockList.get(i);
				HibernateStatus hs = string2hsNoIsForward(str);
				if (hs != null)
				{
					rtn.add(hs);
				}
			}
		}

		for (int i = 0; i < rtn.size() - 1; )
		{
			if (((HibernateStatus)rtn.get(i)).getId() == ((HibernateStatus)rtn.get(i + 1)).getId())
				rtn.remove(i + 1);
			else
				i++;
		}
		return rtn;
     }
 
   private HibernateStatus string2hsNoIsForward(String text)
   {
	   HibernateStatus rtn = new HibernateStatus();
	   if (!text.contains("feed_list_item") || text.contains("act_id")) return null;
	   try
	   {
		   rtn.setId(Long.valueOf(HtmlParserUtil.getSpecSubStrFromStr(text, "mid=\"", "\"")).longValue());
		   rtn.setUserName(HtmlParserUtil.getSpecSubStrFromStr(text, "&name=", "&"));
		   rtn.setUserId(Long.valueOf(HtmlParserUtil.getSpecSubStrFromStr(text, "&uid=", "&")).longValue());
		   
		   String resourcetext;
		   if(text.contains("node-type=\"feed_list_content\" nick-name"))
		   {
			   resourcetext = HtmlParserUtil.getSpecSubStrFromStr(text, "node-type=\"feed_list_content\" nick-name=\"" + rtn.getUserName() + "\">", "</div>");
		   }
		   else
		   {
			   resourcetext = HtmlParserUtil.getSpecSubStrFromStr(text, "node-type=\"feed_list_content\">", "</div>");
		   }
		   resourcetext = resourcetext.replaceAll("<[^>]*>", "");
		   rtn.setText(resourcetext);
		   List<String> pichd = HtmlParserUtil.getCodeBlockList(text,"ul", "feed_list_media_prev");
		   if(pichd.size() > 0)
		   {
			   String picture = HtmlParserUtil.getSpecSubStrFromStr(pichd.get(0), "src=\"", "\"");
				   //HtmlParserUtil.getSpecSubStrFromStr(text,"bigcursor\" node-type=\"feed_list_media_bgimg\" src=\"", "\"");
			   if(picture != null) rtn.setPicURL(picture);
		   }
		   
		   
		   if(text.contains("node-type=\"feed_list_reason\""))
		   {
			   String forwardtext = HtmlParserUtil.getSpecSubStrFromStr(text, "class=\"WB_text\" node-type=\"feed_list_reason\">","</div>");
			   forwardtext = forwardtext.replaceAll("<[^>]*>", "");
			   String data = HtmlParserUtil.getSpecSubStrFromStr(text, "rootmid", "</div>");
			   rtn.setForwardText(forwardtext);
			   rtn.setForwardUserID(Long.parseLong(HtmlParserUtil.getSpecSubStrFromStr(text, "rootuid=", "&")));
			   rtn.setForwardUserName(HtmlParserUtil.getSpecSubStrFromStr(text, "rootname=", "&"));
			   rtn.setForwardStatusID(Long.parseLong(HtmlParserUtil.getSpecSubStrFromStr(text, "rootmid=", "&")));
			   rtn.setForwardSourceURL(HtmlParserUtil.getSpecSubStrFromStr(data, "rooturl=", "&"));
			   rtn.setSourceURL(HtmlParserUtil.getSpecSubStrFromStr(data, "url=", "&"));
			   String Comments = HtmlParserUtil.getSpecSubStrFromStr(data, ">评论", "</a>");
			   if(Comments.contains("("))
			   {
			   	rtn.setCommentsCount(Integer.parseInt(Comments.substring(1, Comments.length()-1)));
			   }
			   else rtn.setCommentsCount(0);
			   String Reposts = HtmlParserUtil.getSpecSubStrFromStr(data, ">转发", "</a>");
			   if(Reposts.contains("("))
			   {
			   	rtn.setRepostsCount(Integer.parseInt(Reposts.substring(1, Reposts.length()-1)));
			   }
			   else rtn.setRepostsCount(0);
			   String[] timesource = text.split("<div class=\"WB_from\">");
			   String Created = HtmlParserUtil.getSpecSubStrFromStr(HtmlParserUtil.getSpecSubStrFromStr(timesource[2], "<div class=\"WB_from\">", "</a>"), "title=\"", "\"");
			   rtn.setCreatedAt(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Created).getTime()));
			   System.out.println(Created);
		   }
		   else
		   {
			   String dataList = HtmlParserUtil.getCodeBlockList(text, "div",  "WB_func clearfix").get(0);
			   rtn.setSourceURL(HtmlParserUtil.getSpecSubStrFromStr(dataList, "url=", "&"));
			   String Comments = HtmlParserUtil.getSpecSubStrFromStr(dataList, "评论", "</a>");
			   if(Comments.contains("("))
			   {
			   	rtn.setCommentsCount(Integer.parseInt(Comments.substring(1, Comments.length()-1)));
			   }
			   else rtn.setCommentsCount(0);
			   String Reposts = HtmlParserUtil.getSpecSubStrFromStr(dataList, "转发", "</a>");
			   if(Reposts.contains("("))
			   {
			   	rtn.setRepostsCount(Integer.parseInt(Reposts.substring(1, Reposts.length()-1)));
			   }
			   else rtn.setRepostsCount(0);
			   String Created = HtmlParserUtil.getSpecSubStrFromStr(HtmlParserUtil.getSpecSubStrFromStr(dataList, "<div class=\"WB_from\">", "</a>"), "title=\"", "\"");
			   rtn.setCreatedAt(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Created).getTime()));
			   System.out.println(Created);
		   }
	   }
	   catch (Exception e)	
	   {
		   logger.error("解析微博产生错误" + text, e);
	   }
	   return rtn;
   }
   
private HibernateStatus string2hsWithIsForward(String rep)
/*     */   {
/* 180 */     HibernateStatus rtn = string2hsNoIsForward(rep);
/*     */ 
/*     */ 
/* 184 */     if (!rep.contains("rootmid=")) {
/* 185 */       rtn.setForwardText("此微博已被删除。");
/*     */     }
/*     */     else {
/* 188 */       rtn.setForwardStatusID(Long.parseLong(
/* 189 */         HtmlParserUtil.getSpecSubStrFromStr(rep, "rootmid=", "&")));
/*     */ 
/* 192 */       List pList = getNodeListString(rep, "dt", 
/* 193 */         "feed_list_forwardContent");
/* 194 */       String forwardtext = ((Node)pList.get(0)).toPlainTextString();
/* 195 */       forwardtext.replaceFirst(":", "");
/* 196 */       rtn.setForwardText(forwardtext);
/*     */       try
/*     */       {
/* 200 */         Parser parser = new Parser(rep);
/* 201 */         HasAttributeFilter hasAttributeFilter = new HasAttributeFilter(
/* 202 */           "class", "piclist");
/* 203 */         HasChildFilter hasChildFilter = new HasChildFilter(
/* 204 */           hasAttributeFilter);
/* 205 */         NodeList nodeList = parser
/* 206 */           .extractAllNodesThatMatch(hasChildFilter);
/* 207 */         if (nodeList.elementAt(0) != null) {
/* 208 */           Node node = nodeList.elementAt(0);
/* 209 */           parser.setResource(node.toHtml());
/* 210 */           hasAttributeFilter.setAttributeValue("bigcursor");
/* 211 */           nodeList = parser
/* 212 */             .extractAllNodesThatMatch(hasAttributeFilter);
/* 213 */           if (nodeList.elementAt(0) != null) {
/* 214 */             String imgTag = nodeList.elementAt(0).toHtml();
/* 215 */             String forwardpicURL = 
/* 216 */               HtmlParserUtil.getSpecSubStrFromStr(imgTag, "src=\"", "\"");
/* 217 */             forwardpicURL = forwardpicURL.replaceAll("thumbnail", 
/* 218 */               "bmiddle");
/* 219 */             rtn.setForwardpicURL(forwardpicURL);
/* 220 */             rtn.setForwardText(forwardtext + "【转发微博图片地址：" + 
/* 221 */               forwardpicURL + "】");
/*     */           }
/*     */         }
/*     */       } catch (ParserException e1) {
/* 225 */         e1.printStackTrace();
/*     */       }
/*     */       try
/*     */       {
/* 229 */         if (rep.contains("isForward")) {
/* 230 */           int rootmidBegin = rep.indexOf("&rootmid=") + 9;
/* 231 */           int rootmidEnd = rep.indexOf("&rootname", rootmidBegin);
/* 232 */           if (rootmidBegin != 8) {
/* 233 */             String rootmid = rep
/* 234 */               .substring(rootmidBegin, rootmidEnd);
///* 235 */             rtn.setParentId(Long.valueOf(rootmid).longValue());
///* 236 */             rtn.setSourceStatusId(Long.valueOf(rootmid).longValue());
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 240 */         logger.error("解析微博产生错误" + rep, e);
/*     */       }
/*     */     }
/* 243 */     return rtn;
/*     */   }
/*     */ 
/*     */   private List<Node> getNodeListString(String text, String type, String key)
/*     */   {
/* 258 */     Parser parser = new Parser();
/* 259 */     NodeFilter filter = new TagNameFilter(type);
/* 260 */     NodeList nodeList = null;
/*     */     try {
/* 262 */       parser.setInputHTML(text);
/*     */ 
/* 264 */       nodeList = parser.extractAllNodesThatMatch(filter);
/*     */     } catch (ParserException e) {
/* 266 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 269 */     List rtn = new ArrayList();
/* 270 */     for (int i = 0; i < nodeList.size(); i++) {
/* 271 */       Node node = nodeList.elementAt(i);
/* 272 */       if (key == null) {
/* 273 */         rtn.add(node);
/*     */       } else {
/* 275 */         String strTemp = node.toHtml();
/* 276 */         if (strTemp.contains(key)) {
/* 277 */           rtn.add(node);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 282 */     return rtn;
/*     */   }


			public List<HibernateStatus> getIDFromText(String text)//正则
/*     */   {
/*  35 */     List rtn = new ArrayList();
/*     */ 
/*  38 */     int begin = text.indexOf("action-type=\"itmeClick\" action-data=\"uid=") + 8;
/*  39 */     int end = text.lastIndexOf("&fnick");
/*  40 */     text = text.substring(begin, end);
/*  41 */     text = UnicodeUtf8.unicode2utf8(text);
/*  42 */     List blockList = HtmlParserUtil.getCodeBlockList(text, "dl", 
/*  43 */       "action-type=\"itemClick\"");
/*  44 */     for (int i = 0; i < blockList.size(); i++) {
/*  45 */       String str = (String)blockList.get(i);
/*  46 */       if (str.contains("itemClick")) {
/*  47 */         HibernateStatus hs = string2hsWithIsForward(str);
/*  48 */         if (hs != null)
/*  49 */           rtn.add(hs);
/*     */       }
/*     */       else {
/*  52 */         HibernateStatus hs = string2hsNoIsForward(str);
/*  53 */         if (hs != null) {
/*  54 */           rtn.add(hs);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  59 */     for (int i = 0; i < rtn.size() - 1; ) {
/*  60 */       if (((HibernateStatus)rtn.get(i)).getId() == ((HibernateStatus)rtn.get(i + 1)).getId())
/*  61 */         rtn.remove(i + 1);
/*     */       else {
/*  63 */         i++;
/*     */       }
/*     */     }
/*     */ 
/*  67 */     return rtn;
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.parser.StatusParser
 * JD-Core Version:    0.6.1
 */