package kr.or.ddit.util.page;

import lombok.Getter;

@Getter
public class PaginationInfo<T> {
	private T detailSearch;
	public void setDetailSearch(T detailSearch) {
		this.detailSearch = detailSearch;
	}
	
	
	// 검색 조건 추가
	private SimpleSearch simpleSearch;
	public void setSimpleSearch(SimpleSearch simpleSearch) {
		this.simpleSearch = simpleSearch;
	}
	
	private int totalRecordCount; // totalRecord, 데이터베이스 조회
	
	private int currentPageNo; // currentPage, 요청 파라미터
	
	private int pageSize = 5; // blockSize
	private int recordCountPerPage = 10; // screenSize
	
	private int totalPageCount; // totalPage, (totalRecord, screenSize)
	
	private int firstPageNoOnPageList; // startPage, (blockSize, currentPage)
	private int lastPageNoOnPageList; // endPage, (blockSize, currentPage)
	
	private int firstRecordIndex; // startRow, (screenSize, currentPage)
	private int lastRecordIndex; // endRow, (screenSize, currentPage)
	
	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	
	public int getTotalPageCount() {
		totalPageCount = ((totalRecordCount-1)/recordCountPerPage) + 1;
		return totalPageCount;
	}
	
	public int getFirstRecordIndex() {
		firstRecordIndex = (currentPageNo - 1) * recordCountPerPage + 1;
		return firstRecordIndex;
	}
	
	public int getLastRecordIndex() {
		lastRecordIndex = currentPageNo * recordCountPerPage;
		return lastRecordIndex;
	}
	
	public int getFirstPageNoOnPageList() {
		firstPageNoOnPageList = ((currentPageNo-1)/pageSize)*pageSize + 1;
		return firstPageNoOnPageList;
	}
	public int getLastPageNoOnPageList() {
		lastPageNoOnPageList = firstPageNoOnPageList+pageSize-1;
		if(lastPageNoOnPageList>getTotalPageCount()){
			lastPageNoOnPageList=totalPageCount;
		}
		return lastPageNoOnPageList;
	}
}











