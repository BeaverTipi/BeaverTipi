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
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@EnableEncryptableProperties
@Configuration
public class QuartzSchedulerConfig {

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

}