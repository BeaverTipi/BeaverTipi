package kr.or.ddit.util.batch.contract;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.websocket.ContractExpireWebSocketHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class ContractSignatureExpireJob implements Job {

    private BrokerContractService contService;
    private ContractExpireWebSocketHandler expireWebSocketHandler;

    public ContractSignatureExpireJob() {
        // 기본 생성자 필수!!
    }

    @Autowired
    public void setContService(BrokerContractService contService) {
        this.contService = contService;
    }

    @Autowired
    public void setExpireWebSocketHandler(ContractExpireWebSocketHandler handler) {
        this.expireWebSocketHandler = handler;
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		String contId = context.getMergedJobDataMap().getString("contId");
		log.info("🔔 [Quartz] 서명 만료 Job 실행 시작 → contId: {}", contId);
		try {
			int rec = 0;
			if (contId != null) 
				rec = contService.expireContractSignaturePage(contId); // 계약 상태를 만료 처리
            expireWebSocketHandler.broadcastExpiredContract(contId); //실시간 알림
			log.info("✅ 서명 만료 처리 완료 → contId: {}, updatedCount: {}", contId, rec);
		} catch (Exception e){
		    log.error("❌ 서명 만료 Job 처리 실패 → contId: {}, error: {}", contId, e.getMessage(), e);
		    throw new JobExecutionException(e); // 반드시 throw
		}
    }
}