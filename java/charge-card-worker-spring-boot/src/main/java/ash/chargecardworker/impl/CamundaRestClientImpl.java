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
    public boolean startProcess(String businessKey, int num, String item, long amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"businessKey\": \"%3$s\",\"variables\": " +
                "{\"amount\": {\"value\":%1$d, \"type\":\"long\"}, \"num\": {\"value\":%4$d, \"type\":\"integer\"}, \"item\": {\"value\": \"%2$s\"} } }",
                amount, item, businessKey, num);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                camundaBaseURL + "/process-definition/key/" + processDefinitionKey + "/start", request , String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Not 200 code returned but " + response.getStatusCodeValue() + " check response:");
            System.out.println(response.getBody());
            return false;
        }

        return true;
    }

    public long countProcessInstances() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("processDefinitionKey", processDefinitionKey);

        ResponseEntity<CountProcessInstancesResult> response = restTemplate.getForEntity(
                camundaBaseURL + "/process-instance/count?processDefinitionKey=" + processDefinitionKey, CountProcessInstancesResult.class);

        long count = (response.getStatusCode() == HttpStatus.OK)? response.getBody().getCount() : -1;
        return count;
    }

    public String getProcessInstanceVariables(String instanceId) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                camundaBaseURL + String.format("/process-instance/%1$s/variables", instanceId), String.class);
        String answer = (response.getStatusCode() == HttpStatus.OK)? response.getBody(): "";
        return answer;
    }
    static class CountProcessInstancesResult{
        private long count;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
