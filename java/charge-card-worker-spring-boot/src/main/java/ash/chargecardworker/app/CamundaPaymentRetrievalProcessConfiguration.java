package ash.chargecardworker.app;

import ash.chargecardworker.impl.CamundaProcessService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CamundaPaymentRetrievalProcessConfiguration {
    @Autowired
    CamundaProcessService processService;

    @Scheduled(fixedDelay = 1000)
    public void scheduleNewPaymentRetrievalProcess() {
        processService.create();
    }

    @Bean
    @ExternalTaskSubscription("charge-card")
    public ExternalTaskHandler chargeCardExtTaskHandler() {
        return (externalTask, externalTaskService) -> {
            processService.handle(externalTask, externalTaskService);
        };
    }
}
