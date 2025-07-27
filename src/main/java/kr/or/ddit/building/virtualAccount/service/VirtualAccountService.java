package kr.or.ddit.building.virtualAccount.service;

import java.util.List;
import kr.or.ddit.vo.VirtualAccountVO;

public interface VirtualAccountService {
    VirtualAccountVO registerVirtualAccount(VirtualAccountVO vo); // 등록
    List<VirtualAccountVO> getVirtualAccountListByMember(String mbrCd); // 목록
    VirtualAccountVO getVirtualAccountById(String virtualAccountId); // 단건
    void deleteVirtualAccount(String virtualAccountId, String mbrCd); //삭제
}
