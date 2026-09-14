<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Gestión de Producción</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/gestionar-produccion.css" rel="stylesheet">
</head>

<body class="admin-page d-flex flex-column min-vh-100">

    <!-- =========================================================
         NAVBAR
         ========================================================= -->
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

    <!-- =========================================================
         CONTENIDO PRINCIPAL
         ========================================================= -->
    <main class="admin-dashboard flex-grow-1 py-5">
        <div class="container-fluid px-lg-4">

            <!-- ENCABEZADO Y ACCIONES UNIFICADAS (ESTILO GESTIÓN DE USUARIOS) -->
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <div class="admin-header m-0 p-0 border-0 bg-transparent d-flex flex-row justify-content-between align-items-center flex-wrap gap-3">
                    
                    <!-- BOTÓN VOLVER Y TÍTULO -->
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/AdminServlet" class="btn-volver-admin shadow-2xs d-inline-flex align-items-center gap-2 text-decoration-none px-3 py-2 rounded-pill border bg-white">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                            </svg>
                            Volver al panel
                        </a>
                        <div>
                            <span class="admin-eyebrow text-uppercase text-secondary small fw-semibold">ADMINISTRACIÓN</span>
                            <h1 class="admin-title h3 mb-0 fw-bold">Gestión de producción</h1>
                            <p class="admin-description text-secondary small mb-0">Consulta y administra las órdenes de producción de Punto Cajas.</p>
                        </div>
                    </div>

                    <!-- ACCIONES DE BÚSQUEDA Y FILTRADO -->
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        
                        <!-- BUSCAR POR NÚMERO DE ORDEN -->
                        <form method="get" action="${pageContext.request.contextPath}/GestionarProduccionServlet" class="d-flex">
                            <input type="hidden" name="accion" value="buscar">
                            <div class="input-group input-group-sm shadow-2xs" style="max-width: 250px;">
                                <input type="text" class="form-control border-end-0 rounded-start-pill ps-3" id="numeroOrden" name="numeroOrden" value="${numeroBuscado}" placeholder="Nro. de orden..." required>
                                <button type="submit" class="btn btn-punto-primario rounded-end-pill px-3 text-white">Buscar</button>
                            </div>
                        </form>

                        <!-- FILTRAR POR ESTADO -->
                        <form method="get" action="${pageContext.request.contextPath}/GestionarProduccionServlet" class="d-flex">
                            <input type="hidden" name="accion" value="filtrar">
                            <div class="input-group input-group-sm shadow-2xs" style="max-width: 240px;">
                                <select class="form-select border-end-0 rounded-start-pill ps-3" id="estado" name="estado">
                                    <option value="TODOS" ${empty estadoSeleccionado || estadoSeleccionado == 'TODOS' ? 'selected' : ''}>Todos</option>
                                    <option value="PENDIENTE" ${estadoSeleccionado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                    <option value="EN_PRODUCCION" ${estadoSeleccionado == 'EN_PRODUCCION' ? 'selected' : ''}>En producción</option>
                                    <option value="FINALIZADA" ${estadoSeleccionado == 'FINALIZADA' ? 'selected' : ''}>Finalizada</option>
                                </select>
                                <button type="submit" class="btn btn-punto-primario rounded-end-pill px-3 text-white">Filtrar</button>
                            </div>
                        </form>

                        <!-- MOSTRAR TODAS -->
                        <a href="${pageContext.request.contextPath}/GestionarProduccionServlet?accion=listar" class="btn btn-outline-secondary btn-sm rounded-pill px-3 py-2">
                            Mostrar todas
                        </a>

                    </div>
                </div>
            </div>

            <!-- =================================================
                 RESULTADO DE BÚSQUEDA
                 ================================================= -->
            <c:if test="${not empty numeroBuscado}">
                <div class="mb-4">
                    <div class="subtitulo-seccion fw-bold mb-2">Resultado de búsqueda</div>
                    <c:choose>
                        <c:when test="${not empty ordenBuscada}">
                            <div class="card border-0 shadow-sm rounded-4 p-4">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <span class="text-uppercase text-secondary small fw-semibold">ORDEN ENCONTRADA</span>
                                        <h2 class="h4 mb-1 fw-bold">${ordenBuscada.numeroOrden}</h2>
                                        <p class="mb-1 text-secondary">Cotización: <strong class="text-dark">#${ordenBuscada.idCotizacion}</strong></p>
                                        <p class="mb-0 text-secondary">Estado: <strong class="text-dark">${ordenBuscada.estado}</strong></p>
                                    </div>
                                    <div class="col-md-4 text-md-end mt-3 mt-md-0">
                                        <a href="${pageContext.request.contextPath}/GestionarProduccionServlet?accion=ver&id=${ordenBuscada.idOrdenProduccion}" class="btn btn-punto-primario btn-sm rounded-pill px-4 text-white">
                                            Ver detalles
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning border-0 rounded-4 shadow-sm">
                                No se encontró una orden con el número: <strong>${numeroBuscado}</strong>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <!-- =================================================
                 DETALLE DE LA ORDEN (CUANDO SE SELECCIONA UNA)
                 ================================================= -->
            <c:if test="${not empty orden}">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-5">
                    <div class="subtitulo-seccion fw-bold mb-3">Detalle de la orden</div>
                    
                    <div class="row g-4 mb-4">
                        <div class="col-md-6">
                            <span class="text-secondary small">Número de orden</span>
                            <h2 class="h4 fw-bold mb-0">${orden.numeroOrden}</h2>
                        </div>
                        <div class="col-md-3">
                            <span class="text-secondary small">Cotización</span>
                            <div class="fw-medium">#${orden.idCotizacion}</div>
                        </div>
                        <div class="col-md-3">
                            <span class="text-secondary small">Versión</span>
                            <div class="fw-medium">#${orden.idVersion}</div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Estado</span>
                            <div>
                                <span class="badge bg-secondary text-white">${orden.estado}</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Posición en turno</span>
                            <div class="fw-medium">
                                <c:choose>
                                    <c:when test="${not empty orden.posicionTurno}">${orden.posicionTurno}</c:when>
                                    <c:otherwise><span class="text-muted">Sin asignar</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Fecha de creación</span>
                            <div class="fw-medium">${orden.fechaCreacion}</div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Fecha de inicio</span>
                            <div class="fw-medium">
                                <c:choose>
                                    <c:when test="${not empty orden.fechaInicio}">${orden.fechaInicio}</c:when>
                                    <c:otherwise><span class="text-muted">Sin definir</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Fecha de compromiso</span>
                            <div class="fw-medium">
                                <c:choose>
                                    <c:when test="${not empty orden.fechaCompromiso}">${orden.fechaCompromiso}</c:when>
                                    <c:otherwise><span class="text-muted">Sin definir</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <span class="text-secondary small">Fecha de finalización</span>
                            <div class="fw-medium">
                                <c:choose>
                                    <c:when test="${not empty orden.fechaFinalizacion}">${orden.fechaFinalizacion}</c:when>
                                    <c:otherwise><span class="text-muted">Pendiente</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <hr class="text-muted opacity-25">

                    <!-- ACCIÓN: CAMBIAR ESTADO -->
                    <div class="mb-4">
                        <h5 class="fw-bold fs-6">Estado de producción</h5>
                        <p class="text-secondary small">Actualice el estado de la orden según su avance en producción.</p>
                        <form method="post" action="${pageContext.request.contextPath}/GestionarProduccionServlet">
                            <input type="hidden" name="accion" value="cambiarEstado">
                            <input type="hidden" name="idOrdenProduccion" value="${orden.idOrdenProduccion}">
                            <div class="row g-3 align-items-end">
                                <div class="col-md-8">
                                    <label for="estadoOrden" class="form-label small">Estado</label>
                                    <select class="form-select rounded-3" id="estadoOrden" name="estado" required>
                                        <option value="PENDIENTE" ${orden.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                        <option value="EN_PRODUCCION" ${orden.estado == 'EN_PRODUCCION' ? 'selected' : ''}>En producción</option>
                                        <option value="FINALIZADA" ${orden.estado == 'FINALIZADA' ? 'selected' : ''}>Finalizada</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 text-white w-100">Actualizar estado</button>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- ACCIÓN: FECHA FINALIZACIÓN -->
                    <div class="mb-4">
                        <h5 class="fw-bold fs-6">Fecha de finalización</h5>
                        <p class="text-secondary small">Registre la fecha y hora en que terminó la producción.</p>
                        <form method="post" action="${pageContext.request.contextPath}/GestionarProduccionServlet">
                            <input type="hidden" name="accion" value="actualizarFinalizacion">
                            <input type="hidden" name="idOrdenProduccion" value="${orden.idOrdenProduccion}">
                            <div class="row g-3 align-items-end">
                                <div class="col-md-8">
                                    <label for="fechaFinalizacion" class="form-label small">Fecha y hora</label>
                                    <input type="datetime-local" class="form-control rounded-3" id="fechaFinalizacion" name="fechaFinalizacion" required>
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 text-white w-100">Guardar fecha</button>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- ACCIÓN: OBSERVACIÓN -->
                    <div class="mb-4">
                        <h5 class="fw-bold fs-6">Observación</h5>
                        <form method="post" action="${pageContext.request.contextPath}/GestionarProduccionServlet">
                            <input type="hidden" name="accion" value="actualizarObservacion">
                            <input type="hidden" name="idOrdenProduccion" value="${orden.idOrdenProduccion}">
                            <div class="mb-3">
                                <label for="observacion" class="form-label small">Observación de producción</label>
                                <textarea class="form-control rounded-3" id="observacion" name="observacion" rows="3" placeholder="Escriba una observación...">${orden.observacion}</textarea>
                            </div>
                            <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 text-white">Guardar observación</button>
                        </form>
                    </div>

                    <!-- VOLVER -->
                    <div>
                        <a href="${pageContext.request.contextPath}/GestionarProduccionServlet?accion=listar" class="btn btn-outline-secondary btn-sm rounded-pill px-4">
                            Volver a órdenes
                        </a>
                    </div>
                </div>
            </c:if>

            <!-- =================================================
                 LISTADO DE ÓRDENES
                 ================================================= -->
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
                <div class="table-responsive p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light text-uppercase fs-7">
                            <tr>
                                <th class="py-3 ps-4">Turno</th>
                                <th class="py-3">Orden</th>
                                <th class="py-3">Cotización</th>
                                <th class="py-3">Creación</th>
                                <th class="py-3">Compromiso</th>
                                <th class="py-3">Estado</th>
                                <th class="py-3 text-center pe-4">Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ordenItem" items="${ordenes}">
                                <tr>
                                    <!-- TURNO -->
                                    <td class="ps-4 py-3">
                                        <c:choose>
                                            <c:when test="${not empty ordenItem.posicionTurno}">
                                                <span class="badge bg-light text-dark border px-2 py-1">${ordenItem.posicionTurno}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-secondary">—</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <!-- ORDEN -->
                                    <td class="fw-medium py-3">${ordenItem.numeroOrden}</td>

                                    <!-- COTIZACIÓN -->
                                    <td class="py-3">#${ordenItem.idCotizacion}</td>

                                    <!-- CREACIÓN -->
                                    <td class="py-3">${ordenItem.fechaCreacion}</td>

                                    <!-- COMPROMISO -->
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${not empty ordenItem.fechaCompromiso}">${ordenItem.fechaCompromiso}</c:when>
                                            <c:otherwise><span class="text-secondary small">Sin definir</span></c:otherwise>
                                        </c:choose>
                                    </td>

                                    <!-- ESTADO -->
                                    <td class="py-3">
                                        <span class="badge bg-warning text-dark">${ordenItem.estado}</span>
                                    </td>

                                    <!-- VER DETALLES -->
                                    <td class="text-center pe-4 py-3">
                                        <a href="${pageContext.request.contextPath}/GestionarProduccionServlet?accion=ver&id=${ordenItem.idOrdenProduccion}" class="btn btn-outline-primary btn-sm rounded-pill px-3" title="Ver detalles de la orden">
                                            Ver
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty ordenes}">
                                <tr>
                                    <td colspan="7" class="text-center text-secondary py-5">No hay órdenes de producción registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </main>

    <!-- =========================================================
         FOOTER
         ========================================================= -->
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
    <script src="${pageContext.request.contextPath}/Vista/javaScript/gestionar-produccion.js"></script>

</body>

</html>