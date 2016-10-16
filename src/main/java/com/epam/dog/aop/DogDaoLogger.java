package com.epam.dog.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DogDaoLogger implements InvocationHandler{

    private Object invocationTarget;

    public DogDaoLogger(Object invocationTarget) {
        this.invocationTarget = invocationTarget;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Before execution");
        Object result = method.invoke(invocationTarget, args);
        System.out.println("After execution");

        return result;
    }
}
