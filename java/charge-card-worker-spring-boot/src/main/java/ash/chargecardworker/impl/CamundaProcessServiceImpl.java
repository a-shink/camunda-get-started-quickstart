package ash.chargecardworker.impl;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
class CamundaProcessServiceImpl implements CamundaProcessService{
    private static final Logger LOG = LoggerFactory.getLogger(CamundaProcessServiceImpl.class);
    private static final List<String> items = List.of("FIRST ITEM", "item two", "Item#3", "Medium price product", "Luxury item$$$");
    private static final AtomicLong instancesCreated = new AtomicLong();

    @Autowired
    CamundaRestClient camundaClient;

    @Override
    public void create() {
        long instancesNow = camundaClient.countProcessInstances();

        if (instancesNow == -1) {
            instancesNow = instancesCreated.get();
        }

        if (instancesNow > 20) {
            LOG.debug(String.format("Too many instances(%1$d) already created - so skip creating new", instancesNow));
            return;
        }


        String businessKey = UUID.randomUUID().toString();

        for (int i = 0; i < 2; i++) {
            long amount = 200L + new Random().nextInt(300);
            String item = items.get(new Random().nextInt(items.size()));
            boolean started = camundaClient.startProcess(businessKey, i, item, amount);

            if (started) {
                instancesNow = instancesCreated.incrementAndGet();
                LOG.debug("Created another instance - " + instancesNow);
            }
        }
    }

    @Override
    public void handle(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Put your business logic here
        LOG.info("Execute business logic for External Task with id '{}'", externalTask.getId());

        /*
        System.out.println("Show all variables with type:");
        Map<String, Object> vars = externalTask.getAllVariables();
        vars.entrySet()
              .stream()
              .map(e -> e.getKey() + ": " + e.getValue().getClass().getName())
              .forEach(System.out::println);
        */

        // Get a process variable
        String instanceId = externalTask.getProcessInstanceId();
        String item = externalTask.getVariable("item");
        Long amount = externalTask.getVariable("amount");
        LOG.info(instanceId + ": Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");

        String varsDump = camundaClient.getProcessInstanceVariables(instanceId);
        LOG.debug("All variables of instance '{}' via REST: {}",  instanceId, varsDump);

        long instancesNow = camundaClient.countProcessInstances();
        // Don't pause when >13 instances
        long pause = (instancesNow > 13)? 1 : 7500;
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {}

        // Complete the task
        externalTaskService.complete(externalTask);
        LOG.info("Complete External Task with id '{}'", externalTask.getId());
        instancesCreated.decrementAndGet();
    }
}
