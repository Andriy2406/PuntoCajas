package Controlador;

import Modelo.CotizacionERP;
import Modelo.CotizacionVersion;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EnviarCorreo {

    private final String correoRemitente = "punto.cajas6@gmail.com";
    private final String claveAplicacion = System.getenv("PUNTO_CAJAS_MAIL_PASSWORD");

    public boolean enviarCodigo(String correoDestino, String codigo) {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(correoRemitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject("Punto Cajas - Código de recuperación");
            mensaje.setText("Hola,\n\nTu código de recuperación de Punto Cajas es: " + codigo + "\n\nEste código tiene una validez de 10 minutos.\n\nSi no solicitaste recuperar tu clave, ignora este mensaje.");

            Transport.send(mensaje);
            System.out.println("Correo enviado correctamente a: " + correoDestino);
            return true;
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }

    public boolean enviarSoporte(String correoUsuario, String asunto, String mensajeUsuario) {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(correoRemitente, "Punto Cajas"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoRemitente));
            mensaje.setReplyTo(new InternetAddress[]{new InternetAddress(correoUsuario)});
            mensaje.setSubject("Soporte de acceso Punto Cajas - " + asunto);
            mensaje.setText("SOLICITUD DE SOPORTE DE ACCESO\n\nCorreo del usuario: " + correoUsuario + "\n\nAsunto: " + asunto + "\n\nMensaje:\n" + mensajeUsuario + "\n\n--------------------------------\nPunto Cajas");

            Transport.send(mensaje);
            System.out.println("Solicitud de soporte enviada correctamente.");
            return true;
        } catch (Exception e) {
            System.out.println("Error al enviar soporte: " + e.getMessage());
            return false;
        }
    }

    public boolean enviarNotificacionCotizacion(String nombreUsuario, String correoUsuario, int idCotizacion) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            nombreUsuario = "Usuario";
        }
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            correoUsuario = "No disponible";
        }
        if (idCotizacion <= 0) {
            System.out.println("No se puede enviar la notificación porque el ID de la cotización no es válido.");
            return false;
        }

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(correoRemitente, "Punto Cajas"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse("punto.cajas6@gmail.com"));

            if (!correoUsuario.equals("No disponible")) {
                mensaje.setReplyTo(new InternetAddress[]{new InternetAddress(correoUsuario)});
            }

            mensaje.setSubject("Nueva solicitud de cotización - Punto Cajas");
            mensaje.setText("NUEVA SOLICITUD DE COTIZACIÓN\n\nUn usuario ha realizado una solicitud de cotización en Punto Cajas.\n\n--------------------------------\nDATOS DEL USUARIO\n--------------------------------\n\nNombre: " + nombreUsuario + "\nCorreo: " + correoUsuario + "\n\nID de cotización: " + idCotizacion + "\n\n--------------------------------\nIMPORTANTE\n--------------------------------\n\nLa cotización no ha sido calculada automáticamente.\n\nDebes ingresar al panel administrativo de Punto Cajas para revisar los datos de la solicitud y gestionar la cotización.\n\nSaludos,\nSistema Punto Cajas");

            Transport.send(mensaje);
            System.out.println("Notificación de cotización enviada correctamente al administrador.");
            return true;
        } catch (Exception e) {
            System.out.println("Error al enviar notificación de cotización: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /** Notificaciones del flujo ERP de cotizaciones. */
    public boolean enviarNotificacionERP(String nombre, String correo, int idCotizacion, String tipo, String detalle) {
        try {
            String destinatario = correo;
            String asunto;
            String cuerpo;
            boolean esAdmin = "ACEPTACION".equals(tipo) || "RECHAZO".equals(tipo);
            if (esAdmin) {
                destinatario = correoRemitente;
            }
            switch (tipo == null ? "" : tipo) {
                case "CORRECCION":
                    asunto = "Corrección solicitada - Cotización #" + idCotizacion;
                    cuerpo = "Hola " + nombre + ",\n\nEl equipo de Punto Cajas solicitó una corrección en tu cotización #" + idCotizacion + ".\n\nMotivo:\n" + (detalle == null ? "Revisa los datos solicitados." : detalle) + "\n\nIngresa a Punto Cajas para corregirla y enviarla nuevamente.\n\nLa cotización conserva su mismo número y la corrección genera una nueva versión.";
                    break;
                case "COTIZACION":
                    asunto = "Tu cotización está lista - #" + idCotizacion;
                    cuerpo = "Hola " + nombre + ",\n\nTu cotización #" + idCotizacion + " ya fue preparada por Punto Cajas.\n\nIngresa a tu cuenta para revisar el precio y aceptar o rechazar la cotización.";
                    break;
                case "ACEPTACION":
                    asunto = "Cotización aceptada - #" + idCotizacion;
                    cuerpo = "El cliente " + nombre + " aceptó la cotización #" + idCotizacion + ".\n\nEl sistema dejó generado el anticipo del 50% como pendiente de confirmación.";
                    break;
                case "RECHAZO":
                    asunto = "Cotización rechazada - #" + idCotizacion;
                    cuerpo = "El cliente " + nombre + " rechazó la cotización #" + idCotizacion + ".\n\nMotivo:\n" + (detalle == null ? "No indicado." : detalle);
                    break;
                default:
                    asunto = "Actualización de cotización - #" + idCotizacion;
                    cuerpo = "La cotización #" + idCotizacion + " tiene una nueva actualización en Punto Cajas.";
            }
            Properties propiedades = new Properties();
            propiedades.put("mail.smtp.host", "smtp.gmail.com");
            propiedades.put("mail.smtp.port", "587");
            propiedades.put("mail.smtp.auth", "true");
            propiedades.put("mail.smtp.starttls.enable", "true");
            propiedades.put("mail.smtp.starttls.required", "true");
            propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
            propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            Session sesion = Session.getInstance(propiedades, new Authenticator() {
                @Override protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoRemitente, claveAplicacion);
                }
            });
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(correoRemitente, "Punto Cajas"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo + "\n\nSaludos,\nSistema Punto Cajas");
            Transport.send(mensaje);
            return true;
        } catch (Exception e) {
            System.out.println("Error al enviar notificación ERP: " + e.getMessage());
            return false;
        }
    }
    /**
     * Envía al cliente la cotización comercial completa en formato HTML.
     * Incluye especificaciones técnicas, precio, IVA, total, anticipo y tiempo estimado.
     */
    public boolean enviarCotizacionComercial(CotizacionERP q) {
        return enviarCotizacionComercial(q, null);
    }

    /**
     * Envía la cotización comercial y permite construir el enlace absoluto
     * directamente desde la URL real de la aplicación.
     */
    public boolean enviarCotizacionComercial(CotizacionERP q, String baseUrl) {
        if (q == null || q.getVersion() == null || q.getCorreoCliente() == null
                || q.getCorreoCliente().trim().isEmpty()) {
            return false;
        }

        CotizacionVersion v = q.getVersion();

        try {
            Properties propiedades = propiedadesSMTP();
            Session sesion = Session.getInstance(propiedades, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoRemitente, claveAplicacion);
                }
            });

            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(correoRemitente, "Punto Cajas"));
            mensaje.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(q.getCorreoCliente()));
            mensaje.setSubject("Tu cotización está lista - Punto Cajas #" + q.getIdCotizacion());

            String base = baseUrl;
            if (base == null || base.isBlank()) base = System.getenv("PUNTO_CAJAS_BASE_URL");
            if (base == null || base.isBlank()) base = System.getProperty("app.base.url", "");
            String enlace = base == null || base.isBlank()
                    ? "/MisCotizacionesServlet"
                    : base.replaceAll("/+$", "") + "/MisCotizacionesServlet?cotizacion=" + q.getIdCotizacion();

            String nombre = escapar(q.getNombreCliente());
            String color = escapar(v.getTipoCarton());
            String acabado = escapar(v.getAcabado());
            String uso = escapar(v.getDescripcionUsoCaja());

            String html = """
                    <!doctype html>
                    <html lang="es">
                    <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial,sans-serif;color:#263238;">
                      <div style="max-width:680px;margin:30px auto;background:#ffffff;border-radius:14px;overflow:hidden;border:1px solid #e3e7ea;">
                        <div style="padding:24px;background:#17324d;color:#ffffff;">
                          <div style="font-size:24px;font-weight:700;">Punto Cajas</div>
                          <div style="margin-top:6px;font-size:15px;">Tu cotización está lista</div>
                        </div>
                        <div style="padding:28px;">
                          <p style="font-size:16px;">Hola <strong>%s</strong>,</p>
                          <p>Hemos preparado la cotización <strong>#%d · V%d</strong>. Revisa los datos antes de decidir.</p>

                          <h3 style="margin-top:26px;">Especificaciones técnicas</h3>
                          <table width="100%%" cellpadding="8" cellspacing="0" style="border-collapse:collapse;font-size:14px;">
                            <tr><td style="border-bottom:1px solid #e8ecef;"><strong>Cantidad</strong></td><td style="border-bottom:1px solid #e8ecef;">%,d unidades</td></tr>
                            <tr><td style="border-bottom:1px solid #e8ecef;"><strong>Dimensiones</strong></td><td style="border-bottom:1px solid #e8ecef;">%s × %s × %s cm</td></tr>
                            <tr><td style="border-bottom:1px solid #e8ecef;"><strong>Color / cartón</strong></td><td style="border-bottom:1px solid #e8ecef;">%s</td></tr>
                            <tr><td style="border-bottom:1px solid #e8ecef;"><strong>Acabado</strong></td><td style="border-bottom:1px solid #e8ecef;">%s</td></tr>
                            <tr><td><strong>Uso</strong></td><td>%s</td></tr>
                          </table>

                          <h3 style="margin-top:28px;">Cotización comercial</h3>
                          <table width="100%%" cellpadding="9" cellspacing="0" style="border-collapse:collapse;font-size:14px;">
                            <tr><td style="border-bottom:1px solid #e8ecef;">Valor unitario</td><td align="right" style="border-bottom:1px solid #e8ecef;"><strong>$ %s</strong></td></tr>
                            <tr><td style="border-bottom:1px solid #e8ecef;">Subtotal</td><td align="right" style="border-bottom:1px solid #e8ecef;">$ %s</td></tr>
                            <tr><td style="border-bottom:1px solid #e8ecef;">IVA 19%%</td><td align="right" style="border-bottom:1px solid #e8ecef;">$ %s</td></tr>
                            <tr><td style="font-size:17px;"><strong>Total</strong></td><td align="right" style="font-size:17px;"><strong>$ %s</strong></td></tr>
                            <tr><td style="color:#137333;"><strong>Anticipo 50%%</strong></td><td align="right" style="color:#137333;"><strong>$ %s</strong></td></tr>
                          </table>

                          <div style="margin-top:22px;padding:16px;background:#eef7ff;border-radius:10px;">
                            <strong>Tiempo estimado de elaboración: %d días hábiles</strong><br>
                            <span style="font-size:13px;">El tiempo empieza a contar una vez confirmado el anticipo del 50 %%.</span>
                          </div>

                          <p style="margin-top:24px;">Puedes aceptar o rechazar la cotización desde tu cuenta.</p>
                          <div style="text-align:center;margin:28px 0;">
                            <a href="%s" style="display:inline-block;padding:13px 24px;background:#0d6efd;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:700;">Revisar mi cotización</a>
                          </div>
                          <p style="font-size:12px;color:#6c757d;">Si el botón no funciona, ingresa directamente a Punto Cajas desde tu navegador.</p>
                        </div>
                      </div>
                    </body>
                    </html>
                    """.formatted(
                    nombre, q.getIdCotizacion(), v.getNumeroVersion(), v.getCantidad(),
                    escapar(String.valueOf(v.getAlto())), escapar(String.valueOf(v.getLargo())),
                    escapar(String.valueOf(v.getAncho())), color, acabado, uso,
                    moneda(v.getValorUnitario()), moneda(v.getSubtotal()), moneda(v.getValorIva()),
                    moneda(v.getTotal()), moneda(q.getAnticipo()), v.getDiasElaboracion(), enlace);

            mensaje.setContent(html, "text/html; charset=UTF-8");
            Transport.send(mensaje);
            return true;
        } catch (Exception e) {
            System.out.println("Error al enviar cotización HTML: " + e.getMessage());
            return false;
        }
    }

    private Properties propiedadesSMTP() {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return propiedades;
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String moneda(java.math.BigDecimal valor) {
        if (valor == null) return "0.00";
        return valor.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

}
