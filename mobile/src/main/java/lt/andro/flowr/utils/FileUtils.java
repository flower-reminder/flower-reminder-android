package lt.andro.flowr.utils;

import java.io.IOException;
import java.io.InputStream;

import lt.andro.flowr.MainApplication;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 05:19.
 */
public class FileUtils {
    public static String readFile(int locations) {
        try {
            InputStream is = MainApplication.context.getResources().openRawResource(locations);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
