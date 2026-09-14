document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formularioSoporte");
    const asunto = document.getElementById("asunto");
    const mensaje = document.getElementById("mensaje");
    const contador = document.getElementById("contadorCaracteres");
    const botonEnviar = document.getElementById("btnEnviarSoporte");

    function actualizarContador() {
        const cantidad = mensaje.value.length;
        contador.textContent = cantidad + " / 1000";
    }

    mensaje.addEventListener("input", actualizarContador);
    actualizarContador();

    formulario.addEventListener("submit", function (event) {
        const asuntoTexto = asunto.value.trim();
        const mensajeTexto = mensaje.value.trim();

        if (asuntoTexto.length === 0) {
            event.preventDefault();
            alert("Debes ingresar un asunto.");
            asunto.focus();
            return;
        }

        if (mensajeTexto.length === 0) {
            event.preventDefault();
            alert("Debes describir tu inquietud.");
            mensaje.focus();
            return;
        }

        if (mensajeTexto.length > 1000) {
            event.preventDefault();
            alert("La inquietud no puede superar los 1000 caracteres.");
            mensaje.focus();
            return;
        }

        botonEnviar.disabled = true;
        botonEnviar.textContent = "Enviando...";
    });
});