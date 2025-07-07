
package kr.or.ddit.building.mapper;

import kr.or.ddit.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RentalOwnerProductMapper {

    int insertProduct(ProductVO product);

    List<ProductVO> selectProductListByMember(String mbrCd);

    ProductVO selectProductById(String lstgId);

    int updateProduct(ProductVO product);

    int deleteProduct(String lstgId);
}
