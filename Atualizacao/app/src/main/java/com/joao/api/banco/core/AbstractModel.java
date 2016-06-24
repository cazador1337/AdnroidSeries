package com.joao.api.banco.core;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import com.joao.api.banco.annotations.Attribute;
import com.joao.api.banco.annotations.Blob;
import com.joao.api.banco.annotations.PrimaryKey;
import com.joao.api.banco.sql.Q;
import com.joao.api.banco.sql.Settings;
import com.joao.api.banco.sql.SQLUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by joao on 11/06/2016.
 */
public abstract class AbstractModel<T extends Parcelable> {
    private Class<T> model;
    protected static Objects<?> objects = new Objects<>();
    public static Manager manager;
    public static Settings settings;

    public static void open(Context context, String database, int version, Class<?> type) {
        Settings settings = new Settings(database, version, type);
        settings.setContext(context);
        AbstractModel.settings = settings;
        AbstractModel.manager = new Manager(settings);
    }

    public AbstractModel(Class<T> model) {
        this.model = model;
    }

    public AbstractModel() {

    }

    private final ContentValues setValues(Field[] fields, Field key) throws IllegalAccessException {
        ContentValues cv = new ContentValues();
        for (Field field : fields) {
            field.setAccessible(true);
            Attribute a = field.getAnnotation(Attribute.class);
            if (a != null && field != key) {
                if (a.type() == Integer.class || a.type() == int.class) {
                    cv.put(a.column(), Integer.parseInt(field.get(this).toString()));
                } else if (a.type() == String.class) {
                    cv.put(a.column(), field.get(this).toString());
                } else if (a.type() == Float.class) {
                    cv.put(a.column(), Float.parseFloat(field.get(this).toString()));
                } else if (a.type() == Blob.class){
                    cv.put(a.column(), genTitle(key.get(this)+""));
                }
            }
        }
        return cv;
    }

    private boolean hasBlob(Field[] fields){
        boolean r = false;
        for(Field f : fields){
            r = r || (f.getAnnotation(Blob.class) != null);
            if(r){
                break;
            }
        }
        return r;
    }

    private String genTitle(String key) {
        String pattern = "%y_%m_%d_%n";

        GregorianCalendar now = new GregorianCalendar();

        pattern = pattern.replace("%y", now.get(GregorianCalendar.YEAR) + "");
        pattern = pattern.replace("%m", now.get(GregorianCalendar.MONTH) + "");
        pattern = pattern.replace("%d", now.get(GregorianCalendar.DAY_OF_MONTH) + "");
        pattern = pattern.replace("%n", key);

        return pattern;
    }

    private final Field findKey(Field[] fields) {
        Field keyF = null;
        for (Field f : fields) {
            f.setAccessible(true);
            PrimaryKey p = f.getAnnotation(PrimaryKey.class);
            if (p != null) {
                return f;
            }
        }
        return null;
    }

    private final void setKey(long id) throws IllegalAccessException {
        Field key = findKey(model.getDeclaredFields());
        if (key != null) {
            key.set(this, (int) id);
        }
    }

    public final void save() {
        try {
            SQLiteDatabase db = AbstractModel.manager.getWritableDatabase();
            ContentValues cv = setValues(model.getDeclaredFields(), findKey(model.getDeclaredFields()));
            long id = db.insert(SQLUtils.findTable(model), null, cv);
            setKey(id);
            if(hasBlob(model.getDeclaredFields())){
                update();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBlobs(ContentValues cv, ArrayList<Field> blobs) {
        for(Field f : blobs){
            Attribute a = f.getAnnotation(Attribute.class);
            String name = cv.get(a.column()).toString();
        }
    }

    public final int del() {
        try {
            SQLiteDatabase db = manager.getWritableDatabase();
            Field keyF = findKey(model.getDeclaredFields());
            Attribute key = keyF.getAnnotation(Attribute.class);
            if(hasBlob(model.getDeclaredFields())){
                delBlobs(findBlobs(model.getDeclaredFields()));
            }
            int r = db.delete(SQLUtils.findTable(model), Q.equal(key.column()), new String[]{keyF.get(this).toString()});
            db.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void delBlobs(ArrayList<Field> blobs) {

    }

    public final int update() {
        try {
            SQLiteDatabase db = manager.getWritableDatabase();
            Field keyF = findKey(model.getDeclaredFields());
            Attribute key = keyF.getAnnotation(Attribute.class);
            ContentValues cv = setValues(model.getDeclaredFields(), keyF);
            String[] dic = new String[]{keyF.get(this).toString()};
            int r = db.update(SQLUtils.findTable(model), cv, Q.equal(key.column()), dic);
            db.close();
            if(hasBlob(model.getDeclaredFields())){
                saveBlobs(cv, findBlobs(model.getDeclaredFields()));
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ArrayList<Field> findBlobs(Field[] declaredFields) {
        ArrayList<Field> blobs = new ArrayList<>();
        for(Field f : declaredFields){
            if(f.getAnnotation(Blob.class) != null){
                blobs.add(f);
            }
        }
        return blobs;
    }

    public static class Objects<T extends Parcelable> {
        public static String SQL;

        Objects() {
        }

        private String[] convert(Object... list) {
            String[] r = new String[list.length];
            int i = 0;
            for (Object o : list) {
                r[i++] = o.toString();
            }
            return r;
        }

        private T setData(Cursor cs, Field f, T row) throws IllegalAccessException {
            f.setAccessible(true);
            Attribute attribute = f.getAnnotation(Attribute.class);
            if (attribute != null) {
                if (attribute.type() == String.class) {
                    f.set(row, cs.getString(cs.getColumnIndex(attribute.column())));
                } else if (attribute.type() == int.class || attribute.type() == Integer.class) {
                    f.set(row, cs.getInt(cs.getColumnIndex(attribute.column())));
                } else if (attribute.type() == Float.class) {
                    f.set(row, cs.getFloat(cs.getColumnIndex(attribute.column())));
                }
            }
            return row;
        }

        private T.Creator<T> getCreator(Class<?> core) throws NoSuchFieldException, IllegalAccessException {
            Field field = core.getField("CREATOR");
            field.setAccessible(true);
            return (T.Creator) field.get(null);
        }

        public T[] all() {
            SQLiteDatabase db = AbstractModel.manager.getReadableDatabase();
            try {
                Class<?> core = AbstractModel.settings.getCore();
                Cursor cs = db.rawQuery(SQLUtils.makeSelect(core), null);
                T[] array = getCreator(core).newArray(cs.getCount());
                Field[] fields = core.getDeclaredFields();
                int p = 0;
                while (cs.moveToNext()) {
                    T row = (T) core.newInstance();
                    for (Field f : fields) {
                        setData(cs, f, row);
                    }
                    array[p++] = row;
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public T get(Q.Option... args) {
            SQLiteDatabase db = AbstractModel.manager.getReadableDatabase();
            StringBuilder where = new StringBuilder(" where ");
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                where.append(args[i].getExpression());
                if (args[i].getValue() != null) {
                    values.add(args[i].getValue());
                }
            }

            try {
                Class<?> core = AbstractModel.settings.getCore();
                SQL = SQLUtils.makeSelect(core) + where.toString();
                Cursor cs = db.rawQuery(SQL, convert(values.toArray()));
                Field[] fields = core.getDeclaredFields();
                if (cs.moveToNext()) {
                    T row = (T) core.newInstance();
                    for (Field f : fields) {
                        setData(cs, f, row);
                    }
                    return row;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public T[] filter(Q.Option... args) {
            SQLiteDatabase db = AbstractModel.manager.getReadableDatabase();
            StringBuilder where = new StringBuilder(" where ");
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                where.append(args[i].getExpression());
                if (args[i].getValue() != null) {
                    values.add(args[i].getValue());
                }
            }

            try {
                Class<?> core = AbstractModel.settings.getCore();
                SQL = SQLUtils.makeSelect(core) + where.toString();
                Cursor cs = db.rawQuery(SQL, convert(values.toArray()));

                Field field = core.getField("CREATOR");
                field.setAccessible(true);
                T.Creator<T> creator = (T.Creator) field.get(null);
                T[] array = creator.newArray(cs.getCount());
                Field[] fields = core.getDeclaredFields();

                int p = 0;
                while (cs.moveToNext()) {
                    T row = (T) core.newInstance();
                    for (Field f : fields) {
                        setData(cs, f, row);
                    }
                    array[p++] = row;
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public int drop() {
            try {
                SQLiteDatabase db = manager.getWritableDatabase();
                int r = db.delete(SQLUtils.findTable(settings.getCore()), null, null);
                db.close();
                return r;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
