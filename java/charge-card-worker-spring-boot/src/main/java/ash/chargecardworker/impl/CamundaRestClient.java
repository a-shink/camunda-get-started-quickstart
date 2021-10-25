package ash.chargecardworker.impl;

import org.springframework.http.ResponseEntity;

public interface CamundaRestClient {
    ResponseEntity<String> startProcess(String item, long amount);
    long countProcessInstances();
}
