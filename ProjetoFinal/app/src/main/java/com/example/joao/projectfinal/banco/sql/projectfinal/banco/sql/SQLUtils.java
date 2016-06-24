package com.example.joao.projectfinal.banco.sql.projectfinal.banco.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.example.joao.projectfinal.banco.annotations.Attribute;
import com.example.joao.projectfinal.banco.annotations.CharField;
import com.example.joao.projectfinal.banco.annotations.NotNull;
import com.example.joao.projectfinal.banco.annotations.PrimaryKey;
import com.example.joao.projectfinal.banco.annotations.Table;
import com.example.joao.projectfinal.banco.annotations.Unique;


/**
 * Created by joao on 11/06/2016.
 */
public class SQLUtils {

    private static String getPreferences(Class<?> type, Field f) throws Exception {
        String aux = getTypeSQL(type) + " ";
        if (type == String.class) {
            com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.CharField ch = f.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.CharField.class);
            if (ch != null) {
                aux += "(" + ch.max_length() + ") ";
            } else {
                throw new Exception("erro falta max_length para charfield");
            }
        }

        com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.NotNull not = f.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.NotNull.class);
        if (not != null && not.isNotNull) {
            aux += "NOT NULL ";
        }

        com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.PrimaryKey key = f.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.PrimaryKey.class);
        if (key != null && key.isPrymaryKey) {
            aux += "PRIMARY KEY " + (key.auto_generated() ? " AUTOINCREMENT " : "");
        }

        com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Unique uni = f.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Unique.class);
        if (uni != null && uni.isUnique) {
            aux += "UNIQUE ";
        }

        return aux;
    }

    private static String getTypeSQL(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return "INTEGER";
        } else if (type == String.class) {
            return "varchar";
        } else if (float.class == type) {
            return "REAL";
        } else if(long.class == type){
            return "INTEGER";
        }

        return null;
    }

    public static String makeCreate(Class<?> source) throws Exception {
        Field[] fields = source.getDeclaredFields();
        String create = "CREATE TABLE %s (%s);";
        String body = "";
        String table = findTable(source);
        for (Field f : fields) {
            com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Attribute a = f.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Attribute.class);
            String aux = "";
            if (a != null) {
                aux = a.column() + " " + getPreferences(a.type(), f) + ",\n ";
            }
            body += aux;
        }
        body = body.substring(0, body.length() - 3);
        return String.format(create, table, body);
    }

    public static String findTable(Class<?> source) throws NoSuchMethodException {
        Constructor[] list = source.getConstructors();
        for (Constructor con : list) {
            com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Table table = (com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Table) con.getAnnotation(com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations.Table.class);
            if (table != null) {
                return table.name();
            }
        }
        return null;
    }

    public static String makeSelect(Class<?> source) throws Exception {
        Field[] fields = source.getDeclaredFields();
        String create = "select * from %s";
        String table = findTable(source);
        return String.format(create, table);
    }

}
