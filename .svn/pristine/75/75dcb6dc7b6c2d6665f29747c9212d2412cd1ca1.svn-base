package kr.or.ddit.util.page;

import lombok.Getter;
import lombok.Setter; // ⭐ @Setter 어노테이션 포함 ⭐

import kr.or.ddit.vo.BoardVO; // ⭐ BoardVO import 포함 ⭐
import java.time.LocalDateTime; // ⭐ LocalDateTime import 포함 ⭐

@Getter
@Setter // ⭐ @Setter 어노테이션 포함 ⭐
public class PaginationInfo<T> {
	private T detailSearch;
	public void setDetailSearch(T detailSearch) {
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

	// currentPageNo에 대한 setter. (Lombok @Setter가 있어도 명시적으로 있으면 유지)
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	
    // ⭐ 쿼리 스트링 생성 메서드 포함 ⭐
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();

        // detailSearch (BoardVO) 필드 처리
        if (this.detailSearch instanceof BoardVO) {
            BoardVO boardVO = (BoardVO) this.detailSearch;
            if (boardVO.getSearchTitle() != null && !boardVO.getSearchTitle().isEmpty()) {
                sb.append("&detailSearch.searchTitle=").append(boardVO.getSearchTitle());
            }
            if (boardVO.getSearchWriter() != null && !boardVO.getSearchWriter().isEmpty()) {
                sb.append("&detailSearch.searchWriter=").append(boardVO.getSearchWriter());
            }
            if (boardVO.getSearchRptStatusCode() != null && !boardVO.getSearchRptStatusCode().isEmpty()) {
                sb.append("&detailSearch.searchRptStatusCode=").append(boardVO.getSearchRptStatusCode());
            }
            // 날짜 필드는 특수문자 처리 또는 URL 인코딩 필요
            if (boardVO.getBrdPblsDtmFrom() != null) {
                sb.append("&detailSearch.brdPblsDtmFrom=").append(boardVO.getBrdPblsDtmFrom().toLocalDate());
            }
            if (boardVO.getBrdPblsDtmTo() != null) {
                sb.append("&detailSearch.brdPblsDtmTo=").append(boardVO.getBrdPblsDtmTo().toLocalDate());
            }
        }
        
        // simpleSearch 필드 처리
        if (this.simpleSearch != null) {
            if (this.simpleSearch.getSearchType() != null && !this.simpleSearch.getSearchType().isEmpty()) {
                sb.append("&simpleSearch.searchType=").append(this.simpleSearch.getSearchType());
            }
            if (this.simpleSearch.getSearchWord() != null && !this.simpleSearch.getSearchWord().isEmpty()) {
                sb.append("&simpleSearch.searchWord=").append(this.simpleSearch.getSearchWord());
            }
            if (this.simpleSearch.getBldgId() != null && !this.simpleSearch.getBldgId().isEmpty()) {
                sb.append("&simpleSearch.bldgId=").append(this.simpleSearch.getBldgId());
            }
        }

        return sb.toString();
    }
}