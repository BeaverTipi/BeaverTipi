/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 17.				김찬영			클래스명 변경 (Listing... -> Default)
 * 2025. 7. 21.				김찬영			가독성 좋게 리팩토링
 * </pre>
 * 
 * Quartz 스케줄링 Job
 * - application.properties에 등록된 시퀀스 목록을 RESTART START WITH 1로 초기화.
 * - 시퀀스 리셋 결과는 SEQ_RESET_LOG 테이블에 기록.
 */
package kr.or.ddit.util.batch.idseq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableEncryptableProperties
@Component
public class DefaultIdSequenceResetJob extends AbstractSequenceResetJob {

    @Autowired
    private DataSource dataSource;

    @Value("${sequence.reset.targets}")
    private String sequenceList;

    @Override
    protected List<String> getTargetSequences() {
        return Arrays.stream(sequenceList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
//    
//    private static final Logger log = Logger.getLogger(DefaultIdSequenceResetJob.class.getName());
//
//    @Override
//    public void execute(JobExecutionContext context) {
//        List<String> sequences = Arrays.stream(sequenceList.split(","))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .toList();
//
//        for (String seqName : sequences) {
//            try (Connection conn = dataSource.getConnection();
//                 Statement stmt = conn.createStatement()) {
//
//                stmt.execute("ALTER SEQUENCE " + seqName + " RESTART START WITH 1");
//                log.info("[Quartz] ✅ 시퀀스 리셋 완료: " + seqName);
//                logResetResult(seqName, "SUCCESS", null);
//
//            } catch (Exception e) {
//                String errorMsg = truncate(e.getMessage(), 1000);
//                log.severe("[Quartz] ❌ 시퀀스 리셋 실패: " + seqName + " → " + errorMsg);
//                logResetResult(seqName, "FAIL", errorMsg);
//            }
//        }
//    }
//
//    private void logResetResult(String seqName, String result, String errorMessage) {
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(
//                 "INSERT INTO SEQ_RESET_LOG (SEQ_NAME, EXEC_RESULT, ERROR_MESSAGE) VALUES (?, ?, ?)")) {
//            pstmt.setString(1, seqName);
//            pstmt.setString(2, result);
//            pstmt.setString(3, errorMessage);
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            log.severe("[Quartz] 🚨 로그 기록 실패: " + e.getMessage());
//        }
//    }
//
//    private String truncate(String str, int maxLength) {
//        return (str != null && str.length() > maxLength) ? str.substring(0, maxLength) : str;
//    }

