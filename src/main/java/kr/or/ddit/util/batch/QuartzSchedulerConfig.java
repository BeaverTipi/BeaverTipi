
package kr.or.ddit.util.batch;

/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영            최초 생성
 *
 * </pre>
 */

import kr.or.ddit.util.batch.idseq.ListingIdSequenceResetJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
 * - listingIdResetJob을 Quartz를 이용해 매월 1일 자정에 실행.
 */
@EnableEncryptableProperties
@Configuration
public class QuartzSchedulerConfig {

    @Bean
    public JobDetail listingIdResetJobDetail() {
        return JobBuilder.newJob(ListingIdSequenceResetJob.class)
                .withIdentity("listingIdResetJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger listingIdResetTrigger(JobDetail listingIdResetJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(listingIdResetJobDetail)
                .withIdentity("listingIdResetTrigger")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0 0 1 * ?")
                        .withMisfireHandlingInstructionFireAndProceed()  // 서버 구동 시 지나간 작업 체크
                )
                .build();
    }
}