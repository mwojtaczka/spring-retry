package com.simple.springretry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

@Service
public class MyAnnotatedRetryableServiceImpl implements MyAnnotatedRetryableService {

  private static final Logger logger = LoggerFactory.getLogger(MyAnnotatedRetryableServiceImpl.class);
  private int counter;
  private boolean recovered;

  @Override
  public void retryableMethod() {
    counter++;
    logger.info("throw RuntimeException in method retryableMethod()");
    throw new RuntimeException();
  }

  @Override
  public void retryableMethodForSQLExceptionsAndMaxAttempts2AndBackoff1000(String sql) throws SQLException {
    if (StringUtils.isEmpty(sql)) {
      counter++;
      logger.info("throw SQLException in method retryableMethodForSQLExceptionsAndMaxAttempts2AndBackoff1000()");
      throw new SQLException();
    }
  }

  @Override
  public void recover(SQLException e, String sql) {
    logger.info("In recover method");
    recovered = true;
  }

  @Override
  public int getCounter() {
    return counter;
  }

  @Override
  public boolean isRecovered() {
    return recovered;
  }

  @Override
  public void reset() {
    this.counter = 0;
    this.recovered = false;
  }
}
