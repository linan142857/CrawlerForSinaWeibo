package mvs.weibo.page.service.status;

import java.util.List;
import javax.swing.JTextArea;

import mvs.weibo.page.vo.status.HibernateStatus;

public abstract interface IStatusService
{
  public abstract List<HibernateStatus> getStatusListByUid(String paramString);

  public abstract int getProcessNum();
}
