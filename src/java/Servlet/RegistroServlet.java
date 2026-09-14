package Servlet;

import Controlador.UsuarioDAO;
import Modelo.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String identificacion = request.getParameter("identificacion_usuario");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String confirmarClave = request.getParameter("confirmarClave");
        String fechaNacimiento = request.getParameter("fecha_de_nacimiento");
        String idDocumento = request.getParameter("id_documento");
        String autorizacion = request.getParameter("autorizacionDatos");

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError(request, response, "El nombre es obligatorio.");
            return;
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            mostrarError(request, response, "El apellido es obligatorio.");
            return;
        }

        if (identificacion == null || identificacion.trim().isEmpty()) {
            mostrarError(request, response, "El número de documento es obligatorio.");
            return;
        }

        if (idDocumento == null || idDocumento.trim().isEmpty()) {
            mostrarError(request, response, "Debes seleccionar un tipo de documento.");
            return;
        }

        if (correo == null || correo.trim().isEmpty()) {
            mostrarError(request, response, "El correo electrónico es obligatorio.");
            return;
        }

        String correoLimpio = correo.trim();

        if (!correoLimpio.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            mostrarError(request, response, "El correo electrónico no tiene un formato válido.");
            return;
        }

        String errorClave = Util.PasswordUtil.validar(clave);
        if (errorClave != null) {
            mostrarError(request, response, errorClave);
            return;
        }

        if (confirmarClave == null || confirmarClave.isEmpty()) {
            mostrarError(request, response, "Debes confirmar la contraseña.");
            return;
        }

        if (!clave.equals(confirmarClave)) {
            mostrarError(request, response, "Las contraseñas no coinciden.");
            return;
        }

        if (telefono != null && !telefono.trim().isEmpty() && !telefono.trim().matches("\\d{7,15}")) {
            mostrarError(request, response, "El teléfono debe contener únicamente números y tener entre 7 y 15 dígitos.");
            return;
        }

        if (autorizacion == null) {
            mostrarError(request, response, "Debes autorizar el tratamiento de tus datos personales.");
            return;
        }

        LocalDate fechaNac = null;

        if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
            mostrarError(request, response, "La fecha de nacimiento es obligatoria.");
            return;
        } else {
            try {
                fechaNac = LocalDate.parse(fechaNacimiento);
            } catch (DateTimeParseException e) {
                mostrarError(request, response, "La fecha de nacimiento no es válida.");
                return;
            }
        }

        LocalDate hoy = LocalDate.now();

        if (fechaNac.isAfter(hoy)) {
            mostrarError(request, response, "La fecha de nacimiento no puede ser futura.");
            return;
        }

        int edad = Period.between(fechaNac, hoy).getYears();

        if (edad < 18) {
            mostrarError(request, response, "Debes ser mayor de edad para registrarte.");
            return;
        }

        int documento;

        try {
            documento = Integer.parseInt(idDocumento);
        } catch (NumberFormatException e) {
            mostrarError(request, response, "El tipo de documento seleccionado no es válido.");
            return;
        }

        Usuarios usuario = new Usuarios();
        usuario.setNombre(nombre.trim());
        usuario.setApellido(apellido.trim());
        usuario.setIdentificacionUsuario(identificacion.trim());
        usuario.setDireccion(direccion != null ? direccion.trim() : "");
        usuario.setTelefono(telefono != null ? telefono.trim() : "");
        usuario.setCorreo(correoLimpio);
        usuario.setClave(clave);
        usuario.setFechaDeNacimiento(fechaNac);
        usuario.setAutorizacionDatos(true);
        usuario.setIdDocumento(documento);
        usuario.setIdRol(2);

        boolean registrado = dao.insertarUsuario(usuario);

        if (registrado) {
            request.setAttribute("mensaje", "Usuario creado con éxito. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("/Vista/Login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se pudo crear el usuario. Verifica que el correo y el documento no estén registrados.");
            request.getRequestDispatcher("/Vista/Registro.jsp").forward(request, response);
        }
    }

    private void mostrarError(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws ServletException, IOException {

        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/Vista/Registro.jsp").forward(request, response);
    }
}