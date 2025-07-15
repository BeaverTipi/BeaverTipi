package kr.or.ddit.building.product.service;

import java.util.List;

import kr.or.ddit.vo.CommonCodeVO;
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

    // 매물등록 > 매물유형 목록 가져오기
	List<CommonCodeVO> commonCodeLstg1List();
	List<CommonCodeVO> commonCodeLstg2List();
}
