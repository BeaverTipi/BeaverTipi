package kr.or.ddit.util.notifications.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.NotificationVO;

public interface NotificationsService {
	public Integer readTotalNotificationCount(String mbrCd);
	public void createNotificationApprove(NotificationVO notification);
	public void createNotificationReject(NotificationVO notification);
	
	public List<NotificationVO> readNotificationList(String username,PaginationInfo<NotificationVO> paging);
	public List<NotificationVO> readNotificationList(String username);
	
	public NotificationVO readAndReturn(String notifId);

}
