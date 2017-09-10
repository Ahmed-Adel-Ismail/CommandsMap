package com.mapper;

/**
 * a function that does not throw an {@link Exception} in it's {@link #accept(Object)}
 * method
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
public interface Command {

    void accept(Object object);

}
