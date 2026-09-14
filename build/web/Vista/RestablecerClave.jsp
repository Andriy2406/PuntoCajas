<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Nueva clave</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.googleapis.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/perfil.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-punto py-3">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/Vista/Login.jsp">
                <svg class="logo-caja" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                    <path d="M3 7l9-4 9 4-9 4-9-4z"/>
                    <path d="M3 7v10l9 4 9-4V7"/>
                    <path d="M12 11v10"/>
                </svg>
                Punto Cajas
            </a>
        </div>
    </nav>
    <main>
        <div class="container">
            <div class="contenedor-formulario">
                <h1>Nueva clave</h1>
                <p class="subtitulo">Escribe una nueva clave para tu cuenta.</p>
                <c:if test="${not empty requestScope.mensajeError}">
                    <div class="alerta-punto alerta-punto-error">
                        ${requestScope.mensajeError}
                    </div>
                </c:if>
                <form action="${pageContext.request.contextPath}/RestablecerClaveServlet" method="post">
                    <div class="mb-3">
                        <label for="clave" class="form-label">Nueva clave</label>
                        <input type="password" class="form-control" id="clave" name="clave" minlength="8" autocomplete="new-password" required>
                        <div class="barra-fortaleza-clave mt-2" id="barraFortalezaClave">
                            <div class="barra-fortaleza-relleno" id="barraFortalezaRelleno"></div>
                        </div>
                        <p class="texto-ayuda-clave" id="textoFortalezaClave">Mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo.</p>
                    </div>
                    <div class="mb-4">
                        <label for="confirmarClave" class="form-label">Confirmar nueva clave</label>
                        <input type="password" class="form-control" id="confirmarClave" name="confirmarClave" minlength="8" autocomplete="new-password" required>
                    </div>
                    <button type="submit" class="btn btn-punto-primario w-100">Cambiar clave</button>
                </form>
            </div>
        </div>
    </main>
    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/passwordStrength.js"></script>
</body>
</html>