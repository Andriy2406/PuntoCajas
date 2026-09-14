<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Términos y condiciones</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
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
                                <a class="nav-link" href="<%=request.getContextPath()%>/PerfilServlet">Mi perfil</a>
                            </li>
                            <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                                <li class="nav-item">
                                    <a class="nav-link " href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
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
        <section class="seccion-gris py-5">
            <div class="container" style="max-width: 900px;">
                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/InicioServlet" class="btn-volver-admin shadow-2xs d-inline-flex align-items-center gap-2 text-decoration-none px-3 py-2 rounded-pill border bg-white text-dark">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        Volver al inicio
                    </a>
                </div>
                <h1 class="h3 mb-1">Términos y condiciones de uso</h1>
                <p class="text-secondary mb-4">Última actualización: 2026</p>

                <div class="alert alert-secondary" role="alert">
                    Este documento establece las condiciones legales que regulan la navegación, cotización de cajas de cartón, manejo del carrito y transacciones en la plataforma web de Punto Cajas, conforme a la legislación comercial y de protección al consumidor de la República de Colombia (Ley 1480 de 2011).
                </div>

                <p>
                    Bienvenido a Punto Cajas. Al acceder, registrarse o realizar solicitudes de cotización y compra a través de nuestro sitio web, el usuario acepta de manera expresa e incondicional los presentes Términos y Condiciones. Le recomendamos leer este documento detenidamente antes de utilizar nuestros servicios de comercialización de soluciones de empaque y embalaje.
                </p>

                <h2 class="h5 mt-4">1. Objeto y ámbito de aplicación</h2>
                <p>
                    Punto Cajas pone a disposición de los usuarios una plataforma digital orientada a la consulta de un catálogo especializado de cajas de cartón y embalajes, la simulación o solicitud de cotizaciones personalizadas (según dimensiones, calibres, acabados y cantidades), la gestión de un carrito de compras y la formalización de pedidos comerciales en línea.
                </p>

                <h2 class="h5 mt-4">2. Capacidad legal y registro de usuarios</h2>
                <p>
                    Los servicios de la plataforma están disponibles únicamente para personas naturales con capacidad legal para contratar o representantes debidamente autorizados de personas jurídicas. El usuario es responsable de suministrar información veraz, exacta y actualizada durante su proceso de registro, así como de custodiar su contraseña de acceso. Cualquier operación realizada bajo las credenciales del usuario se presumirá efectuada por este.
                </p>

                <h2 class="h5 mt-4">3. Cotizaciones, disponibilidad, stock y precios</h2>
                <ul>
                    <li><strong>Cotizaciones:</strong> Los valores generados a través del módulo de cotización son estimados basados en los parámetros ingresados por el usuario. Están sujetos a validación técnica y disponibilidad de inventario al momento de formalizar el pedido.</li>
                    <li><strong>Precios y Stock:</strong> Los precios expresados en pesos colombianos (COP) y las existencias corresponden a la información vigente en el sistema. El stock reservado temporalmente en el carrito de compras podrá liberarse automáticamente si el usuario abandona el proceso o expira la sesión bajo las reglas lógicas del sistema.</li>
                    <li><strong>Características de las cajas:</strong> Las medidas, tolerancias y especificaciones técnicas de las cajas de cartón descritas en el catálogo pueden presentar variaciones menores propias de los procesos de fabricación y materiales de empaque.</li>
                </ul>

                <h2 class="h5 mt-4">4. Proceso de pagos y seguridad transaccional</h2>
                <p>
                    La plataforma registra los medios, referencias y estados de los pagos asociados a las transacciones. En cumplimiento de las normativas de seguridad de la información y pasarelas de pago, <strong>Punto Cajas no almacena números completos de tarjetas de crédito/débito ni códigos de seguridad (CVV)</strong> en sus bases de datos locales.
                </p>

                <h2 class="h5 mt-4">5. Facturación y transacciones comerciales</h2>
                <p>
                    Los comprobantes o soportes de venta generados inicialmente por el sistema constituyen documentos internos de control y cotización. Estos deben distinguirse claramente de una factura electrónica de venta con validación previa de la DIAN, la cual se emitirá e integrará conforme la empresa complete sus procesos de habilitación, numeración y transmisión electrónica obligatoria según el calendario fiscal colombiano.
                </p>

                <h2 class="h5 mt-4">6. Propiedad intelectual</h2>
                <p>
                    Todos los contenidos de la plataforma (textos, gráficos, logotipos, iconos, códigos fuente, software y diseño visual de las cajas y catálogos) son propiedad exclusiva de Punto Cajas o cuentan con las autorizaciones debidas, estando protegidos por las leyes nacionales e internacionales de propiedad intelectual y derechos de autor. Queda prohibida su reproducción total o parcial sin autorización expresa.
                </p>

                <h2 class="h5 mt-4">7. Protección de datos personales</h2>
                <p>
                    El almacenamiento, uso y tratamiento de los datos personales suministrados por los usuarios se realiza en estricto cumplimiento de la Ley Estatutaria 1581 de 2012 y normativas concordantes. Para más detalles sobre cómo ejercitamos la seguridad y los derechos de los titulares, consulte nuestra <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de Privacidad</a>.
                </p>

                <h2 class="h5 mt-4">8. Modificaciones de los términos</h2>
                <p>
                    Punto Cajas se reserva el derecho de modificar, actualizar o complementar en cualquier momento los presentes Términos y Condiciones para adaptarlos a cambios normativos, tecnológicos o comerciales. Las modificaciones entrarán en vigencia a partir de su publicación en el sitio web.
                </p>
                <c:choose>
                            <c:when test="${not empty sessionScope.usuarioActivo}">
                                <div class="mt-4 d-flex gap-3">
                                    <a class="btn btn-punto-secundario" href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a>
                                </div> 
                            </c:when>
                            <c:otherwise>
                                <div class="mt-4 d-flex gap-3">
                                    <a class="btn btn-punto-primario" href="${pageContext.request.contextPath}/Vista/Registro.jsp">Volver al registro</a> 
                                    <a class="btn btn-punto-secundario" href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a>
                                </div> 
                            </c:otherwise>
                        </c:choose>
            </div>
        </section>
    </main>

    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>