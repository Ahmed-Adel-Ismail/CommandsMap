package com.tere.playground.apt;

import android.util.Log;

import com.annotations.Command;
import com.annotations.CommandsMapFactory;

/**
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
@CommandsMapFactory
class MyCommandsMap {

    private static final String TAG = MyCommandsMap.class.getSimpleName();

    @Command(keyString = "0")
    void printZero() {
        Log.d(TAG, "printZero()");
    }

    @Command(keyLong = 1L)
    void printOne(String s) {
        Log.e(TAG, s);
    }

    @Command(2)
    void printTwo(String s1, String s2) {
        Log.e(TAG, s1 + s2);
    }
}
