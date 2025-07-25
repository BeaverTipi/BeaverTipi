/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.09   김찬영      최초 생성 - 계약 서명 만료 처리 Job 구성
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - 계약 서명 페이지 만료 대상 Job 실행 시 `BrokerContractService`를 통해 상태 만료 처리
 * - 처리 후 WebSocket을 통해 중개인에게 만료 알림 전송
 * - Quartz에서 동적 잡으로 등록되어 실행됨 (JobDataMap에 contId 필요)
 */
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