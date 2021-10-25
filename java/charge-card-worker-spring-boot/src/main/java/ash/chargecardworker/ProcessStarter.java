package ash.chargecardworker;

import ash.chargecardworker.impl.CamundaProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ProcessStarter {
    @Autowired
    CamundaProcessService processService;

    @Scheduled(fixedDelay = 5000)
    public void scheduleStartCamundaPaymentRetrieval() {
        processService.create();
    }
}
