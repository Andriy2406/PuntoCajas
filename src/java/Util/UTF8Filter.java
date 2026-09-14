package Util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Antes, varios servlets (Gestionar productos, catálogos, usuarios, ventas,
 * carrito, login, etc.) no llamaban a request.setCharacterEncoding("UTF-8")
 * antes de leer los parámetros del formulario. Cuando eso pasa, el servidor
 * asume por defecto ISO-8859-1 y las tildes/ñ que el usuario escribe llegan
 * corrompidas (y así quedan guardadas en la base de datos).
 *
 * Este filtro se ejecuta ANTES que cualquier servlet, para todas las rutas
 * (/*), y deja la codificación en UTF-8 tanto de entrada como de salida.
 * Con esto ya no hace falta repetir esa línea en cada servlet nuevo.
 */
@WebFilter(filterName = "UTF8Filter", urlPatterns = {"/*"})
public class UTF8Filter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se necesita configuración inicial.
    }

    @Override
    public void destroy() {
        // No se necesita liberar nada.
    }
}
