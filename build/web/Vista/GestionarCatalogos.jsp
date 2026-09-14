<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Punto Cajas | Gestionar catálogos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
</head>
<body class="admin-page d-flex flex-column min-vh-100">

    <!-- Barra de navegación -->
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

    <!-- Contenido Principal -->
    <main class="admin-dashboard flex-grow-1 py-5">
        <div class="container-fluid px-lg-4">
            
            <!-- Alerta Dinámica de Notificaciones -->
            <c:if test="${not empty sessionScope.mensajeAlerta}">
                <div class="alert alert-${sessionScope.tipoAlerta} alert-dismissible fade show shadow-sm border-0 rounded-4 mb-4" role="alert">
                    ${sessionScope.mensajeAlerta}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("mensajeAlerta"); %>
                <% session.removeAttribute("tipoAlerta"); %>
            </c:if>

            <!-- Encabezado y Barra de Acciones Alineados -->
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <div class="admin-header m-0 p-0 border-0 bg-transparent d-flex flex-row justify-content-between align-items-center flex-wrap gap-3">
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/AdminServlet" class="btn-volver-admin shadow-2xs">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                            </svg>
                            Volver al panel
                        </a>
                        <div>
                            <span class="admin-eyebrow">Gestión del Sistema</span>
                            <h1 class="admin-title">Gestionar catálogos</h1>
                            <p class="admin-description mb-0">Crea y administra las categorías que usa el catálogo público para agrupar productos.</p>
                        </div>
                    </div>
                    
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <!-- Barra de Búsqueda -->
                        <div class="input-group input-group-sm shadow-2xs" style="max-width: 260px;">
                            <input type="text" id="inputBuscarCatalogo" class="form-control border-end-0 rounded-start-pill ps-3" placeholder="Buscar por nombre...">
                            <span class="input-group-text bg-white text-secondary border-start-0 rounded-end-pill pe-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                                    <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                                </svg>
                            </span>
                        </div>

                        <button type="button" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 shadow-sm text-white" id="btnNuevoCatalogo" data-bs-toggle="modal" data-bs-target="#modalCatalogo">
                            + Nuevo catálogo
                        </button>
                    </div>
                </div>
            </div>

            <!-- Tabla de Datos -->
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
                <div class="table-responsive p-0">
                    <table class="table table-hover align-middle mb-0" id="tablaCatalogos">
                        <thead class="table-light text-uppercase fs-7">
                            <tr>
                                <th class="py-3 ps-4">ID</th>
                                <th class="py-3" style="min-width: 250px;">Nombre</th>
                                <th class="py-3">Estado</th>
                                <th class="py-3 text-center pe-4" style="min-width: 220px;">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="catalogo" items="${listaCatalogos}">
                                <tr>
                                    <td class="ps-4 fw-medium py-3">${catalogo.idCatalogo}</td>
                                    <td class="col-nombre py-3">${catalogo.nombre}</td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${catalogo.estado}">
                                                <span class="badge bg-success text-white">Activo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary text-white">Inactivo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center pe-4 py-3">
                                        <div class="d-flex gap-2 justify-content-center">
                                            <!-- Botón Editar con atributos de Bootstrap -->
                                            <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3 btn-editar-catalogo" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#modalCatalogo" 
                                                    data-id="${catalogo.idCatalogo}" 
                                                    data-nombre="${catalogo.nombre}">
                                                Editar
                                            </button>

                                            <!-- Botón Activar / Inactivar -->
                                            <form action="${pageContext.request.contextPath}/GestionarCatalogosServlet" method="POST" class="d-inline">
                                                <input type="hidden" name="id" value="${catalogo.idCatalogo}">
                                                <c:choose>
                                                    <c:when test="${catalogo.estado}">
                                                        <input type="hidden" name="accion" value="inactivar">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3">Inactivar</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="hidden" name="accion" value="activar">
                                                        <button type="submit" class="btn btn-outline-success btn-sm rounded-pill px-3">Activar</button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </form>

                                            <!-- Botón Eliminar -->
                                            <form action="${pageContext.request.contextPath}/GestionarCatalogosServlet" method="POST" class="d-inline" onsubmit="return confirm('¿Estás seguro de eliminar este catálogo? Los productos relacionados podrían verse afectados.');">
                                                <input type="hidden" name="id" value="${catalogo.idCatalogo}">
                                                <input type="hidden" name="accion" value="eliminar">
                                                <button type="submit" class="btn btn-danger btn-sm rounded-pill px-3">Eliminar</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listaCatalogos}">
                                <tr>
                                    <td colspan="4" class="text-center text-secondary py-5">No hay catálogos registrados.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal para Crear / Editar Catálogo -->
    <div class="modal fade" id="modalCatalogo" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <form id="formCatalogo" action="${pageContext.request.contextPath}/GestionarCatalogosServlet" method="POST">
                    <input type="hidden" name="accion" id="inputAccion" value="crear">
                    <input type="hidden" name="id" id="inputIdCatalogo">

                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                        <h5 class="modal-title fw-bold" id="modalLabel">Nuevo catálogo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label">Nombre de la categoría</label>
                            <input type="text" class="form-control rounded-3" name="nombre" id="nombreCatalogo" placeholder="Ej. Cajas para mudanza" required>
                        </div>
                    </div>
                    <div class="modal-footer bg-light px-4 py-3 border-top">
                        <button type="button" class="btn btn-secondary btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 text-white">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Pie de página -->
    <footer class="footer-punto text-center">
        <div class="container">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/GestionarCatalogos.js"></script>
</body>
</html>