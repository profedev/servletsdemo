package edu.profedev.servletsdemo.modelo;

import java.time.LocalDateTime;

/** Relaciona un alumno con un curso. */
public class Inscripcion {
    private final int id;
    private final Alumno alumno;
    private final Curso curso;
    private final LocalDateTime fecha;

    public Inscripcion(int id, Alumno alumno, Curso curso, LocalDateTime fecha) {
        this.id = id;
        this.alumno = alumno;
        this.curso = curso;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
