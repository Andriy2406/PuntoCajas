document.addEventListener("DOMContentLoaded", function () {
    const mostrarContrasena = document.getElementById("mostrarContrasena");
    const clave = document.getElementById("clave");
    const confirmarClave = document.getElementById("confirmarClave");

    if (mostrarContrasena) {
        mostrarContrasena.addEventListener("change", function () {
            const tipo = this.checked ? "text" : "password";
            clave.type = tipo;
            confirmarClave.type = tipo;
        });
    }

    const campoFecha = document.getElementById("fecha_de_nacimiento");
    const mensajeFecha = document.getElementById("mensajeFecha");
    const formulario = document.getElementById("formRegistro");

    if (!campoFecha || !formulario) {
        return;
    }

    const hoy = new Date();
    
    // Fecha máxima permitida: hace exactamente 18 años (mayor de edad)
    const fechaMaxima = new Date(hoy.getFullYear() - 18, hoy.getMonth(), hoy.getDate());
    
    // Fecha mínima lógica permitida: hace 120 años (ej. año 1906) para evitar años como 1800
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

    function mostrarMensaje(mensaje, tipo) {
        mensajeFecha.textContent = mensaje;
        mensajeFecha.className = "form-text " + tipo;
    }

    function validarFecha() {
        if (!campoFecha.value) {
            mostrarMensaje("", "");
            return false;
        }

        const fechaNacimiento = new Date(campoFecha.value + "T00:00:00");
        const fechaActual = new Date();
        fechaActual.setHours(0, 0, 0, 0);

        if (fechaNacimiento > fechaActual) {
            mostrarMensaje("La fecha de nacimiento no puede ser futura.", "text-danger");
            campoFecha.value = "";
            return false;
        }

        const edad = calcularEdad(fechaNacimiento);

        if (edad < 18) {
            mostrarMensaje("El usuario debe ser mayor de edad (18 años o más).", "text-danger");
            campoFecha.value = "";
            return false;
        }

        if (edad > 120) {
            mostrarMensaje("Por favor, ingrese una fecha de nacimiento válida.", "text-danger");
            campoFecha.value = "";
            return false;
        }

        mostrarMensaje("Fecha válida. El usuario tiene " + edad + " años.", "text-success");
        return true;
    }

    campoFecha.addEventListener("change", validarFecha);

    formulario.addEventListener("submit", function (event) {
        if (!campoFecha.value) {
            event.preventDefault();
            mostrarMensaje("Debe ingresar una fecha de nacimiento.", "text-danger");
            campoFecha.focus();
            return;
        }

        if (!validarFecha()) {
            event.preventDefault();
            campoFecha.focus();
            return;
        }
    });
});
// ===== Medidor de fortaleza de contraseña =====
// Da retroalimentación visual inmediata mientras el usuario escribe,
// usando las mismas reglas que valida el servidor (Util/PasswordUtil.java):
// mínimo 8 caracteres + mayúscula + minúscula + número + carácter especial.
document.addEventListener("DOMContentLoaded", function () {
    const campoClave = document.getElementById("clave");
    const relleno = document.getElementById("barraFortalezaRelleno");
    const texto = document.getElementById("textoFortalezaClave");
    if (!campoClave || !relleno || !texto) return;

    function evaluarFortaleza(clave) {
        let puntos = 0;
        if (clave.length >= 8) puntos++;
        if (/[A-Z]/.test(clave)) puntos++;
        if (/[a-z]/.test(clave)) puntos++;
        if (/[0-9]/.test(clave)) puntos++;
        if (/[^A-Za-z0-9]/.test(clave)) puntos++;
        return puntos;
    }

    campoClave.addEventListener("input", function () {
        const puntos = evaluarFortaleza(campoClave.value);
        const porcentaje = (puntos / 5) * 100;
        relleno.style.width = porcentaje + "%";

        if (campoClave.value.length === 0) {
            relleno.style.background = "#dee2e6";
            texto.textContent = "Usa mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo (por ejemplo: Punto2026$).";
            texto.className = "texto-ayuda-clave";
        } else if (puntos <= 2) {
            relleno.style.background = "#dc3545";
            texto.textContent = "Contraseña débil: agrega mayúsculas, números y símbolos.";
            texto.className = "texto-ayuda-clave texto-clave-debil";
        } else if (puntos <= 4) {
            relleno.style.background = "#fd7e14";
            texto.textContent = "Contraseña media: te falta un requisito.";
            texto.className = "texto-ayuda-clave texto-clave-media";
        } else {
            relleno.style.background = "#198754";
            texto.textContent = "¡Contraseña segura!";
            texto.className = "texto-ayuda-clave texto-clave-fuerte";
        }
    });
});
