package com.example.joao.projectfinal.banco.sql.projectfinal.controller;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.example.joao.projectfinal.R;
import com.example.joao.projectfinal.banco.sql.Q;
import com.example.joao.projectfinal.custom.SeriesAdapter;
import com.example.joao.projectfinal.models.Serie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by joao on 23/06/2016.
 */
public class SerieManager {
    private ListView listView;
    private boolean onlyWatching = false;
    private boolean onlyComplete = false;
    private boolean onlyPlanToWatch = false;
    private com.example.joao.projectfinal.banco.sql.projectfinal.custom.SeriesAdapter adapter;
    private List<com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie> list;

    public SerieManager(ListView listView, Activity main) {
        this.listView = listView;
        list = new LinkedList<>();
        adapter = new com.example.joao.projectfinal.banco.sql.projectfinal.custom.SeriesAdapter(main, R.layout.list_series, list);
        listView.setAdapter(adapter);
    }

    public void update() {
        if (onlyComplete || onlyPlanToWatch || onlyWatching) {
            setList(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.objects.filter(getFilter()));
        } else {
            setList(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.objects.all());
        }
    }

    public void setOnlyWatching(boolean onlyWatching) {
        this.onlyWatching = onlyWatching;
    }

    public void setOnlyComplete(boolean onlyComplete) {
        this.onlyComplete = onlyComplete;
    }

    public void setOnlyPlanToWatch(boolean onlyPlanToWatch) {
        this.onlyPlanToWatch = onlyPlanToWatch;
    }

    public void search(String query) {
        setList(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.objects.filter(getFilter(com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.like(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.TITULO, query), com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.OR, com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.like(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.GENEROS, query))));
    }

    private com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[] getFilter() {
        int size = 0;
        boolean[] bols = new boolean[]{onlyComplete, onlyWatching, onlyPlanToWatch};
        com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[] opts = new com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[]{com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.equal(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.STATUS, com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.COMPLETO),
                com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.equal(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.STATUS, com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.ASSISTINDO), com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.equal(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.STATUS, com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie.PLANO)};
        ArrayList<com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option> array = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (bols[i]) {
                array.add(opts[i]);
                array.add(com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.OR);
            }
        }
        if (array.isEmpty()) return null;
        array.remove(array.size() - 1);
        return array.toArray(new com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[array.size()]);
    }

    private com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[] getFilter(com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option... more) {
        ArrayList<com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option> array = new ArrayList<>();
        com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[] aux = getFilter();
        if (aux != null) {
            for (com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option o : aux) {
                array.add(o);
            }
            array.add(com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.AND);
        }

        for (com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option o : more) {
            array.add(o);
        }
        return array.toArray(new com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql.Q.Option[array.size()]);
    }

    public void setList(com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie[] series) {
        list.clear();
        if (series != null) {
            for (com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie s : series) {
                list.add(s);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public com.example.joao.projectfinal.banco.sql.projectfinal.models.Serie get(int position) {
        return list.get(position);
    }
}
