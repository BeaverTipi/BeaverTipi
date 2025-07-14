package kr.or.ddit.util.notifications.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.NotificationVO;
@Mapper
public interface NotificationsMapper {
	public Integer insertNotificationApprove(NotificationVO notification);
	public Integer insertNotificationReject(NotificationVO notification);
	public List<NotificationVO> selectNotificationList(String username);
	public Integer selectTotalNotificationCount(String username);
	public List<NotificationVO> selectNotificationListPaging(@Param("username") String username, @Param("firstRecordIndex") int firstRecordIndex,
		    @Param("lastRecordIndex") int lastRecordIndex);
	public NotificationVO selectNotification(String notifId);
	public Integer updateReadYn(String notifId);
}	
