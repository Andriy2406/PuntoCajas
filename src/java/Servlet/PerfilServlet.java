package Servlet;

import Controlador.UsuarioDAO;
import Modelo.Usuarios;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "PerfilServlet",
        urlPatterns = {"/PerfilServlet"}
)
public class PerfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/Vista/Login.jsp"
            );

            return;
        }

        Usuarios usuarioSesion =
                (Usuarios) session.getAttribute(
                        "usuarioActivo"
                );

        if (usuarioSesion == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/Vista/Login.jsp"
            );

            return;
        }

        Usuarios usuario =
                dao.consultarUsuarioPorId(
                        usuarioSesion.getIdUsuario()
                );

        if (usuario != null) {

            request.setAttribute(
                    "usuario",
                    usuario
            );

            request.getRequestDispatcher(
                    "/Vista/Perfil.jsp"
            ).forward(request, response);

        } else {

            request.setAttribute(
                    "error",
                    "No fue posible cargar la información del perfil."
            );

            request.getRequestDispatcher(
                    "/Vista/Index.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/Vista/Login.jsp"
            );

            return;
        }

        Usuarios usuarioSesion =
                (Usuarios) session.getAttribute(
                        "usuarioActivo"
                );

        if (usuarioSesion == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/Vista/Login.jsp"
            );

            return;
        }

        String clave =
                request.getParameter("clave");

        if (clave != null) {

            if (clave.trim().isEmpty()) {

                request.setAttribute(
                        "error",
                        "La contraseña no puede estar vacía."
                );

                cargarPerfil(
                        request,
                        response,
                        usuarioSesion
                );

                return;
            }

            String errorClave = Util.PasswordUtil.validar(clave);
            if (errorClave != null) {

                request.setAttribute(
                        "error",
                        errorClave
                );

                cargarPerfil(
                        request,
                        response,
                        usuarioSesion
                );

                return;
            }

            boolean actualizado =
                    dao.actualizarClave(
                            usuarioSesion.getIdUsuario(),
                            clave
                    );

            if (actualizado) {

                usuarioSesion.setClave(clave);

                session.setAttribute(
                        "usuarioActivo",
                        usuarioSesion
                );

                Usuarios usuarioActualizado =
                        dao.consultarUsuarioPorId(
                                usuarioSesion.getIdUsuario()
                        );

                request.setAttribute(
                        "mensaje",
                        "La contraseña se actualizó correctamente."
                );

                request.setAttribute(
                        "usuario",
                        usuarioActualizado
                );

                request.getRequestDispatcher(
                        "/Vista/Perfil.jsp"
                ).forward(request, response);

            } else {

                request.setAttribute(
                        "error",
                        "No fue posible actualizar la contraseña."
                );

                cargarPerfil(
                        request,
                        response,
                        usuarioSesion
                );
            }

            return;
        }

        String nombre =
                request.getParameter("nombre");

        String apellido =
                request.getParameter("apellido");

        String correo =
                request.getParameter("correo");

        String telefono =
                request.getParameter("telefono");

        String direccion =
                request.getParameter("direccion");

        if (nombre == null
                || nombre.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "El nombre es obligatorio."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );

            return;
        }

        if (apellido == null
                || apellido.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "El apellido es obligatorio."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );

            return;
        }

        if (correo == null
                || correo.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "El correo es obligatorio."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );

            return;
        }

        String correoLimpio =
                correo.trim();

        if (!correoLimpio.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            request.setAttribute(
                    "error",
                    "Ingrese un correo electrónico válido."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );

            return;
        }

        if (telefono != null
                && !telefono.trim().isEmpty()
                && !telefono.trim().matches("\\d{7,15}")) {

            request.setAttribute(
                    "error",
                    "El teléfono debe contener entre 7 y 15 números."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );

            return;
        }

        Usuarios usuario =
                new Usuarios();

        usuario.setIdUsuario(
                usuarioSesion.getIdUsuario()
        );

        usuario.setNombre(
                nombre.trim()
        );

        usuario.setApellido(
                apellido.trim()
        );

        usuario.setCorreo(
                correoLimpio
        );

        usuario.setTelefono(
                telefono != null
                        ? telefono.trim()
                        : ""
        );

        usuario.setDireccion(
                direccion != null
                        ? direccion.trim()
                        : ""
        );

        String latitudParam = request.getParameter("latitud");
        String longitudParam = request.getParameter("longitud");
        try {
            if (latitudParam != null && !latitudParam.trim().isEmpty()) {
                usuario.setLatitud(Double.parseDouble(latitudParam.trim()));
            }
            if (longitudParam != null && !longitudParam.trim().isEmpty()) {
                usuario.setLongitud(Double.parseDouble(longitudParam.trim()));
            }
        } catch (NumberFormatException ignored) {
            // Si el mapa no pudo determinar coordenadas, se conservan las anteriores.
        }

        usuario.setClave(
                usuarioSesion.getClave()
        );

        boolean actualizado =
                dao.actualizarUsuario(
                        usuario
                );

        if (actualizado) {

            Usuarios usuarioActualizado =
                    dao.consultarUsuarioPorId(
                            usuarioSesion.getIdUsuario()
                    );

            if (usuarioActualizado != null) {

                session.setAttribute(
                        "usuarioActivo",
                        usuarioActualizado
                );

                request.setAttribute(
                        "usuario",
                        usuarioActualizado
                );

            } else {

                request.setAttribute(
                        "usuario",
                        usuario
                );
            }

            request.setAttribute(
                    "mensaje",
                    "Los datos se actualizaron correctamente."
            );

            request.getRequestDispatcher(
                    "/Vista/Perfil.jsp"
            ).forward(request, response);

        } else {

            request.setAttribute(
                    "error",
                    "No fue posible actualizar los datos. "
                    + "Verifica la información e inténtalo nuevamente."
            );

            cargarPerfil(
                    request,
                    response,
                    usuarioSesion
            );
        }
    }

    private void cargarPerfil(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuarios usuarioSesion)
            throws ServletException, IOException {

        Usuarios usuario =
                dao.consultarUsuarioPorId(
                        usuarioSesion.getIdUsuario()
                );

        request.setAttribute(
                "usuario",
                usuario
        );

        request.getRequestDispatcher(
                "/Vista/Perfil.jsp"
        ).forward(request, response);
    }
}