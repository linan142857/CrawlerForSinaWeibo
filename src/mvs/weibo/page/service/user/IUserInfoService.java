package mvs.weibo.page.service.user;

import mvs.weibo.page.vo.user.HibernateUser;

public abstract interface IUserInfoService
{
  public abstract HibernateUser getUserInfoByUrl(String paramString);
  public abstract String getUserInfoByUrl2(String url);
}
