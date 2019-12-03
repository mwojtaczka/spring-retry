package com.simple.springretry.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MyAnnotatedRetryableServiceImplTest {

    @Autowired
    private MyAnnotatedRetryableService myAnnotatedRetryableService;

    @Autowired
    private RetryTemplate retryTemplate;

    @After
    public void reset() {
        myAnnotatedRetryableService.reset();
    }

    @Test(expected = RuntimeException.class)
    public void shouldRetry3TimesBecauseItIsDefault_whenRuntimeException() {

        myAnnotatedRetryableService.retryableMethod();

        assertThat(myAnnotatedRetryableService.getCounter()).isEqualTo(3);
    }

    @Test
    public void shouldRetry2TimesThenCallRecoveryMethod_whenSqlException() throws SQLException {

        myAnnotatedRetryableService.retryableMethodForSQLExceptionsAndMaxAttempts2AndBackoff1000("");

        assertThat(myAnnotatedRetryableService.getCounter()).isEqualTo(2);
        assertThat(myAnnotatedRetryableService.isRecovered()).isTrue();
    }

    @Test
    public void shouldNotRetryAndNotCallRecoveryMethod_whenDifferentThenSqlException() throws SQLException {

        myAnnotatedRetryableService.retryableMethodForSQLExceptionsAndMaxAttempts2AndBackoff1000("sql_statement");

        assertThat(myAnnotatedRetryableService.getCounter()).isEqualTo(0);
        assertThat(myAnnotatedRetryableService.isRecovered()).isFalse();
    }
}
