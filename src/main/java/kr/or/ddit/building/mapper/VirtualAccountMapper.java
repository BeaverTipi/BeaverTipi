package kr.or.ddit.building.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.VirtualAccountVO;

@Mapper
public interface VirtualAccountMapper {
    int insertVirtualAccount(VirtualAccountVO vo);
    List<VirtualAccountVO> selectVirtualAccountListByMember(String mbrCd);
    VirtualAccountVO selectVirtualAccountById(String virtualAccountId);
    int deleteVirtualAccount(
            @Param("virtualAccountId") String virtualAccountId,
            @Param("mbrCd") String mbrCd
        );
}
