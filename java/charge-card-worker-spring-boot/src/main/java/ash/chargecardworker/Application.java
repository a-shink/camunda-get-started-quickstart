package ash.chargecardworker;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class Application {
  static AtomicLong instancesCreated = new AtomicLong();

  protected static Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @ExternalTaskSubscription("charge-card")
  public ExternalTaskHandler myTopicHandler() {
    return (externalTask, externalTaskService) -> {
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
    };
  }

}
