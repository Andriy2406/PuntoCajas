<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Panel de administración</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Fuente -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <!-- Estilos globales -->
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <!-- Estilos exclusivos del administrador -->
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
</head>

<body>

    <!-- =====================================================
         NAVEGACIÓN
    ====================================================== -->
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
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

    <!-- =====================================================
         CONTENIDO PRINCIPAL
    ====================================================== -->
    <main class="admin-page">
        <section class="admin-dashboard">
            <div class="container">

                <!-- ENCABEZADO DEL PANEL -->
                <div class="admin-header">
                    <div>
                        <span class="admin-eyebrow">ADMINISTRACIÓN</span>
                        <h1 class="admin-title">Panel de administración</h1>
                        <p class="admin-description">
                            Gestiona los usuarios, cotizaciones, productos, ventas y producción de Punto Cajas.
                        </p>
                    </div>
                </div>

                <!-- MÓDULOS ADMINISTRATIVOS -->
                <div class="admin-section-header">
                    <div>
                        <h2>Gestión del sistema</h2>
                        <p>Selecciona el área que deseas administrar.</p>
                    </div>
                </div>

                <div class="row g-4 admin-grid mb-5">

                    <!-- USUARIOS -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128100;</div>
                                    <span class="admin-card-number">01</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestionar usuarios</h3>
                                    <p>Consulta las cuentas registradas, revisa su información y administra su estado de acceso.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarUsuariosServlet">
                                        Ir a usuarios <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                    <!-- COTIZACIONES -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128196;</div>
                                    <span class="admin-card-number">02</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestionar cotizaciones</h3>
                                    <p>Revisa las solicitudes de los clientes, administra versiones y gestiona la información comercial.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarCotizacionesServlet">
                                        Ir a cotizaciones <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                    <!-- PRODUCTOS -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128230;</div>
                                    <span class="admin-card-number">03</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestionar productos</h3>
                                    <p>Administra los productos disponibles, su información y disponibilidad dentro del catálogo.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarProductosServlet">
                                        Ir a productos <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                    <!-- CATÁLOGOS -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128194;</div>
                                    <span class="admin-card-number">04</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestionar catálogos</h3>
                                    <p>Organiza las categorías que agrupan los productos disponibles para los clientes.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarCatalogosServlet">
                                        Ir a catálogos <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                    <!-- VENTAS -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128220;</div>
                                    <span class="admin-card-number">05</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestionar ventas</h3>
                                    <p>Consulta los pedidos, actualiza su seguimiento y administra la asignación de conductores.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarVentasServlet">
                                        Ir a ventas <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                    <!-- PRODUCCIÓN -->
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                        <div class="col-md-6 col-xl-4">
                            <article class="admin-card h-100 d-flex flex-column">
                                <div class="admin-card-header">
                                    <div class="admin-card-icon">&#128221;</div>
                                    <span class="admin-card-number">06</span>
                                </div>
                                <div class="admin-card-content flex-grow-1">
                                    <h3>Gestión de producción</h3>
                                    <p>Organiza y administra las órdenes que se encuentran actualmente en proceso de producción.</p>
                                </div>
                                <div class="admin-card-footer mt-auto">
                                    <a class="btn btn-punto-primario admin-card-button w-100" href="${pageContext.request.contextPath}/GestionarProduccionServlet?accion=listar">
                                        Ir a producción <span aria-hidden="true">→</span>
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:if>

                </div>
            </div>
        </section>
    </main>

    <!-- =====================================================
         PIE DE PÁGINA
    ====================================================== -->
    <footer class="footer-punto mt-auto">
        <div class="container text-center py-4">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp" class="text-decoration-none">Política de privacidad</a>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp" class="text-decoration-none">Términos y condiciones</a>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>