<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | ${producto.descripcion}</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body data-context="${pageContext.request.contextPath}/CarritoAjaxController">

    <nav class="navbar navbar-expand-md navbar-punto py-3">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/InicioServlet">
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
                <ul class="navbar-nav ms-auto align-items-md-center gap-md-2">
                    <li class="nav-item">
                        <a class="nav-link " href="${pageContext.request.contextPath}/InicioServlet">Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CotizacionServlet">Cotizar</a>
                    </li>
                    <li class="nav-item">
                        <c:choose>
                            <%-- Si el usuario ha iniciado sesión, va directo a Mis Cotizaciones/Historial --%>
                            <c:when test="${not empty sessionScope.usuarioActivo}">
                                <a class="nav-link" href="${pageContext.request.contextPath}/MisCotizacionesServlet">Historial</a>
                            </c:when>
                            <%-- Si NO ha iniciado sesión, lo redirige al Login --%>
                            <c:otherwise>
                                <a class="nav-link" href="${pageContext.request.contextPath}/Vista/Login.jsp">Historial</a>
                            </c:otherwise>
                        </c:choose>
                    </li>

                    <c:choose>
                        <c:when test="${not empty sessionScope.usuarioActivo}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/Vista/Soporte.jsp">Soporte</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/PerfilServlet">Mi perfil</a>
                            </li>
                            <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                                <li class="nav-item">
                                    <a class="nav-link " href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
                                </li>
                            </c:if>
                            <li class="nav-item ms-md-3">
                                <span class="nav-link">Hola, <strong>${sessionScope.usuarioActivo.nombre}</strong></span>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-punto-secundario btn-sm" href="${pageContext.request.contextPath}/LogoutServlet">Cerrar sesión</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-punto-primario btn-sm" href="${pageContext.request.contextPath}/Vista/Login.jsp">Iniciar sesión</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-punto-secundario btn-sm" href="${pageContext.request.contextPath}/Vista/Registro.jsp">Registrarse</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <main>
        <section class="py-5">
            <div class="container">
                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn-volver-admin shadow-2xs d-inline-flex align-items-center gap-2 text-decoration-none px-3 py-2 rounded-pill border bg-white text-dark">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        Volver al Catalogo
                    </a>
                </div>

                <div class="row g-4">
                    <div class="col-md-6">
                        <!-- La url debe venir en formato: https://drive.google.com/uc?export=view&id=ID_DEL_ARCHIVO -->
                        <img src="${producto.urlImagen}" alt="${producto.descripcion}" class="img-fluid rounded border" style="width:100%; max-height:420px; object-fit:cover;">
                    </div>

                    <div class="col-md-6">
                        <c:if test="${not empty producto.categoria}">
                            <span class="badge bg-secondary mb-2">${producto.categoria}</span>
                        </c:if>

                        <h1 class="h3">${producto.descripcion}</h1>
                        <p class="h4 fw-bold" style="color: var(--verde-principal);">$${producto.precio}</p>

                        <!-- Especificaciones del producto -->
                        <table class="table table-sm w-auto">
                            <tbody>
                                <tr>
                                    <th class="text-secondary">Categoría</th>
                                    <td>${producto.categoria}</td>
                                </tr>
                                <tr>
                                    <th class="text-secondary">Stock disponible</th>
                                    <td>${producto.stockActual} unidades</td>
                                </tr>
                                <tr>
                                    <th class="text-secondary">Código de producto</th>
                                    <td>#${producto.idProducto}</td>
                                </tr>
                            </tbody>
                        </table>

                        <!-- Controles de cantidad -->
                        <div class="input-group mb-3" style="max-width: 160px;">
                            <button type="button" class="btn btn-outline-secondary px-3" data-cantidad-delta="-1">-</button>
                            <input type="number" id="cantidadDetalle" value="1" min="1" max="${producto.stockActual}" class="form-control text-center" readonly>
                            <button type="button" class="btn btn-outline-secondary px-3" data-cantidad-delta="1">+</button>
                        </div>

                        <button type="button" class="btn btn-punto-primario btn-lg" id="btnAgregarCarrito" data-id-producto="${producto.idProducto}">
                            Agregar al carrito
                        </button>

                        <div id="mensajeDetalle" class="mt-3 fw-bold" style="display:none;"></div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <footer class="footer-punto py-4">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script src="${pageContext.request.contextPath}/Vista/javaScript/DetalleProducto.js"></script>
</body>
</html>
