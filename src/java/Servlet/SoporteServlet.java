package Servlet;

import Modelo.Usuarios;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "SoporteServlet", urlPatterns = {"/SoporteServlet"})
public class SoporteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CORREO_ADMIN = "punto.cajas6@gmail.com";
    private static final String CORREO_ENVIO = "punto.cajas6@gmail.com";
    private static final String CLAVE_CORREO = System.getenv("PUNTO_CAJAS_MAIL_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioActivo") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        Usuarios usuario = (Usuarios) session.getAttribute("usuarioActivo");

        String asunto = request.getParameter("asunto");
        String mensaje = request.getParameter("mensaje");

        if (asunto == null || asunto.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar un asunto.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        if (mensaje == null || mensaje.trim().isEmpty()) {
            request.setAttribute("error", "Debes describir tu inquietud.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        asunto = asunto.trim();
        mensaje = mensaje.trim();

        if (asunto.length() > 100) {
            request.setAttribute("error", "El asunto no puede superar los 100 caracteres.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        if (mensaje.length() > 1000) {
            request.setAttribute("error", "La inquietud no puede superar los 1000 caracteres.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        String correoUsuario = usuario.getCorreo();
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            request.setAttribute("error", "No se pudo identificar el correo del usuario.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        correoUsuario = correoUsuario.trim();

        if (CLAVE_CORREO == null || CLAVE_CORREO.isBlank()) {
            System.out.println("ERROR: No está configurada la variable PUNTO_CAJAS_MAIL_PASSWORD.");
            request.setAttribute("error", "El servicio de soporte por correo no está configurado.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
            return;
        }

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session mailSession = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_ENVIO, CLAVE_CORREO);
            }
        });

        try {
            Message correo = new MimeMessage(mailSession);
            correo.setFrom(new InternetAddress(CORREO_ENVIO, "Punto Cajas"));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CORREO_ADMIN));
            correo.setReplyTo(new InternetAddress[]{new InternetAddress(correoUsuario)});
            correo.setSubject("Soporte Punto Cajas - " + asunto);

            String contenido = "NUEVA SOLICITUD DE SOPORTE\n\n"
                    + "================================\n\n"
                    + "Usuario:\n" + usuario.getNombre() + " " + usuario.getApellido() + "\n\n"
                    + "Correo:\n" + correoUsuario + "\n\n"
                    + "Asunto:\n" + asunto + "\n\n"
                    + "Inquietud:\n" + mensaje + "\n\n"
                    + "================================\n"
                    + "Punto Cajas";

            correo.setText(contenido);
            Transport.send(correo);

            session.setAttribute("mensajeSoporte", "Tu solicitud fue enviada correctamente. El equipo de Punto Cajas revisará tu inquietud.");
            response.sendRedirect(request.getContextPath() + "/Vista/Soporte.jsp");

        } catch (AddressException e) {
            System.out.println("Error en la dirección de correo: " + e.getMessage());
            request.setAttribute("error", "El correo electrónico no es válido.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
        } catch (MessagingException e) {
            System.out.println("Error enviando correo de soporte: " + e.getMessage());
            request.setAttribute("error", "No fue posible enviar la solicitud. Inténtalo nuevamente.");
            request.getRequestDispatcher("/Vista/Soporte.jsp").forward(request, response);
        }
    }
}