/*     */ package mvs.weibo.page.util.html;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class UnicodeUtf8
/*     */ {
/*  14 */   private static Logger logger = Logger.getLogger(UnicodeUtf8.class);
/*     */ 
/*     */   private static String escape(String src)
/*     */   {
/*  25 */     StringBuffer tmp = new StringBuffer();
/*  26 */     tmp.ensureCapacity(src.length() * 6);
/*     */ 
/*  28 */     for (int i = 0; i < src.length(); i++)
/*     */     {
/*  30 */       char j = src.charAt(i);
/*  31 */       if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || 
/*  32 */         (Character.isUpperCase(j))) {
/*  33 */         tmp.append(j);
/*  34 */       } else if (j < 'Ā') {
/*  35 */         tmp.append("\\");
/*  36 */         if (j < '\020')
/*  37 */           tmp.append("0");
/*  38 */         tmp.append(Integer.toString(j, 16));
/*     */       } else {
/*  40 */         tmp.append("\\u");
/*  41 */         tmp.append(Integer.toString(j, 16));
/*     */       }
/*     */     }
/*  44 */     return tmp.toString();
/*     */   }
/*     */ 
/*     */   private static String unescape(String src)
/*     */   {
/*  54 */     StringBuffer tmp = new StringBuffer();
/*     */     try {
/*  56 */       tmp.ensureCapacity(src.length());
/*  57 */       int lastPos = 0; int pos = 0;
/*     */ 
/*  59 */       while (lastPos < src.length())
/*     */       {
/*  61 */         pos = src.indexOf("\\u", lastPos);
/*  62 */         if (pos == lastPos) {
/*  63 */           if (src.charAt(pos + 1) == 'u') {
/*  64 */             char ch = (char)Integer.parseInt(
/*  65 */               src.substring(pos + 2, pos + 6), 16);
/*  66 */             tmp.append(ch);
/*  67 */             lastPos = pos + 6;
/*     */           } else {
/*  69 */             char ch = (char)Integer.parseInt(
/*  70 */               src.substring(pos + 1, pos + 3), 16);
/*  71 */             tmp.append(ch);
/*  72 */             lastPos = pos + 3;
/*     */           }
/*     */         }
/*  75 */         else if (pos == -1) {
/*  76 */           tmp.append(src.substring(lastPos));
/*  77 */           lastPos = src.length();
/*     */         } else {
/*  79 */           tmp.append(src.substring(lastPos, pos));
/*  80 */           lastPos = pos;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  85 */       logger.error("解码html出错：" + src, e);
/*  86 */       logger.error("部分解码结果：" + tmp.toString());
/*     */     }
/*  88 */     return tmp.toString();
/*     */   }
/*     */ 
/*     */   public static String unicode2utf8(String source)
/*     */   {
/*  99 */     if ((source == null) || ("".equals(source))) {
/* 100 */       return source;
/*     */     }
/* 102 */     source = source.replaceAll("\\\\/", "/").replaceAll("\\\\\"", "\"")
/* 103 */       .replaceAll("\\\\n", "").replaceAll("\\\\t", "")
/* 104 */       .replaceAll("\\\\r", "").replaceAll("&gt;", ">")
/* 105 */       .replaceAll("&quot;", "\"").replaceAll("&amp;", "&")
/* 106 */       .replaceAll("&lt;", "<");
/* 107 */     source = unescape(source);
/*     */ 
/* 109 */     return source;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 113 */     String tmp = "~!@#$%^&*()_+|\\=-,./?><;'][{}\"";
/* 114 */     System.out.println("testing escape : " + tmp);
/* 115 */     tmp = escape(tmp);
/* 116 */     System.out.println(tmp);
/* 117 */     tmp = "你好啊，我不是很确定";
/* 118 */     tmp = escape(tmp);
/* 119 */     System.out.println(tmp);
/*     */ 
/* 124 */     String test01 = "你好啊，我不是很确定";
/* 125 */     System.out.println(unescape(test01));
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.util.html.UnicodeUtf8
 * JD-Core Version:    0.6.1
 */