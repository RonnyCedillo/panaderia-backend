package com.panaderia.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // ¡PON TU CONTRASEÑA AQUÍ!
    private static final String URL = "jdbc:postgresql://localhost:5432/panaderia_db";
    private static final String USER = "postgres";
    private static final String PASS = "1234"; 

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}