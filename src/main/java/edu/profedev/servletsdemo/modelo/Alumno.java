package edu.profedev.servletsdemo.modelo;

/** Entidad sencilla que representa a una persona que se inscribe. */
public class Alumno {
    private final int id;
    private final String nombre;
    private final String email;

    public Alumno(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
