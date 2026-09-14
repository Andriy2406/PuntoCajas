package Servlet;

import Controlador.CotizacionesERPDAO;
import Controlador.EnviarCorreo;
import Modelo.Usuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet(name = "CotizacionServlet", urlPatterns = {"/CotizacionServlet"})
@MultipartConfig(
fileSizeThreshold = 1024 * 1024,
maxFileSize = 5 * 1024 * 1024,
maxRequestSize = 6 * 1024 * 1024
)
public class CotizacionServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
private static final int ID_DOC_CON_COTIZACION = 1;

private final CotizacionesERPDAO dao = new CotizacionesERPDAO();

@Override
protected void doGet(
HttpServletRequest request,
HttpServletResponse response)
throws ServletException, IOException {


HttpSession s = request.getSession(false);

if (s == null
        || !(s.getAttribute("usuarioActivo")
        instanceof Usuarios)) {

    response.sendRedirect(
            request.getContextPath()
            + "/Vista/Login.jsp"
    );

    return;
}

request.getRequestDispatcher(
        "/Vista/CotizacionCliente.jsp"
).forward(
        request,
        response
);


}

@Override
protected void doPost(
HttpServletRequest request,
HttpServletResponse response)
throws ServletException, IOException {


request.setCharacterEncoding("UTF-8");

HttpSession s = request.getSession(false);

if (s == null
        || !(s.getAttribute("usuarioActivo")
        instanceof Usuarios)) {

    response.sendRedirect(
            request.getContextPath()
            + "/Vista/Login.jsp"
    );

    return;
}

Usuarios u =
        (Usuarios) s.getAttribute(
                "usuarioActivo"
        );

try {

    int cantidad =
            Integer.parseInt(
                    request.getParameter("cantidad")
            );

    BigDecimal alto =
            decimal(
                    request.getParameter("alto")
            );

    BigDecimal largo =
            decimal(
                    request.getParameter("largo")
            );

    BigDecimal ancho =
            decimal(
                    request.getParameter("ancho")
            );

    String tipoCarton =
            limpiar(
                    request.getParameter("tipoCarton")
            );

    String uso =
            limpiar(
                    request.getParameter(
                            "descripcion_uso_caja"
                    )
            );

    if (tipoCarton.isEmpty()) {

        throw new IllegalArgumentException(
                "Debe seleccionar un tipo de cartón."
        );
    }

    Part parteImagen =
            request.getPart("acabado");

    String rutaImagen = null;

    if (parteImagen != null
            && parteImagen.getSize() > 0) {

        rutaImagen =
                guardarImagen(
                        parteImagen
                );
    }

    long id =
            dao.crearCotizacion(
                    u.getIdUsuario(),
                    ID_DOC_CON_COTIZACION,
                    cantidad,
                    alto,
                    largo,
                    ancho,
                    tipoCarton,
                    rutaImagen,
                    uso
            );

    try {

        new EnviarCorreo().enviarNotificacionCotizacion(
                u.getNombre()
                + " "
                + u.getApellido(),
                u.getCorreo(),
                (int) id
        );

    } catch (Exception ignored) {
    }

    request.setAttribute(
            "cotizacionRegistrada",
            true
    );

    request.setAttribute(
            "idCotizacion",
            id
    );

} catch (Exception e) {

    request.setAttribute(
            "error",
            mensaje(e)
    );
}

request.getRequestDispatcher(
        "/Vista/CotizacionCliente.jsp"
).forward(
        request,
        response
);


}

private String guardarImagen(
Part parteImagen)
throws IOException {


String contentType =
        parteImagen.getContentType();

if (contentType == null
        || !esImagenPermitida(
                contentType
        )) {

    throw new IllegalArgumentException(
            "Solo se permiten imágenes JPG, PNG o WEBP."
    );
}

String nombreOriginal =
        parteImagen.getSubmittedFileName();

String extension =
        obtenerExtension(
                nombreOriginal,
                contentType
        );

String nombreArchivo =
        UUID.randomUUID()
                .toString()
                .replace(
                        "-",
                        ""
                )
        + extension;

String carpetaBase =
        System.getProperty(
                "user.home"
        )
        + java.io.File.separator
        + "punto_cajas_uploads"
        + java.io.File.separator
        + "acabados";

Path carpeta =
        Paths.get(
                carpetaBase
        );

Files.createDirectories(
        carpeta
);

Path archivo =
        carpeta.resolve(
                nombreArchivo
        );

try (
        InputStream input =
                parteImagen.getInputStream()
) {

    Files.copy(
            input,
            archivo,
            StandardCopyOption.REPLACE_EXISTING
    );
}

return "uploads/acabados/"
        + nombreArchivo;


}

private boolean esImagenPermitida(
String contentType) {


return "image/jpeg".equalsIgnoreCase(
        contentType
)
        || "image/png".equalsIgnoreCase(
                contentType
        )
        || "image/webp".equalsIgnoreCase(
                contentType
        );


}

private String obtenerExtension(
String nombreOriginal,
String contentType) {


if (nombreOriginal != null) {

    String nombre =
            Paths.get(
                    nombreOriginal
            )
                    .getFileName()
                    .toString();

    int punto =
            nombre.lastIndexOf('.');

    if (punto >= 0
            && punto < nombre.length() - 1) {

        String extension =
                nombre.substring(
                        punto
                ).toLowerCase();

        if (extension.equals(".jpg")
                || extension.equals(".jpeg")
                || extension.equals(".png")
                || extension.equals(".webp")) {

            return extension;
        }
    }
}

if ("image/png".equalsIgnoreCase(
        contentType
)) {

    return ".png";
}

if ("image/webp".equalsIgnoreCase(
        contentType
)) {

    return ".webp";
}

return ".jpg";


}

private BigDecimal decimal(
String s) {


if (s == null
        || s.trim().isEmpty()) {

    throw new IllegalArgumentException(
            "Las dimensiones son obligatorias."
    );
}

return new BigDecimal(
        s.trim()
);

}

private String limpiar(
String s) {

return s == null
        ? ""
        : s.trim();

}

private String mensaje(
Exception e) {


return e.getMessage() == null
        ? "No fue posible registrar la cotización."
        : e.getMessage();

}

}
