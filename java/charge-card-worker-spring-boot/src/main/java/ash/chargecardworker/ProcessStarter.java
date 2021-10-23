package ash.chargecardworker;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
public class ProcessStarter {
    private static final List<String> items = List.of("FIRST ITEM", "item two", "Item#3", "Medium price product", "Luxury item$$$");

    @Scheduled(fixedDelay = 5000)
    public void scheduleStartCamundaPaymentRetrieval() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long amount = 200l + new Random().nextInt(300);
        String item = items.get(new Random().nextInt(items.size()));
        String body = String.format("{\"variables\": {\"amount\": {\"value\":%1$d, \"type\":\"long\"}, \"item\": {\"value\": \"%2$s\"} } }", amount, item);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/engine-rest/process-definition/key/payment-retrieval/start", request , String.class);
        //System.out.println(response.getBody());
    }
}
