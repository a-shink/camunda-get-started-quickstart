package ash.chargecardworker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
public class ProcessStarter {
    private static final List<String> items = List.of("FIRST ITEM", "item two", "Item#3", "Medium price product", "Luxury item$$$");

    @Value("${camunda.bpm.client.base-url:http://localhost:8080/engine-rest}")
    String camundaBaseURL;

    private String someDefault;
    @Scheduled(fixedDelay = 5000)
    public void scheduleStartCamundaPaymentRetrieval() {
        long instancesNow = Application.instancesCreated.get();

        if (instancesNow > 10) {
            System.out.println("Too many instances already created - so skip creating new");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long amount = 200l + new Random().nextInt(300);
        String item = items.get(new Random().nextInt(items.size()));
        String body = String.format("{\"variables\": {\"amount\": {\"value\":%1$d, \"type\":\"long\"}, \"item\": {\"value\": \"%2$s\"} } }", amount, item);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                camundaBaseURL + "/process-definition/key/payment-retrieval/start", request , String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Not 200 code returned but " + response.getStatusCodeValue() + " check response:");
            System.out.println(response.getBody());
        } else {
            Application.instancesCreated.incrementAndGet();
        }
    }
}
