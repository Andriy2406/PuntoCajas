<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.ItemCarrito"%>
<%
    // Cantidad total de unidades que el usuario ya tiene en el carrito,
    // para mostrar el número en la campanita del carrito al cargar la página.
    List<ItemCarrito> carritoActual = (List<ItemCarrito>) session.getAttribute("carrito");
    int totalCarritoInicial = 0;
    if (carritoActual != null) {
        for (ItemCarrito it : carritoActual) totalCarritoInicial += it.getCantidad();
    }
    request.setAttribute("totalCarritoInicial", totalCarritoInicial);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Catálogo de productos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body>

    <!-- Definir la ruta del contexto global para JS -->
    <script>
        window.contextPath = "${pageContext.request.contextPath}";
    </script>

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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/CatalogoServlet">Catálogo</a>
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
        <section class="seccion-gris py-5">
            <div class="container">
                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/InicioServlet" class="btn-volver-admin shadow-2xs d-inline-flex align-items-center gap-2 text-decoration-none px-3 py-2 rounded-pill border bg-white text-dark">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        Volver al inicio
                    </a>
                </div>
                <h1 class="h3 mb-1">Catálogo de productos</h1>
                <p class="text-secondary mb-4">Elige tus cajas, agrégalas al carrito y revisa cuántas unidades ya llevas de cada una.</p>

                <!-- Buscador y filtro por categoría -->
                <div class="row g-2 mb-4">
                    <div class="col-md-8">
                        <input type="text" id="buscadorProductos" class="form-control" placeholder="Buscar producto por nombre...">
                    </div>
                    <div class="col-md-4">
                        <select id="filtroCategoria" class="form-select">
                            <option value="">Todas las categorías</option>
                            <c:forEach var="cat" items="${listaCategorias}">
                                <option value="${cat.nombre}">${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row g-4" id="gridProductos">
                    <c:forEach var="producto" items="${listaProductos}">
                        <div class="col-sm-6 col-lg-4 tarjeta-producto"
                             data-nombre="${fn:toLowerCase(producto.descripcion)}"
                             data-categoria="${producto.categoria}">
                            <div class="card h-100 shadow-sm">
                                <a href="${pageContext.request.contextPath}/DetalleProductoServlet?id=${producto.idProducto}">
                                    <img src="${producto.urlImagen}" class="card-img-top" alt="${producto.descripcion}" style="height:200px; object-fit:cover;">
                                </a>
                                <div class="card-body d-flex flex-column">
                                    <c:if test="${not empty producto.categoria}">
                                        <span class="badge bg-secondary align-self-start mb-1">${producto.categoria}</span>
                                    </c:if>
                                    <h2 class="h6">
                                        <a href="${pageContext.request.contextPath}/DetalleProductoServlet?id=${producto.idProducto}" class="text-decoration-none text-dark">
                                            ${producto.descripcion}
                                        </a>
                                    </h2>
                                    <p class="text-secondary mb-1">Stock disponible: ${producto.stockActual}</p>
                                    <p class="fw-bold mb-3">$${producto.precio}</p>

                                    <div class="mt-auto">
                                        <!-- Controles de cantidad con Botones + y - -->
                                        <div class="input-group mb-2" style="max-width: 140px; margin: 0 auto;">
                                            <button type="button" class="btn btn-outline-secondary px-2" onclick="cambiarCantidad(${producto.idProducto}, -1, ${producto.stockActual})">-</button>
                                            <input type="number" id="cantidad_${producto.idProducto}" value="1" min="1" max="${producto.stockActual}" class="form-control text-center" readonly>
                                            <button type="button" class="btn btn-outline-secondary px-2" onclick="cambiarCantidad(${producto.idProducto}, 1, ${producto.stockActual})">+</button>
                                        </div>

                                        <!-- Cuántas unidades de este producto ya están en el carrito -->
                                        <c:set var="cantidadPrevia" value="${cantidadesEnCarrito[producto.idProducto]}"/>
                                        <span id="enCarrito_${producto.idProducto}" class="badge-cantidad-producto mb-1"><c:if test="${not empty cantidadPrevia && cantidadPrevia > 0}">En tu carrito: ${cantidadPrevia} unidad${cantidadPrevia == 1 ? '' : 'es'}</c:if></span>

                                        <!-- Botones de Acción -->
                                        <div class="d-flex gap-2">
                                            <button type="button" class="btn btn-punto-primario flex-fill" onclick="procesarCarrito(${producto.idProducto}, 'agregar')">Agregar al carrito</button>
                                        </div>

                                        <!-- Mensaje flotante de confirmación -->
                                        <div id="mensaje_${producto.idProducto}" class="mt-2 text-center fw-bold" style="font-size: 0.85rem; display: none;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty listaProductos}">
                        <p class="text-secondary">No hay productos disponibles por el momento.</p>
                    </c:if>
                </div>

                <p id="sinResultados" class="text-secondary text-center mt-4" style="display:none;">
                    No se encontraron productos con ese criterio.
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
    <script>
        window.contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/catalogo.js"></script>
    
</body>
</html>