<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Procesar pago</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/perfil.css" rel="stylesheet">
    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/InicioServlet">Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CotizacionServlet">Cotizar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/Vista/Soporte.jsp">Soporte</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/PerfilServlet">Mi perfil</a>
                    </li>
                    <li class="nav-item ms-md-3">
                        <span class="nav-link">Hola, <strong>${sessionScope.usuarioActivo.nombre}</strong></span>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-punto-secundario btn-sm" href="${pageContext.request.contextPath}/LogoutServlet">Cerrar sesión</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="container my-5" style="max-width: 700px;">
        <div class="tarjeta-perfil p-4 shadow-sm bg-white rounded-3">
            <h1 class="h4 mb-3">Procesar pago</h1>
            <p class="text-secondary small mb-4">Confirma o actualiza tus datos de envío y selecciona el medio de pago.</p>

            <c:if test="${not empty requestScope.error}">
                <div class="alerta-punto alerta-punto-error mb-3">
                    ${requestScope.error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/ProcesarPagoServlet" method="POST">
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" value="${sessionScope.usuarioActivo.nombre}" required>
                </div>

                <div class="mb-3">
                    <label for="correo" class="form-label">Correo electrónico</label>
                    <input type="email" class="form-control" id="correo" name="correo" value="${sessionScope.usuarioActivo.correo}" required>
                </div>

                <!-- SECCIÓN DEL MAPA Y DIRECCIÓN DE ENVÍO -->
                <div class="mb-3">
                    <label for="direccion" class="form-label">Dirección de envío</label>
                    
                    <!-- Buscador flotante para autocompletar calles -->
                    <div class="buscador-direccion-mapa position-relative mb-2">
                        <input type="text" class="form-control" id="buscadorDireccion" placeholder="Busca tu dirección o calle (ej. Carrera 27, Bogotá)..." autocomplete="off">
                        <div id="resultadosBusquedaDireccion" class="resultados-busqueda-direccion shadow-sm" style="display: none; position: absolute; z-index: 1000; width: 100%; background: white; max-height: 200px; overflow-y: auto; border: 1px solid #ced4da; border-radius: 0.375rem;"></div>
                    </div>

                    <!-- Input principal de dirección que se envía al servidor -->
                    <input type="text" class="form-control mb-2" id="direccion" name="direccion" value="${sessionScope.usuarioActivo.direccion}" placeholder="Se completa al elegir un punto en el mapa, o escríbela tú mismo" required>
                    
                    <!-- Contenedor visual del mapa -->
                    <div id="mapaDireccion" class="mapa-direccion mb-2" style="height: 260px; width: 100%; border-radius: 0.375rem; border: 1px solid #ced4da;"></div>

                    <!-- Inputs ocultos para enviar la latitud y longitud -->
                    <input type="hidden" id="latitud" name="latitud" value="${sessionScope.usuarioActivo.latitud}">
                    <input type="hidden" id="longitud" name="longitud" value="${sessionScope.usuarioActivo.longitud}">
                    
                    <div class="form-text text-muted small">Arrastra el marcador en el mapa para ubicar con precisión el punto exacto de entrega para el conductor.</div>
                </div>

                <div class="mb-3">
                    <label for="identificacion" class="form-label">Nro. Identificación</label>
                    <input type="text" class="form-control campo-solo-lectura" id="identificacion" value="${sessionScope.usuarioActivo.identificacionUsuario}" readonly>
                    <div class="form-text text-muted small">Tu documento de identidad registrado en el perfil.</div>
                </div>

                <div class="mb-4">
                    <label for="medioPago" class="form-label">Medio de pago</label>
                    <select class="form-select" id="medioPago" name="medioPago" required>
                        <option value="" selected disabled>Seleccione...</option>
                        <option value="tarjeta_credito">Tarjeta de crédito</option>
                        <option value="tarjeta_debito">Tarjeta de débito</option>
                        <option value="pse">PSE</option>
                    </select>
                </div>

                <div class="p-3 bg-light rounded-3 mb-4 text-secondary small">
                    Por seguridad, este sistema no almacena números completos de tarjeta ni CVV.
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-punto-primario w-100 py-2">Confirmar y pagar</button>
                </div>
            </form>
        </div>
    </main>

    <footer class="footer-punto mt-5">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
        </div>
    </footer>

    <!-- Scripts externos -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/mapaDireccion.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            if (typeof inicializarMapaDireccion === "function") {
                inicializarMapaDireccion({
                    mapaId: 'mapaDireccion',
                    inputDireccion: 'direccion',
                    inputBuscador: 'buscadorDireccion',
                    resultadosId: 'resultadosBusquedaDireccion',
                    inputLat: 'latitud',
                    inputLng: 'longitud',
                    latInicial: ${not empty sessionScope.usuarioActivo.latitud ? sessionScope.usuarioActivo.latitud : 4.7110},
                    lngInicial: ${not empty sessionScope.usuarioActivo.longitud ? sessionScope.usuarioActivo.longitud : -74.0721},
                    zoomInicial: 14,
                    soloLectura: false
                });
            }
        });
    </script>
</body>
</html>