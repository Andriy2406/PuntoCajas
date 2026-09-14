<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>

<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Punto Cajas | Preparando pago</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
      rel="stylesheet">

<link href="${pageContext.request.contextPath}/Vista/CSS/styles.css"
      rel="stylesheet">

<link href="${pageContext.request.contextPath}/Vista/CSS/pago-mercado-pago.css"
      rel="stylesheet">

</head>

<body>

<!-- BARRA DE NAVEGACIÓN -->
<nav class="navbar navbar-expand-md navbar-punto py-3">

    <div class="container">

        <a class="navbar-brand"
           href="${pageContext.request.contextPath}/InicioServlet">

            <svg class="logo-caja"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="2"
                 width="24"
                 height="24"
                 aria-hidden="true">

                <path d="M3 7l9-4 9 4-9 4-9-4z"/>
                <path d="M3 7v10l9 4 9-4V7"/>
                <path d="M12 11v10"/>

            </svg>

            Punto Cajas

        </a>

        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#menuPrincipal"
                aria-controls="menuPrincipal"
                aria-expanded="false"
                aria-label="Mostrar menú">

            <span class="navbar-toggler-icon"></span>

        </button>

        <div class="collapse navbar-collapse"
             id="menuPrincipal">

            <ul class="navbar-nav ms-auto align-items-md-center gap-md-2">

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/InicioServlet">
                        Inicio
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/CatalogoServlet">
                        Catálogo
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/CotizacionServlet">
                        Cotizar
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link active"
                       href="${pageContext.request.contextPath}/MisCotizacionesServlet">
                        Mis cotizaciones
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/Vista/Soporte.jsp">
                        Soporte
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/CarritoServlet">
                        Carrito
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/PerfilServlet">
                        Mi perfil
                    </a>
                </li>

                <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">

                    <li class="nav-item">

                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/AdminServlet">
                            Administración
                        </a>

                    </li>

                </c:if>

                <li class="nav-item ms-md-3">

                    <span class="nav-link">
                        Hola,
                        <strong>${sessionScope.usuarioActivo.nombre}</strong>
                    </span>

                </li>

                <li class="nav-item">

                    <a class="btn btn-punto-secundario btn-sm"
                       href="${pageContext.request.contextPath}/LogoutServlet">

                        Cerrar sesión

                    </a>

                </li>

            </ul>

        </div>

    </div>

</nav>


<!-- ENCABEZADO DE LA PÁGINA -->
<section class="hero-punto py-4">

    <div class="container">

        <h1 class="h3 mb-1">
            Preparando pago
        </h1>

        <p class="text-secondary mb-0">
            Estamos configurando tu pago de forma segura.
        </p>

    </div>

</section>


<!-- CONTENIDO PRINCIPAL -->
<main class="pago-mercado-pago">

    <div class="container">

        <div class="row justify-content-center">

            <div class="col-12 col-md-8 col-lg-6">

                <section class="contenedor-pago text-center">

                    <div class="icono-pago" aria-hidden="true">

                        <svg viewBox="0 0 24 24"
                             fill="none"
                             stroke="currentColor"
                             stroke-width="1.8">

                            <rect x="3" y="5" width="18" height="14" rx="2"/>
                            <path d="M3 10h18"/>
                            <path d="M7 15h3"/>

                        </svg>

                    </div>

                    <div class="spinner-pago"
                         role="status"
                         aria-label="Preparando pago">

                        <span class="visually-hidden">
                            Preparando pago...
                        </span>

                    </div>

                    <h2 class="h4">
                        Preparando tu pago
                    </h2>

                    <p class="texto-pago">

                        Estamos preparando el pago del anticipo de tu cotización
                        con Mercado Pago.

                    </p>

                    <p class="texto-pago-secundario">

                        Serás redirigido automáticamente en unos segundos.

                    </p>

                    <div class="mt-4">

                        <a class="btn btn-punto-primario"
                           href="${pageContext.request.contextPath}/MercadoPagoPagoServlet?id=${idCotizacionPago}">

                            Continuar al pago

                        </a>

                    </div>

                    <div class="aviso-seguridad mt-4">

                        <svg viewBox="0 0 24 24"
                             fill="none"
                             stroke="currentColor"
                             stroke-width="1.8"
                             aria-hidden="true">

                            <path d="M12 3l7 4v5c0 4.5-3 7.8-7 9-4-1.2-7-4.5-7-9V7l7-4z"/>
                            <path d="M9 12l2 2 4-4"/>

                        </svg>

                        <span>
                            Serás dirigido a la plataforma segura de Mercado Pago
                            para completar el pago.
                        </span>

                    </div>

                </section>

            </div>

        </div>

    </div>

</main>


<!-- PIE DE PÁGINA -->
<footer class="footer-punto">

    <div class="container text-center">

        <span>
            &copy; 2026 Punto Cajas. Todos los derechos reservados.
        </span>

        <span class="mx-2">|</span>

        <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">
            Política de privacidad
        </a>

        <span class="mx-2">|</span>

        <a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">
            Términos y condiciones
        </a>

    </div>

</footer>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script src="${pageContext.request.contextPath}/Vista/javaScript/pago-mercado-pago.js"></script>

</body>

</html>
