package com.simple.springretry.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.sql.SQLException;


public interface MyAnnotatedRetryableService {

  @Retryable //this method will be retried 3 times by default
  void retryableMethod();

  @Retryable(
      value = {SQLException.class}, //indicates for what kind of exception will retry
      maxAttempts = 2,
      backoff = @Backoff(delay = 1000)) //how long should wait until next retry
  void retryableMethodForSQLExceptionsAndMaxAttempts2AndBackoff1000(String sql) throws SQLException;

  @Recover //this method will be invoked when all retries from above method exceed
  void recover(SQLException e, String sql); //the framework match this method to all retryable method based on arguments passed into (in our case String)

  int getCounter();

  boolean isRecovered();

  void reset();
}
