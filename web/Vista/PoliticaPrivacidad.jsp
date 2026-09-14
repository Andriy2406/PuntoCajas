<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Política de tratamiento de datos personales</title>

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
                <h1 class="h3 mb-1">Política de tratamiento de datos personales</h1>
                <p class="text-secondary mb-4">Última actualización: 2026</p>

                <div class="alert alert-secondary" role="alert">
                    Este documento tiene fines académicos e informativos. No constituye asesoría jurídica.
                    Antes de usarse en un entorno real, debe ser revisado y validado por un abogado especializado
                    en protección de datos personales.
                </div>

                <p>
                    Punto Cajas, es la Responsable del Tratamiento de los datos personales que
                    los usuarios registran en este sitio web. Esta política se expide de conformidad con la
                    Ley Estatutaria 1581 de 2012, el Decreto 1377 de 2013 y las demás normas que los desarrollen,
                    bajo la vigilancia de la Superintendencia de Industria y Comercio (SIC).
                </p>

                <h2 class="h5 mt-4">1. Responsable del tratamiento</h2>
                <ul>
                    <li>Razón social: Punto Cajas</li>
                    <li>NIT: [NIT de la empresa]</li>
                    <li>Dirección: [Dirección]</li>
                    <li>Correo de contacto: [Correo de contacto]</li>
                    <li>Teléfono: [Teléfono]</li>
                </ul>

                <h2 class="h5 mt-4">2. Datos personales que se recolectan</h2>
                <p>
                    A través del registro de usuarios, el inicio de sesión, las cotizaciones y los pedidos
                    realizados en este sitio, se pueden recolectar datos como: nombres, apellidos, tipo y
                    número de documento de identidad, dirección, teléfono, correo electrónico y fecha de
                    nacimiento, así como la información de las cotizaciones, pedidos y facturas asociados
                    a la cuenta del usuario.
                </p>

                <h2 class="h5 mt-4">3. Finalidad del tratamiento</h2>
                <p>Los datos personales recolectados se usan para:</p>
                <ul>
                    <li>Crear y administrar la cuenta del usuario dentro de la plataforma.</li>
                    <li>Gestionar cotizaciones, pedidos, facturas y pagos.</li>
                    <li>Dar respuesta a solicitudes, quejas o reclamos.</li>
                    <li>Enviar comunicaciones relacionadas con el servicio (por ejemplo, recuperación de contraseña).</li>
                    <li>Cumplir con obligaciones legales y contractuales aplicables al servicio prestado.</li>
                </ul>
                <p>No se utilizarán los datos personales para finalidades distintas a las aquí descritas sin autorización previa del titular.</p>

                <h2 class="h5 mt-4">4. Autorización del titular</h2>
                <p>
                    Al registrarse en el sitio y marcar la casilla de autorización dispuesta en el formulario
                    de registro, el titular otorga su consentimiento libre, previo, expreso e informado para
                    el tratamiento de sus datos personales conforme a las finalidades descritas en esta política.
                </p>

                <h2 class="h5 mt-4">5. Derechos del titular</h2>
                <p>De acuerdo con el artículo 8 de la Ley 1581 de 2012, el titular de los datos personales tiene derecho a:</p>
                <ul>
                    <li>Conocer, actualizar y rectificar sus datos personales.</li>
                    <li>Solicitar prueba de la autorización otorgada.</li>
                    <li>Ser informado sobre el uso que se ha dado a sus datos.</li>
                    <li>Presentar quejas ante la Superintendencia de Industria y Comercio por infracciones a la ley.</li>
                    <li>Revocar la autorización y/o solicitar la supresión del dato, cuando no exista un deber legal o contractual que impida eliminarlo.</li>
                    <li>Acceder de forma gratuita a sus datos personales que hayan sido objeto de tratamiento.</li>
                </ul>

                <h2 class="h5 mt-4">6. Mecanismos para ejercer los derechos</h2>
                <p>
                    El titular puede ejercer sus derechos enviando una solicitud al correo electrónico de contacto
                    o comunicándose a los canales de soporte de la plataforma. La solicitud será
                    atendida dentro de los términos establecidos en la Ley 1581 de 2012.
                </p>

                <h2 class="h5 mt-4">7. Seguridad de la información</h2>
                <p>
                    Se implementan medidas técnicas y administrativas razonables para proteger los datos
                    personales contra pérdida, uso indebido, acceso no autorizado, alteración o divulgación,
                    tales como el uso de contraseñas, control de acceso por roles y conexiones controladas
                    a la base de datos.
                </p>

                <h2 class="h5 mt-4">8. Vigencia</h2>
                <p>
                    Esta política rige a partir de su fecha de publicación y los datos personales se
                    conservarán durante el tiempo necesario para cumplir las finalidades descritas y las
                    obligaciones legales aplicables.
                </p>

                <p class="text-secondary mt-4">
                    Para más información sobre la normativa colombiana de protección de datos personales,
                    puede consultar la página oficial de la Superintendencia de Industria y Comercio (SIC).
                </p>
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