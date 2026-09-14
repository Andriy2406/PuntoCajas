<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Punto Cajas | Mis cotizaciones</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/Vista/CSS/MisCotizaciones.css" rel="stylesheet">
    </head>

    <body data-cotizacion-seleccionada="${cotizacionSeleccionada}">

        <!-- NAVBAR -->
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
                            <a class="nav-link" href="${pageContext.request.contextPath}/InicioServlet">Inicio</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/CotizacionServlet">Cotizar</a>
                        </li>
                        <li class="nav-item">
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuarioActivo}">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/MisCotizacionesServlet">Historial</a>
                                </c:when>
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
                                    <a class="nav-link" href="${pageContext.request.contextPath}/PerfilServlet">Mi perfil</a>
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

        <!-- HERO -->
        <div class="hero-punto py-4">
            <div class="container">
                <h1 class="h3 mb-1 fw-bold">Mis cotizaciones y pedidos</h1>
                <p class="text-secondary mb-0">Consulta, organiza y administra el historial de tus cotizaciones y el estado de tus pedidos.</p>
                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/InicioServlet" class="btn-volver-admin shadow-2xs d-inline-flex align-items-center gap-2 text-decoration-none px-3 py-2 rounded-pill border bg-white text-dark">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        Volver al inicio
                    </a>
                </div>
            </div>
        </div>
        
        <!-- CONTENIDO PRINCIPAL -->
        <main class="mis-cotizaciones-page py-5">
            <div class="container">

                <!-- MENSAJES DE ALERTA -->
                <c:if test="${not empty sessionScope.mensajeAlerta}">
                    <div class="alert alert-${sessionScope.tipoAlerta}">
                        ${sessionScope.mensajeAlerta}
                    </div>
                    <c:remove var="mensajeAlerta" scope="session"/>
                    <c:remove var="tipoAlerta" scope="session"/>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <div class="row g-4">

                    <!-- COLUMNA IZQUIERDA: COTIZACIONES -->
                    <div class="col-lg-6">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h2 class="h4 mb-0 fw-bold">Cotizaciones</h2>
                            <span class="badge bg-secondary" id="contadorMisCotizaciones">${not empty listaCotizaciones ? listaCotizaciones.size() : 0}</span>
                        </div>
                        
                        <p class="text-secondary small mb-3">Sigue el estado de fabricación y entrega de tus cotizaciones.</p>

                        <!-- CONTROLES DE BÚSQUEDA -->
                        <c:if test="${not empty listaCotizaciones}">
                            <div class="card shadow-sm border-0 mb-3 cotizaciones-controles">
                                <div class="card-body p-3">
                                    <div class="row g-2">
                                        <div class="col-md-7">
                                            <input type="search" class="form-control form-control-sm" id="buscarMisCotizaciones" placeholder="Buscar cotización..." autocomplete="off">
                                        </div>
                                        <div class="col-md-5">
                                            <select class="form-select form-select-sm" id="filtroMisCotizaciones">
                                                <option value="">Todos los estados</option>
                                                <option value="NUEVA">Nueva</option>
                                                <option value="EN_REVISION">En revisión</option>
                                                <option value="CORREGIDA">Corregida</option>
                                                <option value="COTIZADA">Cotizada</option>
                                                <option value="ENVIADA">Valorada por empresa</option>
                                                <option value="ANTICIPO_PENDIENTE">Anticipo pendiente</option>
                                                <option value="ORDEN_PRODUCCION">En producción</option>
                                                <option value="RECHAZADA">Rechazada</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div id="listaMisCotizaciones">
                            <c:forEach var="q" items="${listaCotizaciones}">
                                <div id="cotizacion-${q.idCotizacion}" class="card shadow-sm cotizacion-card mb-3 ${cotizacionSeleccionada == q.idCotizacion ? 'seleccionada' : ''} filaCotizacionCliente" data-estado="${q.codigoEstado}">
                                    <div class="cotizacion-resumen p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <h3 class="h6 mb-1 fw-bold">Cotización #${q.idCotizacion}</h3>
                                                <div class="small text-secondary">Versión ${q.versionActual} &middot; ${q.fecha}</div>
                                            </div>
                                            <span class="badge bg-success estado-badge">
                                                <c:choose>
                                                    <c:when test="${q.codigoEstado == 'ENVIADA'}">Valorada por empresa</c:when>
                                                    <c:otherwise>${q.nombreEstado}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="mt-2 text-end">
                                            <button type="button" class="btn btn-punto-primario btn-sm btn-ver-detalles" data-bs-toggle="collapse" data-bs-target="#detalleCotizacion-${q.idCotizacion}" aria-expanded="${cotizacionSeleccionada == q.idCotizacion ? 'true' : 'false'}">
                                                Ver detalles ▼
                                            </button>
                                        </div>
                                    </div>

                                    <div id="detalleCotizacion-${q.idCotizacion}" class="collapse ${cotizacionSeleccionada == q.idCotizacion ? 'show' : ''}">
                                        <div class="card-body p-3 border-top bg-light">
                                            
                                            <!-- Pestañas de Navegación -->
                                            <ul class="nav nav-tabs nav-sm mb-3" id="tab-${q.idCotizacion}" role="tablist">
                                                <li class="nav-item" role="presentation">
                                                    <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#ver-${q.idCotizacion}" type="button" role="tab">Detalles Completos</button>
                                                </li>
                                                <c:if test="${q.codigoEstado == 'NUEVA' || q.codigoEstado == 'EN_REVISION' || q.codigoEstado == 'CORRECCION_SOLICITADA' || q.codigoEstado == 'CORREGIDA'}">
                                                    <li class="nav-item" role="presentation">
                                                        <button class="nav-link text-warning" data-bs-toggle="tab" data-bs-target="#modificar-${q.idCotizacion}" type="button" role="tab">Modificar Cotización</button>
                                                    </li>
                                                </c:if>
                                            </ul>

                                            <div class="tab-content">
                                                <!-- PESTAÑA 1: VER DETALLES -->
                                                <div class="tab-pane fade show active" id="ver-${q.idCotizacion}" role="tabpanel">
                                                    <div class="row small mb-2">
                                                        <div class="col-md-6 mb-2"><strong>Cantidad:</strong> ${q.version.cantidad} unidades</div>
                                                        <div class="col-md-6 mb-2"><strong>Tipo de cartón:</strong> ${q.version.tipoCarton}</div>
                                                        <div class="col-md-6 mb-2"><strong>Dimensiones (cm):</strong> ${q.version.alto} (Alto) × ${q.version.largo} (Largo) × ${q.version.ancho} (Ancho)</div>
                                                        <div class="col-md-6 mb-2"><strong>Uso:</strong> ${q.version.descripcionUsoCaja}</div>
                                                        
                                                        <c:if test="${not empty q.version.acabado}">
                                                            <div class="col-md-12 mb-3">
                                                                <strong>Diseño/Acabado adjunto:</strong><br>
                                                                <a href="${pageContext.request.contextPath}/ImagenServlet?archivo=${q.version.acabado}" target="_blank" title="Ver imagen en tamaño completo">
                                                                    <img src="${pageContext.request.contextPath}/ImagenServlet?archivo=${q.version.acabado}" alt="Acabado adjunto por el usuario" class="img-thumbnail mt-1 shadow-sm" style="max-height: 120px; object-fit: contain;">
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                        
                                                        <c:if test="${q.version.total > 0}">
                                                            <div class="col-md-4 mb-1"><strong>Valor Unitario:</strong> $${q.version.valorUnitario}</div>
                                                            <div class="col-md-4 mb-1"><strong>IVA:</strong> $${q.version.valorIva}</div>
                                                            <div class="col-md-4 mb-1"><strong>Total:</strong> <b>$${q.version.total}</b></div>
                                                            <div class="col-md-12 mt-2 mb-2 text-secondary"><strong>Tiempo estimado de elaboración:</strong> ${q.version.diasElaboracion} días hábiles</div>
                                                        </c:if>
                                                    </div>

                                                    <c:if test="${q.codigoEstado == 'ENVIADA'}">
                                                        <div class="alert alert-info mt-3 p-3">
                                                            <div class="d-flex flex-column flex-md-row justify-content-between align-items-center gap-2">
                                                                <div class="text-center text-md-start">
                                                                    <strong>¡La empresa ha valorado tu cotización!</strong><br>
                                                                    <span class="small">Revisa los costos y el tiempo. ¿Deseas continuar?</span>
                                                                </div>
                                                                <div class="d-flex gap-2">
                                                                    <form action="${pageContext.request.contextPath}/MisCotizacionesServlet" method="post" class="m-0">
                                                                        <input type="hidden" name="accion" value="aceptar">
                                                                        <input type="hidden" name="id" value="${q.idCotizacion}">
                                                                        <button type="submit" class="btn btn-success btn-sm px-3 shadow-sm">Aceptar</button>
                                                                    </form>
                                                                    <button type="button" class="btn btn-outline-danger btn-sm px-3 bg-white" data-bs-toggle="modal" data-bs-target="#modalRechazar-${q.idCotizacion}">
                                                                        Rechazar
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>

                                                    <c:if test="${q.codigoEstado == 'ANTICIPO_PENDIENTE'}">
                                                        <div class="alert alert-warning p-2 small mt-3 mb-2 text-center">
                                                            Anticipo pendiente: <strong>$ ${q.anticipo}</strong>
                                                        </div>
                                                        <a href="${pageContext.request.contextPath}/MercadoPagoPagoServlet?id=${q.idCotizacion}" class="btn btn-punto-primario btn-sm w-100">Pagar con Mercado Pago</a>
                                                    </c:if>
                                                </div>

                                                <!-- PESTAÑA 2: MODIFICACIÓN -->
                                                <c:if test="${q.codigoEstado == 'NUEVA' || q.codigoEstado == 'EN_REVISION' || q.codigoEstado == 'CORRECCION_SOLICITADA' || q.codigoEstado == 'CORREGIDA'}">
                                                    <div class="tab-pane fade" id="modificar-${q.idCotizacion}" role="tabpanel">
                                                        <form action="${pageContext.request.contextPath}/MisCotizacionesServlet" method="post" enctype="multipart/form-data">
                                                            <input type="hidden" name="accion" value="corregir">
                                                            <input type="hidden" name="id" value="${q.idCotizacion}">
                                                            
                                                            <div class="row g-2 small">
                                                                <div class="col-md-6">
                                                                    <label class="form-label mb-0">Cantidad</label>
                                                                    <input type="number" name="cantidad" class="form-control form-control-sm" value="${q.version.cantidad}" required min="1">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label mb-0">Tipo de Cartón</label>
                                                                    <select name="tipoCarton" class="form-select form-select-sm" required>
                                                                        <option value="Corrugado" ${q.version.tipoCarton == 'Corrugado' ? 'selected' : ''}>Corrugado</option>
                                                                        <option value="Microcorrugado" ${q.version.tipoCarton == 'Microcorrugado' ? 'selected' : ''}>Microcorrugado</option>
                                                                        <option value="Kraft" ${q.version.tipoCarton == 'Kraft' ? 'selected' : ''}>Kraft</option>
                                                                    </select>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label mb-0">Alto (cm)</label>
                                                                    <input type="number" step="0.01" name="alto" class="form-control form-control-sm" value="${q.version.alto}" required>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label mb-0">Largo (cm)</label>
                                                                    <input type="number" step="0.01" name="largo" class="form-control form-control-sm" value="${q.version.largo}" required>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label mb-0">Ancho (cm)</label>
                                                                    <input type="number" step="0.01" name="ancho" class="form-control form-control-sm" value="${q.version.ancho}" required>
                                                                </div>
                                                                <div class="col-md-12">
                                                                    <label class="form-label mb-0">Uso de la caja</label>
                                                                    <input type="text" name="descripcionUsoCaja" class="form-control form-control-sm" value="${q.version.descripcionUsoCaja}" required>
                                                                </div>
                                                                <div class="col-md-12">
                                                                    <label class="form-label mb-0">Acabado (opcional)</label>
                                                                    <input type="file" name="acabado" class="form-control form-control-sm" accept="image/jpeg, image/png, image/webp">
                                                                </div>
                                                                <div class="col-md-12">
                                                                    <label class="form-label mb-0">Motivo de la modificación</label>
                                                                    <textarea name="motivo" class="form-control form-control-sm" rows="2" required placeholder="Explica brevemente los cambios..."></textarea>
                                                                </div>
                                                                <div class="col-12 text-end mt-2">
                                                                    <button type="submit" class="btn btn-punto-primario btn-sm">Guardar Cambios</button>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- MODAL RECHAZAR -->
                                <c:if test="${q.codigoEstado == 'ENVIADA'}">
                                    <div class="modal fade" id="modalRechazar-${q.idCotizacion}" tabindex="-1" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                                                <form action="${pageContext.request.contextPath}/MisCotizacionesServlet" method="post">
                                                    <input type="hidden" name="accion" value="rechazar">
                                                    <input type="hidden" name="id" value="${q.idCotizacion}">
                                                    
                                                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                                                        <h5 class="modal-title fw-bold">Rechazar Cotización #${q.idCotizacion}</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    
                                                    <div class="modal-body p-4">
                                                        <p class="mb-3 text-secondary">¿Por qué decidiste no continuar con esta cotización?</p>
                                                        <div class="mb-3">
                                                            <label class="form-label fw-semibold">Motivo del rechazo <span class="text-danger">*</span></label>
                                                            <textarea name="motivoRechazo" class="form-control rounded-3" rows="3" required placeholder="Ej: Precio alto..."></textarea>
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="modal-footer bg-light px-4 py-3 border-top">
                                                        <button type="button" class="btn btn-secondary btn-sm rounded-pill px-4" data-bs-dismiss="modal">Cancelar</button>
                                                        <button type="submit" class="btn btn-danger btn-sm rounded-pill px-4 text-white">Confirmar Rechazo</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>

                            <div id="sinResultadosMisCotizaciones" style="display: none;" class="alert alert-light border text-center py-4 text-secondary">
                                No se encontraron cotizaciones con los filtros seleccionados.
                            </div>

                            <c:if test="${empty listaCotizaciones}">
                                <div class="alert alert-light border text-center py-4 text-secondary">
                                    Todavía no tienes cotizaciones.
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- COLUMNA DERECHA: PEDIDOS -->
                    <div class="col-lg-6" id="mis-pedidos">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h2 class="h4 mb-0 fw-bold">Mis pedidos</h2>
                            <span class="badge bg-primary">${not empty listaPedidosCliente ? listaPedidosCliente.size() : 0}</span>
                        </div>
                        
                        <p class="text-secondary small mb-3">Sigue el estado de fabricación y entrega de tus pedidos.</p>

                        <c:if test="${empty listaPedidosCliente}">
                            <div class="alert alert-light border text-center py-4 text-secondary">
                                Todavía no tienes pedidos generados.
                            </div>
                        </c:if>

                        <c:forEach var="pedido" items="${listaPedidosCliente}">
                            <div class="card shadow-sm p-3 mb-3 tarjeta-pedido-historial">
                                <div class="d-flex justify-content-between align-items-start gap-2">
                                    <div>
                                        <h3 class="h6 mb-1 fw-bold">Pedido #${pedido.idPedido}</h3>
                                        <p class="text-secondary small mb-1">Dirección: ${pedido.direccionEnvio}</p>
                                        <p class="text-secondary small mb-0">Fecha: ${pedido.fecha} &middot; Total: <strong>$${pedido.total}</strong></p>
                                    </div>
                                    <span class="badge bg-primary">${pedido.estadoPedido}</span>
                                </div>

                                <c:if test="${not empty pedido.nombreConductor}">
                                    <p class="mt-2 mb-0 text-secondary small">
                                        <em>Conductor asignado:</em> ${pedido.nombreConductor}
                                    </p>
                                </c:if>

                                <c:if test="${not empty historialPedidosCliente[pedido.idPedido]}">
                                    <div class="d-flex flex-wrap gap-1 mt-3">
                                        <c:forEach var="evento" items="${historialPedidosCliente[pedido.idPedido]}" varStatus="st">
                                            <span class="badge bg-light text-dark border ${st.last ? 'border-primary fw-bold' : ''}" title="${evento.fechaEvento}">
                                                ${evento.estadoNuevo}
                                            </span>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>

                </div>

            </div>
        </main>

        <!-- FOOTER -->
        <footer class="footer-punto text-center py-4">
            <div class="container">
                <span>
                    &copy; 2026 Punto Cajas. Todos los derechos reservados. |
                    <a href="#">Política de privacidad</a> |
                    <a href="#">Términos y condiciones</a>
                </span>
            </div>
        </footer>

        <!-- SCRIPTS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Vista/javaScript/MisCotizaciones.js"></script>
    </body>
</html>