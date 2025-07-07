package kr.or.ddit.building.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.RentalOwnerProductMapper;
import kr.or.ddit.vo.ProductVO;
@Service
public class RentalOwnerProductServiceImpl implements RentalOwnerProductService {


    @Autowired
    private RentalOwnerProductMapper productMapper;

    @Override
    public int insertProduct(ProductVO product) {
        return productMapper.insertProduct(product);
    }

    @Override
    public List<ProductVO> selectProductList(String mbrCd) {
        return productMapper.selectProductList(mbrCd);
    }

    @Override
    public ProductVO selectProductById(String lstgId) {
        return productMapper.selectProductById(lstgId);
    }

    @Override
    public int updateProduct(ProductVO product) {
        return productMapper.updateProduct(product);
    }

    @Override
    public int deleteProduct(String lstgId) {
        return productMapper.deleteProduct(lstgId);
    }
}
