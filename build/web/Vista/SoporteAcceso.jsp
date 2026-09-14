<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Solicitar soporte</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/soporte-acceso.css" rel="stylesheet">
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

                    <c:choose>
                        <c:when test="${not empty sessionScope.usuarioActivo}">
                            <li class="nav-item">
                                <a class="nav-link active " href="${pageContext.request.contextPath}/Vista/Soporte.jsp">Soporte</a>
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
                                <a class="nav-link active " href="${pageContext.request.contextPath}/Vista/SoporteAcceso.jsp">Soporte</a>
                            </li>
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
    <main class="seccion-soporte-acceso">
        <div class="container">
            <div class="fila-soporte-acceso">
                <div class="contenedor-formulario soporte-acceso-card">
                    <div class="soporte-acceso-encabezado">
                        <div class="icono-soporte-acceso">?</div>
                        <h1>Solicitar soporte</h1>
                        <p class="subtitulo">Si no puedes acceder a tu cuenta, puedes contactar al administrador mediante este formulario.</p>
                    </div>
                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger" role="alert">
                            ${requestScope.error}
                        </div>
                    </c:if>
                    <c:if test="${not empty sessionScope.mensajeSoporteAcceso}">
                        <div class="alert alert-success" role="alert">
                            ${sessionScope.mensajeSoporteAcceso}
                        </div>
                        <c:remove var="mensajeSoporteAcceso" scope="session"/>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/SoporteAccesoServlet" method="POST">
                        <div class="mb-4">
                            <label for="correo" class="form-label">Correo electrónico</label>
                            <input type="email" class="form-control" id="correo" name="correo" value="${requestScope.correoInactivo}" placeholder="ejemplo@correo.com" required>
                            <div class="form-text">Ingresa el correo asociado a tu cuenta.</div>
                        </div>
                        <div class="mb-4">
                            <label for="asunto" class="form-label">Asunto</label>
                            <input type="text" class="form-control" id="asunto" name="asunto" maxlength="100" placeholder="Ejemplo: Mi cuenta está inactiva" required>
                        </div>
                        <div class="mb-4">
                            <label for="mensaje" class="form-label">Describe tu problema</label>
                            <textarea class="form-control" id="mensaje" name="mensaje" rows="7" maxlength="1000" placeholder="Describe el problema que estás teniendo con tu cuenta..." required></textarea>
                            <div class="form-text">Máximo 1000 caracteres.</div>
                        </div>
                        <div class="botones-soporte-acceso">
                            <a href="${pageContext.request.contextPath}/Vista/Login.jsp" class="btn btn-punto-secundario">Volver al login</a>
                            <button type="submit" class="btn btn-punto-primario">Enviar solicitud</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>