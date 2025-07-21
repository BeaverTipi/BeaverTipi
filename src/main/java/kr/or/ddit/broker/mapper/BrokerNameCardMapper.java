package kr.or.ddit.broker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.FileVO;

@Mapper
public interface BrokerNameCardMapper {

    // 1. 회원별 명함 리스트 조회
    List<FileVO> selectNameCardListByMember(String mbrCd);

    // 2. 명함 이미지 업로드
    int insertFile(FileVO file);

    // 3. 명함 이미지 삭제
    int deleteFileById(Map<String, Object> params);

    // 4. 명함 이미지 상세 조회
    FileVO selectNameCardDetail(Map<String, Object> params);
    
    // 기존 대표명함 해제
    int unsetMainNameCard(String mbrCd);

    // 새 대표명함 지정
    int setMainNameCard(@Param("nameCardId") String nameCardId, @Param("mbrCd") String mbrCd);
    
    
}
