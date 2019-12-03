package com.simple.springretry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry //this annotation is sufficient to enable @Retryable
public class AnnotationRetryConfig {

}
