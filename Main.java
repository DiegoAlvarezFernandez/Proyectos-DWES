package es.educastur.diegoaf65.app;

import es.educastur.diegoaf65.dao.AlbumDAO;
import es.educastur.diegoaf65.dao.AlbumDAOJdbc;
import es.educastur.diegoaf65.dao.ArtistaDAO;
import es.educastur.diegoaf65.dao.ArtistaDAOJdbc;
import es.educastur.diegoaf65.dao.GeneroDAO;
import es.educastur.diegoaf65.dao.GeneroDAOJdbc;
import es.educastur.diegoaf65.dao.PistaDAO;
import es.educastur.diegoaf65.dao.PistaDAOJdbc;
import es.educastur.diegoaf65.modelo.Album;
import es.educastur.diegoaf65.modelo.Artista;
import es.educastur.diegoaf65.modelo.Genero;
import es.educastur.diegoaf65.modelo.Pista;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);

    private static final GeneroDAO generoDAO = new GeneroDAOJdbc();
    private static final ArtistaDAO artistaDAO = new ArtistaDAOJdbc();
    private static final AlbumDAO albumDAO = new AlbumDAOJdbc();
    private static final PistaDAO pistaDAO = new PistaDAOJdbc();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            try {
                switch (opcion) {
                    case 1 -> listarGeneros();
                    case 2 -> contarPistasPorGenero();
                    case 3 -> buscarArtistas();
                    case 4 -> artistasMasVendidos();
                    case 5 -> albumesDeArtista();
                    case 6 -> pistasDeGenero();
                    case 7 -> pistasDeAlbum();
                    case 8 -> insertarAlbum();
                    case 9 -> cambiarTituloAlbum();
                    case 10 -> borrarAlbum();
                    case 0 -> System.out.println("Hasta pronto.");
                    default -> System.out.println("Esa opción no existe.");
                }
            } catch (SQLException e) {
                System.out.println("No se ha podido completar la operación: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("========= CHINOOK =========");
        System.out.println(" 1. Listar géneros");
        System.out.println(" 2. Géneros con su número de pistas");
        System.out.println(" 3. Buscar artistas");
        System.out.println(" 4. Artistas más vendidos");
        System.out.println(" 5. Álbumes de un artista");
        System.out.println(" 6. Pistas de un género");
        System.out.println(" 7. Pistas de un álbum");
        System.out.println(" 8. Añadir un álbum");
        System.out.println(" 9. Cambiar el título de un álbum");
        System.out.println("10. Borrar un álbum");
        System.out.println(" 0. Salir");
        System.out.println("===========================");
    }

    // ---------- Métodos del menú ----------

    private static void listarGeneros() throws SQLException {
        for (Genero genero : generoDAO.findAll()) {
            System.out.println(genero);
        }
    }

    private static void contarPistasPorGenero() throws SQLException {
        Map<String, Integer> recuento = generoDAO.contarPistas();
        for (Map.Entry<String, Integer> fila : recuento.entrySet()) {
            System.out.println(fila.getKey() + ": " + fila.getValue());
        }
    }

    private static void buscarArtistas() throws SQLException {
        String texto = leerTexto("Texto a buscar: ");
        List<Artista> artistas = artistaDAO.buscarPorNombre(texto);
        if (artistas.isEmpty()) {
            System.out.println("Ningún artista contiene \"" + texto + "\".");
        }
        for (Artista artista : artistas) {
            System.out.println(artista);
        }
    }

    private static void artistasMasVendidos() throws SQLException {
        int n = leerEntero("¿Cuántos artistas quieres ver? ");
        Map<String, Integer> ventas = artistaDAO.masVendidos(n);
        for (Map.Entry<String, Integer> fila : ventas.entrySet()) {
            System.out.println(fila.getKey() + ": " + fila.getValue() + " unidades");
        }
    }

    private static void albumesDeArtista() throws SQLException {
        int artistaId = leerEntero("Id del artista: ");
        List<Album> albumes = albumDAO.findByArtista(artistaId);
        if (albumes.isEmpty()) {
            System.out.println("No se encontraron álbumes para ese artista.");
        }
        for (Album album : albumes) {
            System.out.println(album);
        }
    }

    private static void pistasDeGenero() throws SQLException {
        int generoId = leerEntero("Id del género: ");
        List<Pista> pistas = pistaDAO.findByGenero(generoId);
        System.out.println("Total: " + pistas.size() + " pistas.");
        List<Pista> muestra = pistas.subList(0, Math.min(20, pistas.size()));
        for (Pista pista : muestra) {
            System.out.println(pista);
        }
    }

    private static void pistasDeAlbum() throws SQLException {
        int albumId = leerEntero("Id del álbum: ");
        List<Pista> pistas = pistaDAO.findByAlbum(albumId);
        if (pistas.isEmpty()) {
            System.out.println("No se encontraron pistas para ese álbum.");
        }
        for (Pista pista : pistas) {
            System.out.println(pista);
        }
    }

    private static void insertarAlbum() throws SQLException {
        String titulo = leerTexto("Título del álbum: ");
        int artistaId = leerEntero("Id del artista: ");
        Album album = new Album(null, titulo, new Artista(artistaId, null));
        if (albumDAO.insert(album)) {
            System.out.println("Álbum guardado con el id " + album.getId() + ".");
        }
    }

    private static void cambiarTituloAlbum() throws SQLException {
        int albumId = leerEntero("Id del álbum que quieres modificar: ");
        int artistaId = leerEntero("Id de su artista: ");
        String nuevoTitulo = leerTexto("Nuevo título del álbum: ");
        Album album = new Album(albumId, nuevoTitulo, new Artista(artistaId, null));
        if (albumDAO.update(album)) {
            System.out.println("Álbum actualizado correctamente.");
        } else {
            System.out.println("No existía ningún álbum con el id " + albumId + ".");
        }
    }

    private static void borrarAlbum() throws SQLException {
        int albumId = leerEntero("Id del álbum que quieres borrar: ");
        if (albumDAO.delete(albumId)) {
            System.out.println("Álbum borrado correctamente.");
        } else {
            System.out.println("No existía ningún álbum con el id " + albumId + ".");
        }
    }

    // ---------- Lectura de teclado ----------

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero.");
            }
        }
    }
}
