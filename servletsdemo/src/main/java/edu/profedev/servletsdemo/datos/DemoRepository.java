package edu.profedev.servletsdemo.datos;

import edu.profedev.servletsdemo.modelo.Alumno;
import edu.profedev.servletsdemo.modelo.Curso;
import edu.profedev.servletsdemo.modelo.Inscripcion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemoRepository {
    private final List<Curso> cursos = List.of(
            new Curso(1, "Introduccion a Java", 24),
            new Curso(2, "JSP y Servlets", 16),
            new Curso(3, "Bases de datos con JDBC", 20)
    );
    private final List<Inscripcion> inscripciones = new ArrayList<>();
    private int alumnosId;
    private int inscripcionesId;
   

    public DemoRepository() {
        Alumno ejemplo = new Alumno(1,"Ana Demo", "ana@ejemplo.com");
        inscripciones.add(new Inscripcion(1,ejemplo, cursos.get(1), LocalDateTime.now()));
    }
    
    

    public List<Curso> listarCursos() {
        return cursos;
    }

    public List<Inscripcion> listarInscripciones() {
        synchronized (inscripciones) {
            return List.copyOf(inscripciones);
        }
    }

    public Inscripcion inscribir(String nombre, String email, int cursoId) {
        Curso curso = cursos.stream()
                .filter(item -> item.getId() == cursoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Curso inexistente"));

        alumnosId += 1;
        inscripcionesId +=1;
        
        Alumno alumno = new Alumno(alumnosId, nombre, email);
        Inscripcion inscripcion = new Inscripcion(inscripcionesId, alumno, curso, LocalDateTime.now());
        inscripciones.add(inscripcion);
        
        return inscripcion;
    }
}
