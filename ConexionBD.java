package es.educastur.diegoaf65.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ConexionBD {

    private static final String URL = "jdbc:mariadb://localhost:3306/chinook";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}