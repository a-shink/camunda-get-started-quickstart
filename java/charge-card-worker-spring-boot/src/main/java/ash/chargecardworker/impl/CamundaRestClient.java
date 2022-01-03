package ash.chargecardworker.impl;

public interface CamundaRestClient {
    boolean startProcess(String businessKey, int num, String item, long amount);
    long countProcessInstances();
    String getProcessInstanceVariables(String instanceId);
}
