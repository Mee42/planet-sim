package dev.mee42.db;

import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Database {
    private static String getPassword() {
        try {
            return Files.readAllLines(new File("./key.txt").toPath()).get(0).trim();
        } catch (IOException e) {
            throw doThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <E extends Exception> E doThrow(Exception e) throws E {
        throw (E) e;
    }
    private static String password = getPassword();

    public static Jdbi connection = Jdbi.create("jdbc:mariadb://localhost:3306/planetSim?user=ssh&password=" + password);
}