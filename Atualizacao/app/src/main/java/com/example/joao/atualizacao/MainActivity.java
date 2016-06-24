package com.example.joao.atualizacao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TESTE", genTitle("ola"));
    }

    private String genTitle(String name) {
        String pattern = "%y_%m_%d_%n";

        GregorianCalendar now = new GregorianCalendar();

        pattern = pattern.replace("%y", now.get(GregorianCalendar.YEAR) + "");
        pattern = pattern.replace("%m", now.get(GregorianCalendar.MONTH) + "");
        pattern = pattern.replace("%d", now.get(GregorianCalendar.DAY_OF_MONTH) + "");
        pattern = pattern.replace("%n", name);

        return pattern;
    }
}
