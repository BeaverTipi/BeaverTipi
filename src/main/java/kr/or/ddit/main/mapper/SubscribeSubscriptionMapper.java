package kr.or.ddit.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.EasyPayVO;
import kr.or.ddit.vo.RoleAchievedVO;
import kr.or.ddit.vo.SolutionSubscriptionPaymentVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.SolutionnSubscriptionAutopayMethodVO;
import kr.or.ddit.vo.TenancyVO;
import kr.or.ddit.vo.VirtualAccountVO;

@Mapper
public interface SubscribeSubscriptionMapper {
	
	public List<SolutionVO> selectSolutionList();
	public SolutionVO selectSolution(String solId);
	public SolutionSubscriptionVO selectSolutionSubscription(String username);
	public List<SolutionVO> selectCommonCodeSolutionList(String sol);
	public Integer insertSolutionSubscription(SolutionSubscriptionVO solSubVO);
	public Integer insertBroker(BrokerVO broker);
	public Integer selectBrokerCount(String username);
	public Integer selectTenancyCount(String username);
	public Integer insertTenancy(TenancyVO tenancy);
	public TenancyVO selectTenancy(String username);
	public BrokerVO selectBroker(String username);
	// Service or Repository
	public SolutionSubscriptionVO checkedSolutionSubscription(@Param("username") String username, @Param("solCcCd") String solCcCd);
	public List<SolutionSubscriptionVO> checkedSolutionSubscriptionList(String username);

	public Integer insertSubscriptionBillingKey(SolutionnSubscriptionAutopayMethodVO methodVO);
	public Integer insertSubscriptionPayment(SolutionSubscriptionPaymentVO paymentVO);
	public Integer insertRoleAchived(RoleAchievedVO roleAchievedVO);
	public Integer insertCard(CardVO cardVO);
	public Integer insertEasyPay(EasyPayVO easyPay);
	public Integer insertVirtualAccount(VirtualAccountVO va);
}
