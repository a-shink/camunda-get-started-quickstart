/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ash.chargecardworker;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.URI;
import java.util.Map;

@SpringBootApplication
public class Application {

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

      System.out.println("Show all variables with type:");
      Map<String, Object> vars = externalTask.getAllVariables();
      vars.entrySet()
              .stream()
              .map(e -> e.getKey() + ": " + e.getValue().getClass().getName())
              .forEach(System.out::println);

      // Get a process variable
      String item = externalTask.getVariable("item");
      Long amount = externalTask.getVariable("amount");
      LOG.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");


      // Complete the task
      externalTaskService.complete(externalTask);
    };
  }

}
