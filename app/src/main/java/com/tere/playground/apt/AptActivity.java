package com.tere.playground.apt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapper.CommandsMap;
import com.tere.playground.R;

/**
 * a test activity for annotation processing and Commands Map operations
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
public class AptActivity extends AppCompatActivity {

    private CommandsMap commandsMap;

    public AptActivity() {

        // initializing without reflections :
        commandsMap = new com.tere.playground.apt.MyCommandsMap$$CommandsMap();
        commandsMap.setCommandsMapFactory(new MyCommandsMap());

        // initializing with reflections
        commandsMap = CommandsMap.of(new MyCommandsMap());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandsMap.execute("0");
        commandsMap.execute(1L, "test >>> 1");
        commandsMap.execute(2, "test >>> 2", "<<<<<");
    }


    @Override
    protected void onDestroy() {
        commandsMap.clear();
        super.onDestroy();
    }
}
