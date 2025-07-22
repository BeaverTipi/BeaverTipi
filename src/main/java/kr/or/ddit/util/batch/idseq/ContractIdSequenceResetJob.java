/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 17.				김찬영			시퀀스 갱신주기가 다른 ID 스케쥴러 개별 생성
 * 2025. 7. 21.				김찬영			가독성 좋게 리팩토링
 *
 * </pre>
 * 
 * Quartz 스케줄링 Job
 * - application.properties에 등록된 시퀀스 목록을 RESTART START WITH 1로 초기화.
 * - 시퀀스 리셋 결과는 SEQ_RESET_LOG 테이블에 기록.
 */
package kr.or.ddit.util.batch.idseq;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@Component
public class ContractIdSequenceResetJob extends AbstractSequenceResetJob {

    @Override
    protected List<String> getTargetSequences() {
        return List.of("seq_contr_id");
    }
}
//	
//    @Autowired
//    private DataSource dataSource;
//
//    private static final Logger log = Logger.getLogger(ContractIdSequenceResetJob.class.getName());
//    @Override
//    public void execute(JobExecutionContext context) {
//        String seqName = "seq_contr_id";
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//
//            stmt.execute("ALTER SEQUENCE " + seqName + " RESTART START WITH 1");
//            log.info("[Quartz] ✅ 계약 시퀀스 리셋 완료: " + seqName);
//
//            logResetResult(seqName, "SUCCESS", null);
//
//        } catch (Exception e) {
//            log.severe("[Quartz] ❌ 계약 시퀀스 리셋 실패: " + seqName + " → " + e.getMessage());
//            logResetResult(seqName, "FAIL", e.getMessage());
//        }
//    }
//
//    private void logResetResult(String seqName, String result, String errorMessage) {
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(
//                 "INSERT INTO SEQ_RESET_LOG (SEQ_NAME, EXEC_RESULT, ERROR_MESSAGE) VALUES (?, ?, ?)")) {
//
//            pstmt.setString(1, seqName);
//            pstmt.setString(2, result);
//            pstmt.setString(3, truncate(errorMessage, 1000)); // 오류 메시지 너무 길면 잘라줌
//            pstmt.executeUpdate();
//
//        } catch (Exception ex) {
//            log.severe("[Quartz] ❌ 리셋 결과 로그 기록 실패: " + ex.getMessage());
//        }
//    }
//
//    private String truncate(String str, int maxLength) {
//        return (str != null && str.length() > maxLength) ? str.substring(0, maxLength) : str;
//    }

