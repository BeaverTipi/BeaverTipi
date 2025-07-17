/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 17.				김찬영			시퀀스 갱신주기가 다른 ID 스케쥴러 개별 생성
 *
 * </pre>
 * 
 * Quartz 스케줄링 Job
 * - application.properties에 등록된 시퀀스 목록을 RESTART START WITH 1로 초기화.
 * - 시퀀스 리셋 결과는 SEQ_RESET_LOG 테이블에 기록.
 */
package kr.or.ddit.util.batch.idseq;

import java.sql.*;
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
public class ContractIdSequenceResetJob implements Job {

    @Autowired
    private DataSource dataSource;

    private static final Logger log = Logger.getLogger(ContractIdSequenceResetJob.class.getName());

    @Override
    public void execute(JobExecutionContext context) {
        String seqName = "seq_contr_id";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("ALTER SEQUENCE " + seqName + " RESTART START WITH 1");
            log.info("[Quartz] ✅ 계약 시퀀스 리셋 완료: " + seqName);

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO SEQ_RESET_LOG (SEQ_NAME, EXEC_RESULT, ERROR_MESSAGE) VALUES (?, ?, ?)")) {
                pstmt.setString(1, seqName);
                pstmt.setString(2, "SUCCESS");
                pstmt.setString(3, null);
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            log.severe("[Quartz] ❌ 계약 시퀀스 리셋 실패: " + e.getMessage());
        }
    }
}
