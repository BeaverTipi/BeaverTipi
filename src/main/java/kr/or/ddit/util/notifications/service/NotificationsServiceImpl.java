/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 14.     			윤현식            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.util.notifications.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.util.notifications.mapper.NotificationsMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.validate.exception.NotificationsException;
import kr.or.ddit.vo.NotificationVO;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author 
 * @since
 * @see
 *
 *
 */
@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl implements NotificationsService {
	private final NotificationsMapper mapper;
	@Override
	public void createNotificationApprove(NotificationVO notification) {
		if(mapper.insertNotificationApprove(notification)<1) {
			throw new NotificationsException("승인 알람을 넣던중 오류가 발생했습니다.");
		}
	}

	@Override
	public void createNotificationReject(NotificationVO notification) {
		if(mapper.insertNotificationReject(notification)<1) {
			throw new NotificationsException("거절 알람을 넣던중 오류가 발생했습니다.");
		}
	}

	@Override
	public List<NotificationVO> readNotificationList(String username,PaginationInfo<NotificationVO> paging) {
		
		return mapper.selectNotificationListPaging(username,paging.getFirstRecordIndex(),paging.getLastRecordIndex());
	}
	@Override
	public NotificationVO readAndReturn(String notifId) {
	    NotificationVO noti = mapper.selectNotification(notifId);
	    if (noti != null && !noti.isNotifReadYn()) {
	    	mapper.updateReadYn(notifId);
	    }
	    return noti;
	}

	@Override
	public Integer readTotalNotificationCount(String username) {
		// TODO Auto-generated method stub
		return mapper.selectTotalNotificationCount(username);
	}

	@Override
	public List<NotificationVO> readNotificationList(String username) {
		// TODO Auto-generated method stub
		return mapper.selectNotificationList(username);
	}


}
