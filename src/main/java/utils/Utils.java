package utils;

import java.io.File;

public class Utils {

    private static Utils INSTANCE;

    public static Utils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Utils();
        }

        return INSTANCE;
    }

    public File getFileFromResources(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }
}
