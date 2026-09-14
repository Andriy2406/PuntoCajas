<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Panel de conductor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
</head>
<body>
    <nav class="navbar navbar-expand-md navbar-punto py-3">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/ConductorServlet">
                <svg class="logo-caja" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                    <path d="M3 7l9-4 9 4-9 4-9-4z"/>
                    <path d="M3 7v10l9 4 9-4V7"/>
                    <path d="M12 11v10"/>
                </svg>
                Punto Cajas
            </a>
            <div class="collapse navbar-collapse justify-content-end">
                <ul class="navbar-nav align-items-md-center gap-md-2">
                    <li class="nav-item ms-md-3">
                        <span class="nav-link">Conductor: <strong>${sessionScope.usuarioActivo.nombre}</strong></span>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-punto-secundario btn-sm" href="${pageContext.request.contextPath}/LogoutServlet">Cerrar sesión</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main>
        <section class="seccion-gris py-5">
            <div class="container">
                <h1 class="h3 mb-1">Mis rutas de entrega</h1>
                <p class="text-secondary mb-4">Haz clic en cualquier parada de la lista para centrarla en el mapa y calcular su tiempo estimado de llegada.</p>

                <c:if test="${not empty sessionScope.mensajeAlerta}">
                    <div class="alert alert-${sessionScope.tipoAlerta} alert-dismissible fade show shadow-sm" role="alert">
                        ${sessionScope.mensajeAlerta}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% session.removeAttribute("mensajeAlerta"); %>
                    <% session.removeAttribute("tipoAlerta"); %>
                </c:if>

                <c:if test="${empty pedidosAsignados}">
                    <div class="tarjeta-perfil text-center py-5">
                        <p class="mb-0 text-secondary">No tienes pedidos asignados por el momento.</p>
                    </div>
                </c:if>

                <c:if test="${not empty pedidosAsignados}">
                    <!-- Resumen global de la ruta -->
                    <div id="resumenRutaContainer" class="alert alert-primary shadow-sm mb-4 d-none">
                        <div class="d-flex align-items-center justify-content-between flex-wrap gap-2">
                            <div>
                                <strong>⏱️ Tiempo estimado total de ruta:</strong> <span id="tiempoTotalRuta">--</span>
                            </div>
                            <div>
                                <strong>📍 Distancia total:</strong> <span id="distanciaTotalRuta">--</span>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4">
                        <div class="col-lg-7">
                            <div id="mapaRutas" class="mapa-direccion" style="height: 460px; border-radius: 0.5rem;"></div>
                            <p class="texto-ayuda-perfil mt-2">La línea morada indica la mejor ruta sugerida por OSRM. Las tarjetas muestran el tiempo estimado por tramo.</p>
                        </div>
                        <div class="col-lg-5">
                            <c:forEach var="pedido" items="${pedidosAsignados}" varStatus="st">
                                <div class="tarjeta-perfil mb-3 card-parada shadow-sm" style="cursor: pointer; transition: all 0.2s;" data-lat="${pedido.latitud}" data-lng="${pedido.longitud}" data-id="${pedido.idPedido}">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <h2 class="h6 mb-1">Parada ${st.index + 1} · Pedido #${pedido.idPedido}</h2>
                                        <span class="badge bg-primary">${pedido.estadoPedido}</span>
                                    </div>
                                    <p class="mb-1">${pedido.direccionEnvio}</p>
                                    <p class="text-secondary small mb-2">Total cobro: <strong>$${pedido.total}</strong></p>
                                    
                                    <!-- Espacio para mostrar el tiempo estimado específico de esta parada -->
                                    <div class="badge bg-light text-dark border mb-3 info-tramo d-none" id="info-tramo-${pedido.idPedido}">
                                        🚗 Tiempo estimado hasta aquí: <span class="tiempo-tramo">--</span> (<span class="distancia-tramo">--</span>)
                                    </div>

                                    <form action="${pageContext.request.contextPath}/ConductorServlet" method="POST" class="d-flex gap-2 flex-wrap" onclick="event.stopPropagation();">
                                        <input type="hidden" name="id" value="${pedido.idPedido}">
                                        <button type="submit" name="estadoPedido" value="En reparto" class="btn btn-punto-secundario btn-sm" ${pedido.estadoPedido == 'En reparto' ? 'disabled' : ''}>Marcar "En reparto"</button>
                                        <button type="submit" name="estadoPedido" value="Entregado" class="btn btn-punto-primario btn-sm" onclick="return confirm('¿Confirmas que el cliente ya recibió este pedido?');">Marcar "Entregado"</button>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </section>
    </main>

    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script>
        window.paradasConductor = [
            <c:forEach var="pedido" items="${pedidosAsignados}" varStatus="st">
            {
                id: ${pedido.idPedido},
                lat: ${not empty pedido.latitud ? pedido.latitud : 'null'},
                lng: ${not empty pedido.longitud ? pedido.longitud : 'null'},
                direccion: "${fn:escapeXml(pedido.direccionEnvio)}"
            }<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];
    </script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/conductor.js"></script>
</body>
</html>