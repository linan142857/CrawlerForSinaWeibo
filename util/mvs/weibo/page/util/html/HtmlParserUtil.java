/*     */ package mvs.weibo.page.util.html;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.htmlparser.Node;
/*     */ import org.htmlparser.NodeFilter;
/*     */ import org.htmlparser.Parser;
/*     */ import org.htmlparser.filters.TagNameFilter;
/*     */ import org.htmlparser.util.NodeList;
/*     */ import org.htmlparser.util.ParserException;
/*     */ 
/*     */ public class HtmlParserUtil
/*     */ {
/*     */   public static String getSpecSubStrFromStr(String text, String beginString, String endString)
/*     */   {
/*  35 */     int begin = text.indexOf(beginString) + beginString.length();
/*  36 */     int end = text.indexOf(endString, begin);
/*  37 */     String rtn = text.substring(begin, end);
/*  38 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static NodeList getNodeListFromHtml(String inputHTML, NodeFilter filter)
/*     */   {
/*  50 */     Parser parser = new Parser();
/*  51 */     NodeList nodeList = null;
/*     */     try {
/*  53 */       parser.setInputHTML(inputHTML);
/*     */ 
/*  55 */       nodeList = parser.extractAllNodesThatMatch(filter);
/*     */     } catch (ParserException e) {
/*  57 */       e.printStackTrace();
/*     */     }
/*  59 */     return nodeList;
/*     */   }
/*     */ 
/*     */   public static String getCodeBlock(String text, String type, String key)
/*     */   {
/*  74 */     NodeFilter filter = new TagNameFilter(type);
/*  75 */     NodeList nodeList = getNodeListFromHtml(text, filter);
/*  76 */     String rtn = "";
/*  77 */     for (int i = 0; i < nodeList.size(); i++) {
/*  78 */       Node node = nodeList.elementAt(i);
/*  79 */       String strTemp = node.toHtml();
/*  80 */       if (strTemp.indexOf(key) != -1) {
/*  81 */         rtn = strTemp;
/*  82 */         break;
/*     */       }
/*     */     }
/*  85 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static List<String> getCodeBlockList(String text, String type, String key)
/*     */   {
/* 101 */     NodeFilter filter = new TagNameFilter(type);
/* 102 */     NodeList nodeList = getNodeListFromHtml(text, filter);
/* 103 */     List rtn = new ArrayList();
/* 104 */     for (int i = 0; i < nodeList.size(); i++) {
/* 105 */       Node node = nodeList.elementAt(i);
/* 106 */       String strTemp = node.toHtml();
/* 107 */       strTemp = UnicodeUtf8.unicode2utf8(strTemp);
/* 108 */       if (strTemp.indexOf(key) != -1) {
/* 109 */         rtn.add(strTemp);
/*     */       }
/*     */     }
/* 112 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static String getHtmlFromScript(String text)
/*     */   {
/* 122 */     String rtn = "";
/* 123 */     String beginStr = "\"html\":\"";
/* 124 */     String endStr = "</script>";
/* 125 */     int begin = text.indexOf(beginStr) + beginStr.length();
/* 126 */     int end = text.lastIndexOf(endStr, text.length());
/* 127 */     rtn = text.substring(begin, end);
/* 128 */     return rtn;
/*     */   }
/*     */ 
/*     */   public static String deleteAIMG(String source)
/*     */   {
/* 139 */     int endIndex = source.indexOf('>');
/* 140 */     int beginIndex = 0;
/* 141 */     while (endIndex != -1) {
/* 142 */       String temp = source.substring(0, endIndex);
/* 143 */       beginIndex = temp.lastIndexOf('<');
/* 144 */       if ((beginIndex == -1) || (beginIndex > endIndex))
/*     */       {
/*     */         break;
/*     */       }
/* 148 */       String str_0 = source.substring(0, beginIndex);
/* 149 */       String str_2 = source.substring(endIndex + 1);
/*     */ 
/* 151 */       source = str_0 + str_2;
/* 152 */       endIndex = source.indexOf('>');
/*     */     }
/*     */ 
/* 155 */     return source;
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.util.html.HtmlParserUtil
 * JD-Core Version:    0.6.1
 */