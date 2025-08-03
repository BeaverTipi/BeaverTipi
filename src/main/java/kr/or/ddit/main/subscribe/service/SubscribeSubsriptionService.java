package kr.or.ddit.main.subscribe.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.TenancyVO;

public interface SubscribeSubsriptionService {
	public SolutionSubscriptionVO readSolutionSubscription(String username);
	public List<SolutionVO>	readSolutionList();
	public SolutionVO readSolution(String solId);
	public List<SolutionVO> readCommonCodeSolutionList(String sol);
	public void createBroker(BrokerVO broker);
	public BrokerVO readBroker(String username);
	public TenancyVO readTenancy(String username);
	public void createTenancy(TenancyVO tenancy);
	public int checkedBrokerCount(String username);
	public int checkedTenancyCount(String username);
	public SolutionSubscriptionVO checkedSolutionSubscription(String username, String SolutionCode);
	public List<SolutionSubscriptionVO> checkedSolutionSubscriptionList(String username);
	void savePaymentResult(Map<String, Object> data,String mbrCd,String solId,String role,HttpServletRequest req, HttpServletResponse resp);
	void saveAutopayAndFirstPayment(Map<String, Object> result, String mbrCd, String customerKey, String role,
			String solId,HttpServletRequest req, HttpServletResponse resp);
}
