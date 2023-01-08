package com.iexceed.uiframework.listener;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Listener class to rerun the test cases before marking them as failed.
 */
public class RetryListener implements IAnnotationTransformer {
    @SuppressWarnings("rawtypes")
    public void transform(final ITestAnnotation annotation, final Class testClass, final Constructor testConstructor,
                          final Method testMethod) {
        final IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null) {
            annotation.setRetryAnalyzer(Retry.class);
        }
    }
}
