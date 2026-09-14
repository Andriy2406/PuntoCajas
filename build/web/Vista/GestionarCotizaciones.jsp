<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Punto Cajas | Gestionar cotizaciones</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/GestionarCotizaciones.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
</head>

<body class="cotizaciones-page d-flex flex-column min-vh-100">

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
                            <c:when test="${not empty sessionScope.usuarioActivo}">
                                <a class="nav-link" href="${pageContext.request.contextPath}/MisCotizacionesServlet">Historial</a>
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

<main class="py-5 flex-grow-1">
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

        <c:if test="${not empty error}">
            <div class="alert alert-danger shadow-sm border-0 rounded-4 mb-4">
                ${error}
            </div>
        </c:if>

        <!-- Tarjeta de cabecera -->
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
                        <h1 class="admin-title">Gestionar cotizaciones</h1>
                        <p class="admin-description mb-0">Revisa la solicitud técnica y define precio y tiempo de elaboración.</p>
                    </div>
                </div>
                
                <div class="d-flex align-items-center gap-3 flex-wrap">
                    <div style="width: 240px;">
                        <input
                            id="buscarCotizacion"
                            class="form-control form-control-sm rounded-pill ps-3"
                            type="search"
                            placeholder="Buscar por número, cliente...">
                    </div>

                    <div style="width: 170px;">
                        <select id="filtroEstado" class="form-select form-select-sm rounded-pill px-3">
                            <option value="">Todos los estados</option>
                            <option value="NUEVA">Nueva</option>
                            <option value="CORREGIDA">Corregida</option>
                            <option value="COTIZADA">Cotizada</option>
                            <option value="ENVIADA">Enviada</option>
                            <option value="ACEPTADA">Aceptada</option>
                            <option value="ANTICIPO_PENDIENTE">Anticipo pendiente</option>
                            <option value="ORDEN_PRODUCCION">Orden de producción</option>
                            <option value="RECHAZADA">Rechazada</option>
                        </select>
                    </div>

                    <button class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 shadow-sm text-white" type="button" data-bs-toggle="modal" data-bs-target="#nuevaCotizacion">
                        + Nueva cotización
                    </button>
                </div>
            </div>
        </div>

        <!-- Tabla principal ampliada y con aire -->
        <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
            <div class="table-responsive p-0">
                <table id="tablaCotizaciones" class="table table-hover cotizaciones-table align-middle mb-0">
                    <thead class="table-light text-uppercase fs-7">
                    <tr>
                        <th class="py-3 ps-4">Cotización</th>
                        <th class="py-3">Cliente</th>
                        <th class="py-3">Versión</th>
                        <th class="py-3" style="min-width: 250px;">Solicitud técnica</th>
                        <th class="py-3" style="min-width: 200px;">Comercial</th>
                        <th class="py-3">Estado</th>
                        <th class="py-3 text-center pe-4" style="min-width: 180px;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="q" items="${listaCotizaciones}">
                        <tr class="filaCotizacion" data-estado="${q.codigoEstado}">
                            <td class="ps-4 py-4">
                                <strong>#${q.idCotizacion}</strong>
                                <br>
                                <small class="text-secondary">${q.fecha}</small>
                            </td>
                            <td class="py-4">
                                <strong>${q.nombreCliente}</strong>
                                <br>
                                <span class="cliente-correo text-secondary">${q.correoCliente}</span>
                            </td>
                            <td class="py-4">
                                <span class="badge bg-light text-dark border fw-normal">v${q.versionActual}</span>
                                <br>
                                <small class="text-secondary">${q.version.estadoVersion}</small>
                            </td>
                            <td class="py-4">
                                <div class="mb-1"><strong>${q.version.cantidad} unidades</strong></div>
                                <div class="text-secondary small mb-1">${q.version.alto} × ${q.version.largo} × ${q.version.ancho} cm</div>
                                <div class="mb-1"><strong>Tipo de cartón:</strong> ${q.version.tipoCarton}</div>
                                <c:choose>
                                    <c:when test="${not empty q.version.acabado}">
                                        <div class="mt-2">
                                            <strong>Imagen de impresión:</strong>
                                            <br>
                                            <img src="${pageContext.request.contextPath}/ImagenServlet?archivo=${q.version.acabado}"
                                                 alt="Imagen de impresión"
                                                 class="img-fluid rounded border mt-2 imagen-acabado-admin" style="max-width: 120px; height: auto;">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="small text-secondary mt-1">Sin impresión.</div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="cotizaciones-comercial py-4">
                                <div class="mb-1">
                                    Unitario: <strong>$ <fmt:formatNumber value="${q.version.valorUnitario}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                                </div>
                                <div class="mb-1 text-secondary">
                                    Subtotal: $ <fmt:formatNumber value="${q.version.subtotal}" minFractionDigits="2" maxFractionDigits="2"/>
                                </div>
                                <div class="mb-1 text-secondary">
                                    IVA 19%: $ <fmt:formatNumber value="${q.version.valorIva}" minFractionDigits="2" maxFractionDigits="2"/>
                                </div>
                                <div class="total mb-1">
                                    <strong>Total: $ <fmt:formatNumber value="${q.version.total}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                                </div>
                                <div class="anticipo text-success mb-1">
                                    Anticipo 50%: $ <fmt:formatNumber value="${q.anticipo}" minFractionDigits="2" maxFractionDigits="2"/>
                                </div>
                                <div class="detalle-secundario text-secondary">
                                    Elaboración: <strong>${q.version.diasElaboracion} días hábiles</strong>
                                </div>
                            </td>
                            <td class="py-4">
                                <span class="badge bg-primary text-white">${q.nombreEstado}</span>
                                <c:if test="${not empty q.motivoCorreccion}">
                                    <div class="small text-warning mt-1">
                                        <strong>Corrección:</strong> ${q.motivoCorreccion}
                                    </div>
                                </c:if>
                                <c:if test="${not empty q.motivoRechazo}">
                                    <div class="small text-danger mt-1">
                                        <strong>Rechazo:</strong> ${q.motivoRechazo}
                                    </div>
                                </c:if>
                            </td>
                            <td class="cotizaciones-acciones text-center pe-4 py-4">
                                <div class="d-grid gap-2">
                                    <button
                                        class="btn btn-sm btn-outline-primary rounded-pill px-3 py-2 mb-1"
                                        type="button"
                                        data-bs-toggle="modal"
                                        data-bs-target="#detalleCotizacion${q.idCotizacion}">
                                        Ver completa
                                    </button>

                                    <!-- BOTÓN PARA DEFINIR O EDITAR PRECIO (INCLUYE ESTADO COTIZADA) -->
                                    <c:if test="${q.codigoEstado == 'NUEVA' || q.codigoEstado == 'EN_REVISION' || q.codigoEstado == 'CORREGIDA' || q.codigoEstado == 'COTIZANDO' || q.codigoEstado == 'BORRADOR' || q.codigoEstado == 'COTIZADA' || q.codigoEstado == 'ENVIADA' || q.codigoEstado == 'ANTICIPO_PENDIENTE'}">
                                        <button
                                            class="btn btn-sm ${q.codigoEstado != 'NUEVA' && q.codigoEstado != 'EN_REVISION' && q.codigoEstado != 'CORREGIDA' && q.codigoEstado != 'COTIZANDO' && q.codigoEstado != 'BORRADOR' ? 'btn-warning text-dark fw-semibold' : 'btn-primary text-white'} rounded-pill px-3 py-2 mb-1"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#cotizar${q.idCotizacion}">
                                            ${q.codigoEstado != 'NUEVA' && q.codigoEstado != 'EN_REVISION' && q.codigoEstado != 'CORREGIDA' && q.codigoEstado != 'COTIZANDO' && q.codigoEstado != 'BORRADOR' ? '✏️ Editar precio' : 'Definir precio'}
                                        </button>
                                    </c:if>

                                    <c:if test="${q.codigoEstado == 'COTIZADA'}">
                                        <form method="post" action="${pageContext.request.contextPath}/GestionarCotizacionesServlet" class="m-0">
                                            <input type="hidden" name="accion" value="enviarCliente">
                                            <input type="hidden" name="id" value="${q.idCotizacion}">
                                            <button class="btn btn-sm btn-success w-100 rounded-pill px-3 py-2" type="submit">
                                                Enviar cliente
                                            </button>
                                        </form>
                                    </c:if>

                                    <c:if test="${q.codigoEstado == 'ACEPTADA'}">
                                        <div class="small text-success fw-semibold py-1">
                                            Aceptada por cliente
                                        </div>
                                    </c:if>

                                    <c:if test="${q.codigoEstado == 'ANTICIPO_PENDIENTE'}">
                                        <button
                                            class="btn btn-sm btn-success rounded-pill px-3 py-2"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#anticipo${q.idCotizacion}">
                                            Confirmar anticipo
                                        </button>
                                    </c:if>
                                </div>
                            </td>
                        </tr>

                        <!-- Modal de Detalle Completo -->
                        <div class="modal fade modal-detalle-cotizacion" id="detalleCotizacion${q.idCotizacion}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
                                <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                                        <div>
                                            <h4 class="modal-title fw-bold mb-1">Cotización #${q.idCotizacion}</h4>
                                            <div class="text-secondary small">Detalle completo de la solicitud técnica y comercial</div>
                                        </div>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body p-4">
                                        
                                        <!-- Resumen de Estado -->
                                        <div class="row g-3 mb-4">
                                            <div class="col-md-3"><div class="detalle-dato border p-3 rounded-3 bg-light"><span>Número</span><br><strong>#${q.idCotizacion}</strong></div></div>
                                            <div class="col-md-3"><div class="detalle-dato border p-3 rounded-3 bg-light"><span>Fecha</span><br><strong>${q.fecha}</strong></div></div>
                                            <div class="col-md-3"><div class="detalle-dato border p-3 rounded-3 bg-light"><span>Versión</span><br><strong>V${q.versionActual}</strong></div></div>
                                            <div class="col-md-3"><div class="detalle-dato border p-3 rounded-3 bg-light"><span>Estado</span><br><strong><span class="badge bg-primary">${q.nombreEstado}</span></strong></div></div>
                                        </div>

                                        <!-- Detalles Técnicos -->
                                        <div class="detalle-seccion mb-4">
                                            <div class="detalle-seccion-titulo fw-semibold mb-3 border-bottom pb-2">Detalles Técnicos (Solicitud del Cliente)</div>
                                            <div class="row small mb-2">
                                                <div class="col-md-4 mb-3"><strong>Cantidad solicitada:</strong> ${q.version.cantidad} unidades</div>
                                                <div class="col-md-4 mb-3"><strong>Tipo de cartón:</strong> ${q.version.tipoCarton}</div>
                                                <div class="col-md-4 mb-3"><strong>Dimensiones:</strong> ${q.version.alto} (Alto) × ${q.version.largo} (Largo) × ${q.version.ancho} (Ancho) cm</div>
                                                <div class="col-md-12 mb-3"><strong>Uso previsto:</strong> ${q.version.descripcionUsoCaja}</div>
                                                
                                                <c:if test="${not empty q.version.acabado}">
                                                    <div class="col-md-12 mb-2">
                                                        <strong>Diseño/Acabado adjunto:</strong><br>
                                                        <a href="${pageContext.request.contextPath}/ImagenServlet?archivo=${q.version.acabado}" target="_blank" title="Ver imagen completa">
                                                            <img src="${pageContext.request.contextPath}/ImagenServlet?archivo=${q.version.acabado}" alt="Acabado enviado por el cliente" class="img-thumbnail mt-2 shadow-sm" style="max-height: 200px; object-fit: contain;">
                                                        </a>
                                                    </div>
                                                </c:if>
                                                <c:if test="${empty q.version.acabado}">
                                                    <div class="col-md-12 mb-2 text-secondary">
                                                        <em>El cliente no adjuntó imagen de diseño o acabado.</em>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>

                                        <!-- Detalles Comerciales -->
                                        <c:if test="${q.version.total > 0}">
                                        <div class="detalle-seccion mb-4">
                                            <div class="detalle-seccion-titulo fw-semibold mb-3 border-bottom pb-2">Detalles Comerciales (Valoración)</div>
                                            <div class="row small mb-2">
                                                <div class="col-md-3 mb-3"><strong>Valor Unitario:</strong> $ <fmt:formatNumber value="${q.version.valorUnitario}" minFractionDigits="2" maxFractionDigits="2"/></div>
                                                <div class="col-md-3 mb-3"><strong>Subtotal:</strong> $ <fmt:formatNumber value="${q.version.subtotal}" minFractionDigits="2" maxFractionDigits="2"/></div>
                                                <div class="col-md-3 mb-3"><strong>IVA (19%):</strong> $ <fmt:formatNumber value="${q.version.valorIva}" minFractionDigits="2" maxFractionDigits="2"/></div>
                                                <div class="col-md-3 mb-3"><strong>Total:</strong> <b class="text-success">$ <fmt:formatNumber value="${q.version.total}" minFractionDigits="2" maxFractionDigits="2"/></b></div>
                                                
                                                <div class="col-md-6 mb-2"><strong>Anticipo Requerido (50%):</strong> $ <fmt:formatNumber value="${q.anticipo}" minFractionDigits="2" maxFractionDigits="2"/></div>
                                                <div class="col-md-6 mb-2"><strong>Tiempo de elaboración:</strong> ${q.version.diasElaboracion} días hábiles</div>
                                            </div>
                                        </div>
                                        </c:if>

                                        <!-- Observaciones e Historial -->
                                        <c:if test="${not empty q.motivoCorreccion or not empty q.motivoRechazo or not empty q.observacion}">
                                        <div class="detalle-seccion">
                                            <div class="detalle-seccion-titulo fw-semibold mb-3 border-bottom pb-2">Historial y Observaciones</div>
                                            
                                            <c:if test="${not empty q.motivoCorreccion}">
                                                <div class="alert alert-warning p-3 small mb-2">
                                                    <strong>Motivo de corrección (Solicitado por cliente):</strong><br> ${q.motivoCorreccion}
                                                </div>
                                            </c:if>
                                            
                                            <c:if test="${not empty q.motivoRechazo}">
                                                <div class="alert alert-danger p-3 small mb-2">
                                                    <strong>Motivo de rechazo (Cliente):</strong><br> ${q.motivoRechazo}
                                                </div>
                                            </c:if>
                                            
                                            <c:if test="${not empty q.observacion}">
                                                <div class="alert alert-info p-3 small mb-2">
                                                    <strong>Observación interna:</strong><br> ${q.observacion}
                                                </div>
                                            </c:if>
                                        </div>
                                        </c:if>

                                    </div>
                                    <div class="modal-footer bg-light px-4 py-3 border-top">
                                        <button type="button" class="btn btn-secondary btn-sm rounded-pill px-4" data-bs-dismiss="modal">Cerrar detalle</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- ==========================================
                             MODAL COTIZAR (CON LOS IDS QUE EL JS BUSCA)
                        =========================================== -->
                        <div class="modal fade modal-cotizacion" id="cotizar${q.idCotizacion}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog modal-lg modal-dialog-centered">
                                <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                                    <form method="post" action="${pageContext.request.contextPath}/GestionarCotizacionesServlet">
                                        <div class="modal-header bg-light px-4 py-3 border-bottom">
                                            <h5 class="modal-title fw-bold">Cotización comercial #${q.idCotizacion} · V${q.versionActual}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        
                                        <div class="modal-body p-4">
                                            <input type="hidden" name="accion" value="guardarCotizacion">
                                            <input type="hidden" name="id" value="${q.idCotizacion}">
                                            
                                            <!-- CAMPO DE CANTIDAD NECESARIO PARA EL JS (CON EL ID REQUERIDO) -->
                                            <input type="hidden" id="cantidad${q.idCotizacion}" value="${q.version.cantidad}">
                                            
                                            <div class="row">
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label">Tiempo estimado (días hábiles)</label>
                                                    <input class="form-control rounded-3" type="number" name="diasElaboracion" min="0" step="1" value="${q.version.diasElaboracion}" required>
                                                </div>
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label">Valor unitario</label>
                                                    <!-- CAMPO UNITARIO CON EL ID QUE EL JS LEE -->
                                                    <input class="form-control rounded-3" id="valorUnitario${q.idCotizacion}" name="valorUnitario" type="number" min="0" step="0.01" value="${q.version.valorUnitario}" required>
                                                </div>
                                            </div>

                                            <!-- RECUADRO DE CÁLCULOS EN VIVO (CON LOS IDS EXACTOS QUE EL JS ACTUALIZA) -->
                                            <div class="row g-2 mb-3 bg-light p-3 rounded-3 border">
                                                <div class="col-6 col-md-3">
                                                    <label class="form-label small text-secondary mb-0">Subtotal</label>
                                                    <input type="text" class="form-control form-control-sm bg-transparent border-0 fw-bold px-0 text-dark" id="subtotal${q.idCotizacion}" readonly>
                                                </div>
                                                <div class="col-6 col-md-3">
                                                    <label class="form-label small text-secondary mb-0">IVA (19%)</label>
                                                    <input type="text" class="form-control form-control-sm bg-transparent border-0 fw-bold px-0 text-dark" id="iva${q.idCotizacion}" readonly>
                                                </div>
                                                <div class="col-6 col-md-3">
                                                    <label class="form-label small text-secondary mb-0">Total</label>
                                                    <input type="text" class="form-control form-control-sm bg-transparent border-0 fw-bold text-primary px-0" id="total${q.idCotizacion}" readonly>
                                                </div>
                                                <div class="col-6 col-md-3">
                                                    <label class="form-label small text-secondary mb-0">Anticipo (50%)</label>
                                                    <input type="text" class="form-control form-control-sm bg-transparent border-0 fw-bold text-success px-0" id="anticipoCalculo${q.idCotizacion}" readonly>
                                                </div>
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label">Observación</label>
                                                <textarea class="form-control rounded-3" name="observacion" rows="2">${q.observacion}</textarea>
                                            </div>
                                        </div>
                                        
                                        <div class="modal-footer bg-light px-4 py-3 border-top">
                                            <button type="button" class="btn btn-secondary btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                                            <button class="btn btn-primary btn-sm rounded-pill px-4 text-white" type="submit">Guardar cotización</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Modal Anticipo -->
                        <div class="modal fade modal-cotizacion" id="anticipo${q.idCotizacion}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                                    <form method="post" action="${pageContext.request.contextPath}/GestionarCotizacionesServlet">
                                        <div class="modal-header bg-light px-4 py-3 border-bottom">
                                            <h5 class="modal-title fw-bold">Confirmar anticipo #${q.idCotizacion}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body p-4">
                                            <input type="hidden" name="accion" value="registrarAnticipo">
                                            <input type="hidden" name="id" value="${q.idCotizacion}">
                                            <p class="mb-3">Confirma que recibiste el 50% del total: <strong>$ <fmt:formatNumber value="${q.anticipo}" minFractionDigits="2" maxFractionDigits="2"/></strong>.</p>
                                            <div class="mb-3">
                                                <label class="form-label">Medio de pago</label>
                                                <input class="form-control rounded-3" name="medioPago" maxlength="45" required placeholder="Transferencia, etc.">
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Referencia</label>
                                                <input class="form-control rounded-3" name="referencia" maxlength="100" placeholder="Referencia externa opcional">
                                            </div>
                                        </div>
                                        <div class="modal-footer bg-light px-4 py-3 border-top">
                                            <button type="button" class="btn btn-secondary btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                                            <button class="btn btn-success btn-sm rounded-pill px-4 text-white" type="submit">Confirmar y crear producción</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </c:forEach>

                    </tbody>
                </table>
            </div>

            <div id="sinResultados" class="alert alert-light border text-center mt-3 mb-0" style="display: none;">
                No se encontraron cotizaciones con los filtros seleccionados.
            </div>
        </div>

    </div>
</main>

<!-- Modal Nueva Cotización -->
<div class="modal fade" id="nuevaCotizacion" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
            <form method="post" action="${pageContext.request.contextPath}/GestionarCotizacionesServlet">
                <input type="hidden" name="accion" value="crearAdmin">
                
                <div class="modal-header bg-light px-4 py-3 border-bottom">
                    <h5 class="modal-title fw-bold">Crear Nueva Cotización</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label">Seleccionar Cliente</label>
                        <select class="form-select rounded-3" name="idUsuario" required>
                            <option value="">Seleccione un cliente...</option>
                            <c:forEach var="u" items="${listaUsuarios}">
                                <c:if test="${u.idRol == 2}">
                                    <option value="${u.idUsuario}">${u.nombre} ${u.apellido} (${u.correo})</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Cantidad de unidades</label>
                            <input type="number" class="form-control rounded-3" name="cantidad" min="1" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Tipo de cartón</label>
                            <input type="text" class="form-control rounded-3" name="tipoCarton" required placeholder="Ej: Convencional, Corrugado">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Alto (cm)</label>
                            <input type="number" step="0.01" class="form-control rounded-3" name="alto" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Largo (cm)</label>
                            <input type="number" step="0.01" class="form-control rounded-3" name="largo" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Ancho (cm)</label>
                            <input type="number" step="0.01" class="form-control rounded-3" name="ancho" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Acabado / Impresión (Opcional)</label>
                        <input type="text" class="form-control rounded-3" name="acabado" placeholder="Detalles de acabado">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Uso (Opcional)</label>
                        <input type="text" class="form-control rounded-3" name="uso" placeholder="Uso de la caja">
                    </div>
                </div>

                <div class="modal-footer bg-light px-4 py-3 border-top">
                    <button type="button" class="btn btn-secondary btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-success btn-sm rounded-pill px-4 text-white">Guardar cotización</button>
                </div>
            </form>
        </div>
    </div>
</div>
                        
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
<script src="${pageContext.request.contextPath}/Vista/javaScript/GestionarCotizaciones.js"></script>

</body>
</html>