package kr.or.ddit.util.batch.contract;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.or.ddit.broker.service.BrokerContractService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ContractSignatureExpireJob implements Job {

    @Autowired
    private BrokerContractService contService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String contId = context.getMergedJobDataMap().getString("contId");
        log.info("🔔 [Quartz] 서명 만료 Job 실행 시작 → contId: {}", contId);
        try {
        	int rec = 0;
        	if (contId != null) 
        		rec = contService.expireContractSignaturePage(contId); // 계약 상태를 만료 처리
            log.info("✅ 서명 만료 처리 완료 → contId: {}, updatedCount: {}", contId, rec);
        } catch (Exception e){
            log.error("❌ 서명 만료 Job 처리 실패 → contId: {}, error: {}", contId, e.getMessage(), e);
        }
        
    }
}