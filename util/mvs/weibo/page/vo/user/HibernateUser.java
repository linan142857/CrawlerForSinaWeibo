/*     */ package mvs.weibo.page.vo.user;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.sql.Timestamp;
/*     */ 
/*     */ public class HibernateUser
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4637110151328142066L;
/*  14 */   private long userID = 0L;
/*     */   private String screenName;
/*     */   private String province;
/*     */   private String city;
/*     */   private String location;
/*     */   private String description;
/*     */   private String url;
/*     */   private String profileImageUrl;
/*     */   private String userDomain;
/*     */   private String gender;
/*     */   private int followersCount;
/*     */   private int friendsCount;
/*     */   private int statusesCount;
/*     */   private int favouritesCount;
/*     */   private Timestamp createdAt;
/*     */   private String createdAtStr;
/*     */   private int verified;
/*     */   private VeriReason verified_reason;
/*     */   private int biFollowersCount;
/*     */   private String remark;
/*     */   private Timestamp inDBTime;
/*     */ 
/*     */   public int getBiFollowersCount()
/*     */   {
/*  37 */     return this.biFollowersCount;
/*     */   }
/*     */ 
/*     */   public String getCity() {
/*  41 */     return this.city;
/*     */   }
/*     */ 
/*     */   public Timestamp getCreatedAt() {
/*  45 */     return this.createdAt;
/*     */   }

			public int getVerified()
			{
				return this.verified;
			}
/*     */ 
/*     */   public String getCreatedAtStr() {
/*  49 */     return this.createdAtStr;
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/*  53 */     return this.description;
/*     */   }
/*     */ 
/*     */   public int getFavouritesCount() {
/*  57 */     return this.favouritesCount;
/*     */   }
/*     */ 
/*     */   public int getFollowersCount() {
/*  61 */     return this.followersCount;
/*     */   }
/*     */ 
/*     */   public int getFriendsCount() {
/*  65 */     return this.friendsCount;
/*     */   }
/*     */   public String getGender() {
/*  68 */     return this.gender;
/*     */   }
/*     */   public Timestamp getInDBTime() {
/*  71 */     return this.inDBTime;
/*     */   }
/*     */   public String getLocation() {
/*  74 */     return this.location;
/*     */   }
/*     */   public String getProfileImageUrl() {
/*  77 */     return this.profileImageUrl;
/*     */   }
/*     */   public String getProvince() {
/*  80 */     return this.province;
/*     */   }
/*     */   public String getRemark() {
/*  83 */     return this.remark;
/*     */   }
/*     */   public String getScreenName() {
/*  86 */     return this.screenName;
/*     */   }
/*     */   public int getStatusesCount() {
/*  89 */     return this.statusesCount;
/*     */   }
/*     */   public String getUrl() {
/*  92 */     return this.url;
/*     */   }
/*     */   public String getUserDomain() {
/*  95 */     return this.userDomain;
/*     */   }
/*     */   public long getUserID() {
/*  98 */     return this.userID;
/*     */   }
/*     */   public VeriReason getVerified_reason() {
/* 101 */     return this.verified_reason;
/*     */   }
///*     */   public int isVerified() {
///* 104 */     return this.verified;
///*     */   }
/*     */   public void setBiFollowersCount(int biFollowersCount) {
/* 107 */     this.biFollowersCount = biFollowersCount;
/*     */   }
/*     */   public void setCity(String city) {
/* 110 */     this.city = city;
/*     */   }
/*     */   public void setCreatedAt(Timestamp createdAt) {
/* 113 */     this.createdAt = createdAt;
/*     */   }
/*     */   public void setCreatedAtStr(String createdAtStr) {
/* 116 */     this.createdAtStr = createdAtStr;
/*     */   }
/*     */   public void setDescription(String description) {
/* 119 */     this.description = description;
/*     */   }
/*     */   public void setFavouritesCount(int favouritesCount) {
/* 122 */     this.favouritesCount = favouritesCount;
/*     */   }
/*     */   public void setFollowersCount(int followersCount) {
/* 125 */     this.followersCount = followersCount;
/*     */   }
/*     */   public void setFriendsCount(int friendsCount) {
/* 128 */     this.friendsCount = friendsCount;
/*     */   }
/*     */   public void setGender(String gender) {
/* 131 */     this.gender = gender;
/*     */   }
/*     */   public void setInDBTime(Timestamp inDBTime) {
/* 134 */     this.inDBTime = inDBTime;
/*     */   }
/*     */   public void setLocation(String location) {
/* 137 */     this.location = location;
/*     */   }
/*     */   public void setProfileImageUrl(String profileImageUrl) {
/* 140 */     this.profileImageUrl = profileImageUrl;
/*     */   }
/*     */   public void setProvince(String province) {
/* 143 */     this.province = province;
/*     */   }
/*     */   public void setRemark(String remark) {
/* 146 */     this.remark = remark;
/*     */   }
/*     */   public void setScreenName(String screenName) {
/* 149 */     this.screenName = screenName;
/*     */   }
/*     */   public void setStatusesCount(int statusesCount) {
/* 152 */     this.statusesCount = statusesCount;
/*     */   }
/*     */   public void setUrl(String url) {
/* 155 */     this.url = url;
/*     */   }
/*     */   public void setUserDomain(String userDomain) {
/* 158 */     this.userDomain = userDomain;
/*     */   }
/*     */   public void setUserID(long userID) {
/* 161 */     this.userID = userID;
/*     */   }
/*     */   public void setVerified(int verified) {
/* 164 */     this.verified = verified;
/*     */   }
/*     */   public void setVerified_reason(VeriReason verified_reason) {
/* 167 */     this.verified_reason = verified_reason;
/*     */   }
/*     */   public String toString() {
/* 170 */     return "userID=" + this.userID + ",screenName=" + this.screenName + ",url=" + this.url + 
/* 171 */       ",关注数=" + this.friendsCount + ",粉丝数=" + this.followersCount + ",微博数=" + this.statusesCount + 
/* 172 */       ",地址=" + this.location + ",创建时间=" + this.createdAtStr;
/*     */   }
/*     */ 
/*     */   public static enum VeriReason
/*     */   {
/*   8 */     GOVERNMENT, ENTERPRISE, PERSON;
/*     */   }
/*     */ }

/* Location:           C:\Users\Yaoxu\SkyDrive\backup+hit\bin\
 * Qualified Name:     org.cbapple.weibo.page.vo.user.HibernateUser
 * JD-Core Version:    0.6.1
 */