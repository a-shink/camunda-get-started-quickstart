package ash.chargecardworker.impl;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

public interface CamundaProcessService {
    void create();
    void handle(ExternalTask externalTask, ExternalTaskService externalTaskService);
}
