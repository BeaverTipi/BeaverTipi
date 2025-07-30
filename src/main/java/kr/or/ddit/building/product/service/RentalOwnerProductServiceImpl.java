package kr.or.ddit.building.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.admin.code.service.CommonCodeServiceImpl;
import kr.or.ddit.building.mapper.RentalOwnerProductMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.ListingVO;

@Service
public class RentalOwnerProductServiceImpl implements RentalOwnerProductService {

    @Autowired
    private RentalOwnerProductMapper productMapper;
    @Autowired
    private CommonCodeService codeService;
    @Override
    public void insertProduct(ListingVO listing, List<String> brokerIds) {
        for(String broker : brokerIds) {
        	listing.setMbrCd(broker);
        	if(productMapper.insertProduct(listing)<1) {
        		throw new ListingException(String.format("중개인 %s 정보를 매물에 넣는 도중 오류 발생 ", broker));
        	}
        }
         
         
    }
    @Override
    public ListingVO selectProductById(String lstgId) {
        return productMapper.selectProductById(lstgId);
    }

    @Override
    public Integer updateProduct(ListingVO listing) {
        return productMapper.updateProduct(listing);
    }

    @Override
    public Integer deleteProduct(String lstgId) {
        return productMapper.deleteProduct(lstgId);
    }

    @Override
    public List<FacilityOptionVO> selectAllFacilityOptions() {
        return productMapper.selectAllFacilityOptions();
    }

	@Override
	public List<CommonCodeVO> commonCodeLstg1List() {
		return productMapper.commonCodeLstg1List();
	}
	
	@Override
	public List<CommonCodeVO> commonCodeLstg2List() {
		return productMapper.commonCodeLstg2List();
	}

	@Override
	public Map<String, Object> readPagingAndListing(ListingSearchFormVO form, int currentPage) {
	    Map<String, Object> result = new HashMap<>();

	    PaginationInfo<ListingSearchFormVO> pagingVO = new PaginationInfo();
	    pagingVO.setCurrentPageNo(currentPage);
	    pagingVO.setDetailSearch(form);

	    int total = productMapper.selectProductCount(pagingVO);
	    pagingVO.setTotalRecordCount(total);

	    List<ListingVO> listingList = List.of();
	    if (total > 0) {
	        listingList = productMapper.selectProductList(pagingVO);
	    }
	    
	    List<CommonCodeVO> statusCodeList = codeService.readCommonCodeList("PRDST");
	    List<CommonCodeVO> typeSaleCodeList = codeService.readCommonCodeList("TRDST");
	    
	    // ✅ JSP에서 쓸 map 구성
	    
	    result.put("pagingVO", pagingVO);
	    result.put("dataList", listingList);
	    result.put("statusCodeList", statusCodeList);
	    result.put("typeSaleCodeList", typeSaleCodeList);
	    result.put("pagingHTML", new DefaultPaginationRenderer().renderPagination(pagingVO, "fn_paging"));

	    return result;
	}
	@Override
	public List<ListingVO> readRoomsList(ListingVO listing) {
		// TODO Auto-generated method stub
		return productMapper.selectRoomsList(listing);
	}


}
