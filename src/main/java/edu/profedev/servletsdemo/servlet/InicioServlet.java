package edu.profedev.servletsdemo.servlet;

import edu.profedev.servletsdemo.datos.DemoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Prepara los datos del formulario y entrega el control a la JSP. */
@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DemoRepository repositorio = repositorio(request);
        request.setAttribute("cursos", repositorio.listarCursos());
        
        request.getRequestDispatcher("/WEB-INF/views/formulario.jsp")
                .forward(request, response);
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
