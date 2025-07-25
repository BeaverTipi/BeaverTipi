/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.09   김찬영      최초 생성 - 계약 ID 시퀀스 리셋 Job 구현
 *  2025.07.21   김찬영      Abstract 기반으로 리팩토링 적용
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - 계약 관련 ID 시퀀스(`seq_contr_id`)를 1부터 다시 시작
 * - 매일 자정에 Quartz를 통해 실행되며 리셋 결과를 로그로 저장
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

