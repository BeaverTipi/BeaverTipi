package kr.or.ddit.main.subscribe.service;

import java.util.List;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.RoleAchievedVO;
import kr.or.ddit.vo.SolutionSubscriptionPaymentVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.SolutionnSubscriptionAutopayMethodVO;
import kr.or.ddit.vo.TenancyVO;

public interface SubscribeSubsriptionService {
	public SolutionSubscriptionVO readSolutionSubscription(String username);
	public List<SolutionVO>	readSolutionList();
	public SolutionVO readSolution(String solId);
	public void createSolutionSubscription(SolutionSubscriptionVO solitionSubVO);
	public List<SolutionVO> readCommonCodeSolutionList(String sol);
	public void createBroker(BrokerVO broker);
	public BrokerVO readBroker(String username);
	public TenancyVO readTenancy(String username);
	public void createTenancy(TenancyVO tenancy);
	public int checkedBrokerCount(String username);
	public int checkedTenancyCount(String username);
	public SolutionSubscriptionVO checkedSolutionSubscription(String username, String SolutionCode);
	public List<SolutionSubscriptionVO> checkedSolutionSubscriptionList(String username);
	public void savePaymentResult(SolutionSubscriptionPaymentVO paymentVO ,RoleAchievedVO roleAchievedVO,SolutionSubscriptionVO solutionSubVO);
	public void saveAutopayAndFirstPayment(SolutionnSubscriptionAutopayMethodVO methodVO,
			SolutionSubscriptionPaymentVO paymentVO,RoleAchievedVO roleAchievedVO,SolutionSubscriptionVO solutionSubVO, CardVO cardVO);
}
