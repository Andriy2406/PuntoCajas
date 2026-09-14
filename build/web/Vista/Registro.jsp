<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Crear cuenta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/registro.css" rel="stylesheet">
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
                                <a class="btn btn-punto-primario btn-sm" href="${pageContext.request.contextPath}/Vista/Login.jsp">Iniciar sesión</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="contenedor-formulario">
                <div class="text-center">
                    <h1>Crear cuenta</h1>
                    <p class="subtitulo">Regístrate para cotizar y comprar tus cajas.</p>
                </div>

                <c:if test="${not empty requestScope.mensaje}">
                    <div class="alert alert-success text-center" role="alert">
                        ${requestScope.mensaje}
                    </div>
                </c:if>

                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger text-center" role="alert">
                        ${requestScope.error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/RegistroServlet" method="POST" id="formRegistro" novalidate>
                    <h2>Datos personales</h2>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Tu nombre" minlength="2" maxlength="50" autocomplete="given-name" required>
                        </div>
                        <div class="col-md-6">
                            <label for="apellido" class="form-label">Apellido</label>
                            <input type="text" class="form-control" id="apellido" name="apellido" placeholder="Tu apellido" minlength="2" maxlength="50" autocomplete="family-name" required>
                        </div>
                    </div>

                    <h2>Documento de identidad</h2>
                    <div class="row g-3">
                        <div class="col-md-5">
                            <label for="tipoDocumento" class="form-label">Tipo de documento</label>
                            <select class="form-select" id="tipoDocumento" name="id_documento" required>
                                <option value="" selected disabled>Selecciona...</option>
                                <option value="1">Cédula de Ciudadanía</option>
                                <option value="2">Tarjeta de Identidad</option>
                                <option value="3">Pasaporte</option>
                                <option value="4">Cédula de Extranjería</option>
                                <option value="5">Registro Civil</option>
                                <option value="6">NIT</option>
                                <option value="7">Documento Nacional de Identidad</option>
                                <option value="8">Carné de Extranjería</option>
                                <option value="9">PEP</option>
                                <option value="10">Permiso por Protección Temporal</option>
                            </select>
                        </div>
                        <div class="col-md-7">
                            <label for="identificacion_usuario" class="form-label">Número de documento</label>
                            <input type="text" class="form-control" id="identificacion_usuario" name="identificacion_usuario" placeholder="Ej: 1020304050" minlength="5" maxlength="20" autocomplete="off" required>
                        </div>
                    </div>

                    <h2>Información de contacto</h2>
                    <div class="mb-3">
                        <label for="correo" class="form-label">Correo electrónico</label>
                        <input type="email" class="form-control" id="correo" name="correo" placeholder="nombre@correo.com" maxlength="100" autocomplete="email" required>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="telefono" class="form-label">Teléfono</label>
                            <input type="tel" class="form-control" id="telefono" name="telefono" placeholder="Ej: 3001234567" pattern="[0-9]{7,15}" maxlength="15" autocomplete="tel">
                            <div class="form-text">Ingresa entre 7 y 15 números.</div>
                        </div>
                        <div class="col-md-6">
                            <label for="fecha_de_nacimiento" class="form-label">Fecha de nacimiento</label>
                            <input type="date" class="form-control" id="fecha_de_nacimiento" name="fecha_de_nacimiento" autocomplete="bday" required>
                            <div id="mensajeFecha" class="form-text"></div>
                        </div>
                    </div>

                    <div class="mb-3 mt-3">
                        <label for="direccion" class="form-label">Dirección</label>
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Ej: Calle 10 # 20-30" maxlength="150" autocomplete="street-address">
                    </div>

                    <h2>Seguridad</h2>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="clave" class="form-label">Contraseña</label>
                            <input type="password" class="form-control" id="clave" name="clave" placeholder="Mínimo 8 caracteres" minlength="8" maxlength="100" autocomplete="new-password" pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}" title="Debe incluir mayúscula, minúscula, número y carácter especial" required>
                            <div class="barra-fortaleza-clave mt-2" id="barraFortalezaClave">
                                <div class="barra-fortaleza-relleno" id="barraFortalezaRelleno"></div>
                            </div>
                            <p class="texto-ayuda-clave" id="textoFortalezaClave">Usa mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo (por ejemplo: Punto2026$).</p>

                            <!-- Lista de requisitos interactiva con checks en tiempo real -->
                            <ul id="listaRequisitos" class="list-unstyled mt-2 small">
                                <li id="req-length" class="text-danger">❌ Mínimo 8 caracteres</li>
                                <li id="req-upper" class="text-danger">❌ Al menos una letra mayúscula</li>
                                <li id="req-lower" class="text-danger">❌ Al menos una letra minúscula</li>
                                <li id="req-number" class="text-danger">❌ Al menos un número</li>
                                <li id="req-special" class="text-danger">❌ Al menos un carácter especial</li>
                            </ul>
                        </div>
                        <div class="col-md-6">
                            <label for="confirmarClave" class="form-label">Confirmar contraseña</label>
                            <input type="password" class="form-control" id="confirmarClave" name="confirmarClave" placeholder="Repite tu contraseña" minlength="8" maxlength="100" autocomplete="new-password" required>
                        </div>
                    </div>

                    <div class="form-check mt-3">
                        <input class="form-check-input" type="checkbox" id="mostrarContrasena">
                        <label class="form-check-label" for="mostrarContrasena">
                            Mostrar contraseña
                        </label>
                    </div>

                    <div class="form-check mt-4 mb-4">
                        <input class="form-check-input" type="checkbox" id="autorizacionDatos" name="autorizacionDatos" value="true" required>
                        <label class="form-check-label" for="autorizacionDatos">
                            Autorizo el tratamiento de mis datos personales conforme a la <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">política de privacidad</a>.
                        </label>
                    </div>

                    <button type="submit" class="btn btn-punto-primario w-100">Crear cuenta</button>
                </form>

                <p class="text-center mt-4 mb-0">
                    ¿Ya tienes cuenta?
                    <a href="${pageContext.request.contextPath}/Vista/Login.jsp">Inicia sesión aquí</a>
                </p>
            </div>
        </div>
    </main>

    <footer class="footer-punto">
        <div class="container text-center">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a><span class="mx-2">|</span><a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/registro.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/passwordStrength.js"></script>
</body>
</html>