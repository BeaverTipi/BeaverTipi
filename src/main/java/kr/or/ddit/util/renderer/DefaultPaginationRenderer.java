package kr.or.ddit.util.renderer;

import kr.or.ddit.util.page.PaginationInfo;

public class DefaultPaginationRenderer implements PaginationRenderer {
	
	private final String A_PATTERN = "<a href='javascript:void(0);' onclick='%s(%s)'>[%s]</a>";
	private final String CURRENT = "<span class='bg-primary'>[%s]</span>";
	

	@Override
	public String renderPagination(PaginationInfo paging, String fnName) {
		
		int startPage = paging.getFirstPageNoOnPageList(); 
		int endPage = paging.getLastPageNoOnPageList();
		int blockSize = paging.getPageSize();
		int totalPage = paging.getTotalPageCount();
		int currentPage = paging.getCurrentPageNo();
		
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











