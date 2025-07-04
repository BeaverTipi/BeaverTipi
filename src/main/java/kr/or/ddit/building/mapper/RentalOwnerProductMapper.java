package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.ProductVO;

@Mapper
public interface RentalOwnerProductMapper {




	    // 매물 등록
	    public int insertProduct(ProductVO product);

	    // 매물 목록 조회 (회원 코드 기준)
	    public List<ProductVO> selectProductList(String mbrCd);

	    // 매물 단건 상세 조회
	    public ProductVO selectProductById(String lstgId);

	    // 매물 수정
	    public int updateProduct(ProductVO product);

	    // 매물 삭제
	    public int deleteProduct(String lstgId);

}
