package kr.or.ddit.util.renderer;

import kr.or.ddit.util.page.PaginationInfo;

public interface PaginationRenderer {
	/**
	 * startPage 부터 endPage 까지의 동적 페이지 링크를 생성하는 메소드
	 * @param paging
	 * @param fnName
	 * @return
	 */
	public String renderPagination(PaginationInfo paging, String fnName);
}
