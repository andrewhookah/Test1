package org.example;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TestRunner {
    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(MethodInterception.class);
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

        public static class MethodInterception {

            @Test
            public void annotationValue() {
                MainPage mainPage = createPage(MainPage.class);
                assertNotNull(mainPage);
                assertEquals(mainPage.buttonSearch(), ".//*[@test-attr='button_search']");
                assertEquals(mainPage.textInputSearch(), ".//*[@test-attr='input_search']");
            }

            private MainPage createPage(Class clazz) {
                MainPage mainPage = new MainPage() {
                    @Override
                    public String textInputSearch() {
                        return ".//*[@test-attr='input_search']";
                    }

                    @Override
                    public String buttonSearch() {
                        return ".//*[@test-attr='button_search']";
                    }
                };
                ClassLoader classLoader = mainPage.getClass().getClassLoader();
                Class[] interfaces = mainPage.getClass().getInterfaces();
                return (MainPage) Proxy.newProxyInstance(classLoader, interfaces, new Handler(mainPage));
            }
        }
    }