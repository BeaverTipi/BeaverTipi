package kr.or.ddit.util.page;

import lombok.Data;

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
 *  2025. 6. 25.     	SEM	          최초 생성
 *
 * </pre>
 */
@Data
public class SimpleSearch {
	private String searchType;
	private String searchWord;
}
