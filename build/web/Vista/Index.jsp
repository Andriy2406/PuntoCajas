<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Inicio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body>

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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/InicioServlet">Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
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
                                    <a class="nav-link" href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
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
        <section class="hero-punto">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-md-7">
                        <c:choose>
                            <c:when test="${not empty sessionScope.usuarioActivo}">
                                <h1 class="display-6 fw-bold">Bienvenido a Punto Cajas, ${sessionScope.usuarioActivo.nombre}</h1>
                                <p class="lema fs-5">"Donde cada caja encaja contigo."</p>
                                <p class="text-secondary">En Punto Cajas puedes solicitar cotizaciones según las medidas, color y acabado que necesites, realizar compras y recibir atención cuando la necesites.</p>
                                <div class="d-flex gap-3 mt-4">
                                    <a href="${pageContext.request.contextPath}/CotizacionServlet" class="btn btn-punto-primario btn-lg px-4">Cotizar ahora</a>
                                    <a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn btn-punto-secundario btn-lg px-4">Ver catálogo</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <h1 class="display-6 fw-bold">Bienvenido a Punto Cajas</h1>
                                <p class="lema fs-5">"Donde cada caja encaja contigo."</p>
                                <p class="text-secondary">En Punto Cajas puedes consultar nuestro catálogo y solicitar una cotización según las medidas, color y acabado que necesites.</p>
                                <div class="d-flex gap-3 mt-4">
                                    <a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn btn-punto-primario btn-lg px-4">Ver catálogo</a>
                                    <a href="${pageContext.request.contextPath}/Vista/Login.jsp" class="btn btn-punto-secundario btn-lg px-4">Iniciar sesión</a>
                                    <a href="${pageContext.request.contextPath}/Vista/Registro.jsp" class="btn btn-punto-secundario btn-lg px-4">Registrarse</a>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div class="franja-cajas">
                            <span class="caja-decorativa"></span>
                            <span class="caja-decorativa"></span>
                            <span class="caja-decorativa"></span>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="seccion-gris">
            <div class="container">
                <h2 class="text-center mb-5">¿Qué puedes hacer en Punto Cajas?</h2>
                <div class="row g-4">
                    <div class="col-md-4 col-sm-6">
                        <div class="tarjeta-servicio">
                            <div class="icono-servicio">📐</div>
                            <h3 class="h5">Cotizaciones</h3>
                            <p class="text-secondary mb-0">Indica cantidad, medidas, color y acabado de la caja que necesitas.</p>
                        </div>
                    </div>
                    <div class="col-md-4 col-sm-6">
                        <div class="tarjeta-servicio">
                            <div class="icono-servicio">📦</div>
                            <h3 class="h5">Catálogo</h3>
                            <p class="text-secondary mb-0">Consulta nuestros productos disponibles y conoce sus características.</p>
                        </div>
                    </div>
                    <div class="col-md-4 col-sm-6">
                        <div class="tarjeta-servicio">
                            <div class="icono-servicio">🛒</div>
                            <h3 class="h5">Compras</h3>
                            <p class="text-secondary mb-0">Agrega productos al carrito y realiza tu pedido.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>