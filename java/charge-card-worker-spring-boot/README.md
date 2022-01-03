Cloned Camunda getting started repo.
Java sample rewritten with Spring Boot.
 
Used [pom.xml](https://github.com/camunda/camunda-bpm-examples/blob/master/spring-boot-starter/external-task-client/request-interceptor-spring-boot/pom.xml) from Camunda sample for external service on Spring Boot 

Use this curl command to send message to process:
`curl -H "Content-Type: application/json" -X POST -d '{"messageName": "msg_break_proc", "businessKey": "c41f64e1-9c66-4a6b-926a-4bd618514d79"}' http://localhost:8080/engine-rest/message`