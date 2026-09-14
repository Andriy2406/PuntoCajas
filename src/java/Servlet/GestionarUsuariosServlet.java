package Servlet;

import Controlador.UsuarioDAO;
import Modelo.Usuarios;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "GestionarUsuariosServlet", urlPatterns = {"/GestionarUsuariosServlet"})
public class GestionarUsuariosServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        List<Usuarios> listaUsuarios = dao.listarUsuarios();
        request.setAttribute("listaUsuarios", listaUsuarios);

        request.getRequestDispatcher("/Vista/GestionarUsuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        HttpSession session = request.getSession();
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        boolean operacionExitosa = false;
        String mensaje = "";

        switch (accion) {
            case "crear":
                try {
                    Usuarios nuevoUsuario = new Usuarios();
                    nuevoUsuario.setIdUsuario(0);
                    nuevoUsuario.setNombre(request.getParameter("nombre"));
                    nuevoUsuario.setApellido(request.getParameter("apellido"));
                    nuevoUsuario.setIdentificacionUsuario(request.getParameter("identificacionUsuario"));
                    nuevoUsuario.setTelefono(request.getParameter("telefono"));
                    nuevoUsuario.setDireccion(request.getParameter("direccion"));
                    nuevoUsuario.setCorreo(request.getParameter("correo"));
                    nuevoUsuario.setClave(request.getParameter("clave"));
                    nuevoUsuario.setIdRol(Integer.parseInt(request.getParameter("idRol")));
                    nuevoUsuario.setIdDocumento(Integer.parseInt(request.getParameter("idDocumento")));
                    nuevoUsuario.setAutorizacionDatos(true);
                    nuevoUsuario.setEstado(true);

                    operacionExitosa = dao.insertarUsuario(nuevoUsuario);
                    mensaje = operacionExitosa ? "¡Usuario creado exitosamente!" : "Error al crear el usuario.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error técnico al crear: " + e.getMessage();
                }
                break;

            case "actualizar":
                try {
                    Usuarios usuEdit = new Usuarios();
                    usuEdit.setIdUsuario(Integer.parseInt(request.getParameter("id")));
                    usuEdit.setNombre(request.getParameter("nombre"));
                    usuEdit.setApellido(request.getParameter("apellido"));
                    usuEdit.setTelefono(request.getParameter("telefono"));
                    usuEdit.setDireccion(request.getParameter("direccion"));
                    usuEdit.setCorreo(request.getParameter("correo"));

                    operacionExitosa = dao.actualizarUsuario(usuEdit);
                    mensaje = operacionExitosa ? "¡Usuario actualizado exitosamente!" : "Error al actualizar el usuario.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al actualizar: " + e.getMessage();
                }
                break;

            case "activar":
                try {
                    operacionExitosa = dao.activarUsuario(Integer.parseInt(request.getParameter("id")));
                    mensaje = operacionExitosa ? "¡Usuario activado correctamente!" : "Error al activar el usuario.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al activar: " + e.getMessage();
                }
                break;

            case "inactivar":
                try {
                    operacionExitosa = dao.inactivarUsuario(Integer.parseInt(request.getParameter("id")));
                    mensaje = operacionExitosa ? "¡Usuario inactivado correctamente!" : "Error al inactivar el usuario.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al inactivar: " + e.getMessage();
                }
                break;

            case "eliminar":
                try {
                    operacionExitosa = dao.eliminarUsuario(Integer.parseInt(request.getParameter("id")));
                    mensaje = operacionExitosa ? "¡Usuario eliminado correctamente!" : "Error al eliminar el usuario.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al eliminar: " + e.getMessage();
                }
                break;
        }

        session.setAttribute("mensajeAlerta", mensaje);
        session.setAttribute("tipoAlerta", operacionExitosa ? "success" : "danger");

        response.sendRedirect(request.getContextPath() + "/GestionarUsuariosServlet");
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        return usuarioActivo != null && usuarioActivo.getIdRol() == 1;
    }
}   