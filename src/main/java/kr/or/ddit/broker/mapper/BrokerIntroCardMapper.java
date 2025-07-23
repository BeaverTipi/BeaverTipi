package kr.or.ddit.broker.mapper;

import kr.or.ddit.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface BrokerIntroCardMapper {

    // 1. 소개카드 단건 조회 (가장 최근 등록)
    FileVO selectIntroCardByMember(@Param("mbrCd") String mbrCd, @Param("docTypeCd") String docTypeCd);

    // 2. 소개카드 이미지 저장 (INSERT)
    int insertIntroCardFile(FileVO file);

    // 3. 소개카드 이미지 삭제
    int deleteIntroCardFile(Map<String, Object> params);

}
