package com.simple.springretry.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MyRetryableServiceWithRetryTemplateTest {


    @Autowired
    private MyRetryableServiceWithRetryTemplate myRetryableServiceWithRetryTemplate;

    @Autowired
    private DefaultListenerSupport listenerSupport;

    @Before
    public void reset() {
        myRetryableServiceWithRetryTemplate.reset();
        listenerSupport.reset();
    }

    // IllegalArgumentException - retryable exception due to RetryTemplateConfig
    // NullPointerException - not retryable exception

    @Test
    public void shouldRetry2Times_whenIllegalArgumentExceptionThrown() {

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, true);

        assertThat(myRetryableServiceWithRetryTemplate.counter).isEqualTo(2);
    }

    @Test
    public void shouldReturnRecoveryMessage_whenIllegalArgumentExceptionThrown() {

        String result = myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, true);

        assertThat(result).startsWith("Operation failed after 2 tries");
    }

    @Test
    public void shouldReturnRecoveryMessageButTryOnlyOnce_whenNullPointerExceptionThrown() {

        String result = myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, false);

        assertThat(result).startsWith("Operation failed after 1 tries");
    }

    @Test
    public void shouldInvokeOpenMethodFromListener() {

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(false, false);

        assertThat(listenerSupport.opened).isTrue();
    }

    @Test
    public void shouldInvokeCloseMethodFromListener() {

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(false, false);
        assertThat(listenerSupport.closed).isTrue();

        listenerSupport.reset();

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, false);
        assertThat(listenerSupport.closed).isTrue();

    }

    @Test
    public void shouldInvokeOnErrorMethodFromListener_whenAnyExceptionThrown() {

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, false);
        assertThat(listenerSupport.errorThrown).isTrue();

        listenerSupport.reset();

        myRetryableServiceWithRetryTemplate.methodWithRetryTemplate(true, true);
        assertThat(listenerSupport.errorThrown).isTrue();
    }

}
