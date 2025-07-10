package kr.or.ddit.building.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.TenancyAccountMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.TenancyAccountVO;

@Service
public class TenancyAccountServiceImpl implements TenancyAccountService {

    @Autowired
    private TenancyAccountMapper mapper;
	
	
	@Override
	public List<TenancyAccountVO> retrieveAccountList(String mbrCd) {
        return mapper.selectAccountListByMbrCd(mbrCd);
    }

	@Override
	 public int createAccount(TenancyAccountVO account) {
        return mapper.insertAccount(account);
    }
	

	@Override
	 public int removeAccount(TenancyAccountVO account) {
        return mapper.deleteAccount(account);
    }

	@Override
	 public List<BuildingVO> retrieveBuildingListByMbrCd(String mbrCd) {
	    return mapper.selectBuildingListByMbrCd(mbrCd);
	}
	
	@Override
	public String findRentalPtyIdByMbrCd(String mbrCd) {
	    return mapper.findRentalPtyIdByMbrCd(mbrCd);
	}
	
	@Override
	public List<BuildingVO> retrieveBuildingListByRentalPtyId(String rentalPtyId) {
	    return mapper.selectBuildingListByRentalPtyId(rentalPtyId);
	}
}
