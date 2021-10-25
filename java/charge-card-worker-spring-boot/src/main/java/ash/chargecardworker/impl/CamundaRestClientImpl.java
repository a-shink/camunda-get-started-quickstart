package ash.chargecardworker.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CamundaRestClientImpl implements CamundaRestClient {
    @Value("${camunda.bpm.client.base-url:http://localhost:8080/engine-rest}")
    private String camundaBaseURL;

    @Override
    public ResponseEntity<String> startProcess(String item, long amount) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"variables\": {\"amount\": {\"value\":%1$d, \"type\":\"long\"}, \"item\": {\"value\": \"%2$s\"} } }", amount, item);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                camundaBaseURL + "/process-definition/key/payment-retrieval/start", request , String.class);

        return response;
    }
}
