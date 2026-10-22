<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.profedev.servletsdemo.modelo.Curso" %>
<%
    List<Curso> cursos = (List<Curso>) request.getAttribute("cursos");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nueva inscripcion | Demo JSP</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<main class="contenedor">
    <header>
        <p class="etiqueta">Programación Avanzada</p>
        <h1>JSP + Servlet</h1>
        <p>La JSP dibuja este formulario. Al enviarlo, el navegador realiza un
            <strong>POST</strong> al servlet <code>/inscripciones</code>.</p>
    </header>

    <% if (error != null) { %>
        <div class="alerta error"><%= error %></div>
    <% } %>

    <section class="tarjeta">
        <h2>Inscribir un alumno</h2>
        <form action="<%= request.getContextPath() %>/inscripciones" method="post">
            <label for="nombre">Nombre y apellido</label>
            <input id="nombre" name="nombre" required placeholder="Ej.: Laura Gomez">

            <label for="email">Correo electronico</label>
            <input id="email" name="email" type="email" required
                   placeholder="laura@ejemplo.com">

            <label for="cursoId">Curso</label>
            <select id="cursoId" name="cursoId" required>
                <% for (Curso curso : cursos) { %>
                    <option value="<%= curso.getId() %>">
                        <%= curso.getNombre() %> (<%= curso.getHoras() %> h)
                    </option>
                <% } %>
            </select>

            <button type="submit">Guardar inscripcion</button>
        </form>
    </section>

    <a class="enlace" href="<%= request.getContextPath() %>/inscripciones">
        Ver todas las inscripciones →
    </a>
</main>
</body>
</html>
