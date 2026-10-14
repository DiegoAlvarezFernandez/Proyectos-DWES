package es.educastur.diegoaf65.dao;

import es.educastur.diegoaf65.conexion.ConexionBD;
import es.educastur.diegoaf65.modelo.Genero;
import es.educastur.diegoaf65.modelo.Pista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PistaDAOJdbc implements PistaDAO {

    private static final String SELECT_PISTA =
            "SELECT t.TrackId, t.Name, t.Composer, t.Milliseconds, t.UnitPrice, "
                    + "g.GenreId, g.Name AS GenreName "
                    + "FROM Track t JOIN Genre g ON g.GenreId = t.GenreId ";

    @Override
    public List<Pista> findByGenero(int generoId) throws SQLException {
        String sql = SELECT_PISTA + "WHERE t.GenreId = ? ORDER BY t.Name";
        return consultar(sql, generoId);
    }

    @Override
    public List<Pista> findByAlbum(int albumId) throws SQLException {
        // TODO 1 y 2: completa el SQL filtrando por AlbumId y devuelve el resultado de consultar
        String sql = SELECT_PISTA + "WHERE t.AlbumId = ? ORDER BY t.TrackId";
        return consultar(sql, albumId);
    }

    private List<Pista> consultar(String sql, int parametro) throws SQLException {
        List<Pista> pistas = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, parametro);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pistas.add(mapearPista(resultado));
                }
            }
        }
        return pistas;
    }

    private Pista mapearPista(ResultSet resultado) throws SQLException {
        Pista pista = new Pista();
        pista.setId(resultado.getInt("TrackId"));
        pista.setNombre(resultado.getString("Name"));
        pista.setCompositor(resultado.getString("Composer"));

        // TODO 3: guarda los milisegundos
        pista.setMilisegundos(resultado.getInt("Milliseconds"));

        // TODO 4: guarda el precio unitario
        pista.setPrecioUnitario(resultado.getBigDecimal("UnitPrice"));

        // TODO 5: crea el objeto Genero y asígnalo a la pista
        pista.setGenero(new Genero(resultado.getInt("GenreId"), resultado.getString("GenreName")));

        return pista;
    }
}