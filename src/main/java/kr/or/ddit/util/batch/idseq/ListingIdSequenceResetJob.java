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

/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영            최초 생성
 *
 * </pre>
 * 
 * Quartz 스케줄링 Job
 * - application.properties에 등록된 시퀀스 목록을 RESTART START WITH 1로 초기화.
 * - 시퀀스 리셋 결과는 SEQ_RESET_LOG 테이블에 기록.
 */
@EnableEncryptableProperties
@Component
public class ListingIdSequenceResetJob implements Job {

    // Oracle DB와 연결하기 위한 Spring DataSource 자동 주입
    @Autowired
    private DataSource dataSource;

    // application.properties에서 시퀀스 목록을 문자열로 가져옴 (쉼표로 구분)
    @Value("${sequence.reset.targets}")
    private String sequenceList;

    // Java 기본 Logger
    private static final Logger log = Logger.getLogger(ListingIdSequenceResetJob.class.getName());

    @Override
    public void execute(JobExecutionContext context) {
        // 문자열 → 리스트로 변환 (쉼표 기준)
        List<String> sequences = Arrays.stream(sequenceList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // DB 연결 시작
        try (Connection conn = dataSource.getConnection()) {

            for (String seqName : sequences) {
                // 각 시퀀스를 RESTART START WITH 1로 초기화
                String sql = "ALTER SEQUENCE " + seqName + " RESTART START WITH 1";
                boolean success = false;
                String errorMsg = null;

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql); // 시퀀스 리셋
                    success = true;
                    log.info("[Quartz] ✅ 시퀀스 리셋 완료: " + seqName);
                } catch (Exception e) {
                    // 실패 시 에러 메시지 저장
                    errorMsg = e.getMessage();
                    log.severe("[Quartz] ❌ 시퀀스 리셋 실패: " + seqName + " → " + errorMsg);
                }

                // 리셋 결과를 로그 테이블에 기록
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO SEQ_RESET_LOG (SEQ_NAME, EXEC_RESULT, ERROR_MESSAGE) VALUES (?, ?, ?)")) {
                    pstmt.setString(1, seqName);                          // 시퀀스 이름
                    pstmt.setString(2, success ? "SUCCESS" : "FAIL");     // 실행 결과
                    pstmt.setString(3, errorMsg);                         // 에러 메시지 (null 가능)
                    pstmt.executeUpdate();                                // 로그 INSERT
                } catch (Exception e) {
                    log.severe("[Quartz] 🚨 로그 기록 실패: " + e.getMessage());
                }
            }

        } catch (Exception ex) {
            // 전체 DB 연결/루프 실패 시
            log.severe("[Quartz] ❌ 전체 리셋 작업 실패: " + ex.getMessage());
        }
    }
}