package mvs.weibo.page.parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mvs.weibo.page.service.BaseService;
import mvs.weibo.page.util.html.HtmlParserUtil;
import mvs.weibo.page.util.html.UnicodeUtf8;
import mvs.weibo.page.vo.user.HibernateUser;
import mvs.weibo.page.vo.user.HibernateUser.VeriReason;

import org.apache.log4j.Logger;

import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class UserInfoParser {
	private static Logger logger = Logger.getLogger(UserInfoParser.class);

	public HibernateUser getUserInfo(String text) {
		HibernateUser hu = new HibernateUser();

		getGeneralInfo(hu, text);

		// if (text.contains("$userversion    : \"4\"")) {
		// hu.setVerified_reason(HibernateUser.VeriReason.GOVERNMENT);
		//
		// getGovernInfo(hu, text);
		// } else if (text.contains("['any'] = \"1\"")) {
		// hu.setVerified_reason(HibernateUser.VeriReason.ENTERPRISE);
		//
		// getEnterInfo(hu, text);
		// } else {
		// hu.setVerified_reason(HibernateUser.VeriReason.PERSON);
		//
		// getPersonInfo(hu, text);
		// }
		return hu;
	}

	public HibernateUser getGeneralInfo(HibernateUser hu, String text) {
		String pl_profile_info = "";

		if (text.contains("pl_profile_hisInfo")) {
			String pl_profile_hisInfo = UnicodeUtf8.unicode2utf8(HtmlParserUtil
					.getCodeBlock(text, "script", "pl_profile_hisInfo"));
			pl_profile_info = pl_profile_hisInfo;
		} else if (text.contains("pl_profile_myInfo")) {
			String pl_profile_myInfo = UnicodeUtf8.unicode2utf8(HtmlParserUtil
					.getCodeBlock(text, "script", "pl_profile_myInfo"));
			pl_profile_info = pl_profile_myInfo;
		} else {
			System.out.println("用户信息不存在！");
			return null;
		}

		String pl_profile_extraInfo = UnicodeUtf8.unicode2utf8(HtmlParserUtil
				.getCodeBlock(text, "script", "pl_profile_extraInfo"));
		String pl_profile_photo = UnicodeUtf8.unicode2utf8(HtmlParserUtil
				.getCodeBlock(text, "script", "pl_profile_photo"));
		String pl_profile_infoFind = UnicodeUtf8.unicode2utf8(HtmlParserUtil
				.getCodeBlock(text, "script", "pl_profile_infoFind"));
		if (pl_profile_extraInfo == null || pl_profile_photo == null
				|| pl_profile_infoFind == null) {
			System.out.println("用户信息不存在！");
			return null;
		}

//		System.out.println(pl_profile_extraInfo);
//		System.out.println(pl_profile_info);
//		System.out.println(pl_profile_infoFind);
//		System.out.println(pl_profile_photo);
		//用户id
		String id = HtmlParserUtil.getSpecSubStrFromStr(pl_profile_info, "href=\"http://level.account.weibo.com/u/?id=", "\"");
		hu.setUserID(Long.parseLong(id));
//		System.out.println(id);
		// 注意以下均应当可以为空
		// 用户名
		if (pl_profile_info.contains("<span class=\"name\">")) {
			String name = HtmlParserUtil.getSpecSubStrFromStr(pl_profile_info,
					"<span class=\"name\">", "<");
//			System.out.println("用户姓名：" + name);
			hu.setScreenName(name);
		}

		// 主页地址
		if (pl_profile_info.contains("class=\"pf_lin S_link1\">")) {
			String url = HtmlParserUtil.getSpecSubStrFromStr(pl_profile_info,
					"class=\"pf_lin S_link1\">", "<");
//			System.out.println("用户主页：" + url.trim());
			if (url != null) {
				hu.setUrl(url);	
			}
		}

		// 图片地址
		if (pl_profile_photo.contains("<img src=\"")) {
			hu.setProfileImageUrl(HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_photo, "<img src=\"", "\""));
		}
		// 性别
		if (pl_profile_info.contains("W_ico12 male")) {
			hu.setGender("男");
		} else if (pl_profile_info.contains("W_ico12 female")) {
			hu.setGender("女");
		}
		// 位置（两级不一定全有）
		if (pl_profile_info.contains("infsign")) {

			String allLocation = HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_info, "infsign\" title=\"", "\"");

//			System.out.println(allLocation.trim());
			if (allLocation != null) {
				String[] locs = allLocation.split(" ");
				if (locs.length >= 1) {
					hu.setProvince(locs[0]);
					if (locs.length >= 2) {
						hu.setCity(locs[1]);
					}
				}
			}
		}

		// 用户类型：企业；加V用户；普通用户 0， 2， 4
		if (pl_profile_info.contains("approve_co"))
			hu.setVerified(4);
		else if (pl_profile_info.contains("approve"))
			hu.setVerified(2);
		else
			hu.setVerified(0);
//		System.out.print("用户类型：" + hu.getVerified() + "\n");

		// 用户自我评价
//		System.out.println(pl_profile_info);
		if (pl_profile_info.contains("<div class=\"pf_intro bsp\">") && !pl_profile_info.contains("她还没有填写个人简介"))
		{
			String description = HtmlParserUtil.getCodeBlock(pl_profile_info, "S_txt2", "S_txt2");
//			System.out.println("自我评价：" + description.trim());
			if (description != null) {
				hu.setDescription(description);
			}
		}
		else hu.setDescription("");

		// 加V用户认证信息
		if (pl_profile_info.contains("<i title= \"")) {
			String remark = HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_info, "<i title= \"", "\"");
//			System.out.println("认证信息：" + remark.trim());
			if (remark != null) {
				hu.setRemark(remark);
			}
		}

		// 粉丝数
		if (pl_profile_photo.contains("<strong node-type=\"fans\">")) {
			String fanscount = HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_photo, "<strong node-type=\"fans\">", "<");
//			System.out.println(fanscount.trim());

			if (fanscount != null) {
				hu.setFollowersCount(Integer.parseInt(fanscount));
			}
		}
		// 朋友数
		if (pl_profile_photo.contains("<strong node-type=\"follow\">")) {
			String friendcount = HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_photo, "<strong node-type=\"follow\">", "<");
//			System.out.println(friendcount.trim());

			if (friendcount != null) {
				hu.setFriendsCount(Integer.parseInt(friendcount));
			}
		}
		// 微博数
		if (pl_profile_photo.contains("<strong node-type=\"weibo\">")) {
			String tweetcount = HtmlParserUtil.getSpecSubStrFromStr(
					pl_profile_photo, "<strong node-type=\"weibo\">", "<");
//			System.out.println(tweetcount.trim());

			if (tweetcount != null) {
				hu.setStatusesCount(Integer.parseInt(tweetcount));
			}
		}

		return null;
	}

	/*     */
	/*     */private HibernateUser getEnterInfo(HibernateUser hu, String text)
	/*     */{
		/* 99 */hu.setUserID(Long.valueOf(
				HtmlParserUtil.getSpecSubStrFromStr(text,
				/* 100 */"$CONFIG['oid'] = '", "'")).longValue());

		hu.setScreenName(String.valueOf(HtmlParserUtil.getSpecSubStrFromStr(
				text, "$CONFIG['onick'] = \"", "\";")));

		String text1 = UnicodeUtf8.unicode2utf8(HtmlParserUtil.getCodeBlock(
				text, "script", "pl_leftNav_profilePersonal"));
		System.out.println(text1);

		hu.setUrl(HtmlParserUtil.getSpecSubStrFromStr(text1, "href=\"",
				"\" title="));
		hu.setProfileImageUrl(HtmlParserUtil.getSpecSubStrFromStr(text1,
				"<img src=\"", "\"></a><div class=\"state"));
		/* 103 */text = UnicodeUtf8.unicode2utf8(HtmlParserUtil.getCodeBlock(
				text,
				/* 104 */"script", "pl_content_litePersonInfo"));
		/*     */
		/* 107 */hu.setFriendsCount(Integer.valueOf(
				HtmlParserUtil.getSpecSubStrFromStr(
				/* 108 */text, "follow\"><strong>", "<")).intValue());
		/* 112 */hu.setFollowersCount(Integer.valueOf(
				/* 113 */HtmlParserUtil.getSpecSubStrFromStr(text,
						"fans\"><strong>", "<")).intValue());
		/* 117 */hu.setStatusesCount(Integer.valueOf(
				/* 118 */HtmlParserUtil.getSpecSubStrFromStr(text,
						"type=0\"><strong>", "<")).intValue());
		/*     */

		// System.out.println("&&*)"+text);
		//
		// String regEx = " node-type=.*title";
		// /** 根据URl从文本中抽取和UID有关的子文本**/
		// Pattern p = Pattern.compile(regEx);
		// Matcher m = p.matcher(text);
		// while (m.find()) {
		// // System.out.println("Mathc:" + m.group());
		// System.out.println("##"+m.group());
		// }

		/* 120 */return hu;
		/*     */}

	private HibernateUser getGovernInfo(HibernateUser hu, String text) {
		String userIDString = HtmlParserUtil.getSpecSubStrFromStr(text,
		/* 64 */"$oid\t\t\t: \"", "\"");
		/* 65 */hu.setUserID(Long.valueOf(userIDString).longValue());
		/*     */
		/* 67 */String url = "http://gov.weibo.com/mblog/aj_eps_getuserinfo.php?oid="
				+
				/* 68 */userIDString + "&rnd=" + Math.random();
		/* 69 */text = BaseService.getPageContentFromUrl(url, "");

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + text);
		/*     */try
		/*     */{
			/* 73 */JSONObject jsonObject = new JSONObject(text);
			/* 74 */jsonObject = (JSONObject) jsonObject.get("data");
			/*     */
			/* 76 */hu.setFriendsCount(jsonObject.getInt("attentions"));
			/*     */
			/* 79 */hu.setFollowersCount(jsonObject.getInt("myfans"));
			/*     */
			/* 82 */hu.setStatusesCount(jsonObject.getInt("mblogs"));
			/*     */}
		/*     */catch (JSONException e) {
			/* 85 */e.printStackTrace();
			/*     */}
		/* 87 */return hu;
		/*     */}

	/*     */
	/*     */private HibernateUser getPersonInfo(HibernateUser hu, String text)
	/*     */{
		/* 132 */if (text.contains("['any'] = \"&wvr=3.6\""))
		/*     */{
			/* 134 */String personalInfo = HtmlParserUtil.getCodeBlock(text,
					"script",
					/* 135 */"\"pid\":\"pl_content_personInfo\"");
			/* 136 */if (!"".equals(personalInfo))
			/*     */{
				/* 138 */personalInfo = UnicodeUtf8.unicode2utf8(personalInfo);
				/* 139 */getMySelfInfo(hu, personalInfo);
				/*     */}
			/*     */else {
				/* 142 */personalInfo = HtmlParserUtil.getCodeBlock(text,
						"script",
						/* 143 */"pl_content_hisPersonalInfo");
				/* 144 */personalInfo = UnicodeUtf8.unicode2utf8(personalInfo);
				/*     */
				/* 146 */String litePersonalInfo = HtmlParserUtil.getCodeBlock(
						text,
						/* 147 */"script", "pl_content_litePersonInfo");
				/* 148 */litePersonalInfo = UnicodeUtf8
						.unicode2utf8(litePersonalInfo);
				/*     */
				/* 150 */getOthersUserInfo36(hu, personalInfo);
				/* 151 */getOthersLiteInfo(hu, litePersonalInfo);
				/*     */}
			/*     */}
		/*     */else {

			// String face =
			// UnicodeUtf8.unicode2utf8(HtmlParserUtil.getCodeBlock( text,
			// "a","face"));
			// hu.setProfileImageUrl(HtmlParserUtil.getSpecSubStrFromStr(face,
			// "\"><img src=\"", "\" width=\"150"));

			/* 155 */String personalInfo = HtmlParserUtil.getCodeBlock(text,
					"script", "pl_content_hisPersonalInfo");
			System.out.println(HtmlParserUtil.getCodeBlock(text, "script",
					"pl_content_hisPersonalInfo"));

			/* 157 */if ("".equals(personalInfo))
			/*     */{
				/* 159 */personalInfo = HtmlParserUtil.getCodeBlock(text, "ul",
				/* 160 */"user_atten clearfix");
				/* 161 */getMySelfInfo(hu, personalInfo);
				/*     */}
			/*     */else {
				/* 164 */personalInfo = UnicodeUtf8.unicode2utf8(personalInfo);
				/*     */
				hu.setUrl(HtmlParserUtil.getSpecSubStrFromStr(personalInfo,
						"online\">", "<"));

				hu.setDescription(HtmlParserUtil.getSpecSubStrFromStr(
						personalInfo, "<p class=\"text\">简介：", "<a"));

				hu.setGender(HtmlParserUtil.getSpecSubStrFromStr(personalInfo,
						" title=\"", "\">&nbsp;"));

				hu.setRemark(HtmlParserUtil.getSpecSubStrFromStr(personalInfo,
						"transparent.gif\" title= \"", "\" alt="));

				hu.setVerified(hu.getVerified());

				String allLocation = HtmlParserUtil.getSpecSubStrFromStr(
						personalInfo, "&nbsp;", "<");
				System.out.println(allLocation.trim());

				if (allLocation != null) {
					String[] locs = allLocation.split("，");
					if (locs[0] != null) {
						hu.setProvince(locs[0]);
						if (locs[1] != null) {
							hu.setCity(locs[1]);
						}
					}
				}
				/* 166 */String litePersonalInfo = HtmlParserUtil.getCodeBlock(
						text,
						/* 167 */"script", "pl_content_litePersonInfo");
				/* 168 */litePersonalInfo = UnicodeUtf8
						.unicode2utf8(litePersonalInfo);
				/*     */
				/* 170 */getOthersUserInfo40(hu, personalInfo);
				/* 171 */getOthersLiteInfo(hu, litePersonalInfo);
				/*     */}
			/*     */}
		/*     */
		/* 175 */return hu;
		/*     */}

	/*     */
	/*     */private void getMySelfInfo(HibernateUser user, String source)
	/*     */{
		/*     */try
		/*     */{
			/* 187 */long userId = Long.valueOf(
					HtmlParserUtil.getSpecSubStrFromStr(source,
							"<li><a href=\"/", "/follow?")).longValue();
			/* 188 */user.setUserID(userId);
			/*     */
			/* 190 */int feedNum = Integer.valueOf(
					HtmlParserUtil.getSpecSubStrFromStr(source,
							"<strong node-type=\"weibo\">", "</")).intValue();
			/* 191 */user.setStatusesCount(feedNum);
			/*     */}
		/*     */catch (Exception e) {
			/* 194 */logger.error("解析自己的信息产生错误：" + source, e);
			/*     */}
		/*     */}

	/*     */
	/*     */private void getOthersUserInfo36(HibernateUser user, String source)
	/*     */{
		/*     */try
		/*     */{
			/* 208 */user.setUserID(Long.valueOf(
					HtmlParserUtil.getSpecSubStrFromStr(
					/* 209 */source, "sinaimg.cn/", "/")).longValue());
			/*     */
			/* 211 */user.setScreenName(HtmlParserUtil.getSpecSubStrFromStr(
					source,
					/* 212 */"alt=\"", "\""));
			/*     */
			/* 214 */String url = HtmlParserUtil.getSpecSubStrFromStr(source,
			/* 215 */"online\">", "</a>");
			/* 216 */user.setUrl(url);
			/* 217 */user.setUserDomain(url);
			/*     */
			/* 219 */int beginGen = source.indexOf("title=\"男\">") + 10;
			/* 220 */user.setGender("m");
			/* 221 */if (beginGen == 9) {
				/* 222 */beginGen = source.indexOf("title=\"女\">") + 10;
				/* 223 */user.setGender("f");
				/*     */}
			/* 225 */int endGen = source.indexOf("</p>", beginGen);
			/* 226 */String local = source.substring(beginGen, endGen).trim();
			/* 227 */user.setLocation(local);
			/*     */} catch (Exception e) {
			/* 229 */logger.error("解析获得用户统计信息出错，开始采用摘点的方式解析：" + source, e);
			/*     */try {
				/* 231 */user.setUserID(Long.valueOf(
						/* 232 */HtmlParserUtil.getSpecSubStrFromStr(source,
								"\"uid=", "&")).longValue());
				/*     */
				/* 234 */user.setScreenName(HtmlParserUtil
						.getSpecSubStrFromStr(source,
						/* 235 */"&nick=", "\""));
				/*     */}
			/*     */catch (Exception ee) {
				/* 238 */logger.error("解析再次产生错误：" + source, ee);
				/*     */try
				/*     */{
					/* 241 */user.setUserID(Long.valueOf(
					/* 242 */HtmlParserUtil.getSpecSubStrFromStr(source,
					/* 243 */"http://photo.weibo.com/", "/")).longValue());
					/*     */
					/* 245 */user.setScreenName(HtmlParserUtil
							.getSpecSubStrFromStr(
							/* 246 */source, "title=\"", "\""));
					/*     */}
				/*     */catch (Exception eee) {
					/* 249 */logger.error("按头像解析产生错误：" + source, eee);
					/*     */}
				/*     */}
			/*     */}
		/*     */}

	/*     */
	/*     */private void getOthersUserInfo40(HibernateUser user,
			String personalInfo)
	/*     */{
		/*     */try
		/*     */{
			/* 265 */user.setScreenName(HtmlParserUtil.getSpecSubStrFromStr(
					personalInfo, "class=\"left\">", "<").trim());
			/*     */
			/* 269 */user.setUserID(Long.valueOf(
					HtmlParserUtil.getSpecSubStrFromStr(
					/* 270 */personalInfo,
					/* 271 */"href=\"http://level.account.weibo.com/u/?id=",
							"\"")).longValue());
			/*     */}
		/*     */catch (Exception e)
		/*     */{
			/* 274 */logger.error("个人基本信息解析错误(4.0版)", e);
			/*     */}
		/*     */}

	/*     */
	/*     */private void getOthersLiteInfo(HibernateUser user, String html)
	/*     */{
		/* 288 */user.setFriendsCount(Integer.valueOf(
				/* 289 */HtmlParserUtil.getSpecSubStrFromStr(html,
						"node-type=\"follow\">", "<")).intValue());
		/*     */
		/* 292 */user.setFollowersCount(Integer.valueOf(
				/* 293 */HtmlParserUtil.getSpecSubStrFromStr(html,
						"node-type=\"fans\">", "<")).intValue());
		/*     */
		/* 296 */user.setStatusesCount(Integer.valueOf(
				/* 297 */HtmlParserUtil.getSpecSubStrFromStr(html,
						"node-type=\"weibo\">", "<")).intValue());
		/*     */}
	/*     */
}

/*
 * Location: C:\Users\Yaoxu\SkyDrive\backup+hit\bin\ Qualified Name:
 * org.cbapple.weibo.page.parser.UserInfoParser JD-Core Version: 0.6.1
 */