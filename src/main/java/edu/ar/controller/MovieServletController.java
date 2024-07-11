package edu.ar.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ar.dao.PeliculaDAO;
import edu.ar.model.Pelicula;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/movies")
public class MovieServletController extends HttpServlet {
  static Logger logger = LoggerFactory.getLogger(MovieServletController.class);
  List<Pelicula> movieList = new ArrayList<>();
  ObjectMapper mapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String route = req.getParameter("action");
    logger.info("route : " + route);
    switch (route) {
      case "getAll" -> {
        res.setContentType("application/json; charset=UTF-8");
        movieList = PeliculaDAO.obtener();
        logger.info("Dentro de getAll : " + movieList.size());
        // transformo en json y lo envio
        mapper.writeValue(res.getWriter(), movieList);
        logger.info(mapper.toString());
      }

      case "getById" -> {
        try {
          int id = Integer.parseInt(req.getParameter("id"));
          Pelicula movie = PeliculaDAO.obtenerPorId(id);
          if (movie != null) {
            res.setContentType("application/json; charset=UTF-8");
            mapper.writeValue(res.getWriter(), movie);
          } else {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Película no encontrada.");
          }
        } catch (NumberFormatException e) {
          logger.error("ID de película no válido", e);
          res.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de película no válido.");
        }
      }

      default -> {
        logger.warn("Parámetro no válido: " + route);
        res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
      }
    }
  }

  // En el doPost enviamos los datos del formulario a Java para que los
  // inserte en la base de datos.
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {
      String nombre = req.getParameter("nombre");
      String descripcion = req.getParameter("descripcion");
      String genero = req.getParameter("genero");
      int calificacion = Integer.parseInt(req.getParameter("calificacion"));
      int anio = Integer.parseInt(req.getParameter("anio"));
      String director = req.getParameter("director");

      logger.info("Adding movie: {}, {}, {}, {}, {}, {}", nombre, descripcion, genero, calificacion, anio, director);

      Pelicula newMovie = new Pelicula(nombre, descripcion, genero, calificacion, anio, director);
      PeliculaDAO.insertar(newMovie);

      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> response = new HashMap<>();
      response.put("message", "Película guardada exitosamente!!!");
      new ObjectMapper().writeValue(res.getWriter(), response);
    } catch (Exception e) {
      logger.error("Error al guardar la película", e);
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Error al guardar la película");
      new ObjectMapper().writeValue(res.getWriter(), errorResponse);
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {
      // Leer el cuerpo de la solicitud como JSON
      ObjectMapper mapper = new ObjectMapper();
      Pelicula updatedMovie = mapper.readValue(req.getReader(), Pelicula.class);

      int id = updatedMovie.getId();
      String nombre = updatedMovie.getNombre();
      String descripcion = updatedMovie.getDescripcion();
      String genero = updatedMovie.getGenero();
      int calificacion = updatedMovie.getCalificacion();
      int anio = updatedMovie.getAnio();
      String director = updatedMovie.getDirector();

      logger.info("Updating movie: {}, {}, {}, {}, {}, {}, {}", id, nombre, descripcion, genero, calificacion, anio,
          director);

      int updatedRows = PeliculaDAO.actualizar(updatedMovie);

      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> response = new HashMap<>();
      if (updatedRows > 0) {
        response.put("message", "Película actualizada exitosamente!!!");
      } else {
        response.put("message", "No se encontró ninguna película con el ID proporcionado.");
      }
      mapper.writeValue(res.getWriter(), response);
    } catch (NumberFormatException e) {
      logger.error("ID de película no válido", e);
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "ID de película no válido");
      new ObjectMapper().writeValue(res.getWriter(), errorResponse);
    } catch (Exception e) {
      logger.error("Error al actualizar la película", e);
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Error al actualizar la película");
      new ObjectMapper().writeValue(res.getWriter(), errorResponse);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {
      int id = Integer.parseInt(req.getParameter("id"));

      int registros = PeliculaDAO.eliminar(id);

      res.setContentType("application/json; charset=UTF-8");
      Map<String, Object> response = new HashMap<>();
      if (registros > 0) {
        response.put("message", "Película eliminada exitosamente!!!");
      } else {
        response.put("message", "No se encontró ninguna película con el ID proporcionado.");
      }
      new ObjectMapper().writeValue(res.getWriter(), response);
    } catch (NumberFormatException e) {
      logger.error("ID de película no válido", e);
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "ID de película no válido");
      new ObjectMapper().writeValue(res.getWriter(), errorResponse);
    } catch (Exception e) {
      logger.error("Error al eliminar la película", e);
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.setContentType("application/json; charset=UTF-8");
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Error al eliminar la película");
      new ObjectMapper().writeValue(res.getWriter(), errorResponse);
    }
  }

}
