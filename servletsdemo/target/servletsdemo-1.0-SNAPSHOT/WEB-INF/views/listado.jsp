<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="edu.profedev.servletsdemo.modelo.Inscripcion" %>
<%
    List<Inscripcion> inscripciones =
            (List<Inscripcion>) request.getAttribute("inscripciones");
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Inscripciones | Demo JSP</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<main class="contenedor ancho">
    <header>
        <p class="etiqueta">Respuesta del servlet</p>
        <h1>Inscripciones</h1>
        <p>El servlet consulto el repositorio, guardo la lista como atributo del
            request y delego la presentacion a esta JSP.</p>
    </header>

    <% if ("1".equals(request.getParameter("creada"))) { %>
        <div class="alerta exito">La inscripcion se guardo correctamente.</div>
    <% } %>

    <section class="tarjeta tabla-contenedor">
        <table>
            <thead>
                <tr><th>ID</th><th>Alumno</th><th>Email</th><th>Curso</th><th>Fecha</th></tr>
            </thead>
            <tbody>
                <% for (Inscripcion item : inscripciones) { %>
                    <tr>
                        <td><%= item.getId() %></td>
                        <td><%= item.getAlumno().getNombre() %></td>
                        <td><%= item.getAlumno().getEmail() %></td>
                        <td><%= item.getCurso().getNombre() %></td>
                        <td><%= item.getFecha().format(formato) %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </section>

    <a class="boton-secundario" href="<%= request.getContextPath() %>/inicio">
        + Nueva inscripcion
    </a>
</main>
</body>
</html>
