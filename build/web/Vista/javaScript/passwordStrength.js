// Medidor de fortaleza y lista de requisitos interactiva para contraseñas.
document.addEventListener("DOMContentLoaded", function () {
    const campoClave = document.getElementById("clave");
    const relleno = document.getElementById("barraFortalezaRelleno");
    const texto = document.getElementById("textoFortalezaClave");

    const reqLength = document.getElementById("req-length");
    const reqUpper = document.getElementById("req-upper");
    const reqLower = document.getElementById("req-lower");
    const reqNumber = document.getElementById("req-number");
    const reqSpecial = document.getElementById("req-special");

    if (!campoClave) return;

    function evaluarRequisitos(clave) {
        return {
            length: clave.length >= 8,
            upper: /[A-Z]/.test(clave),
            lower: /[a-z]/.test(clave),
            number: /[0-9]/.test(clave),
            special: /[^A-Za-z0-9]/.test(clave)
        };
    }

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

    campoClave.addEventListener("input", function () {
        const val = campoClave.value;
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
            if (val.length === 0) {
                if (relleno) relleno.style.background = "#dee2e6";
                texto.textContent = "Seguridad de la contraseña"; // Texto inicial o vacío si prefieres
                texto.className = "texto-ayuda-clave";
            } else if (puntos <= 2) {
                if (relleno) relleno.style.background = "#dc3545";
                texto.textContent = "Débil";
                texto.className = "texto-ayuda-clave texto-clave-debil";
            } else if (puntos <= 4) {
                if (relleno) relleno.style.background = "#fd7e14";
                texto.textContent = "Media";
                texto.className = "texto-ayuda-clave texto-clave-media";
            } else {
                if (relleno) relleno.style.background = "#198754";
                texto.textContent = "Fuerte";
                texto.className = "texto-ayuda-clave texto-clave-fuerte";
            }
        }
    });
});