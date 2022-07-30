package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Handler implements InvocationHandler {
    TestRunner.MainPage mainPage;
    public Handler(TestRunner.MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(mainPage,args);
    }
}
