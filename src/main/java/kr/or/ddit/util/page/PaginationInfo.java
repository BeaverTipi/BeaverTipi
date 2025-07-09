package kr.or.ddit.util.page;

import lombok.Getter;
import lombok.Setter; // ⭐ @Setter 어노테이션 포함 ⭐

import kr.or.ddit.vo.BoardVO; // ⭐ BoardVO import 포함 ⭐
import kr.or.ddit.vo.MemberSearchVO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime; // ⭐ LocalDateTime import 포함 ⭐

@Getter
@Setter
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
			// 기존 BoardVO 처리 로직 유지
			else if (this.detailSearch instanceof BoardVO) {
				BoardVO boardVO = (BoardVO) this.detailSearch;
				try {
					if (boardVO.getSearchTitle() != null && !boardVO.getSearchTitle().isEmpty()) {
						sb.append("&detailSearch.searchTitle=").append(URLEncoder.encode(boardVO.getSearchTitle(), StandardCharsets.UTF_8.toString()));
					}
					if (boardVO.getSearchWriter() != null && !boardVO.getSearchWriter().isEmpty()) {
						sb.append("&detailSearch.searchWriter=").append(URLEncoder.encode(boardVO.getSearchWriter(), StandardCharsets.UTF_8.toString()));
					}
					if (boardVO.getSearchRptStatusCode() != null && !boardVO.getSearchRptStatusCode().isEmpty()) {
						sb.append("&detailSearch.searchRptStatusCode=").append(URLEncoder.encode(boardVO.getSearchRptStatusCode(), StandardCharsets.UTF_8.toString()));
					}
					if (boardVO.getBrdPblsDtmFrom() != null) {
						sb.append("&detailSearch.brdPblsDtmFrom=").append(URLEncoder.encode(boardVO.getBrdPblsDtmFrom().toLocalDate().toString(), StandardCharsets.UTF_8.toString()));
					}
					if (boardVO.getBrdPblsDtmTo() != null) {
						sb.append("&detailSearch.brdPblsDtmTo=").append(URLEncoder.encode(boardVO.getBrdPblsDtmTo().toLocalDate().toString(), StandardCharsets.UTF_8.toString()));
					}
				} catch (UnsupportedEncodingException e) {
					System.err.println("URL Encoding failed for BoardVO: " + e.getMessage());
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