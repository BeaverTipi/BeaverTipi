/**
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자      수정내용
 *  -----------  ----------  -------------------------------------
 *  2025.07.17   김찬영      최초 생성 - Quartz Job 인스턴스에 DI 적용
 * </pre>
 * 
 * <b>[기능 설명]</b>
 * - Spring BeanFactory를 사용해 Quartz Job 인스턴스에 의존성 주입 가능하게 지원
 * - SpringBeanJobFactory를 상속하여 Job 생성 후 자동 Autowiring 수행
 */
package kr.or.ddit.util.batch;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

    private AutowireCapableBeanFactory beanFactory;
    public void setApplicationContext(ApplicationContext context) {
        this.beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object job = super.createJobInstance(bundle);
        log.debug("🛠️ Job 인스턴스 생성됨 → {}", job.getClass().getName());
        beanFactory.autowireBean(job);
        log.debug("🛠️ Job 인스턴스 주입됨 → {}", job.getClass().getName());
        return job;
    }
}
