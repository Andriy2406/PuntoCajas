<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Punto Cajas | Gestionar usuarios</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Vista/CSS/Admin.css" rel="stylesheet">
</head>
<body class="admin-page d-flex flex-column min-vh-100">

    <!-- Barra de navegación -->
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/CarritoServlet">Carrito</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/PerfilServlet">Mi perfil</a>
                            </li>
                            <c:if test="${sessionScope.usuarioActivo.idRol == 1 || sessionScope.usuarioActivo.idRol == 3}">
                                <li class="nav-item">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/AdminServlet">Administración</a>
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

    <!-- Contenido Principal -->
    <main class="admin-dashboard flex-grow-1 py-5">
        <div class="container-fluid px-lg-4">
            
            <!-- Alerta Dinámica -->
            <c:if test="${not empty sessionScope.mensajeAlerta}">
                <div class="alert alert-${sessionScope.tipoAlerta} alert-dismissible fade show shadow-sm border-0 rounded-4 mb-4" role="alert">
                    ${sessionScope.mensajeAlerta}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("mensajeAlerta"); %>
                <% session.removeAttribute("tipoAlerta"); %>
            </c:if>

            <!-- Encabezado y Barra de Acciones Alineados -->
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <div class="admin-header m-0 p-0 border-0 bg-transparent d-flex flex-row justify-content-between align-items-center flex-wrap gap-3">
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/AdminServlet" class="btn-volver-admin shadow-2xs">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                            </svg>
                            Volver al panel
                        </a>
                        <div>
                            <span class="admin-eyebrow">Gestión del Sistema</span>
                            <h1 class="admin-title">Gestionar usuarios</h1>
                            <p class="admin-description mb-0">Administra las cuentas de clientes y administradores del sistema con total seguridad.</p>
                        </div>
                    </div>
                    
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <!-- BARRA DE BÚSQUEDA POR CORREO -->
                        <div class="input-group input-group-sm shadow-2xs" style="max-width: 260px;">
                            <input type="text" id="inputBuscarCorreo" class="form-control border-end-0 rounded-start-pill ps-3" placeholder="Buscar por correo...">
                            <span class="input-group-text bg-white text-secondary border-start-0 rounded-end-pill pe-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                                    <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                                </svg>
                            </span>
                        </div>

                        <button type="button" class="btn btn-punto-primario btn-sm rounded-pill px-4 py-2 shadow-sm text-white" data-bs-toggle="modal" data-bs-target="#modalUsuario">
                            + Nuevo Usuario
                        </button>
                    </div>
                </div>
            </div>

            <!-- Tabla de Datos -->
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
                <div class="table-responsive p-0">
                    <table class="table table-hover align-middle mb-0" id="tablaUsuarios">
                        <thead class="table-light text-uppercase fs-7">
                            <tr>
                                <th class="py-3 ps-4">Nombre</th>
                                <th class="py-3">Apellido</th>
                                <th class="py-3">Documento</th>
                                <th class="py-3">Teléfono</th>
                                <th class="py-3">Correo</th>
                                <th class="py-3">Rol</th>
                                <th class="py-3">Estado</th>
                                <th class="py-3 text-center pe-4">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="usuario" items="${listaUsuarios}">
                                <tr>
                                    <td class="ps-4 fw-medium py-3">${usuario.nombre}</td>
                                    <td class="py-3">${usuario.apellido}</td>
                                    <td class="text-secondary small py-3">${usuario.identificacionUsuario}</td>
                                    <td class="text-secondary small py-3">${usuario.telefono}</td>
                                    <td class="col-correo text-secondary py-3">${usuario.correo}</td>
                                    <td class="py-3">
                                        <span class="badge bg-light text-dark border fw-normal">
                                            <c:choose>
                                                <c:when test="${usuario.idRol == 1}">Administrador</c:when>
                                                <c:when test="${usuario.idRol == 2}">Cliente</c:when>
                                                <c:when test="${usuario.idRol == 3}">Vendedor</c:when>
                                                <c:when test="${usuario.idRol == 4}">Conductor</c:when>
                                                <c:otherwise>Cliente</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td class="py-3">
                                        <c:choose>
                                            <c:when test="${usuario.estado}">
                                                <span class="badge bg-success text-white">Activo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary text-white">Inactivo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center pe-4 py-3">
                                        <div class="d-flex gap-2 justify-content-center">
                                            <!-- Botón Editar -->
                                            <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3 btn-editar-usuario" 
                                                    data-id="${usuario.idUsuario}" 
                                                    data-nombre="${usuario.nombre}" 
                                                    data-apellido="${usuario.apellido}" 
                                                    data-telefono="${usuario.telefono}" 
                                                    data-direccion="${usuario.direccion}" 
                                                    data-correo="${usuario.correo}">
                                                Editar
                                            </button>

                                            <!-- Botón Activar / Inactivar -->
                                            <form action="${pageContext.request.contextPath}/GestionarUsuariosServlet" method="POST" class="d-inline">
                                                <input type="hidden" name="id" value="${usuario.idUsuario}">
                                                <c:choose>
                                                    <c:when test="${usuario.estado}">
                                                        <input type="hidden" name="accion" value="inactivar">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3">Inactivar</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="hidden" name="accion" value="activar">
                                                        <button type="submit" class="btn btn-outline-success btn-sm rounded-pill px-3">Activar</button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listaUsuarios}">
                                <tr>
                                    <td colspan="8" class="text-center text-secondary py-5">No hay usuarios registrados actualmente.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal para Crear / Editar Usuario -->
    <div class="modal fade" id="modalUsuario" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <form id="formUsuario" action="${pageContext.request.contextPath}/GestionarUsuariosServlet" method="POST">
                    <input type="hidden" name="accion" id="inputAccion" value="crear">
                    <input type="hidden" name="id" id="inputIdUsuario">

                    <div class="modal-header bg-light px-4 py-3 border-bottom">
                        <h5 class="modal-title fw-bold" id="modalLabel">Registrar Nuevo Usuario</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    
                    <div class="modal-body p-4">
                        <!-- Fila 1: Nombre y Apellido -->
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Nombre</label>
                                <input type="text" class="form-control rounded-3" name="nombre" id="nombre" maxlength="50" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Apellido</label>
                                <input type="text" class="form-control rounded-3" name="apellido" id="apellido" maxlength="50" required>
                            </div>
                        </div>

                        <!-- Fila 2: Identificación y Teléfono -->
                        <div class="row">
                            <div class="col-md-6 mb-3" id="campoIdentificacion">
                                <label class="form-label">Nro. Identificación</label>
                                <input type="text" class="form-control rounded-3" name="identificacionUsuario" id="identificacionUsuario" maxlength="15">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Teléfono</label>
                                <input type="text" class="form-control rounded-3" name="telefono" id="telefono" maxlength="15">
                            </div>
                        </div>

                        <!-- Fila 3: Correo y Dirección -->
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Correo electrónico</label>
                                <input type="email" class="form-control rounded-3" name="correo" id="correo" maxlength="100" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Dirección</label>
                                <input type="text" class="form-control rounded-3" name="direccion" id="direccion" maxlength="100">
                            </div>
                        </div>
                        
                        <!-- Fila 4: Fecha y Contraseña -->
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Fecha de nacimiento / registro</label>
                                <input type="date" class="form-control rounded-3" name="fechaRegistro" id="fechaRegistro" required>
                                <div id="mensajeFecha" class="form-text"></div>
                            </div>
                            
                            <div class="col-md-6 mb-3" id="campoClave">
                                <label class="form-label">Contraseña</label>
                                <input type="password" class="form-control rounded-3" name="clave" id="clave" maxlength="50">
                                
                                <div class="progress mt-2" style="height: 5px;" id="barraFortalezaClave">
                                    <div class="progress-bar" id="barraFortalezaRelleno" role="progressbar" style="width: 0%; background: #dee2e6;"></div>
                                </div>
                                <div class="form-text" id="textoFortalezaClave">Usa mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo.</div>
                                
                                <ul id="listaRequisitos" class="list-unstyled mt-2 small">
                                    <li id="req-length" class="text-danger">❌ Mínimo 8 caracteres</li>
                                    <li id="req-upper" class="text-danger">❌ Al menos una letra mayúscula</li>
                                    <li id="req-lower" class="text-danger">❌ Al menos una letra minúscula</li>
                                    <li id="req-number" class="text-danger">❌ Al menos un número</li>
                                    <li id="req-special" class="text-danger">❌ Al menos un carácter especial</li>
                                </ul>

                                <div class="form-check mt-2">
                                    <input class="form-check-input" type="checkbox" id="mostrarContrasena">
                                    <label class="form-check-label small" for="mostrarContrasena">Mostrar contraseña</label>
                                </div>
                            </div>
                        </div>

                        <!-- Fila 5: Roles y Documento -->
                        <div class="row" id="camposExtra">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Rol del usuario</label>
                                <select class="form-select rounded-3" name="idRol">
                                    <option value="2">Cliente</option>
                                    <option value="3" selected>Vendedor</option>
                                    <option value="4">Conductor</option>
                                    <option value="1">Administrador</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Tipo de Documento</label>
                                <select class="form-select rounded-3" name="idDocumento" required>
                                    <option value="1" selected>Cédula de Ciudadanía</option>
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
                        </div>
                    </div>

                    <div class="modal-footer bg-light px-4 py-3 border-top">
                        <button type="button" class="btn btn-punto-secundario btn-sm rounded-pill px-3" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-punto-primario btn-sm rounded-pill px-4 text-white" id="btnGuardarModal">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Pie de página -->
    <footer class="footer-punto text-center">
        <div class="container">
            <span>&copy; 2026 Punto Cajas. Todos los derechos reservados.</span>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/PoliticaPrivacidad.jsp">Política de privacidad</a>
            <span class="mx-2">|</span>
            <a href="${pageContext.request.contextPath}/Vista/TerminosCondiciones.jsp">Términos y condiciones</a>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Vista/javaScript/GestionarUsuarios.js?v=2"></script>

</body>
</html>