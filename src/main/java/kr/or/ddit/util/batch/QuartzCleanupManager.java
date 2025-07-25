/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일       수정자         수정내용
 *  -----------  ----------    ---------------------------
 *  2025.07.25    김찬영         최초 생성 및 Quartz 동적 잡(expireSignPage-*) 정리 기능 구현 및 알림 연동
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - Quartz Scheduler에서 생성된 동적 잡 중 `expireSignPage-*` 패턴의 Job을 식별
 * - 상태가 COMPLETE, ERROR, NONE 이고 nextFireTime이 현재 시각 이전인 Job만 대상으로 삭제
 * - 삭제 실패 시 rollback 처리 + 시스템 관리자 알림 자동 전송 + 로그 기록
 * - 성공/실패 내역은 QRTZ_CLEANUP_LOG 테이블에 기록
 * 
 * <b>[실행 시점]</b>
 * - 매일 자정 (00:00) 자동 실행됨
 * 
 * <b>[알림 연동 대상]</b>
 * - 시스템 관리자 (MBR_CD = M2507000110)
 */
package kr.or.ddit.util.batch;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuartzCleanupManager {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 스케줄링 시 매주 일요일 자정에 실행
	 * 
	 * @test 매일 자정에 실행.
	 */
//	@Scheduled(cron = "0 0 0 ? * SUN")
	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정 00:00
	public void scheduledCleanup() {
		try {
			int result = performCleanup("beavertipiQuartzScheduler", System.currentTimeMillis());
			log.info("✅ [Quartz Cleanup] 완료: {}건 삭제됨", result);
		} catch (Exception e) {
			log.error("❌ [Quartz Cleanup] 실패: {}", e.getMessage(), e);
		}
	}

	/**
	 * 실제 Quartz Job 정리 실행 메서드 (트랜잭션 적용) 삭제 실패 시 트랜잭션을 롤백하고 알림 전송 및 로그 기록을 수행함.
	 * 
	 * 현재: 상태가 COMPLETE, ERROR, NONE이고 nextFireTime이 지난 `expireSignPage-*`
	 * Job/Trigger만 해당
	 * 
	 * @param schedName Quartz Scheduler 이름
	 * @param now       현재 시간 (ms)
	 * @return 삭제 성공한 Job 개수
	 * @throws RuntimeException 삭제 중 예외 발생 시 rollback 유도
	 */
	@Transactional
	public int performCleanup(String schedName, long now) {
		String query = """
				    SELECT T.JOB_NAME, T.TRIGGER_NAME, T.TRIGGER_STATE, T.NEXT_FIRE_TIME
				    FROM QRTZ_TRIGGERS T
				    WHERE T.SCHED_NAME = ?
				      AND T.JOB_NAME LIKE 'expireSignPage-%'
				      AND T.NEXT_FIRE_TIME < ?
				      AND T.TRIGGER_STATE IN ('COMPLETE', 'ERROR', 'NONE')
				""";

		List<Map<String, Object>> candidates = jdbcTemplate.queryForList(query, schedName, now);

		if (candidates.isEmpty()) {
			log.info("🟡 정리 대상 없음 → 클린업 생략");
			return 0;
		}

		int deletedJobs = 0;

		for (Map<String, Object> row : candidates) {
			String jobName = (String) row.get("JOB_NAME");
			String triggerName = (String) row.get("TRIGGER_NAME");
			String state = (String) row.get("TRIGGER_STATE");
			Long fireTime = (Long) row.get("NEXT_FIRE_TIME");

			Instant fireTimeInstant = Instant.ofEpochMilli(fireTime);
			log.info("🧽 삭제 대상 확인 → jobName={}, triggerName={}, 상태={}, nextFire={}", jobName, triggerName, state,
					fireTimeInstant);
			try {
				// 무결성 참조 원칙을 준수하며 순차적으로 삭제.
				safeDelete("DELETE FROM QRTZ_FIRED_TRIGGERS WHERE SCHED_NAME = ? AND JOB_NAME = ?", schedName, jobName);
				safeDelete("DELETE FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = ? AND TRIGGER_NAME = ?", schedName,
						triggerName);
				safeDelete("DELETE FROM QRTZ_CRON_TRIGGERS WHERE SCHED_NAME = ? AND TRIGGER_NAME = ?", schedName,
						triggerName);
				safeDelete("DELETE FROM QRTZ_TRIGGERS WHERE SCHED_NAME = ? AND JOB_NAME = ?", schedName, jobName);
				safeDelete("DELETE FROM QRTZ_JOB_DETAILS WHERE SCHED_NAME = ? AND JOB_NAME = ?", schedName, jobName);

				log.info("✅ 삭제 완료 → jobName={}, triggerName={}", jobName, triggerName);
				deletedJobs++;

				insertCleanupLog(jobName, triggerName, state, fireTimeInstant, "Y", null);
			} catch (Exception e) {
				log.warn(
						"📣 [ALERT] Quartz 삭제 실패 발생!\n"
								+ "🔸 JobName: {}\n🔸 TriggerName: {}\n🔸 상태: {}\n🔸 nextFire: {}\n🔸 사유: {}",
						jobName, triggerName, state, fireTime, e.getMessage());
				insertCleanupLog(jobName, triggerName, state, fireTimeInstant, "N", e.getMessage());
				insertFailureNotification(jobName, triggerName, state, fireTimeInstant, e.getMessage()); // 알림 전송
				throw e; // <-- 전체 트랜잭션 롤백 유도
			}
		}
		return deletedJobs;
	}

	/**
	 * 실제 DB 삭제 처리. 실패 시 예외를 throw하여 트랜잭션 rollback을 유도한다.
	 * 
	 * @param sql  실행할 DELETE 쿼리
	 * @param args 바인딩 파라미터들
	 */
	private void safeDelete(String sql, Object... args) {
		int affected = jdbcTemplate.update(sql, args);
		if (affected == 0) {
			log.warn("⚠️ 삭제되지 않음 - SQL: {}, ARGS: {}", sql, args);
		}
	}

	/**
	 * Quartz 삭제 실패 시 QRTZ_CLEANUP_LOG 테이블에 이력성 레코드 생성
	 */
	private void insertCleanupLog(String jobName, String triggerName, String triggerState, Instant nextFire,
			String successYn, String errorMessage) {
		String insertSql = """
				INSERT INTO QRTZ_CLEANUP_LOG
				(JOB_NAME, TRIGGER_NAME, TRIGGER_STATE, NEXT_FIRE_TIME, DELETED_AT, SUCCESS_YN, ERROR_MESSAGE)
				VALUES (?, ?, ?, ?, SYSTIMESTAMP, ?, ?)
				""";

		jdbcTemplate.update(insertSql, jobName, triggerName, triggerState, java.sql.Timestamp.from(nextFire), successYn,
				errorMessage != null && errorMessage.length() > 900 ? errorMessage.substring(0, 900) : errorMessage);
	}

	/**
	 * Quartz 삭제 실패 시 NOTIFICATIONS 테이블에 알림 레코드 생성
	 */
	private void insertFailureNotification(String jobName, String triggerName, String triggerState,
			Instant nextFireTime, String errorMessage) {

		String sql = """
				INSERT INTO NOTIFICATIONS
				(MBR_CD, NOTIF_TITLE, NOTIF_MSG, NOTIF_TYPE_CD, NOTIF_TYPE_GROUP_CD, NOTIF_REF_URL)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		String title = "[Quartz 삭제 실패] " + jobName;
		String message = String.format("""
				❗ Quartz 잡 삭제 실패
				▸ Job: %s
				▸ Trigger: %s
				▸ 상태: %s
				▸ 실행 예정: %s
				▸ 사유: %s
				""", jobName, triggerName, triggerState, nextFireTime, errorMessage);

		jdbcTemplate.update(sql, "M2507000110", // 시스템 관리자
				title, message.length() > 1800 ? message.substring(0, 1800) : message, // 컬럼 길이 대응
				"009", // 알림 타입 코드 예: Quartz
				"NTFS", // 알림 그룹 코드 예: 시스템
				"javascript:void(0)" // 참고 URL 필요 없을 경우
		);
	}

}
