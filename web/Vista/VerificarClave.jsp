<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Verificar código</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
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
                <h1>Verificar código</h1>
                <p class="subtitulo">Ingresa el código de 6 dígitos que enviamos a tu correo electrónico.</p>
                <c:if test="${not empty requestScope.mensajeError}">
                    <div class="alerta-punto alerta-punto-error">
                        ${requestScope.mensajeError}
                    </div>
                </c:if>
                <form action="${pageContext.request.contextPath}/VerificarClaveServlet" method="post">
                    <div class="mb-3">
                        <label for="codigo" class="form-label">Código de recuperación</label>
                        <input type="text" class="form-control text-center" id="codigo" name="codigo" placeholder="000000" maxlength="6" inputmode="numeric" autocomplete="one-time-code" required autofocus>
                    </div>
                    <button type="submit" class="btn btn-punto-primario w-100">Verificar código</button>
                </form>
                <p class="texto-ayuda-perfil mt-4 mb-0 text-center">
                    ¿No recibiste el código?
                    <a href="${pageContext.request.contextPath}/Vista/RecuperarClave.jsp">Solicitar otro</a>
                </p>
            </div>
        </div>
    </main>
    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>