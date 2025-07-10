package kr.or.ddit.main.subscribe.service;

import java.util.List;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.TenancyVO;

public interface SubscribeSubsriptionService {
	public SolutionSubscriptionVO readSolutionSubscription(String username);
	public List<SolutionVO>	readSolutionList();
	public SolutionVO readSolution(String solId);
	public void createSolutionSubscription(SolutionSubscriptionVO solitionSubVO);
	public List<SolutionVO> readCommonCodeSolutionList(String sol);
	public void createBroker(BrokerVO broker);
	public void createTenancy(TenancyVO tenancy);
	
	public SolutionSubscriptionVO checkedSolutionSubscription(String username, String SolutionCode);
	public List<SolutionSubscriptionVO> checkedSolutionSubscriptionList(String username);
}
