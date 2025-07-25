/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.25   김찬영      최초 생성 - Spring Scheduler 전역 활성화
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - @EnableScheduling을 통해 Spring 기반의 스케줄러 기능을 활성화함
 * - Quartz 외에도 @Scheduled 기반 Job을 사용할 수 있도록 지원
 */
package kr.or.ddit.util.batch;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerEnabler {}