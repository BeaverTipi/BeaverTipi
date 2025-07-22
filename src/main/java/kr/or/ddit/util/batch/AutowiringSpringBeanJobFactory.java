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
