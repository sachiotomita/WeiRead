package com.hsf1002.sky.weread.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by hefeng on 18-5-17.
 */

public class IOUtils {
    public static void close(Closeable closeable){
        if (closeable == null)
        {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
            //close error
        }
    }
}
