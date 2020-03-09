package ru.job4j.servlets.crud.persistent.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseCommonPool {
    private final static BaseCommonPool INSTANCE = new BaseCommonPool();
    private final BasicDataSource source = new BasicDataSource();
//    private static final String DB_NAME = "d9c1859o46sjnh";
    private static final String DB_NAME = "users";

    private BaseCommonPool() {
//        source.setDriverClassName("org.postgresql.Driver");
//        source.setUrl("jdbc:postgresql://ec2-54-195-247-108.eu-west-1.compute.amazonaws.com:5432/" + DB_NAME);
//        source.setUsername("pythnrqxrddjgq");
//        source.setPassword("15f9beff1fbe64a2cae5a40b0a7c304771db484b543e4aed7ed9971b43aa999e");
        source.setDriverClassName("org.postgresql.Driver");
        source.setUrl("jdbc:postgresql://localhost:5432/" + DB_NAME);
        source.setUsername("postgres");
        source.setPassword("password");
        source.setMinIdle(5);
        source.setMaxIdle(10);
        source.setMaxOpenPreparedStatements(100);
    }

    public static BaseCommonPool getInstance() {
        return INSTANCE;
    }

    public Connection getConnect() throws SQLException {
        return source.getConnection();
    }
}
