<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Punto Cajas | Solicitar cotización</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body>

    <!-- =========================================================
         NAVBAR
    ========================================================== -->
    <nav class="navbar navbar-expand-lg navbar-punto py-3">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/InicioServlet">
                <svg class="logo-caja" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                    <path d="M3 7l9-4 9 4-9 4-9-4z"/>
                    <path d="M3 7v10l9 4 9-4V7"/>
                    <path d="M12 11v10"/>
                </svg>
                Punto Cajas
            </a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal" aria-controls="menuPrincipal" aria-expanded="false" aria-label="Mostrar menú">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="menuPrincipal">
                <ul class="navbar-nav ms-auto align-items-lg-center fw-semibold gap-lg-1">
                    <li class="nav-item">
                        <a class="nav-link strong" href="${pageContext.request.contextPath}/InicioServlet">Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/CotizacionServlet">Cotizar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/MisCotizacionesServlet">Historial</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/Vista/Soporte.jsp">Soporte</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/PerfilServlet">Mi perfil</a>
                    </li>

                    <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
                        </li>
                    </c:if>

                    <li class="nav-item ms-lg-2">
                        <span class="nav-link text-secondary">Hola, <strong>${sessionScope.usuarioActivo.nombre}</strong></span>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-punto-secundario btn-sm ms-lg-2" href="${pageContext.request.contextPath}/LogoutServlet">Cerrar sesión</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- =========================================================
         HERO
    ========================================================== -->
    <div class="hero-punto py-4">
        <div class="container">
            <h1 class="h3 mb-1 fw-bold">Solicitar cotización</h1>
            <p class="text-secondary mb-0">Describe técnicamente la caja que necesitas. El precio será definido posteriormente por Punto Cajas.</p>
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/InicioServlet" class="btn bg-white border shadow-sm rounded-pill px-3 py-2 text-dark text-decoration-none d-inline-flex align-items-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                    </svg>
                    Volver al inicio
                </a>
            </div>
        </div>
    </div>

    <!-- =========================================================
         FORMULARIO
    ========================================================== -->
    <main class="py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card shadow-sm border-0 p-4 p-md-5">

                        <!-- MENSAJE DE ERROR -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>

                        <!-- COTIZACIÓN REGISTRADA -->
                        <c:if test="${cotizacionRegistrada}">
                            <div class="alert alert-success">
                                Solicitud registrada. Tu número de cotización es <strong>#${idCotizacion}</strong>. Puedes consultar su avance en <a href="${pageContext.request.contextPath}/MisCotizacionesServlet">Mis cotizaciones</a>.
                            </div>
                        </c:if>

                        <!-- FORMULARIO -->
                        <form method="post" action="${pageContext.request.contextPath}/CotizacionServlet" enctype="multipart/form-data">

                            <!-- CANTIDAD -->
                            <div class="mb-3">
                                <label class="form-label" for="cantidad">Cantidad</label>
                                <input class="form-control" type="number" id="cantidad" name="cantidad" min="1" required>
                            </div>

                            <!-- DIMENSIONES -->
                            <div class="row">
                                <!-- ALTO -->
                                <div class="col-md-4 mb-3">
                                    <label class="form-label" for="alto">Alto (cm)</label>
                                    <input class="form-control" type="number" id="alto" name="alto" min="0.01" step="0.01" required>
                                </div>

                                <!-- LARGO -->
                                <div class="col-md-4 mb-3">
                                    <label class="form-label" for="largo">Largo (cm)</label>
                                    <input class="form-control" type="number" id="largo" name="largo" min="0.01" step="0.01" required>
                                </div>

                                <!-- ANCHO -->
                                <div class="col-md-4 mb-3">
                                    <label class="form-label" for="ancho">Ancho (cm)</label>
                                    <input class="form-control" type="number" id="ancho" name="ancho" min="0.01" step="0.01" required>
                                </div>
                            </div>

                            <!-- TIPO DE CAJA -->
                            <div class="mb-3">
                                <label class="form-label" for="tipoCarton">Tipo de caja</label>
                                <select class="form-select" id="tipoCarton" name="tipoCarton" required>
                                    <option value="">Seleccione un tipo de caja</option>
                                    <option value="Convencional">Convencional</option>
                                    <option value="Troquelada">Troquelada</option>
                                </select>
                            </div>

                            <!-- IMAGEN -->
                            <div class="mb-3">
                                <label class="form-label" for="acabado">Imagen de impresión</label>
                                <input class="form-control" type="file" id="acabado" name="acabado" accept="image/jpeg,image/png,image/webp">
                            </div>

                            <!-- USO / DESCRIPCIÓN -->
                            <div class="mb-4">
                                <label class="form-label" for="descripcionUsoCaja">Uso o descripción del empaque</label>
                                <textarea class="form-control" id="descripcionUsoCaja" name="descripcion_uso_caja" maxlength="45" rows="3" required></textarea>
                            </div>

                            <!-- BOTONES -->
                            <div class="d-flex flex-wrap gap-2">
                                <button class="btn btn-punto-primario" type="submit">Enviar solicitud</button>
                                <a class="btn btn-punto-secundario" href="${pageContext.request.contextPath}/MisCotizacionesServlet">Ver mis cotizaciones</a>
                            </div>

                        </form>

                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- =========================================================
         FOOTER
    ========================================================== -->
    <footer class="footer-punto text-center py-4">
        <div class="container">
            <span>
                &copy; 2026 Punto Cajas. Todos los derechos reservados. |
                <a href="#">Política de privacidad</a> |
                <a href="#">Términos y condiciones</a>
            </span>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>