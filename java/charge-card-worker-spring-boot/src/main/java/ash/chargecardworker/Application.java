package ash.chargecardworker;

import ash.chargecardworker.impl.CamundaProcessService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class Application {
  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }
}
