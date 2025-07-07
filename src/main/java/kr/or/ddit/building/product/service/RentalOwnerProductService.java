package kr.or.ddit.building.product.service;

import java.util.List;

import kr.or.ddit.vo.ProductVO;

public interface RentalOwnerProductService {

	// 매물 등록
    public int insertProduct(ProductVO product);

    // 매물 목록 조회
    public List<ProductVO> selectProductList(String mbrCd);

    // 매물 단건 조회
    public ProductVO selectProductById(String lstgId);

    // 매물 수정
    public int updateProduct(ProductVO product);

    // 매물 삭제
    public int deleteProduct(String lstgId);
}
