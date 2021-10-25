package ash.chargecardworker.impl;

import ash.chargecardworker.Application;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
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
        long instancesNow = instancesCreated.get();

        if (instancesNow > 10) {
            System.out.println("Too many instances already created - so skip creating new");
            return;
        }
        

        long amount = 200l + new Random().nextInt(300);
        String item = items.get(new Random().nextInt(items.size()));
        ResponseEntity<String> response = camundaClient.startProcess(item, amount);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Not 200 code returned but " + response.getStatusCodeValue() + " check response:");
            System.out.println(response.getBody());
        } else {
            instancesCreated.incrementAndGet();
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
        String item = externalTask.getVariable("item");
        Long amount = externalTask.getVariable("amount");
        LOG.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");


        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {}

        // Complete the task
        externalTaskService.complete(externalTask);
        instancesCreated.decrementAndGet();
    }
}
