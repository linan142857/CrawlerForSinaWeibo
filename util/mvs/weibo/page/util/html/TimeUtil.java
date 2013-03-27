/*     */ package mvs.weibo.page.util.html;
/*     */ 
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class TimeUtil
/*     */ {
/*  12 */   private static Logger logger = Logger.getLogger(TimeUtil.class);
/*     */ 
/*     */   public Timestamp getInnerTime(String time)
/*     */   {
/*  19 */     String oldTime = time;
/*  20 */     Timestamp rtn = null;
/*     */     try {
/*  22 */       if ((time.indexOf('(') != -1) || (time.indexOf(65288) != -1)) {
/*  23 */         time = time.substring(1, time.length() - 1);
/*     */       }
/*     */ 
/*  26 */       String regex = "[0-9]{1,2}月[0-9]{1,2}日 [0-9]{1,2}:[0-9]{1,2}";
/*  27 */       Pattern pattern = Pattern.compile(regex);
/*  28 */       Matcher matcher = pattern.matcher(time);
/*  29 */       boolean b = matcher.matches();
/*  30 */       Calendar calendar = Calendar.getInstance();
/*  31 */       if (b) {
/*  32 */         int yearm = calendar.get(1);
/*  33 */         String year = yearm+"";
/*  34 */         time = year + "-" + time + ":00";
/*  35 */         time = time.replace("月", "-").replace("日 ", " ");
/*  36 */         return getNoFormatTime(time);
/*     */       }
/*     */ 
/*  40 */       regex = "今天 [0-9]{1,2}:[0-9]{1,2}";
/*  41 */       pattern = Pattern.compile(regex);
/*  42 */       matcher = pattern.matcher(time);
/*  43 */       b = matcher.matches();
/*  44 */       if (b) {
/*  45 */         int yearm = calendar.get(1);
/*  46 */         int monthm = calendar.get(2) + 1;
/*  47 */         int datem = calendar.get(5);
/*  48 */         time = yearm + "-" + monthm + "-" + datem + time.replace("今天 ", " ") + ":00";
/*  49 */         return getNoFormatTime(time);
/*     */       }
/*     */ 
/*  53 */       regex = "[0-9]*分钟前";
/*  54 */       pattern = Pattern.compile(regex);
/*  55 */       matcher = pattern.matcher(time);
/*  56 */       b = matcher.matches();
/*  57 */       if (b) {
/*  58 */         String beforeMinuteStr = time.substring(0, time.length() - 3);
/*  59 */         int beforeMinute = Integer.valueOf(beforeMinuteStr).intValue();
/*  60 */         calendar.add(12, -beforeMinute);
/*  61 */         return new Timestamp(calendar.getTimeInMillis());
/*     */       }
/*     */ 
/*  65 */       regex = "[0-9]*秒前";
/*  66 */       pattern = Pattern.compile(regex);
/*  67 */       matcher = pattern.matcher(time);
/*  68 */       b = matcher.matches();
/*  69 */       if (b) {
/*  70 */         String beforeSecondStr = time.substring(0, time.length() - 3);
/*  71 */         int beforeSecond = Integer.valueOf(beforeSecondStr).intValue();
/*  72 */         calendar.add(13, -beforeSecond);
/*  73 */         return new Timestamp(calendar.getTimeInMillis());
/*     */       }
/*     */ 
/*  78 */       regex = "[0-9]*-[0-9]*-[0-9]* [0-9]*:[0-9]*";
/*  79 */       pattern = Pattern.compile(regex);
/*  80 */       matcher = pattern.matcher(time);
/*  81 */       b = matcher.matches();
/*  82 */       if (b) {
/*  83 */         time = time + ":00";
/*  84 */         return getNoFormatTime(time);
/*     */       }
/*     */ 
/*  94 */       return getNoFormatTime(time);
/*     */     }
/*     */     catch (Exception e) {
/*  97 */       logger.error("解析时间产生错误" + oldTime + "转化成" + time, e);
/*     */     }
/*  99 */     return rtn;
/*     */   }
/*     */ 
/*     */   private Timestamp getNoFormatTime(String time)
/*     */   {
/* 109 */     String[] dayTime = time.split(" ");
/* 110 */     String strDay = dayTime[0];
/* 111 */     String strTime = dayTime[1];
/*     */ 
/* 113 */     String[] ymd = strDay.split("-");
/* 114 */     String y = ymd[0];
/* 115 */     if (y.length() != 4) {
/* 116 */       y = "20" + y;
/*     */     }
/* 118 */     String m = add02Str(ymd[1]);
/* 119 */     String d = add02Str(ymd[2]);
/*     */ 
/* 121 */     String[] hms = strTime.split(":");
/* 122 */     String hh = add02Str(hms[0]);
/* 123 */     String mm = add02Str(hms[1]);
/* 124 */     String ss = add02Str(hms[2]);
/*     */ 
/* 126 */     time = y + "-" + m + "-" + d + " " + hh + ":" + mm + ":" + ss;
/* 127 */     Timestamp rtn = Timestamp.valueOf(time);
/* 128 */     return rtn;
/*     */   }
/*     */ 
/*     */   private String add02Str(String str) {
/* 132 */     if (str.length() == 1) {
/* 133 */       return "0" + str;
/*     */     }
/* 135 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.util.html.TimeUtil
 * JD-Core Version:    0.6.1
 */