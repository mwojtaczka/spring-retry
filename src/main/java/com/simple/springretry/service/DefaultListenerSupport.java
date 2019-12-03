package com.simple.springretry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class DefaultListenerSupport extends RetryListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(DefaultListenerSupport.class);

    public boolean opened;
    public boolean closed;
    public boolean errorThrown;

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
        logger.info("in onClose");
        closed = true;
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
        logger.info("in onError");
        errorThrown = true;
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        logger.info("in onOpen");
        opened = true;
        return super.open(context, callback);
    }

    public void reset() {
        opened = false;
        closed = false;
        errorThrown = false;
    }
}
