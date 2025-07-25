/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.21   김찬영      최초 작성 - 시퀀스 초기화용 추상 Job 구성
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - 시퀀스를 RESTART START WITH 1로 리셋하는 Job의 추상 클래스
 * - 하위 클래스가 대상 시퀀스만 명시하면 공통 처리 수행
 * - 리셋 결과는 SEQ_RESET_LOG에 자동 기록됨
 */
package kr.or.ddit.util.batch.idseq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSequenceResetJob implements Job {

    @Autowired
    protected DataSource dataSource;

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    // 상속 클래스가 리셋 대상 시퀀스 목록 제공
    protected abstract List<String> getTargetSequences();

    @Override
    public void execute(JobExecutionContext context) {
        for (String seqName : getTargetSequences()) {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.execute("ALTER SEQUENCE " + seqName + " RESTART START WITH 1");
                log.info("[Quartz] ✅ 시퀀스 리셋 완료: " + seqName);
                logResetResult(seqName, "SUCCESS", null);

            } catch (Exception e) {
                String errorMsg = truncate(e.getMessage(), 1000);
                log.severe("[Quartz] ❌ 시퀀스 리셋 실패: " + seqName + " → " + errorMsg);
                logResetResult(seqName, "FAIL", errorMsg);
            }
        }
    }

    private void logResetResult(String seqName, String result, String errorMessage) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO SEQ_RESET_LOG (SEQ_NAME, EXEC_RESULT, ERROR_MESSAGE) VALUES (?, ?, ?)")) {

            pstmt.setString(1, seqName);
            pstmt.setString(2, result);
            pstmt.setString(3, errorMessage);
            pstmt.executeUpdate();

        } catch (Exception e) {
            log.severe("[Quartz] 🚨 로그 기록 실패: " + e.getMessage());
        }
    }

    private String truncate(String str, int maxLength) {
        return (str != null && str.length() > maxLength) ? str.substring(0, maxLength) : str;
    }
}
