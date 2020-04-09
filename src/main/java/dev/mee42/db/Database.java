package dev.mee42.db;

import dev.mee42.Util;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Database {


    private static String password = Util.getPassword().get(0).trim();

    public static Jdbi connection = Jdbi.create("jdbc:mariadb://localhost:3306/planetSim?user=ssh&password=" + password);
}