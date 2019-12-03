package com.simple.springretry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class MyRetryableServiceWithRetryTemplate{

  private static final Logger logger = LoggerFactory.getLogger(MyRetryableServiceWithRetryTemplate.class);
  public int counter;
  public boolean recovered;

  @Autowired
  private RetryTemplate retryTemplate;


  public String methodWithRetryTemplate(boolean shouldThrow, boolean retryableException) {

    return retryTemplate.execute(
            retry(shouldThrow, retryableException),
            recover()
    );
  }

  public void reset() {
    counter = 0;
    recovered = false;
  }

  private RetryCallback<String, RuntimeException> retry(boolean throwException, boolean exceptionRetryable) {
    return context -> { //RetryContext - it is kind of shared container between retries
      counter++;
      context.setAttribute(context.getRetryCount() + "-TryDateTime", LocalDateTime.now()); //that way we e.g. can log time of each retry

      logger.info("throw RuntimeException in methodWithRetryTemplate");
      if (throwException) {
        if (exceptionRetryable) {
          throw new IllegalArgumentException();
        } else {
          throw new NullPointerException();
        }
      }
      return "success";
    };
  }

  private RecoveryCallback<String> recover() {
    recovered = true;
    return retryContext -> String.format("Operation failed after %s tries, first retry at: %s, second at: %s",
            retryContext.getRetryCount(),
            retryContext.getAttribute("0-TryDateTime"),
            retryContext.getAttribute("1-TryDateTime"));
  }

}
