package edu.profedev.servletsdemo.servlet;

import edu.profedev.servletsdemo.datos.DemoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Recibe el formulario por POST y muestra el listado por GET. */
@WebServlet("/inscripciones")
public class InscripcionesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DemoRepository repositorio = repositorio(request);
        request.setAttribute("inscripciones", repositorio.listarInscripciones());
        request.getRequestDispatcher("/WEB-INF/views/listado.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String cursoId = request.getParameter("cursoId");

        if (nombre == null || nombre.isBlank()
                || email == null || email.isBlank()
                || cursoId == null) {
            redirigirConError(response, request, "Complete todos los campos");
            return;
        }

        try {
            repositorio(request).inscribir(nombre.trim(), email.trim(), Integer.parseInt(cursoId));
            // Patron POST/Redirect/GET: evita duplicar el alta al refrescar.
            response.sendRedirect(request.getContextPath()
                    + "/inscripciones?creada=1");
        } catch (IllegalArgumentException error) {
            redirigirConError(response, request, "El curso seleccionado no es valido");
        }
    }

    private DemoRepository repositorio() {
        return (DemoRepository) getServletContext().getAttribute("repositorio");
    }

    private void redirigirConError(HttpServletResponse response,
            HttpServletRequest request, String mensaje) throws IOException {
        response.sendRedirect(request.getContextPath() + "/inicio?error="
                + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
    }
    
    private DemoRepository repositorio(HttpServletRequest request) {
    var sesion = request.getSession();

    synchronized (sesion) {
        DemoRepository repositorio =
                (DemoRepository) sesion.getAttribute("repositorio");

        if (repositorio == null) {
            repositorio = new DemoRepository();
            sesion.setAttribute("repositorio", repositorio);
        }

        return repositorio;
    }
}
}
