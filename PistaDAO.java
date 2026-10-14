package es.educastur.diegoaf65.dao;

import es.educastur.diegoaf65.modelo.Pista;

import java.sql.SQLException;
import java.util.List;

public interface PistaDAO {

    // C2: pistas de un género
    List<Pista> findByGenero(int generoId) throws SQLException;

    // C5: pistas de un álbum, con su género
    List<Pista> findByAlbum(int albumId) throws SQLException;
}