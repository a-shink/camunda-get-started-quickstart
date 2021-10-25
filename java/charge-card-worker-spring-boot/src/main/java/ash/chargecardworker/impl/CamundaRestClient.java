package ash.chargecardworker.impl;

import org.springframework.http.ResponseEntity;

public interface CamundaRestClient {
    boolean startProcess(String item, long amount);
    long countProcessInstances();
}
