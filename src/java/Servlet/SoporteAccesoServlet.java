package Servlet;

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

@WebServlet(name = "SoporteAccesoServlet", urlPatterns = {"/SoporteAccesoServlet"})
public class SoporteAccesoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CORREO_ADMIN = "punto.cajas6@gmail.com";
    private static final String CORREO_ENVIO = "punto.cajas6@gmail.com";
    private static final String CLAVE_CORREO = System.getenv("PUNTO_CAJAS_MAIL_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String correoUsuario = request.getParameter("correo");
        String asunto = request.getParameter("asunto");
        String mensaje = request.getParameter("mensaje");

        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar tu correo electrónico.");
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        correoUsuario = correoUsuario.trim();

        if (!correoUsuario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            request.setAttribute("error", "El correo electrónico no tiene un formato válido.");
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        if (asunto == null || asunto.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar un asunto.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        asunto = asunto.trim();

        if (asunto.length() > 100) {
            request.setAttribute("error", "El asunto no puede superar los 100 caracteres.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        if (mensaje == null || mensaje.trim().isEmpty()) {
            request.setAttribute("error", "Debes describir tu problema.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        mensaje = mensaje.trim();

        if (mensaje.length() > 1000) {
            request.setAttribute("error", "El mensaje no puede superar los 1000 caracteres.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
            return;
        }

        if (CLAVE_CORREO == null || CLAVE_CORREO.isBlank()) {
            System.out.println("ERROR: No está configurada la variable PUNTO_CAJAS_MAIL_PASSWORD.");
            request.setAttribute("error", "El servicio de soporte por correo no está configurado.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
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
            correo.setSubject("Soporte de acceso Punto Cajas - " + asunto);

            String contenido = "SOLICITUD DE SOPORTE DE ACCESO\n\n"
                    + "================================\n\n"
                    + "Correo del usuario:\n" + correoUsuario + "\n\n"
                    + "Asunto:\n" + asunto + "\n\n"
                    + "Problema:\n" + mensaje + "\n\n"
                    + "================================\n"
                    + "Punto Cajas";

            correo.setText(contenido);
            Transport.send(correo);

            System.out.println("Solicitud de soporte de acceso enviada correctamente a: " + CORREO_ADMIN);

            HttpSession session = request.getSession(true);
            session.setAttribute("mensajeSoporteAcceso", "Tu solicitud fue enviada correctamente. El administrador revisará tu caso.");

            response.sendRedirect(request.getContextPath() + "/Vista/SoporteAcceso.jsp");

        } catch (AddressException e) {
            System.out.println("Error en la dirección de correo: " + e.getMessage());
            request.setAttribute("error", "El correo electrónico no es válido.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
        } catch (MessagingException e) {
            System.out.println("Error enviando soporte de acceso: " + e.getMessage());
            request.setAttribute("error", "No fue posible enviar la solicitud. Inténtalo nuevamente.");
            request.setAttribute("correoInactivo", correoUsuario);
            request.getRequestDispatcher("/Vista/SoporteAcceso.jsp").forward(request, response);
        }
    }
}