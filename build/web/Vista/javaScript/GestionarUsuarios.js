document.addEventListener("DOMContentLoaded", function () {
    // 1. Buscador dinámico por correo
    const buscador = document.getElementById("inputBuscarCorreo");
    buscador?.addEventListener("input", () => {
        const filtro = buscador.value.toLowerCase().trim();
        document.querySelectorAll("#tablaUsuarios tbody tr").forEach(tr => {
            const columnaCorreo = tr.querySelector(".col-correo");
            if (columnaCorreo) {
                const textoCorreo = columnaCorreo.textContent.toLowerCase();
                tr.style.display = (!filtro || textoCorreo.includes(filtro)) ? "" : "none";
            }
        });
    });

    // 2. Mostrar / Ocultar Contraseña
    const mostrarContrasena = document.getElementById("mostrarContrasena");
    const clave = document.getElementById("clave");

    if (mostrarContrasena && clave) {
        mostrarContrasena.addEventListener("change", function () {
            clave.type = this.checked ? "text" : "password";
        });
    }

    // 3. Validación de Fecha (Mayor de 18 años)
    const campoFecha = document.getElementById("fechaRegistro");
    const mensajeFecha = document.getElementById("mensajeFecha");
    const formUsuario = document.getElementById("formUsuario");

    if (campoFecha) {
        const hoy = new Date();
        const fechaMaxima = new Date(hoy.getFullYear() - 18, hoy.getMonth(), hoy.getDate());
        const fechaMinima = new Date(hoy.getFullYear() - 120, hoy.getMonth(), hoy.getDate());

        function convertirFecha(fecha) {
            const año = fecha.getFullYear();
            const mes = String(fecha.getMonth() + 1).padStart(2, "0");
            const dia = String(fecha.getDate()).padStart(2, "0");
            return año + "-" + mes + "-" + dia;
        }

        campoFecha.max = convertirFecha(fechaMaxima);
        campoFecha.min = convertirFecha(fechaMinima);

        function calcularEdad(fechaNacimiento) {
            const fechaActual = new Date();
            let edad = fechaActual.getFullYear() - fechaNacimiento.getFullYear();
            if (
                fechaActual.getMonth() < fechaNacimiento.getMonth() ||
                (fechaActual.getMonth() === fechaNacimiento.getMonth() && fechaActual.getDate() < fechaNacimiento.getDate())
            ) {
                edad--;
            }
            return edad;
        }

        function mostrarMensajeFecha(mensaje, tipo) {
            if (mensajeFecha) {
                mensajeFecha.textContent = mensaje;
                mensajeFecha.className = "form-text " + tipo;
            }
        }

        function validarFecha() {
            if (!campoFecha.value) {
                mostrarMensajeFecha("Debe ingresar una fecha.", "text-danger");
                return false;
            }

            const fechaIngresada = new Date(campoFecha.value + "T00:00:00");
            const fechaActual = new Date();
            fechaActual.setHours(0, 0, 0, 0);

            if (fechaIngresada > fechaActual) {
                mostrarMensajeFecha("La fecha no puede ser futura.", "text-danger");
                campoFecha.value = "";
                return false;
            }

            const edad = calcularEdad(fechaIngresada);

            if (edad < 18) {
                mostrarMensajeFecha("El usuario debe ser mayor de edad (18 años o más).", "text-danger");
                campoFecha.value = "";
                return false;
            }

            if (edad > 120) {
                mostrarMensajeFecha("Por favor, ingrese una fecha válida.", "text-danger");
                campoFecha.value = "";
                return false;
            }

            mostrarMensajeFecha("Fecha válida. El usuario tiene " + edad + " años.", "text-success");
            return true;
        }

        campoFecha.addEventListener("change", validarFecha);
        
        if(formUsuario) {
            formUsuario.addEventListener("submit", function(e) {
                // Prevenir envío si la validación falla (pero solo si el input existe y no se está editando una contraseña vacía)
                if (!validarFecha()) {
                     e.preventDefault();
                     campoFecha.focus();
                }
            });
        }
    }

    // 4. Medidor de Fortaleza de Contraseña y Requisitos
    const relleno = document.getElementById("barraFortalezaRelleno");
    const texto = document.getElementById("textoFortalezaClave");
    const reqLength = document.getElementById("req-length");
    const reqUpper = document.getElementById("req-upper");
    const reqLower = document.getElementById("req-lower");
    const reqNumber = document.getElementById("req-number");
    const reqSpecial = document.getElementById("req-special");

    function actualizarRequisitoUI(elemento, cumple, textoMensaje) {
        if (!elemento) return;
        if (cumple) {
            elemento.textContent = "✅ " + textoMensaje;
            elemento.classList.remove("text-danger");
            elemento.classList.add("text-success");
        } else {
            elemento.textContent = "❌ " + textoMensaje;
            elemento.classList.remove("text-success");
            elemento.classList.add("text-danger");
        }
    }

    function resetearFortaleza() {
        if(relleno) {
            relleno.style.width = "0%";
            relleno.style.background = "#dee2e6";
        }
        if(texto) {
            texto.textContent = "Usa mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo.";
            texto.className = "form-text";
        }
        actualizarRequisitoUI(reqLength, false, "Mínimo 8 caracteres");
        actualizarRequisitoUI(reqUpper, false, "Al menos una letra mayúscula");
        actualizarRequisitoUI(reqLower, false, "Al menos una letra minúscula");
        actualizarRequisitoUI(reqNumber, false, "Al menos un número");
        actualizarRequisitoUI(reqSpecial, false, "Al menos un carácter especial");
    }

    if (clave) {
        function evaluarRequisitos(pwd) {
            return {
                length: pwd.length >= 8,
                upper: /[A-Z]/.test(pwd),
                lower: /[a-z]/.test(pwd),
                number: /[0-9]/.test(pwd),
                special: /[^A-Za-z0-9]/.test(pwd)
            };
        }

        clave.addEventListener("input", function () {
            const val = clave.value;
            if (val.length === 0) {
                resetearFortaleza();
                return;
            }

            const reqs = evaluarRequisitos(val);

            actualizarRequisitoUI(reqLength, reqs.length, "Mínimo 8 caracteres");
            actualizarRequisitoUI(reqUpper, reqs.upper, "Al menos una letra mayúscula");
            actualizarRequisitoUI(reqLower, reqs.lower, "Al menos una letra minúscula");
            actualizarRequisitoUI(reqNumber, reqs.number, "Al menos un número");
            actualizarRequisitoUI(reqSpecial, reqs.special, "Al menos un carácter especial");

            let puntos = Object.values(reqs).filter(Boolean).length;

            if (relleno) {
                const porcentaje = (puntos / 5) * 100;
                relleno.style.width = porcentaje + "%";
            }

            if (texto) {
                if (puntos <= 2) {
                    if (relleno) relleno.style.background = "#dc3545";
                    texto.textContent = "Débil: agrega mayúsculas, números y símbolos.";
                    texto.className = "form-text text-danger";
                } else if (puntos <= 4) {
                    if (relleno) relleno.style.background = "#fd7e14";
                    texto.textContent = "Media: te falta un requisito.";
                    texto.className = "form-text text-warning";
                } else {
                    if (relleno) relleno.style.background = "#198754";
                    texto.textContent = "¡Contraseña segura!";
                    texto.className = "form-text text-success";
                }
            }
        });
    }

    // 5. Lógica del Modal para Crear / Editar (Oculta/Muestra campos según la acción)
    const modalElement = document.getElementById("modalUsuario");
    if (!modalElement) return;
    
    const modalInstance = new bootstrap.Modal(modalElement);
    const modalLabel = document.getElementById("modalLabel");
    const inputAccion = document.getElementById("inputAccion");
    const inputIdUsuario = document.getElementById("inputIdUsuario");

    const campoIdentificacion = document.getElementById("campoIdentificacion");
    const campoClave = document.getElementById("campoClave");
    const camposExtra = document.getElementById("camposExtra");
    
    const inputIdentificacion = document.getElementById("identificacionUsuario");

    document.querySelectorAll(".btn-editar-usuario").forEach(btn => {
        btn.addEventListener("click", function () {
            const dataset = this.dataset;

            modalLabel.textContent = "Editar Usuario";
            inputAccion.value = "actualizar";
            inputIdUsuario.value = dataset.id;

            document.getElementById("nombre").value = dataset.nombre || "";
            document.getElementById("apellido").value = dataset.apellido || "";
            document.getElementById("telefono").value = dataset.telefono || "";
            document.getElementById("direccion").value = dataset.direccion || "";
            document.getElementById("correo").value = dataset.correo || "";
            
            if (campoFecha) campoFecha.value = dataset.fecharegistro || dataset.fechanacimiento || ""; 
            if (mensajeFecha) {
                mensajeFecha.textContent = "";
                mensajeFecha.className = "form-text";
            }

            if (campoIdentificacion) campoIdentificacion.style.display = "none";
            if (campoClave) campoClave.style.display = "none";
            if (camposExtra) camposExtra.style.display = "none"; 

            if (inputIdentificacion) inputIdentificacion.removeAttribute("required");
            if (clave) clave.removeAttribute("required");

            modalInstance.show();
        });
    });

    modalElement.addEventListener("hidden.bs.modal", function () {
        if(formUsuario) formUsuario.reset();
        modalLabel.textContent = "Registrar Nuevo Usuario";
        inputAccion.value = "crear";
        inputIdUsuario.value = "";
        
        if (mensajeFecha) {
            mensajeFecha.textContent = "";
            mensajeFecha.className = "form-text";
        }
        if (mostrarContrasena) mostrarContrasena.checked = false;
        if (clave) clave.type = "password";
        resetearFortaleza();

        if (campoIdentificacion) campoIdentificacion.style.display = "block";
        if (campoClave) campoClave.style.display = "block";
        if (camposExtra) camposExtra.style.display = "flex";

        if (inputIdentificacion) inputIdentificacion.setAttribute("required", "true");
        if (clave) clave.setAttribute("required", "true");
    });
});