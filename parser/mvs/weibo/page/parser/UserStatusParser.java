/*     */ package mvs.weibo.page.parser;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;

import mvs.weibo.page.util.html.HtmlParserUtil;
import mvs.weibo.page.util.html.UnicodeUtf8;
import mvs.weibo.page.vo.status.HibernateStatus;

/*     */ import org.apache.log4j.Logger;
/*     */ import org.htmlparser.Node;
/*     */ import org.htmlparser.NodeFilter;
/*     */ import org.htmlparser.filters.TagNameFilter;
/*     */ import org.htmlparser.tags.CompositeTag;
/*     */ import org.htmlparser.tags.LinkTag;
/*     */ import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
/*     */ 
/*     */ public class UserStatusParser
/*     */ {
/*  24 */   private static Logger logger = Logger.getLogger(UserStatusParser.class);
/*     */ 
/*     */   public static List<HibernateStatus> getStatusListFromScript(String html)
/*     */   {
/*  28 */     String nodeStr = getScriptNodeHtml(html);
/*  29 */     List rtn = getStatusList(nodeStr);
/*  30 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static List<HibernateStatus> getStatusListFromJson(String json) {
/*  34 */     json = UnicodeUtf8.unicode2utf8(json);
/*  35 */     List rtn = getStatusList(json);
/*  36 */     return rtn;
/*     */   }
/*     */ 
private static List getStatusList(String nodeStr)
{
    List rtn = new ArrayList();
    NodeList dlList = getNodeListByType("dl", nodeStr);
    List dlTempList = getNodeListByAttribute("action-type", "feed_list_item", dlList);
    HibernateStatus hsOut;
    for(Iterator iterator = dlTempList.iterator(); iterator.hasNext(); rtn.add(hsOut))
    {
        Node dlNode = (Node)iterator.next();
        String dlNodeStr = dlNode.toHtml();
        List twoStatus = getTwoStatus(dlNodeStr);
        String innerDL = "";
        String outDL = "";
        hsOut = null;
        HibernateStatus hsIn = null;
        if(twoStatus.size() == 2)
        {
            innerDL = (String)twoStatus.get(0);
            outDL = (String)twoStatus.get(1);
            hsOut = dealWithOuter(outDL);
        } else
        {
            outDL = (String)twoStatus.get(0);
            hsOut = dealWithOuter(outDL);
        }
    }

    return rtn;
}

/*     */ 
/*     */   private static HibernateStatus dealWithInner(String html, long mid) {
/*  68 */     HibernateStatus hs = null;
/*     */     try {
/*  70 */       hs = new HibernateStatus();
/*  71 */       hs.setId(mid);
/*     */ 
/*  73 */       NodeList dtList = getNodeListByType("dt", html);
/*  74 */       Node dtNode = dtList.elementAt(0);
/*  75 */       String dtNodeStr = dtNode.toHtml();
/*     */ 
/*  77 */       int beginUid = dtNodeStr.indexOf("usercard=\"") + 13;
/*  78 */       if (beginUid != 12) {
/*  79 */         int endUid = dtNodeStr.indexOf('"', beginUid);
/*  80 */         String uid = dtNodeStr.substring(beginUid, endUid);
/*  81 */         hs.setUserId(Long.valueOf(uid).longValue());
/*     */       }
/*     */ 
/*  84 */       int beginText = dtNodeStr.indexOf("<em>") + 4;
/*  85 */       int endText = dtNodeStr.indexOf("</em>");
/*  86 */       String text = dtNodeStr.substring(beginText, endText);
/*  87 */       hs.setText(text);
/*     */ 
/*  90 */       NodeList ddList = getNodeListByType("dd", html);
/*  91 */       Node ddNode = ddList.elementAt(1);
/*  92 */       if (ddNode != null) {
/*  93 */         String ddNodeStr = ddNode.toHtml();
/*     */ 
/*  95 */         int beginRepostsCount = ddNodeStr.indexOf("转发(") + 3;
/*  96 */         if (beginRepostsCount != 2) {
/*  97 */           int endRepostsCount = ddNodeStr.indexOf(')', beginRepostsCount);
/*  98 */           String repostsCount = ddNodeStr.substring(beginRepostsCount, endRepostsCount);
/*  99 */           hs.setRepostsCount(Integer.valueOf(repostsCount).intValue());
/*     */         }
/*     */ 
/* 102 */         int beginCommentsCount = ddNodeStr.indexOf("评论(") + 3;
/* 103 */         if (beginCommentsCount != 2) {
/* 104 */           int endCommentsCount = ddNodeStr.indexOf(')', beginCommentsCount);
/* 105 */           String commentsCount = ddNodeStr.substring(beginCommentsCount, endCommentsCount);
/* 106 */           hs.setCommentsCount(Integer.valueOf(commentsCount).intValue());
/*     */         }
/*     */ 
/* 109 */         LinkTag aTag = (LinkTag)getNodeByClass("a", "date", html);
/* 110 */         String time = aTag.getChildrenHTML();
///* 111 */         hs.setCreatedAt(time);
/* 112 */         Timestamp ttTime = getInnerTime(time);
/* 113 */         hs.setCreatedAt(ttTime);
/*     */       }
/*     */     } catch (Exception e) {
/* 116 */       logger.debug("解析in获得微博错误：" + html, e);
/*     */     }
/* 118 */     return hs;
/*     */   }
/*     */ 
/*     */   private static HibernateStatus dealWithOuter(String html)
/*     */   {
/* 157 */     HibernateStatus hs = null;
/*     */     try
/*     */     {
/* 160 */       hs = new HibernateStatus();
/* 161 */       int begin = html.indexOf("mid=\"") + 5;
/* 162 */       int end = html.indexOf('"', begin);
/* 163 */       String mid = html.substring(begin, end);
/* 164 */       hs.setId(Long.valueOf(mid).longValue());
/*     */ 
/* 166 */       NodeList pList = getNodeListByType("p", html);
/* 167 */       ParagraphTag pt_1 = (ParagraphTag)pList.elementAt(0);
/* 168 */       ParagraphTag pt_2 = (ParagraphTag)pList.elementAt(1);
/*     */ 
/* 170 */       String ptStr_1 = deleteAIMG(pt_1.getChildrenHTML().trim());
/* 171 */       hs.setText(ptStr_1);
/*     */ 
/* 173 */       String ptStr_2 = pt_2.getChildrenHTML().trim();
/* 174 */       int beginRepostsCount = ptStr_2.indexOf("转发(") + 3;
/* 175 */       if (beginRepostsCount != 2) {
/* 176 */         int endRepostsCount = ptStr_2.indexOf(')', beginRepostsCount);
/* 177 */         String repostsCount = ptStr_2.substring(beginRepostsCount, endRepostsCount);
/* 178 */         hs.setRepostsCount(Integer.valueOf(repostsCount).intValue());
/*     */       }
/*     */ 
/* 182 */       int beginCommentsCount = ptStr_2.indexOf("评论(") + 3;
/* 183 */       if (beginCommentsCount != 2) {
/* 184 */         int endCommentsCount = ptStr_2.indexOf(')', beginCommentsCount);
/* 185 */         String commentsCount = ptStr_2.substring(beginCommentsCount, endCommentsCount);
/* 186 */         hs.setCommentsCount(Integer.valueOf(commentsCount).intValue());
/*     */       }
/*     */ 
/* 189 */       int beginRootId = ptStr_2.indexOf("&rootmid=") + 9;
/* 190 */       if (beginRootId != 8) {
/* 191 */         int endRootId = ptStr_2.indexOf('&', beginRootId);
/* 192 */         String rootId = ptStr_2.substring(beginRootId, endRootId);
///* 193 */         hs.setSourceStatusId(Long.valueOf(rootId).longValue());
/*     */       }
/*     */ 
/* 196 */       int beginUid = ptStr_2.indexOf("&uid=") + 5;
/* 197 */       int endUid = ptStr_2.indexOf('&', beginUid);
/* 198 */       String uid = ptStr_2.substring(beginUid, endUid);
/* 199 */       hs.setUserId(Long.valueOf(uid).longValue());
/*     */ 
/* 201 */       List timeList = getNodeListByAttribute("a", "node-type", "feed_list_item_date", ptStr_2);
/* 202 */       if ((timeList != null) && (timeList.size() != 0)) {
/* 203 */         LinkTag aTag = (LinkTag)timeList.get(0);
/* 204 */         String time = aTag.getAttribute("title") + ":00";
///* 205 */         hs.setCreatedAtStr(time);
/* 206 */         hs.setCreatedAt(getInnerTime(time));
/*     */       }
/*     */     } catch (Exception e) {
/* 209 */       logger.debug("解析out获得微博错误：" + html, e);
/*     */     }
/*     */ 
/* 212 */     return hs;
/*     */   }
/*     */ 
/*     */   private static List<String> getTwoStatus(String nodeStr)
/*     */   {
/* 222 */     List rtn = new ArrayList();
/* 223 */     NodeList dlList = getNodeListByType("dl", nodeStr);
/*     */ 
/* 225 */     Node outerDL = dlList.elementAt(0);
/* 226 */     String outerDLStr = outerDL.toHtml();
/*     */ 
/* 228 */     Node innerDL = dlList.elementAt(1);
/* 229 */     if (innerDL != null) {
/* 230 */       String innerDLStr = innerDL.toHtml();
/*     */ 
/* 232 */       int begin = outerDLStr.indexOf("<dl", 3);
/* 233 */       int end = begin + innerDLStr.length();
/* 234 */       String str_1 = outerDLStr.substring(0, begin);
/* 235 */       String str_2 = outerDLStr.substring(end, outerDLStr.length());
/* 236 */       outerDLStr = str_1 + str_2;
/*     */ 
/* 238 */       rtn.add(innerDLStr);
/*     */     }
/* 240 */     rtn.add(outerDLStr);
/* 241 */     return rtn;
/*     */   }
/*     */ 
/*     */   private static String getScriptNodeHtml(String html)
/*     */   {
/* 251 */     String scriptTemp = "";
/* 252 */     NodeList scriptNodeList = getNodeListByType("script", html);
/* 253 */     for (int i = 0; i < scriptNodeList.size(); i++) {
/* 254 */       Node scriptNode = scriptNodeList.elementAt(i);
/* 255 */       scriptTemp = scriptNode.toHtml();
/* 256 */       if (scriptTemp.indexOf("pl_content_hisFeed") != -1) {
/* 257 */         scriptTemp = UnicodeUtf8.unicode2utf8(scriptTemp);
/* 258 */         break;
/*     */       }
/*     */     }
/* 261 */     return scriptTemp;
/*     */   }
/*     */ 
/*     */   protected static Node getNodeByAttribute(String attributeName, String attributeValue, NodeList nlist)
/*     */   {
/* 272 */     Node rtn = null;
/* 273 */     for (int i = 0; i < nlist.size(); i++) {
/* 274 */       CompositeTag temp = (CompositeTag)nlist.elementAt(i);
/* 275 */       if (attributeValue.equals(temp.getAttribute(attributeName))) {
/* 276 */         rtn = temp;
/*     */       }
/*     */     }
/* 279 */     return rtn;
/*     */   }
/*     */ 
/*     */   protected static List<Node> getNodeListByAttribute(String attributeName, String attributeValue, NodeList nlist)
/*     */   {
/* 290 */     List rtn = new ArrayList();
/* 291 */     for (int i = 0; i < nlist.size(); i++) {
/* 292 */       CompositeTag temp = (CompositeTag)nlist.elementAt(i);
/* 293 */       if (attributeValue.equals(temp.getAttribute(attributeName))) {
/* 294 */         rtn.add(temp);
/*     */       }
/*     */     }
/* 297 */     return rtn;
/*     */   }
/*     */ 
/*     */   protected static NodeList getNodeListByType(String tagType, String html)
/*     */   {
/* 307 */     NodeList list = null;
/*     */     try {
/* 309 */       NodeFilter filter = new TagNameFilter(tagType);
/* 310 */       list = HtmlParserUtil.getNodeListFromHtml(html, filter);
/*     */     } catch (Exception e) {
/* 312 */       logger.error("解析html获得[ " + tagType + " ]NodeList出错：" + html, e);
/*     */     }
/* 314 */     return list;
/*     */   }
/*     */ 
/*     */   protected static List<Node> getNodeListByTypeByClass(String tagType, String attributeValue, String html) {
/* 318 */     List rtn = new ArrayList();
/* 319 */     NodeList list = getNodeListByType(tagType, html);
/* 320 */     for (int i = 0; i < list.size(); i++) {
/* 321 */       CompositeTag temp = (CompositeTag)list.elementAt(i);
/* 322 */       if (attributeValue.equals(temp.getAttribute("class"))) {
/* 323 */         rtn.add(temp);
/*     */       }
/*     */     }
/* 326 */     return rtn;
/*     */   }
/*     */ 
/*     */   protected static Node getNodeByClass(String tagType, String attributeValue, String html)
/*     */   {
/* 337 */     NodeList tempList = getNodeListByType(tagType, html);
/* 338 */     Node rtn = getNodeByAttribute("class", attributeValue, tempList);
/* 339 */     return rtn;
/*     */   }
/*     */ 
/*     */   protected static List<Node> getNodeListByAttribute(String tagType, String attributeName, String attributeValue, String html) {
/* 343 */     NodeList tempList = getNodeListByType(tagType, html);
/* 344 */     List rtn = getNodeListByAttribute(attributeName, attributeValue, tempList);
/* 345 */     return rtn;
/*     */   }
/*     */ 
/*     */   protected static Node getNode(String tagType, String html)
/*     */   {
/* 355 */     NodeList temp = getNodeListByType(tagType, html);
/* 356 */     Node rtn = null;
/* 357 */     if (temp.size() > 0) {
/* 358 */       rtn = temp.elementAt(0);
/*     */     }
/* 360 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static String deleteAIMG(String source)
/*     */   {
/* 371 */     int begin = source.indexOf('<');
/* 372 */     while (begin != -1) {
/* 373 */       String str_01 = source.substring(0, begin);
/* 374 */       int end = source.indexOf('>') + 1;
/* 375 */       String str_02 = source.substring(end, source.length());
/* 376 */       source = str_01 + str_02;
/* 377 */       begin = source.indexOf('<');
/*     */     }
/*     */ 
/* 380 */     return source;
/*     */   }
/*     */ 
/*     */   public static Timestamp getInnerTime(String time)
/*     */   {
/* 389 */     Timestamp rtn = null;
/*     */     try {
/* 391 */       if ((time.indexOf('(') != -1) || (time.indexOf(65288) != -1)) {
/* 392 */         time = time.substring(1, time.length() - 1);
/*     */       }
/*     */ 
/* 395 */       String regex = "[0-9]{2}月[0-9]{2}日 [0-9]{2}:[0-9]{2}";
/* 396 */       Pattern pattern = Pattern.compile(regex);
/* 397 */       Matcher matcher = pattern.matcher(time);
/* 398 */       boolean b = matcher.matches();
/* 399 */       Calendar calendar = Calendar.getInstance();
/* 400 */       if (b) {
/* 401 */         int yearm = calendar.get(1);
/* 402 */         String year = (new StringBuilder(String.valueOf(yearm))).toString();
/* 403 */         time = (new StringBuilder(String.valueOf(year))).append("-").append(time).append(":00").toString();
/* 404 */         time = time.replace("月", "-").replace("日 ", " ");
/* 405 */         return Timestamp.valueOf(time);
/*     */       }
/*     */ 
/* 409 */       regex = "今天 [0-9]{2}:[0-9]{2}";
/* 410 */       pattern = Pattern.compile(regex);
/* 411 */       matcher = pattern.matcher(time);
/* 412 */       b = matcher.matches();
/* 413 */       if (b) {
/* 414 */         int yearm = calendar.get(1);
/* 415 */         int monthm = calendar.get(2) + 1;
/* 416 */         int datem = calendar.get(5);
/* 417 */         time = yearm + "-" + monthm + "-" + datem + time.replace("今天 ", " ") + ":00";
/* 418 */         return Timestamp.valueOf(time);
/*     */       }
/*     */ 
/* 422 */       regex = "[0-9]*分钟前";
/* 423 */       pattern = Pattern.compile(regex);
/* 424 */       matcher = pattern.matcher(time);
/* 425 */       b = matcher.matches();
/* 426 */       if (b) {
/* 427 */         String beforeMinuteStr = time.substring(0, time.length() - 3);
/* 428 */         int beforeMinute = Integer.valueOf(beforeMinuteStr).intValue();
/* 429 */         calendar.add(12, -beforeMinute);
/* 430 */         return new Timestamp(calendar.getTimeInMillis());
/*     */       }
/*     */ 
/* 434 */       regex = "[0-9]*秒前";
/* 435 */       pattern = Pattern.compile(regex);
/* 436 */       matcher = pattern.matcher(time);
/* 437 */       b = matcher.matches();
/* 438 */       if (b) {
/* 439 */         String beforeSecondStr = time.substring(0, time.length() - 3);
/* 440 */         int beforeSecond = Integer.valueOf(beforeSecondStr).intValue();
/* 441 */         calendar.add(13, -beforeSecond);
/* 442 */         return new Timestamp(calendar.getTimeInMillis());
/*     */       }
/*     */ 
/* 452 */       return Timestamp.valueOf(time);
/*     */     }
/*     */     catch (Exception e) {
/* 455 */       logger.error("解析时间产生错误" + time, e);
/*     */     }
/* 457 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 461 */     String str = "asdf<111222>3333<nihao>henhao<qiyou>ab";
/* 462 */     System.out.println(deleteAIMG(str));
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.parser.UserStatusParser
 * JD-Core Version:    0.6.1
 */