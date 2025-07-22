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
 * - listingIdResetJob을 Quartz를 이용해 매월 1일 자정에 실행.
 */

package kr.or.ddit.util.batch;

import kr.or.ddit.util.batch.idseq.ContractIdSequenceResetJob;
import kr.or.ddit.util.batch.idseq.DefaultIdSequenceResetJob;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@Slf4j
@Configuration
@EnableEncryptableProperties
public class QuartzSchedulerConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * Autowiring 가능한 JobFactory 등록
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 매월 1일 자정에 실행되는 기본 ID 리셋 Job 등록
     */
    @Bean
    public JobDetail defaultIdResetJobDetail() {
        return JobBuilder.newJob(DefaultIdSequenceResetJob.class)
                .withIdentity("defaultIdResetJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger defaultIdResetTrigger(JobDetail defaultIdResetJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(defaultIdResetJobDetail)
                .withIdentity("defaultIdResetTrigger")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0 0 1 * ?")
                        .withMisfireHandlingInstructionFireAndProceed()  // 서버 구동 시 지나간 작업 체크
                )
                .build();
    }
    
    /**
     * 매일 자정에 실행되는 계약 ID 리셋 Job 등록
     */
    @Bean
    public JobDetail contractIdResetJobDetail() {
        return JobBuilder.newJob(ContractIdSequenceResetJob.class)
                .withIdentity("contractIdResetJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger contractIdResetTrigger(JobDetail contractIdResetJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(contractIdResetJobDetail)
                .withIdentity("contractIdResetTrigger")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0 0 * * ?") // 매일 자정
                        .withMisfireHandlingInstructionFireAndProceed()
                )
                .build();
    }


    
    /**
     * Scheduler 상태	✅ 정상 생성 → standby → started → graceful shutdown
     * ThreadPool 종료	✅ 모든 worker thread 정상 종료
     * Lock 처리		✅ TRIGGER_ACCESS 락 획득 및 반납 정상
     * Shutdown 처리	✅ shutdown complete까지 확인됨
     * 
     * 정적(SeqReset), 동적(Expire_) Job 등록 모두 지원
     */
    @Bean
    @DependsOn("dataSource")
    public SchedulerFactoryBean schedulerFactoryBean(
            Trigger defaultIdResetTrigger,
            Trigger contractIdResetTrigger,
            JobDetail defaultIdResetJobDetail,
            JobDetail contractIdResetJobDetail,
            JobFactory jobFactory
    ) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setJobDetails(defaultIdResetJobDetail, contractIdResetJobDetail);
        factory.setTriggers(defaultIdResetTrigger, contractIdResetTrigger);
        factory.setOverwriteExistingJobs(true);
        factory.setWaitForJobsToCompleteOnShutdown(true);

        log.debug("✅ [QuartzConfig] 설정된 JobFactory 클래스: {}", jobFactory.getClass().getName());
        return factory;
    }

    /**
     * Scheduler Bean 등록 - @Qualifier로 명시적으로 주입 가능하도록 이름 지정
     */
    @Bean(name = "customQuartzScheduler")
    @Primary
    public Scheduler scheduler(SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;
    }
}