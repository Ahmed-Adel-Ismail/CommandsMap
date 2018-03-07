package com.tere.playground.apt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.mapper.CommandsMap;
import com.tere.playground.R;

/**
 * a test activity for annotation processing and Commands Map operations
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
@CommandsMapFactory
public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    private CommandsMap commandsMap;

    public TestActivity() {

        // initializing with reflections
        commandsMap = CommandsMap.of(this);

        // initializing without reflections :
        commandsMap = new com.tere.playground.apt.TestActivity$$CommandsMap();
        commandsMap.setCommandsMapFactory(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandsMap.execute("0");
        commandsMap.execute(1L, "test >>> 1");
        commandsMap.execute(2, "test >>> 2", "<<<<<");
    }

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


    @Override
    protected void onDestroy() {
        commandsMap.clear();
        super.onDestroy();
    }
}
