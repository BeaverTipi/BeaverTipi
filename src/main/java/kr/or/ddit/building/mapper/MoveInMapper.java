package kr.or.ddit.building.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.building.resident.dto.residentListDTO;
import kr.or.ddit.vo.BuildingVO;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import kr.or.ddit.vo.UnitVO;

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
    
    //유닛 정보 깔아줄꺼얌
    List<Map<String, Object>> selectUnitResidentWithVacancy(String bldgId);
    
    //입력 모달에 또 뿌릴거야 ㅡㅡ
    List<UnitVO> selectVacantUnitList(String bldgId);
    
    //대표입주설정 뭐 이딴거 .. ㅡㅡ 그리고 재등록이 안되더라 그것도 포함임
    int restoreResident(UnitResidentVO vo);
    int setUnitMaster(Map<String, Object> param);
    List<Map<String, Object>> selectUnitResidentWithMaster(String bldgId);

	public List<residentListDTO> selectBuildingUnitAll(String rentalPtyId);


 

}
