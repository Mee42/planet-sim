package dev.mee42;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Util {

    public static List<String> getPassword() {
        return makeUnchecked(() -> Files.readAllLines(new File("./key.txt").toPath()));
    }
    public interface Wrapper<T> {
        T execute() throws Exception;
    }
    public static <T> T makeUnchecked(Wrapper<T> wrapper) {
        try {
            return wrapper.execute();
        } catch (Exception e) {
            throw doThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <E extends Exception> E doThrow(Exception e) throws E {
        throw (E) e;
    }
}
