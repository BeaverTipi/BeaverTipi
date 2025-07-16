package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BuildingVO;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;

@Mapper
public interface MoveInMapper {

    // 등록
    public int insertResident(UnitResidentVO vo);

    // 수정
    public int updateResident(UnitResidentVO vo);

    //삭제 (del_YN = 'Y')
    public int deleteResident(UnitResidentVO vo);

    // 입주민 목록
    public List<UnitResidentVO> selectResidentsByBldgId(String bldgId);

    // 모달검색 ID  이름
    public List<MemberVO> searchMemberByKeyword(String keyword);
    
    //셀렉트게 건물 달아주긔
    List<BuildingVO> selectBuildingsByRentalPtyId(String rentalPtyId);
    
 

}
