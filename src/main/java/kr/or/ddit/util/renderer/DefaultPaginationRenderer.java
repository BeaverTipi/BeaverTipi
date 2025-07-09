package kr.or.ddit.util.renderer;

import org.springframework.stereotype.Component;

import kr.or.ddit.util.page.PaginationInfo;

/**
 * 
 * @author SEM
 * @since 2025. 6. 25.
 * @see
 *
 * <pre>
 * 
 * 검색 조건과 검색 키워드로만 구성된 단순 키워드 검색에 사용.
 * 
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 7. ??.     	권성운	          최초 생성
 *  2025. 7. 07.		KNH		  @Component추가, String queryString = paging.getQueryString() 작성
 * </pre>
 */

@Component
public class DefaultPaginationRenderer implements PaginationRenderer {
	
	private final String A_PATTERN = "<a href='javascript:void(0);' onclick='%s(%s)'>%s</a>";
	private final String CURRENT = "<span class='bg-primary'>%s</span>";
	

	@Override
	public String renderPagination(PaginationInfo paging, String fnName) {
		
		int startPage = paging.getFirstPageNoOnPageList(); 
		int endPage = paging.getLastPageNoOnPageList();
		int blockSize = paging.getPageSize();
		int totalPage = paging.getTotalPageCount();
		int currentPage = paging.getCurrentPageNo();
		String queryString = paging.getQueryString();
		
		StringBuffer html = new StringBuffer();
		
		if(startPage>blockSize) {
			html.append(
				String.format(A_PATTERN, fnName, startPage-blockSize, "이전")	
			);
		}
		
		for(int page=startPage; page<=endPage; page++) {
			if(page==currentPage) {
				html.append(
					String.format(CURRENT, page)
				);
			}else {
				html.append(
					String.format(A_PATTERN, fnName, page, page)
				);
			}
		}
		
		if(endPage < totalPage) {
			html.append(
				String.format(A_PATTERN, fnName, endPage+1, "다음")	
			);
		}
		
		
		return html.toString();
	}

}











