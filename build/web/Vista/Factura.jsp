<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Factura ${factura.numeroFactura}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/styles.css" rel="stylesheet">
</head>
<body class="bg-light">
    <main class="container py-5" style="max-width: 900px;">
        <div class="border rounded p-4 p-md-5 shadow-sm bg-white">
            
            <!-- Encabezado de Factura -->
            <div class="d-flex justify-content-between align-items-start mb-4">
                <div>
                    <h1 class="h3 fw-bold text-dark mb-0">Punto Cajas</h1>
                    <p class="text-secondary mb-0">Comprobante de venta electrónico</p>
                </div>
                <div class="text-end">
                    <span class="badge bg-secondary mb-1">Factura No. FV-${factura.numeroFactura}</span><br>
                    <c:if test="${factura.idDocCon > 0}">
                        <small class="text-muted d-block">Consecutivo serie ${factura.codigoCon}</small>
                    </c:if>
                    <small class="text-muted fw-semibold">Pedido #${factura.idPedido}</small>
                </div>
            </div>

            <hr class="mb-4">

            <!-- Datos del Cliente en Tarjeta de dos columnas -->
            <div class="row bg-light p-3 rounded border mb-4">
                <div class="col-md-6 mb-2 mb-md-0">
                    <p class="mb-1"><strong>Cliente:</strong> ${sessionScope.usuarioActivo.nombre} ${sessionScope.usuarioActivo.apellido}</p>
                    <p class="mb-0"><strong>Documento:</strong> ${sessionScope.usuarioActivo.identificacionUsuario}</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p class="mb-1"><strong>Fecha de emisión:</strong> <%= java.time.LocalDate.now()%></p>
                    <p class="mb-0"><strong>Dirección:</strong> ${sessionScope.usuarioActivo.direccion}</p>
                </div>
            </div>

            <!-- Tabla de Resumen de Productos -->
            <h2 class="h5 mb-3 fw-bold">Detalle de productos</h2>
            <div class="table-responsive mb-4">
                <table class="table table-bordered align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th class="text-center" style="width: 10%;">Cant.</th>
                            <th>Descripción</th>
                            <th class="text-end" style="width: 25%;">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${sessionScope.ultimoPedido}">
                            <tr>
                                <td class="text-center fw-bold">${item.cantidad}</td>
                                <td>${item.descripcion}</td>
                                <td class="text-end">$${item.subtotal}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Total -->
            <div class="d-flex justify-content-end align-items-center mb-4">
                <div class="text-end">
                    <h4 class="fw-bold text-success mb-0">Total a Pagar: $${factura.total}</h4>
                </div>
            </div>

            <!-- Nota aclaratoria -->
            <div class="alert alert-warning small mb-4">
                <strong>Aviso legal:</strong> Este comprobante de la aplicación no equivale por sí solo a una factura electrónica validada por la DIAN. 
                Para operación fiscal real se deben configurar los datos tributarios, numeración autorizada y transmisión electrónica correspondientes.
            </div>

            <!-- Botón de retorno -->
            <div class="text-start">
                <a class="btn btn-success px-4" href="${pageContext.request.contextPath}/CatalogoServlet">Volver al catálogo</a>
            </div>

        </div>
    </main>
</body>
</html>