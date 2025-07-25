/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.17   김찬영      최초 생성 - Scheduler 종료 처리용 컴포넌트 구현
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - Spring 종료 시 Quartz Scheduler를 graceful하게 종료
 * - @PreDestroy를 통해 정상적인 shutdown 유도
 */
package kr.or.ddit.util.batch;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuartzShutdownManager {
	@Qualifier("customQuartzScheduler")
    private final Scheduler scheduler;

    @PreDestroy
    public void shutdownScheduler() throws Exception {
        if (scheduler.isStarted()) {
            scheduler.shutdown(true);
        }
    }
}
