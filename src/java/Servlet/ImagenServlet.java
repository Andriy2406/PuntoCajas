package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "ImagenServlet", urlPatterns = {"/ImagenServlet"})
public class ImagenServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String archivo =
                request.getParameter("archivo");

        if (archivo == null
                || archivo.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No se especificó una imagen."
            );

            return;
        }

        String nombreArchivo =
                Paths.get(archivo)
                        .getFileName()
                        .toString();

        String carpetaBase =
                System.getProperty("user.home")
                + java.io.File.separator
                + "punto_cajas_uploads"
                + java.io.File.separator
                + "acabados";

        Path carpeta =
                Paths.get(carpetaBase);

        Path imagen =
                carpeta.resolve(
                        nombreArchivo
                ).normalize();

        if (!imagen.startsWith(
                carpeta.normalize()
        )) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        if (!Files.exists(imagen)
                || !Files.isRegularFile(imagen)) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "La imagen no existe."
            );

            return;
        }

        String contentType =
                Files.probeContentType(imagen);

        if (contentType == null) {

            contentType =
                    obtenerTipoContenido(
                            nombreArchivo
                    );
        }

        response.setContentType(
                contentType
        );

        response.setContentLengthLong(
                Files.size(imagen)
        );

        try (
                OutputStream output =
                        response.getOutputStream()
        ) {

            Files.copy(
                    imagen,
                    output
            );
        }
    }

    private String obtenerTipoContenido(
            String nombreArchivo) {

        String nombre =
                nombreArchivo.toLowerCase();

        if (nombre.endsWith(".png")) {
            return "image/png";
        }

        if (nombre.endsWith(".webp")) {
            return "image/webp";
        }

        if (nombre.endsWith(".jpg")
                || nombre.endsWith(".jpeg")) {

            return "image/jpeg";
        }

        return "application/octet-stream";
    }
}