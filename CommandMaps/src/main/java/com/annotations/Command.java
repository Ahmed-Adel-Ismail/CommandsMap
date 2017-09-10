package com.annotations;

import com.mapper.CommandsMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that marks a method that it is a Command and it can be executed through a
 * {@link CommandsMap}, this annotation should hold a value that indicates a key
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    int NULL_INTEGER = -99;
    long NULL_LONG = -99L;
    double NULL_DOUBLE = -99D;
    float NULL_FLOAT = -99F;
    String NULL_STRING = "#^@_NULL**";

    /**
     * set the key as an {@code int} value
     *
     * @return the {@code int} value that indicates a key for the Command
     */
    int value() default NULL_INTEGER;

    /**
     * set the key as an {@code long} value
     *
     * @return the {@code long} value that indicates a key for the Command
     */
    long keyLong() default NULL_LONG;

    /**
     * set the key as an {@code double} value
     *
     * @return the {@code double} value that indicates a key for the Command
     */
    double keyDouble() default NULL_DOUBLE;

    /**
     * set the key as an {@code float} value
     *
     * @return the {@code float} value that indicates a key for the Command
     */
    float keyFloat() default NULL_FLOAT;

    /**
     * set the key as an {@code String} value
     *
     * @return the {@code String} value that indicates a key for the Command
     */
    String keyString() default NULL_STRING;


}
