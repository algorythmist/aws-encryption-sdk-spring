package com.tecacet.awssecurity.service;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {

    @Test
    void test() throws SQLException {
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/",
                "postgres", "postgres");
    }
}
