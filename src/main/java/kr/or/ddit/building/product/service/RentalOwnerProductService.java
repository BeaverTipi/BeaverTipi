package kr.or.ddit.building.product.service;

import java.util.List;

import kr.or.ddit.vo.ProductVO;

public interface RentalOwnerProductService {

    // 매물 등록
    int insertProduct(ProductVO product);

    // 공인중개사별 매물 목록
    List<ProductVO> getProductListByMember(String mbrCd);

    // 매물 상세 조회
    ProductVO getProductById(String lstgId);

    // 매물 수정
    int updateProduct(ProductVO product);

    // 매물 삭제
    int deleteProduct(String lstgId);
}
