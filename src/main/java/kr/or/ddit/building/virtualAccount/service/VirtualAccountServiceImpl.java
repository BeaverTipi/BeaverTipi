package kr.or.ddit.building.virtualAccount.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.or.ddit.building.mapper.VirtualAccountMapper;
import kr.or.ddit.vo.VirtualAccountVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class VirtualAccountServiceImpl implements VirtualAccountService {

    @Autowired
    private VirtualAccountMapper mapper;

    @Autowired
    private VirtualAccountApiService apiService; // 발급은 API를 통해 진행

    @Override
    @Transactional
    public VirtualAccountVO registerVirtualAccount(VirtualAccountVO vo) {
    	log.info(" 여기는 서비스 customerName={}", vo.getCustomerName());
        try {
            VirtualAccountVO issued = apiService.issueVirtualAccount(vo);
            mapper.insertVirtualAccount(issued);
            log.info(" 쿼리실행햇다 customerName={}", vo.getCustomerName());
            return issued;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("가상계좌 발급 실패: " + e.getMessage());
        }
    }

    @Override
    public List<VirtualAccountVO> getVirtualAccountListByMember(String mbrCd) {
        return mapper.selectVirtualAccountListByMember(mbrCd);
    }

    @Override
    public VirtualAccountVO getVirtualAccountById(String virtualAccountId) {
        return mapper.selectVirtualAccountById(virtualAccountId);
    }


    @Override
    public void deleteVirtualAccount(String virtualAccountId, String mbrCd) {
        mapper.deleteVirtualAccount(virtualAccountId, mbrCd);
    }
    
}
