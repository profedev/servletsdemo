package edu.profedev.servletsdemo.modelo;

/** Entidad del catálogo de cursos. */
public class Curso {
    private final int id;
    private final String nombre;
    private final int horas;

    public Curso(int id, String nombre, int horas) {
        this.id = id;
        this.nombre = nombre;
        this.horas = horas;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHoras() {
        return horas;
    }
}
