package com.simple.springretry.config;

import com.simple.springretry.service.DefaultListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Configuration
public class RetryTemplateConfig {

  @Bean
  public RetryTemplate retryTemplate(DefaultListenerSupport listenerSupport) {

    Map<Class<? extends Throwable>, Boolean> exceptionsRules = new HashMap<>();
    exceptionsRules.put(IllegalArgumentException.class, true); //that is obvious that we should retry only in case of sensible exception, IllegalArgumentException is not one of them

    RetryTemplate retryTemplate = new RetryTemplate();

    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy(); //policy means strategy how the gaps between retries should look like (constant in case of fixed policy)
    fixedBackOffPolicy.setBackOffPeriod(20L); //here we set how long should wait until next retry
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(2, exceptionsRules);

    retryTemplate.setRetryPolicy(retryPolicy);
    retryTemplate.registerListener(listenerSupport); //we can register listener which can invoke actions on open, error, close

    return retryTemplate;
  }

}
