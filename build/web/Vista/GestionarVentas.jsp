<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Punto Cajas | Gestionar ventas</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
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
                            <h1 class="admin-title">Gestionar ventas</h1>
                            <p class="admin-description mb-0">Administra los pedidos generados a partir de las cotizaciones.</p>
                        </div>
                    </div>
                    
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <!-- BARRA DE BÚSQUEDA POR DIRECCIÓN DE ENVÍO -->
                        <div class="input-group input-group-sm shadow-2xs" style="max-width: 260px;">
                            <input type="text" id="inputBuscarDireccion" class="form-control border-end-0 rounded-start-pill ps-3" placeholder="Buscar por dirección...">
                            <span class="input-group-text bg-white text-secondary border-start-0 rounded-end-pill pe-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                                    <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                                </svg>
                            </span>
                        </div>
                    </div>
                </div>
                
                <p class="texto-ayuda-perfil text-secondary small mt-3 mb-0">
                    Los pedidos se generan automáticamente cuando un cliente compra o cuando se acepta una cotización. Desde aquí puedes actualizar el estado de fabricación/entrega y asignar un conductor, pero ya no se pueden crear pedidos manualmente.
                </p>
            </div>

            <!-- Tabla de Datos -->
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
                <div class="table-responsive p-0">
                    <table class="table table-hover align-middle mb-0" id="tablaVentas">
                        <thead class="table-light text-uppercase fs-7">
                            <tr>
                                <th class="py-3 ps-4">ID</th>
                                <th class="py-3">Fecha</th>
                                <th class="py-3" style="min-width: 200px;">Dirección de envío</th>
                                <th class="py-3">Total</th>
                                <th class="py-3">Estado</th>
                                <th class="py-3">ID Cotización</th>
                                <th class="py-3">Conductor</th>
                                <th class="py-3" style="min-width: 220px;">Productos comprados</th>
                                <th class="py-3 text-center pe-4" style="min-width: 200px;">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="pedido" items="${listaPedidos}">
                                <tr>
                                    <td class="ps-4 fw-medium py-3">${pedido.idPedido}</td>
                                    <td class="py-3">${pedido.fecha}</td>
                                    <td class="col-direccion py-3">${pedido.direccionEnvio}</td>
                                    <td class="py-3">$ <fmt:formatNumber value="${pedido.total}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${pedido.estadoPedido == 'Entregado'}">
                                                <span class="badge bg-success text-white">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'En fabricación'}">
                                                <span class="badge bg-warning text-dark">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'En espera'}">
                                                <span class="badge bg-secondary text-white">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'Listo para envío'}">
                                                <span class="badge bg-info text-dark">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'En reparto'}">
                                                <span class="badge bg-primary text-white">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'Pendiente'}">
                                                <span class="badge bg-primary text-white">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPedido == 'Cancelado'}">
                                                <span class="badge bg-danger text-white">${pedido.estadoPedido}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary text-white">${pedido.estadoPedido}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="py-3">${pedido.idCotizacion}</td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${not empty pedido.nombreConductor}">
                                                ${pedido.nombreConductor}
                                            </c:when>
                                            <c:otherwise><span class="text-secondary small">Sin asignar</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${not empty detallesPorPedido[pedido.idPedido]}">
                                                <ul class="mb-0 ps-3 small text-secondary">
                                                    <c:forEach var="detalle" items="${detallesPorPedido[pedido.idPedido]}">
                                                        <li>Producto #${detalle.idProducto} — ${detalle.cantidad} un. — $<fmt:formatNumber value="${detalle.subtotal}" minFractionDigits="2" maxFractionDigits="2"/></li>
                                                    </c:forEach>
                                                </ul>
                                            </c:when>
                                            <c:otherwise><span class="text-secondary small">Sin productos</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center pe-4 py-3">
                                        <div class="d-flex gap-2 justify-content-center">
                                            <!-- Botón Editar con atributos de Bootstrap -->
                                            <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3 btn-editar-pedido" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#modalPedido"
                                                    data-id="${pedido.idPedido}" 
                                                    data-fecha="${pedido.fecha}" 
                                                    data-direccion="${pedido.direccionEnvio}" 
                                                    data-total="${pedido.total}" 
                                                    data-estado="${pedido.estadoPedido}"
                                                    data-conductor="${pedido.idConductor}"
                                                    data-lat="${pedido.latitud}"
                                                    data-lng="${pedido.longitud}">
                                                Actualizar
                                            </button>

                                            <!-- Botón Eliminar -->
                                            <form action="${pageContext.request.contextPath}/GestionarVentasServlet" method="POST" class="d-inline" onsubmit="return confirm('¿Estás seguro de eliminar este pedido?');">
                                                <input type="hidden" name="id" value="${pedido.idPedido}">
                                                <input type="hidden" name="accion" value="eliminar">
                                                <button type="submit" class="btn btn-danger btn-sm rounded-pill px-3">Eliminar</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="fila-seguimiento">
                                    <td colspan="9" class="p-0 border-0">
                                        <div class="linea-tiempo-pedido px-4 py-2 bg-light small">
                                            <c:if test="${not empty historialPorPedido[pedido.idPedido]}">
                                                <div class="d-flex flex-wrap gap-2 align-items-center">
                                                    <span class="fw-semibold text-secondary">Historial:</span>
                                                    <c:forEach var="evento" items="${historialPorPedido[pedido.idPedido]}">
                                                        <span class="badge bg-white text-dark border px-2 py-1" title="${evento.fechaEvento}">${evento.estadoNuevo}</span>
                                                    </c:forEach>
                                                </div>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listaPedidos}">
                                <tr>
                                    <td colspan="9" class="text-center text-secondary py-5">No hay pedidos registrados.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal para Editar / Actualizar seguimiento de Pedido -->
    <div class="modal fade" id="modalPedido" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <form id="formPedido" action="${pageContext.request.contextPath}/GestionarVentasServlet" method="POST">
                    <input type="hidden" name="accion" id="inputAccion" value="actualizar">
                    <input type="hidden" name="id" id="inputIdPedido">

                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                        <h5 class="modal-title fw-bold" id="modalLabel">Seguimiento del pedido</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Fecha</label>
                                <input type="date" class="form-control rounded-3" name="fecha" id="fecha" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Total</label>
                                <input type="number" step="0.01" min="0" class="form-control rounded-3" name="total" id="total" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Dirección de envío</label>
                            <input type="text" class="form-control rounded-3" name="direccionEnvio" id="direccionEnvio" maxlength="100" required>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Estado / seguimiento</label>
                                <select class="form-select rounded-3" name="estadoPedido" id="estadoPedido" required>
                                    <c:forEach var="estado" items="${estadosPedido}">
                                        <option value="${estado}">${estado}</option>
                                    </c:forEach>
                                </select>
                                <div class="form-text">Cada cambio de estado queda guardado en el historial de seguimiento del pedido.</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Conductor asignado</label>
                                <select class="form-select rounded-3" name="idConductor" id="idConductor">
                                    <option value="">Sin asignar</option>
                                    <c:forEach var="conductor" items="${listaConductores}">
                                        <option value="${conductor.idUsuario}">${conductor.nombre} ${conductor.apellido}</option>
                                    </c:forEach>
                                </select>
                                <div class="form-text">El conductor verá este pedido en su panel de rutas de entrega.</div>
                            </div>
                        </div>

                        <div class="mb-2">
                            <label class="form-label">Punto de entrega en el mapa</label>
                            <div id="mapaPedido" class="mapa-direccion mapa-pequeno rounded-3 border"></div>
                            <input type="hidden" id="latitud" name="latitud">
                            <input type="hidden" id="longitud" name="longitud">
                            <div class="form-text">Arrastra el pin si necesitas corregir la ubicación exacta de entrega.</div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light px-4 py-3 border-top">
                        <button type="button" class="btn btn-secondary btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 text-white">Guardar seguimiento</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/mapaDireccion.js"></script>

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
    <script src="${pageContext.request.contextPath}/Vista/javaScript/GestionarVentas.js?v=2"></script>

</body>
</html>