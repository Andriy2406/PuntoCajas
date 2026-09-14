<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Punto Cajas | Iniciar sesión</title>
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
                                <a class="btn btn-punto-secundario btn-sm" href="${pageContext.request.contextPath}/Vista/Registro.jsp">Registrarse</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

        <main class="d-flex align-items-center">
            <div class="container">
                <div class="contenedor-formulario">
                    <h1>Iniciar sesión</h1>
                    <p class="subtitulo">Ingresa con tu correo y contraseña registrados</p>

                    <c:if test="${not empty requestScope.mensaje}">
                        <div class="alert alert-success text-center" role="alert">
                            ${requestScope.mensaje}
                        </div>
                    </c:if>

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger text-center" role="alert">
                            ${requestScope.error}
                        </div>

                        <c:if test="${not empty requestScope.correoInactivo}">
                            <div class="text-center mb-4">
                                <p class="mb-2">
                                    ¿Necesitas contactar al administrador?
                                </p>

                                <a href="${pageContext.request.contextPath}/Vista/SoporteAcceso.jsp"
                                   class="btn btn-punto-secundario btn-sm">
                                    Solicitar soporte
                                </a>
                            </div>
                        </c:if>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/LoginServlet" method="POST">
                        <div class="mb-3">
                            <label for="correo" class="form-label">Correo</label>
                            <input type="email" name="correo" id="correo" class="form-control" placeholder="ejemplo@correo.com" autocomplete="email" required>
                        </div>

                        <div class="mb-3">
                            <label for="clave" class="form-label">Contraseña</label>
                            <input type="password" name="clave" id="clave" class="form-control" placeholder="Ingresa tu contraseña" autocomplete="current-password" required>
                        </div>

                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="mostrarContrasena">
                            <label class="form-check-label" for="mostrarContrasena">Mostrar contraseña</label>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-4">

                            <a href="${pageContext.request.contextPath}/Vista/RecuperarClave.jsp" class="small">¿Olvidaste tu contraseña?</a>
                        </div>

                        <button type="submit" class="btn btn-punto-primario w-100">Iniciar sesión</button>
                    </form>

                    <p class="text-center mt-4 mb-0">
                        ¿Aún no tienes cuenta? 
                        <a href="${pageContext.request.contextPath}/Vista/Registro.jsp">Regístrate aquí</a>
                    </p>

                    <div class="tarjeta-ayuda-soporte mt-4">
                        <h2>¿Necesitas ayuda para ingresar?</h2>
                        <p class="mb-2">Si no puedes iniciar sesión, olvidaste tu correo registrado o tu cuenta fue inactivada, nuestro equipo de soporte puede ayudarte.</p>
                        <div class="d-flex flex-wrap gap-3">
                            <a href="${pageContext.request.contextPath}/Vista/SoporteAcceso.jsp">Solicitar soporte de acceso &rarr;</a>
                            <a href="mailto:punto.cajas6@gmail.com">Escribir a punto.cajas6@gmail.com</a>
                        </div>
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
        <script src="${pageContext.request.contextPath}/Vista/javaScript/login.js"></script>

</body>
</html>