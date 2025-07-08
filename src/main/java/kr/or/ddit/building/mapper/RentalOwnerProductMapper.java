package kr.or.ddit.building.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;

@Mapper
public interface RentalOwnerProductMapper {

    // 매물 등록
    int insertProduct(ListingVO listing);

    // 매물 목록 조회
    List<ListingVO> selectProductList(String mbrCd);

    // 매물 상세 조회
    ListingVO selectProductById(String lstgId);

    // 매물 수정
    int updateProduct(ListingVO listing);

    // 매물 삭제
    int deleteProduct(String lstgId);
    
    // 시설 옵션 전체 조회
    public List<FacilityOptionVO> selectAllFacilityOptions();
}
