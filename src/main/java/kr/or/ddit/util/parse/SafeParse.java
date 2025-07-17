package kr.or.ddit.util.parse;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
public class SafeParse {
	/**
	 * Long으로 안전하게 변환
	 * - null, 빈 문자열, 공백 모두 0L로 반환
	 */
	public static Long safeParseLong(String value) {
	    if (value == null || value.trim().isEmpty()) return 0L;
	    try {
	        return Long.parseLong(value.trim());
	    } catch (NumberFormatException e) {
	        return 0L;
	    }
	}
}
