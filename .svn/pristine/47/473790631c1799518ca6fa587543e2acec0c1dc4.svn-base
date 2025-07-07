package kr.or.ddit.building.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.RentalOwnerProductMapper;
import kr.or.ddit.vo.ProductVO;

@Service
public class RentalOwnerProductServiceImpl implements RentalOwnerProductService {

    @Autowired
    private RentalOwnerProductMapper mapper;

    @Override
    public int insertProduct(ProductVO product) {
        return mapper.insertProduct(product);
    }

    @Override
    public List<ProductVO> getProductListByMember(String mbrCd) {
        return mapper.selectProductListByMember(mbrCd);
    }

    @Override
    public ProductVO getProductById(String lstgId) {
        return mapper.selectProductById(lstgId);
    }

    @Override
    public int updateProduct(ProductVO product) {
        return mapper.updateProduct(product);
    }

    @Override
    public int deleteProduct(String lstgId) {
        return mapper.deleteProduct(lstgId);
    }
}
