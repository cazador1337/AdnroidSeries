package com.example.joao.series;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.joao.series.models.Serie;
import com.joao.api.banco.sql.Q;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Serie.open(getBaseContext(), "SerieDB", 1, Serie.class);
        Serie s = Serie.objects.get(Q.equal(Serie.ID, 1));
        Log.i("TESTE", s.getScore() + "");
    }
}
