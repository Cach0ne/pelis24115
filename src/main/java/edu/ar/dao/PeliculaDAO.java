package edu.ar.dao;

import static edu.ar.conexion.Conexion.close;
import static edu.ar.conexion.Conexion.getConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ar.model.Pelicula;

public class PeliculaDAO {
    private static final String SQL_SELECT = "SELECT * FROM pelicula";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM pelicula WHERE idpelicula = ?";
    private static final String SQL_INSERT = "INSERT INTO pelicula (nombre, descripcion, genero, calificacion, anio, director) VALUES (?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE pelicula SET nombre = ?, descripcion = ?, genero = ?, calificacion = ?, anio = ?, director = ? WHERE idpelicula = ?";
    private static final String SQL_DELETE = "DELETE FROM pelicula WHERE idpelicula = ?";

    public static List<Pelicula> obtener() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pelicula pelicula = null;
        List<Pelicula> peliculas = new ArrayList<>();

        try {
            conn = getConexion();
            ps = conn.prepareStatement(SQL_SELECT);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idpelicula");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero");
                int calificacion = rs.getInt("calificacion");
                int anio = rs.getInt("anio");
                String director = rs.getString("director");

                pelicula = new Pelicula(id, nombre, descripcion, genero, calificacion, anio, director);
                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(ps);
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return peliculas;
    }

    public static Pelicula obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pelicula pelicula = null;

        try {
            conn = getConexion();
            ps = conn.prepareStatement(SQL_SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero");
                int calificacion = rs.getInt("calificacion");
                int anio = rs.getInt("anio");
                String director = rs.getString("director");

                pelicula = new Pelicula(nombre, descripcion, genero, calificacion, anio, director);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(ps);
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return pelicula;
    }

    public static int insertar(Pelicula peli) {
        Connection conn = null;
        PreparedStatement ps = null;
        int registros = 0;
        try {
            conn = getConexion();
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, peli.getNombre());
            ps.setString(2, peli.getDescripcion());
            ps.setString(3, peli.getGenero());
            ps.setInt(4, peli.getCalificacion());
            ps.setInt(5, peli.getAnio());
            ps.setString(6, peli.getDirector());
            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                close(ps);
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return registros;
    }

    public static int actualizar(Pelicula peli) {
        Connection conn = null;
        PreparedStatement ps = null;
        int registros = 0;
        try {
            conn = getConexion();
            ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, peli.getNombre());
            ps.setString(2, peli.getDescripcion());
            ps.setString(3, peli.getGenero());
            ps.setInt(4, peli.getCalificacion());
            ps.setInt(5, peli.getAnio());
            ps.setString(6, peli.getDirector());
            ps.setInt(7, peli.getId());
            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                close(ps);
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return registros;
    }

    public static int eliminar(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        int registros = 0;
        try {
            conn = getConexion();
            ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, id);
            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                close(ps);
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return registros;
    }
}
