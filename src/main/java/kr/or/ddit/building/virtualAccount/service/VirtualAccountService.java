package kr.or.ddit.building.virtualAccount.service;

import java.util.List;
import kr.or.ddit.vo.VirtualAccountVO;

public interface VirtualAccountService {
    VirtualAccountVO registerVirtualAccount(VirtualAccountVO vo); // 발급(등록)
    List<VirtualAccountVO> getVirtualAccountListByMember(String mbrCd); // 회원별 목록
    VirtualAccountVO getVirtualAccountById(String virtualAccountId); // 단건조회
    int removeVirtualAccount(String virtualAccountId); // 삭제
}
