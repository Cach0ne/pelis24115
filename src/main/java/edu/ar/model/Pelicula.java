package edu.ar.model;

public class Pelicula {
  private int idpelicula;
  private String nombre;
  private String descripcion;
  private String genero;
  private int calificacion;
  private int anio;
  private String director;

  public Pelicula() {
  }

  public Pelicula(int idpelicula, String nombre, String descripcion, String genero, int calificacion, int anio,
      String director) {
    this.idpelicula = idpelicula;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.genero = genero;
    this.calificacion = calificacion;
    this.anio = anio;
    this.director = director;
  }

  public Pelicula(String nombre, String descripcion, String genero, int calificacion, int anio, String director) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.genero = genero;
    this.calificacion = calificacion;
    this.anio = anio;
    this.director = director;
  }

  public int getId() {
    return idpelicula;
  }

  public void setId(int id) {
    this.idpelicula = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getAnio() {
    return anio;
  }

  public void setAnio(int anio) {
    this.anio = anio;
  }

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public int getCalificacion() {
    return calificacion;
  }

  public void setCalificacion(int calificacion) {
    this.calificacion = calificacion;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  @Override
  public String toString() {
    return "Pelicula [id=" + idpelicula + ", nombre=" + nombre + ", anio=" + anio + ", genero=" + genero
        + ", descripcion="
        + descripcion + ", calificacion=" + calificacion + "]";
  }
}
