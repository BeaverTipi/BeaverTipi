package kr.or.ddit.util.page;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import kr.or.ddit.vo.BuildingSearchFormVO;
import kr.or.ddit.vo.BusinessAdsSearchVO;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.MemberSearchVO;
import kr.or.ddit.vo.ReportSearchVO;
import lombok.Getter;
import lombok.Setter; // ⭐ @Setter 어노테이션 포함 ⭐

@Getter
@Setter
public class PaginationInfo<T> {
	private Object detailSearch;
	public void setDetailSearch(Object detailSearch) {
		this.detailSearch = detailSearch;
	}
	
	private SimpleSearch simpleSearch;
	public void setSimpleSearch(SimpleSearch simpleSearch) {
		this.simpleSearch = simpleSearch;
	}
	
	private int totalRecordCount;
	private int currentPageNo;
	
	private int pageSize = 5;
	private int recordCountPerPage = 10;
	
	private int totalPageCount;
	
	private int firstPageNoOnPageList;
	private int lastPageNoOnPageList;
	
	private int firstRecordIndex;
	private int lastRecordIndex;
	
	// totalRecordCount에 대한 setter. 이 메서드 호출 시 모든 페이징 관련 값을 계산합니다.
	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
		
		totalPageCount = ((totalRecordCount - 1) / recordCountPerPage) + 1;
		
		firstRecordIndex = (currentPageNo - 1) * recordCountPerPage + 1;
		lastRecordIndex = currentPageNo * recordCountPerPage;
		
		firstPageNoOnPageList = ((currentPageNo - 1) / pageSize) * pageSize + 1;
		lastPageNoOnPageList = firstPageNoOnPageList + pageSize - 1;
		if (lastPageNoOnPageList > totalPageCount) {
			lastPageNoOnPageList = totalPageCount;
		}
	}

    public PaginationInfo() {
        this.currentPageNo = 1;
        this.recordCountPerPage = 10;
        this.pageSize = 5;
    }

	// 쿼리 스트링 생성 메서드
	public String getQueryString() {
		StringBuilder sb = new StringBuilder();

		// detailSearch 필드 처리
		if (this.detailSearch != null) {
			// detailSearch가 MemberSearchVO인 경우 처리
			if (this.detailSearch instanceof MemberSearchVO) {
				MemberSearchVO memberSearchVO = (MemberSearchVO) this.detailSearch;
				try {
					if (memberSearchVO.getMbrId() != null && !memberSearchVO.getMbrId().isEmpty()) {
						sb.append("&detailSearch.mbrId=").append(URLEncoder.encode(memberSearchVO.getMbrId(), StandardCharsets.UTF_8.toString()));
					}
					if (memberSearchVO.getMbrFrstRegDtFrom() != null) {
						sb.append("&detailSearch.mbrFrstRegDtFrom=").append(URLEncoder.encode(memberSearchVO.getMbrFrstRegDtFrom().toString(), StandardCharsets.UTF_8.toString()));
					}
					if (memberSearchVO.getMbrFrstRegDtTo() != null) {
						sb.append("&detailSearch.mbrFrstRegDtTo=").append(URLEncoder.encode(memberSearchVO.getMbrFrstRegDtTo().toString(), StandardCharsets.UTF_8.toString()));
					}
					if (memberSearchVO.getMbrStatusCode() != null && !memberSearchVO.getMbrStatusCode().isEmpty()) {
						sb.append("&detailSearch.mbrStatusCode=").append(URLEncoder.encode(memberSearchVO.getMbrStatusCode(), StandardCharsets.UTF_8.toString()));
					}
					if (memberSearchVO.getMbrEmlAddr() != null && !memberSearchVO.getMbrEmlAddr().isEmpty()) {
						sb.append("&detailSearch.mbrEmlAddr=").append(URLEncoder.encode(memberSearchVO.getMbrEmlAddr(), StandardCharsets.UTF_8.toString()));
					}
					// userRoleId (List<String>) 처리
					if (memberSearchVO.getUserRoleId() != null && !memberSearchVO.getUserRoleId().isEmpty()) {
						sb.append("&detailSearch.userRoleId=").append(URLEncoder.encode(memberSearchVO.getUserRoleId(), StandardCharsets.UTF_8.toString()));
					}
				} catch (UnsupportedEncodingException e) {
					System.err.println("URL Encoding failed for MemberSearchVO: " + e.getMessage());
				}
			}
			// ReportSearchVO 처리 로직
            else if (this.detailSearch instanceof ReportSearchVO) {
                ReportSearchVO reportSearchVO = (ReportSearchVO) this.detailSearch;
                try {
                    if (reportSearchVO.getSearchTitle() != null && !reportSearchVO.getSearchTitle().isEmpty()) {
                        sb.append("&detailSearch.searchTitle=").append(URLEncoder.encode(reportSearchVO.getSearchTitle(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getSearchWriter() != null && !reportSearchVO.getSearchWriter().isEmpty()) {
                        sb.append("&detailSearch.searchWriter=").append(URLEncoder.encode(reportSearchVO.getSearchWriter(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getSearchReportedTargetId() != null && !reportSearchVO.getSearchReportedTargetId().isEmpty()) {
                        sb.append("&detailSearch.searchReportedTargetId=").append(URLEncoder.encode(reportSearchVO.getSearchReportedTargetId(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getSearchRptStatusCode() != null && !reportSearchVO.getSearchRptStatusCode().isEmpty()) {
                        sb.append("&detailSearch.searchRptStatusCode=").append(URLEncoder.encode(reportSearchVO.getSearchRptStatusCode(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getBrdPblsDtmFrom() != null) {
                        sb.append("&detailSearch.brdPblsDtmFrom=").append(URLEncoder.encode(reportSearchVO.getBrdPblsDtmFrom().toString(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getBrdPblsDtmTo() != null) {
                        sb.append("&detailSearch.brdPblsDtmTo=").append(URLEncoder.encode(reportSearchVO.getBrdPblsDtmTo().toString(), StandardCharsets.UTF_8.toString()));
                    }
                    if (reportSearchVO.getSearchRptCode() != null && !reportSearchVO.getSearchRptCode().isEmpty()) {
                        sb.append("&detailSearch.searchRptCode=").append(URLEncoder.encode(reportSearchVO.getSearchRptCode(), StandardCharsets.UTF_8.toString()));
                    }
                } catch (UnsupportedEncodingException e) {
                    System.err.println("URL Encoding failed for ReportSearchVO: " + e.getMessage());
                }
            }
			// 구독
			else if (this.detailSearch instanceof BusinessApproveSearchVO) {
			    BusinessApproveSearchVO vo = (BusinessApproveSearchVO) this.detailSearch;
			    try {
			        if (vo.getMbrCd() != null && !vo.getMbrCd().isEmpty()) {
			            sb.append("&detailSearch.mbrCd=").append(URLEncoder.encode(vo.getMbrCd(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getMbrId() != null && !vo.getMbrId().isEmpty()) {
			            sb.append("&detailSearch.mbrId=").append(URLEncoder.encode(vo.getMbrId(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getMbrNm() != null && !vo.getMbrNm().isEmpty()) {
			            sb.append("&detailSearch.mbrNm=").append(URLEncoder.encode(vo.getMbrNm(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getAuthApprYn() != null && !vo.getAuthApprYn().isEmpty()) {
			            sb.append("&detailSearch.authApprYn=").append(URLEncoder.encode(vo.getAuthApprYn(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getRole() != null && !vo.getRole().isEmpty()) {
			            sb.append("&detailSearch.role=").append(URLEncoder.encode(vo.getRole(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getHasFile() != null && !vo.getHasFile().isEmpty()) {
			        	sb.append("&detailSearch.hasFile=").append(URLEncoder.encode(vo.getHasFile(), StandardCharsets.UTF_8.toString()));
			        }
			    } catch (UnsupportedEncodingException e) {
			        System.err.println("URL Encoding failed for BusinessApproveSearchVO: " + e.getMessage());
			    }
			}
			// ListingSearchFormVO 처리 로직
			else if (this.detailSearch instanceof ListingSearchFormVO) {
			    ListingSearchFormVO vo = (ListingSearchFormVO) this.detailSearch;
			    try {
			        if (vo.getMbrCd() != null && !vo.getMbrCd().isEmpty()) {
			            sb.append("&detailSearch.mbrCd=").append(URLEncoder.encode(vo.getMbrCd(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getRentalPtyId() != null && !vo.getRentalPtyId().isEmpty()) {
			        	sb.append("&detailSearch.rentalPtyId=").append(URLEncoder.encode(vo.getMbrCd(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchListingName() != null && !vo.getSearchListingName().isEmpty()) {
			            sb.append("&detailSearch.searchListingName=").append(URLEncoder.encode(vo.getSearchListingName(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchRoomNum() != null && !vo.getSearchRoomNum().isEmpty()) {
			            sb.append("&detailSearch.searchRoomNum=").append(URLEncoder.encode(vo.getSearchRoomNum(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchStatus() != null && !vo.getSearchStatus().isEmpty()) {
			            sb.append("&detailSearch.searchStatus=").append(URLEncoder.encode(vo.getSearchStatus(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchType() != null && !vo.getSearchType().isEmpty()) {
			            sb.append("&detailSearch.searchType=").append(URLEncoder.encode(vo.getSearchType(), StandardCharsets.UTF_8.toString()));
			        }

			        // 💰 보증금 범위
			        if (vo.getSearchDepositMin() != null) {
			            sb.append("&detailSearch.searchDepositMin=").append(vo.getSearchDepositMin());
			        }
			        if (vo.getSearchDepositMax() != null) {
			            sb.append("&detailSearch.searchDepositMax=").append(vo.getSearchDepositMax());
			        }

			        // 💵 월세 범위
			        if (vo.getSearchMonthlyMin() != null) {
			            sb.append("&detailSearch.searchMonthlyMin=").append(vo.getSearchMonthlyMin());
			        }
			        if (vo.getSearchMonthlyMax() != null) {
			            sb.append("&detailSearch.searchMonthlyMax=").append(vo.getSearchMonthlyMax());
			        }

			        // 🏠 매매가 범위
			        if (vo.getSearchSaleMin() != null) {
			            sb.append("&detailSearch.searchSaleMin=").append(vo.getSearchSaleMin());
			        }
			        if (vo.getSearchSaleMax() != null) {
			            sb.append("&detailSearch.searchSaleMax=").append(vo.getSearchSaleMax());
			        }
			        if (vo.getSearchLeaseMin() != null) {
			            sb.append("&detailSearch.searchLeaseMin=").append(vo.getSearchLeaseMin());
			        }
			        if (vo.getSearchLeaseMax() != null) {
			            sb.append("&detailSearch.searchLeaseMax=").append(vo.getSearchLeaseMax());
			        }
			        if (vo.getSearchRegDateFrom() != null) {
			            sb.append("&detailSearch.searchRegDateFrom=").append(URLEncoder.encode(vo.getSearchRegDateFrom().toString(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchRegDateTo() != null) {
			            sb.append("&detailSearch.searchRegDateTo=").append(URLEncoder.encode(vo.getSearchRegDateTo().toString(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getActiveTab() != null) {
			            sb.append("&detailSearch.activeTab=").append(vo.getActiveTab());
			        }
			    } catch (UnsupportedEncodingException e) {
			        System.err.println("URL Encoding failed for ListingSearchFormVO: " + e.getMessage());
			    }
			}
			else if (this.detailSearch instanceof BuildingSearchFormVO) {
			    BuildingSearchFormVO vo = (BuildingSearchFormVO) this.detailSearch;
			    try {
			        if (vo.getRentalPtyId() != null && !vo.getRentalPtyId().isEmpty()) {
			            sb.append("&detailSearch.rentalPtyId=").append(URLEncoder.encode(vo.getRentalPtyId(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingName() != null && !vo.getSearchBuildingName().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingName=").append(URLEncoder.encode(vo.getSearchBuildingName(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingAddress() != null && !vo.getSearchBuildingAddress().isEmpty()) {
			        	sb.append("&detailSearch.searchBuildingAddress=").append(URLEncoder.encode(vo.getSearchBuildingAddress(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingRoomNum() != null && !vo.getSearchBuildingRoomNum().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingRoomNum=").append(URLEncoder.encode(vo.getSearchBuildingRoomNum(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingStatus() != null && !vo.getSearchBuildingStatus().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingStatus=").append(URLEncoder.encode(vo.getSearchBuildingStatus(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingType() != null && !vo.getSearchBuildingType().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingType=").append(URLEncoder.encode(vo.getSearchBuildingType(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingRegDateFrom() != null && !vo.getSearchBuildingRegDateFrom().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingRegDateFrom=").append(URLEncoder.encode(vo.getSearchBuildingRegDateFrom(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getSearchBuildingRegDateTo() != null && !vo.getSearchBuildingRegDateTo().isEmpty()) {
			            sb.append("&detailSearch.searchBuildingRegDateTo=").append(URLEncoder.encode(vo.getSearchBuildingRegDateTo(), StandardCharsets.UTF_8.toString()));
			        }
			        if (vo.getActiveTab() != null) {
			            sb.append("&detailSearch.activeTab=").append(vo.getActiveTab());
			        }
			    } catch (UnsupportedEncodingException e) {
			        System.err.println("URL Encoding failed for BuildingSearchFormVO: " + e.getMessage());
			    }
			}

			// 광고
			if (this.detailSearch instanceof BusinessAdsSearchVO) {
				BusinessAdsSearchVO businessAdsSearchVO = (BusinessAdsSearchVO) this.detailSearch;
				try {
					if (businessAdsSearchVO.getSearchAdsTitle() != null && !businessAdsSearchVO.getSearchAdsTitle().isEmpty()) {
			            sb.append("&detailSearch.searchAdsTitle=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsTitle(), StandardCharsets.UTF_8.toString()));
			        }
			        if (businessAdsSearchVO.getSearchAdsWriter() != null && !businessAdsSearchVO.getSearchAdsWriter().isEmpty()) {
			            sb.append("&detailSearch.searchAdsWriter=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsWriter(), StandardCharsets.UTF_8.toString()));
			        }
					if (businessAdsSearchVO.getSearchAdsStatusCode() != null && !businessAdsSearchVO.getSearchAdsStatusCode().isEmpty()) {
			            sb.append("&detailSearch.searchAdsStatusCode=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsStatusCode(), StandardCharsets.UTF_8.toString()));
			        }
			        if (businessAdsSearchVO.getSearchAdsBp() != null && !businessAdsSearchVO.getSearchAdsBp().isEmpty()) {
			            sb.append("&detailSearch.searchAdsBp=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsBp(), StandardCharsets.UTF_8.toString()));
			        }
			        if (businessAdsSearchVO.getSearchAdsPic() != null && !businessAdsSearchVO.getSearchAdsPic().isEmpty()) {
			            sb.append("&detailSearch.searchAdsPic=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsPic(), StandardCharsets.UTF_8.toString()));
			        }
			        if (businessAdsSearchVO.getSearchAdsPicTelno() != null && !businessAdsSearchVO.getSearchAdsPicTelno().isEmpty()) {
			            sb.append("&detailSearch.searchAdsPicTelno=").append(URLEncoder.encode(businessAdsSearchVO.getSearchAdsPicTelno(), StandardCharsets.UTF_8.toString()));
			        }
				} catch (UnsupportedEncodingException e) {
					System.err.println("URL Encoding failed for BusinessAdsSearchVO: " + e.getMessage());
				}
			}

		}

		// simpleSearch 필드 처리 (URL 인코딩 추가)
		if (this.simpleSearch != null) {
			try {
				if (this.simpleSearch.getSearchType() != null && !this.simpleSearch.getSearchType().isEmpty()) {
					sb.append("&simpleSearch.searchType=").append(URLEncoder.encode(this.simpleSearch.getSearchType(), StandardCharsets.UTF_8.toString()));
				}
				if (this.simpleSearch.getSearchWord() != null && !this.simpleSearch.getSearchWord().isEmpty()) {
					sb.append("&simpleSearch.searchWord=").append(URLEncoder.encode(this.simpleSearch.getSearchWord(), StandardCharsets.UTF_8.toString()));
				}
				if (this.simpleSearch.getBldgId() != null && !this.simpleSearch.getBldgId().isEmpty()) {
					sb.append("&simpleSearch.bldgId=").append(URLEncoder.encode(this.simpleSearch.getBldgId(), StandardCharsets.UTF_8.toString()));
				}
			} catch (UnsupportedEncodingException e) {
				System.err.println("URL Encoding failed for SimpleSearch: " + e.getMessage());
			}
		}

		return sb.toString();
	}

}