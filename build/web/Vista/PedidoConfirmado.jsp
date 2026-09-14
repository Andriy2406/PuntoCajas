<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Pedido confirmado</title>

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
        </div>
    </nav>

    <main>
        <section class="py-5">
            <div class="container" style="max-width: 640px;">

                <div class="text-center mb-4">
                    <div style="font-size: 3rem;">✅</div>
                    <h1 class="h3">¡Pedido confirmado!</h1>
                    <p class="text-secondary">Gracias por tu compra. Este es el resumen de lo que pediste.</p>
                </div>

                <div class="table-responsive bg-white rounded p-3 border shadow-sm mb-3">
                    <table class="table align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Producto</th>
                                <th>Precio</th>
                                <th>Cantidad</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="total" value="${0}"/>
                            <c:forEach var="item" items="${sessionScope.ultimoPedido}">
                                <c:set var="total" value="${total + item.subtotal}"/>
                                <tr>
                                    <td>${item.descripcion}</td>
                                    <td>$${item.precioUnitario}</td>
                                    <td>${item.cantidad}</td>
                                    <td>$${item.subtotal}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty sessionScope.ultimoPedido}">
                                <tr>
                                    <td colspan="4" class="text-center text-secondary py-4">No hay un pedido reciente para mostrar.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <p class="h5 text-end mb-4">Total: $${total}</p>

                <p class="text-secondary small">
                    Un asesor se pondrá en contacto contigo para coordinar el envío y el pago.
                </p>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn btn-punto-primario">Volver al catálogo</a>
                </div>
            </div>
        </section>
    </main>

    <footer class="footer-punto py-4">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
