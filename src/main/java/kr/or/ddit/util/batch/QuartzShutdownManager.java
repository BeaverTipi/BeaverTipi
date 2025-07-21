package kr.or.ddit.util.batch;

import org.quartz.Scheduler;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuartzShutdownManager {
    private final Scheduler scheduler;

    @PreDestroy
    public void shutdownScheduler() throws Exception {
        if (scheduler.isStarted()) {
            scheduler.shutdown(true);
        }
    }
}
