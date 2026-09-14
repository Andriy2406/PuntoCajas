<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Mi perfil</title>
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
                                <a class="nav-link active" href="<%=request.getContextPath()%>/PerfilServlet">Mi perfil</a>
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
        <section class="encabezado-perfil">
            <div class="container">
                <div class="d-flex align-items-center gap-3">
                    <div class="avatar-perfil">
                        ${usuario.nombre.substring(0,1)}
                        ${usuario.apellido.substring(0,1)}
                    </div>
                    <div>
                        <h1 class="h3 mb-1">
                            ${usuario.nombre}
                            ${usuario.apellido}
                        </h1>
                        <p class="text-secondary mb-0">
                            ${usuario.correo}
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <section class="seccion-gris">
            <div class="container">
                <a href="${pageContext.request.contextPath}/InicioServlet" class="btn-volver">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 18l-6-6 6-6"/></svg>
                    Volver al inicio
                </a>
                <c:if test="${not empty requestScope.error}">
                    <div class="alerta-punto alerta-punto-error">
                        ${requestScope.error}
                    </div>
                </c:if>

                <c:if test="${not empty requestScope.mensaje}">
                    <div class="alerta-punto alerta-punto-exito">
                        ${requestScope.mensaje}
                    </div>
                </c:if>

                <div class="row g-4">
                    <div class="col-lg-7">
                        <div class="tarjeta-perfil">
                            <h2 class="h5 mb-1">Mis datos</h2>
                            <p class="texto-ayuda-perfil mb-4">Actualiza la información de contacto de tu cuenta.</p>

                            <form action="${pageContext.request.contextPath}/PerfilServlet" method="POST">
                                <h3 class="h6 mb-3">Información personal</h3>
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="nombre" class="form-label">Nombre</label>
                                        <input type="text" class="form-control" id="nombre" name="nombre" value="${usuario.nombre}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="apellido" class="form-label">Apellido</label>
                                        <input type="text" class="form-control" id="apellido" name="apellido" value="${usuario.apellido}" required>
                                    </div>
                                </div>

                                <h3 class="h6 mt-4 mb-3">Información de contacto</h3>
                                <div class="mb-3">
                                    <label for="correo" class="form-label">Correo electrónico</label>
                                    <input type="email" class="form-control" id="correo" name="correo" value="${usuario.correo}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="telefono" class="form-label">Teléfono</label>
                                    <input type="tel" class="form-control" id="telefono" name="telefono" value="${usuario.telefono}">
                                </div>

                                <div class="mb-3">
                                    <label for="direccion" class="form-label">Dirección</label>
                                    <div class="buscador-direccion-mapa position-relative">
                                        <input type="text" class="form-control mb-2" id="buscadorDireccion" placeholder="Busca tu dirección o calle (ej. Calle 50, Bogotá)..." autocomplete="off">
                                        <div id="resultadosBusquedaDireccion" class="resultados-busqueda-direccion shadow-sm" style="display: none; position: absolute; z-index: 1000; width: 100%; background: white; max-height: 200px; overflow-y: auto; border: 1px solid #ced4da; border-radius: 0.375rem;"></div>
                                    </div>
                                    <input type="text" class="form-control mb-2" id="direccion" name="direccion" value="${usuario.direccion}" placeholder="Se completa al elegir un punto en el mapa, o escríbela tú mismo">
                                    <div id="mapaDireccion" class="mapa-direccion mb-2" style="height: 300px; width: 100%; border-radius: 0.375rem; border: 1px solid #ced4da;"></div>
                                    <input type="hidden" id="latitud" name="latitud" value="${usuario.latitud}">
                                    <input type="hidden" id="longitud" name="longitud" value="${usuario.longitud}">
                                    <p class="texto-ayuda-clave mt-2">Arrastra el pin o busca tu dirección para ubicarla con precisión, igual que en Mercado Libre. Esto ayuda al vendedor y al conductor a encontrar tu punto de entrega.</p>
                                </div>

                                <div class="acciones-formulario d-flex gap-2 mt-4">
                                    <button type="submit" class="btn btn-punto-primario">Guardar cambios</button>
                                    <a href="${pageContext.request.contextPath}/InicioServlet" class="btn btn-punto-secundario">Cancelar</a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="col-lg-5">
                        <div class="tarjeta-perfil">
                            <h2 class="h5 mb-3">Información de identificación</h2>
                            <div class="mb-3">
                                <label for="identificacion" class="form-label">Número de documento</label>
                                <input type="text" class="form-control campo-solo-lectura" id="identificacion" value="${usuario.identificacionUsuario}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="fechaNacimiento" class="form-label">Fecha de nacimiento</label>
                                <input type="date" class="form-control campo-solo-lectura" id="fechaNacimiento" value="${usuario.fechaDeNacimiento}" readonly>
                            </div>
                            <p class="texto-ayuda-perfil mb-0">Esta información no puede ser modificada desde el perfil.</p>
                        </div>

                        <div class="tarjeta-ayuda-soporte">
                            <h2>¿Necesitas ayuda?</h2>
                            <p class="mb-2">Si tienes dudas sobre tu cuenta, pedidos o cotizaciones, nuestro equipo de soporte puede ayudarte.</p>
                            <a href="${pageContext.request.contextPath}/Vista/Soporte.jsp">Ir a soporte y contacto &rarr;</a>
                        </div>

                        <div class="tarjeta-perfil mt-4">
                            <h2 class="h5 mb-3">Cambiar contraseña</h2>
                            <form action="${pageContext.request.contextPath}/PerfilServlet" method="POST">
                                <div class="mb-3">
                                    <label for="clave" class="form-label">Nueva contraseña</label>
                                    <input type="password" class="form-control" id="clave" name="clave" minlength="8" placeholder="Mínimo 8 caracteres">
                                    <div class="barra-fortaleza-clave mt-2" id="barraFortalezaClave">
                                        <div class="barra-fortaleza-relleno" id="barraFortalezaRelleno"></div>
                                    </div>
                                    <p class="texto-ayuda-clave" id="textoFortalezaClave">Mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo.</p>

                                    <ul id="listaRequisitos" class="list-unstyled mt-2 small">
                                        <li id="req-length" class="text-danger">❌ Mínimo 8 caracteres</li>
                                        <li id="req-upper" class="text-danger">❌ Al menos una letra mayúscula</li>
                                        <li id="req-lower" class="text-danger">❌ Al menos una letra minúscula</li>
                                        <li id="req-number" class="text-danger">❌ Al menos un número</li>
                                        <li id="req-special" class="text-danger">❌ Al menos un carácter especial</li>
                                    </ul>
                                </div>
                                <p class="mensaje-ayuda-password">Si no deseas cambiar tu contraseña, deja este campo vacío. Cambiar la contraseña nunca borra tu teléfono ni tu dirección.</p>
                                <button type="submit" class="btn btn-punto-primario w-100">Actualizar contraseña</button>
                            </form>
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

    <!-- Scripts externos -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/passwordStrength.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/mapaDireccion.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/perfil.js"></script>
</body>
</html>