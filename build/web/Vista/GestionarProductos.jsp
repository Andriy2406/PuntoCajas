<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Punto Cajas | Gestionar productos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
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
            
            <!-- Alerta Dinámica -->
            <c:if test="${not empty sessionScope.mensajeAlerta}">
                <div class="alert alert-${sessionScope.tipoAlerta} alert-dismissible fade show shadow-sm border-0 rounded-4 mb-4" role="alert">
                    ${sessionScope.mensajeAlerta}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("mensajeAlerta"); %>
                <% session.removeAttribute("tipoAlerta"); %>
            </c:if>

            <!-- Encabezado y Barra de Acciones -->
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
                            <h1 class="admin-title">Gestionar productos</h1>
                            <p class="admin-description mb-0">Administra los productos disponibles en el sistema con total seguridad.</p>
                        </div>
                    </div>
                    
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <!-- BARRA DE BÚSQUEDA POR DESCRIPCIÓN -->
                        <div class="input-group input-group-sm shadow-2xs" style="max-width: 260px;">
                            <input type="text" id="inputBuscarDescripcion" class="form-control border-end-0 rounded-start-pill ps-3" placeholder="Buscar por descripción...">
                            <span class="input-group-text bg-white text-secondary border-start-0 rounded-end-pill pe-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                                    <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                                </svg>
                            </span>
                        </div>

                        <button type="button" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 shadow-sm text-white" data-bs-toggle="modal" data-bs-target="#modalProducto">
                            + Nuevo Producto
                        </button>
                    </div>
                </div>
            </div>

            <!-- Tabla de Datos -->
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
                <div class="table-responsive p-0">
                    <table class="table table-hover align-middle mb-0" id="tablaProductos">
                        <thead class="table-light text-uppercase fs-7">
                            <tr>
                                <th class="py-3 ps-4">ID</th>
                                <th class="py-3" style="min-width: 250px;">Descripción</th>
                                <th class="py-3">Precio</th>
                                <th class="py-3">Categoría</th>
                                <th class="py-3">Stock</th>
                                <th class="py-3">Estado</th>
                                <th class="py-3 text-center pe-4" style="min-width: 220px;">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="producto" items="${listaProductos}">
                                <tr>
                                    <td class="ps-4 fw-medium py-3">${producto.idProducto}</td>
                                    <td class="col-descripcion py-3">${producto.descripcion}</td>
                                    <td class="py-3">$ <fmt:formatNumber value="${producto.precio}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                    <td class="py-3 text-secondary small">${producto.categoria}</td>
                                    <td class="py-3">${producto.stockActual}</td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${producto.estado}">
                                                <span class="badge bg-success text-white">Activo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary text-white">Inactivo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center pe-4 py-3">
                                        <div class="d-flex gap-2 justify-content-center">
                                            <!-- Botón Editar -->
                                            <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3 btn-editar-producto" data-id="${producto.idProducto}" data-descripcion="${producto.descripcion}" data-precio="${producto.precio}" data-stock="${producto.stockActual}" data-categoria="${producto.idCatalogo}" data-url="${producto.urlImagen}">Editar</button>

                                            <!-- Botón Activar / Inactivar -->
                                            <form action="${pageContext.request.contextPath}/GestionarProductosServlet" method="POST" class="d-inline">
                                                <input type="hidden" name="id" value="${producto.idProducto}">
                                                <c:choose>
                                                    <c:when test="${producto.estado}">
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
                                            <form action="${pageContext.request.contextPath}/GestionarProductosServlet" method="POST" class="d-inline" onsubmit="return confirm('¿Estás seguro de eliminar este producto?');">
                                                <input type="hidden" name="id" value="${producto.idProducto}">
                                                <input type="hidden" name="accion" value="eliminar">
                                                <button type="submit" class="btn btn-danger btn-sm rounded-pill px-3">Eliminar</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listaProductos}">
                                <tr>
                                    <td colspan="7" class="text-center text-secondary py-5">No hay productos registrados actualmente.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal para Crear / Editar Producto -->
    <div class="modal fade" id="modalProducto" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <form id="formProducto" action="${pageContext.request.contextPath}/GestionarProductosServlet" method="POST">
                    <input type="hidden" name="accion" id="inputAccion" value="crear">
                    <input type="hidden" name="id" id="inputIdProducto">

                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                        <h5 class="modal-title fw-bold" id="modalLabel">Registrar Nuevo Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label">Descripción</label>
                            <input type="text" class="form-control rounded-3" name="descripcion" id="descripcion" maxlength="45" required>
                        </div>
                        <div class="row g-3">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Precio</label>
                                <input type="number" step="0.01" min="0" class="form-control rounded-3" name="precio" id="precio" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Stock inicial</label>
                                <input type="number" min="0" step="1" class="form-control rounded-3" name="stockActual" id="stockActual" value="0" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Categoría</label>
                            <select class="form-select rounded-3" name="idCatalogo" id="idCatalogo" required>
                                <option value="">Seleccione una categoría</option>
                                <c:forEach var="cat" items="${listaCatalogos}">
                                    <option value="${cat.idCatalogo}">${cat.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">URL de imagen</label>
                            <input type="url" class="form-control rounded-3" name="url" id="url" maxlength="200" placeholder="https://...">
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
    <script src="${pageContext.request.contextPath}/Vista/javaScript/GestionarProductos.js"></script>

</body>
</html>
