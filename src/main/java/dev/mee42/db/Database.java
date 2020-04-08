package dev.mee42.db;

import org.jdbi.v3.core.Jdbi;

public class Database {
    public static Jdbi connection = Jdbi.create("jdbc:mariadb://localhost:3306/planetSim?user=ssh&password=ssh-ssh-ssh");
}


