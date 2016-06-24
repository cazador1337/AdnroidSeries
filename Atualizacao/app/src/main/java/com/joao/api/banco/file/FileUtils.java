package com.joao.api.banco.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.joao.api.banco.core.AbstractModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.Buffer;

/**
 * Created by joao on 18/06/2016.
 */
public class FileUtils {

    public static void writeBlob(Object source, Class<?> type, String name) {
        FileOutputStream ops;

        try {
            ops = AbstractModel.settings.getContext().openFileOutput(name, Context.MODE_PRIVATE);
            ops.write(encode(source, type));
            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] encode(Object source, Class<?> type) {
        if (type == Bitmap.class) {
            Bitmap map = (Bitmap) source;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.PNG, 100, stream);//100 de qualidade
            return stream.toByteArray();
        }
        return null;
    }

    public static Object readBlob(Class<?> type, String name) {
        File file = null;
        try {
            file = File.createTempFile(name, null, AbstractModel.settings.getContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decode(type, file);
    }

    private static Object decode(Class<?> type, File file){
        if (type == Bitmap.class) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }
}
