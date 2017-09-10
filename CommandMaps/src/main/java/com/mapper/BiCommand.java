package com.mapper;


/**
 * a function that does not throw an {@link Exception} in it's {@link #accept(Object, Object)}
 * method
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
public interface BiCommand {

    void accept(Object parameterOne, Object parameterTwo);
}
