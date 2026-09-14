<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Carrito de Compras</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body class="bg-light">

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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/PerfilServlet">Mi perfil</a>
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

    <main class="container my-5">
        <div class="card shadow-lg border-0 rounded-4 p-4 bg-white">
            <h1 class="h3 mb-4 fw-bold text-dark">🛒 Tu Carrito de Compras</h1>

            <c:set var="total" value="${0}"/>

            <div class="table-responsive mb-4">
                <table class="table align-middle table-hover mb-0">
                    <thead class="table-success text-dark">
                        <tr>
                            <th>Producto</th>
                            <th>Precio Unitario</th>
                            <th>Cantidad</th>
                            <th>Subtotal</th>
                            <th class="text-center">Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${sessionScope.carrito}">
                            <c:set var="total" value="${total + item.subtotal}"/>
                            <tr>
                                <td class="fw-semibold text-secondary">${item.descripcion}</td>
                                <td>$${item.precioUnitario}</td>
                                <td><span class="badge bg-secondary px-3 py-2">${item.cantidad}</span></td>
                                <td class="fw-bold text-success">$${item.subtotal}</td>
                                <td class="text-center">
                                    <form action="${pageContext.request.contextPath}/CarritoServlet" method="POST" class="d-inline">
                                        <input type="hidden" name="accion" value="quitar">
                                        <input type="hidden" name="idProducto" value="${item.idProducto}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm px-3 rounded-pill">Quitar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sessionScope.carrito}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-5 fs-5">Tu carrito está actualmente vacío.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="d-flex flex-wrap justify-content-between align-items-center pt-3 border-top gap-3">
                <form action="${pageContext.request.contextPath}/CarritoServlet" method="POST">
                    <input type="hidden" name="accion" value="vaciar">
                    <button type="submit" class="btn btn-outline-secondary px-4 rounded-pill">Vaciar carrito</button>
                </form>

                <div class="text-end">
                    <h4 class="fw-bold mb-3">Total: <span class="text-success">$${total}</span></h4>
                    <a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn btn-outline-success px-4 rounded-pill me-2">Seguir comprando</a>
                    <c:if test="${not empty sessionScope.carrito}">
                        <form action="${pageContext.request.contextPath}/CarritoServlet" method="POST" class="d-inline">
                            <input type="hidden" name="accion" value="confirmar">
                            <button type="submit" class="btn btn-success px-5 rounded-pill shadow">Finalizar compra</button>
                        </form>
                    </c:if>
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