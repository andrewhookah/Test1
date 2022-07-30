package org.example;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class Main {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MethodInterception.class);
        System.out.println(result.wasSuccessful());
    }
        @Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
        @Target({METHOD, TYPE})
        public @interface Selector {

            String xpath();
        }

        public interface MainPage {

            @Selector(xpath = ".//*[@test-attr='input_search']")
            String textInputSearch();

            @Selector(xpath = ".//*[@test-attr='button_search']")
            String buttonSearch();
        }

        public class MethodInterception {

            @Test
            public void annotationValue() {
                MainPage mainPage = createPage(MainPage.class);
                assertNotNull(mainPage);
                assertEquals(mainPage.buttonSearch(), ".//*[@test-attr='button_search']");
                assertEquals(mainPage.textInputSearch(), ".//*[@test-attr='input_search']");
            }

            private MainPage createPage(Class clazz) {
                ClassLoader classLoader = clazz.getClassLoader();
                Class[] interfaces = clazz.getInterfaces();
                MainPage proxy = (MainPage) Proxy.newProxyInstance(classLoader, interfaces, new Handler());
                return proxy;
            }
        }
    }