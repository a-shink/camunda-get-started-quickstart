package ash.chargecardworker.app;

import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfiguration {

  protected static Logger LOG = LoggerFactory.getLogger(InterceptorConfiguration.class);

  @Bean
  public ClientRequestInterceptor interceptor() {
    return context -> {
      //LOG.info("Request interceptor called!");
      context.addHeader("X-MY-HEADER", "External Tasks Rock!");
    };
  }

}
