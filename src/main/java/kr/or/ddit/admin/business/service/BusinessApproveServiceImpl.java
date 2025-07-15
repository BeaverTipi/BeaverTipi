package kr.or.ddit.admin.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.BusinessApproveMapper;
import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.validate.exception.ApprovedException;
import kr.or.ddit.util.validate.exception.RejectedException;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BusinessApproveServiceImpl implements BusinessApproveService {
	private final BusinessApproveMapper mapper;
	private final NotificationsService service;
	@Override
	public Integer readTotalRecord(PaginationInfo<BusinessApproveSearchVO> paging) {
		// TODO Auto-generated method stub
		return mapper.selectTotalRecord(paging);
	}

	@Override
	public List<MemberVO> readBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging) {
		// TODO Auto-generated method stub
		return mapper.selectBusinessApproveList(paging);
	}

	@Override
	public void approveMember(String mbrCd, String userType) {
	 
		if ("BROKER".equalsIgnoreCase(userType)) {
			if(mapper.updateBrokerApprove(mbrCd)<1) {
				throw new ApprovedException();
			}
			NotificationVO notification = notificationApprove(mbrCd, "중개인","/broker");
			service.createNotificationApprove(notification);
		}else if ("TENANCY".equalsIgnoreCase(userType)){
			if(mapper.updateTenancyApprove(mbrCd)<1) {
				throw new ApprovedException();
			}
			NotificationVO notification = notificationApprove(mbrCd,"임대인","/tenancy");
			service.createNotificationApprove(notification);
		}else {
			throw new ApprovedException();
		}
	}

	@Override
	public void rejectMember(String mbrCd, String userType) {
		if ("BROKER".equalsIgnoreCase(userType)) {
			if(mapper.updateBrokerReject(mbrCd)<1)throw new RejectedException();
			NotificationVO notification = notificationReject(mbrCd,"중개인","/apply/broker");
			service.createNotificationReject(notification);
		}else if ("TENANCY".equalsIgnoreCase(userType)){
			if(mapper.updateTenancyReject(mbrCd)<1)throw new RejectedException();
			NotificationVO notification = notificationReject(mbrCd,"임대인","/apply/tenancy");
			service.createNotificationReject(notification);
		}else {
			throw new RejectedException();
		}
	}
	
	private NotificationVO notificationApprove(String mbrCd,String role, String url) {
		NotificationVO notification = new NotificationVO();
		notification.setMbrCd(mbrCd);
		notification.setNotifMsg(role + " 승인 완료 되었습니다. 결제를 진행해주세요");
		notification.setNotifTitle(role + " 승인 완료");
		notification.setNotifRefUrl("/payment/business" + url);
		return notification;
	}
	private NotificationVO notificationReject(String mbrCd,String role, String url) {
		NotificationVO notification = new NotificationVO();
		notification.setMbrCd(mbrCd);
		notification.setNotifMsg(role + " 승인 거절 되었습니다. 서류가 미비합니다. 다시 제출해주세요.");
		notification.setNotifTitle(role + " 승인 거절");
		notification.setNotifRefUrl(url);
		return notification;
	}

}
