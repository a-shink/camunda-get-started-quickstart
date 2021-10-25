package ash.chargecardworker.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CamundaRestClientImpl implements CamundaRestClient {
    @Value("${camunda.bpm.client.base-url:http://localhost:8080/engine-rest}")
    private String camundaBaseURL;

    @Value("${process.defkey}")
    private String processDefinitionKey;


    RestTemplate restTemplate;

    public CamundaRestClientImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public ResponseEntity<String> startProcess(String item, long amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"variables\": {\"amount\": {\"value\":%1$d, \"type\":\"long\"}, \"item\": {\"value\": \"%2$s\"} } }", amount, item);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                camundaBaseURL + "/process-definition/key/" + processDefinitionKey + "/start", request , String.class);

        return response;
    }

    public long countProcessInstances() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("processDefinitionKey", processDefinitionKey);

        ResponseEntity<CountProcessInstancesResult> response = restTemplate.getForEntity(
                camundaBaseURL + "/process-instance/count", CountProcessInstancesResult.class, uriVariables);

        return (response.getStatusCode() == HttpStatus.OK)? response.getBody().getCount() : -1;
    }

    static class CountProcessInstancesResult{
        long count;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
