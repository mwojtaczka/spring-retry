# Spring retry  
This repo contains examples of Spring retry usage.  
## Where to start  
To start I recommend clone repo and go through code starting from test classes:
- MyAnnotatedRetryableServiceImplTest - contains usage of `@Retryable` annotation which is defined and configured 
directly in `MyAnnotatedRetryableService` (don't forget `@EnableRetry` above one of configuration classes)
- MyRetryableServiceWithRetryTemplateTest - more advanced way to use retries is to use RetryTemplate which is configured 
in `RetryTemplateConfig`. That is what those couple of tests introduce into.