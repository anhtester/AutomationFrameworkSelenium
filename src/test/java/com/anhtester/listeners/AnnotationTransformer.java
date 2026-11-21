package com.anhtester.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.DisabledRetryAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Add the {@link Retry} analyzer (RETRY_TEST_FAIL) to every test method.
 * <p>
 * Registered for every run in src/test/resources/META-INF/services/org.testng.ITestNGListener:
 * it also works when a test class is run directly from the IDE (an IAnnotationTransformer is ignored in @Listeners).
 * A test with its own {@code @Test(retryAnalyzer = ...)} keeps it.
 */
public class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        Class<?> current = annotation.getRetryAnalyzerClass();
        if (current == null || current == DisabledRetryAnalyzer.class) {
            annotation.setRetryAnalyzer(Retry.class);
        }
    }
}
