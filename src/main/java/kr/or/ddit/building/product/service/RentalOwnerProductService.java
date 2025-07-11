package kr.or.ddit.building.product.service;

import java.util.List;

import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;

public interface RentalOwnerProductService {

	// 등록
    int insertProduct(ListingVO listing);

    // 목록 조회
    List<ListingVO> selectProductList(String mbrCd);

    // 상세 조회
    ListingVO selectProductById(String lstgId);

    // 수정
    int updateProduct(ListingVO listing);

    // 삭제
    int deleteProduct(String lstgId);

    // 시설 옵션 전체 조회
    List<FacilityOptionVO> selectAllFacilityOptions();
}
