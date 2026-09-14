document.addEventListener("DOMContentLoaded", () => {
    const mostrarContrasena = document.getElementById("mostrarContrasena");
    const campoClave = document.getElementById("clave");

    if (!mostrarContrasena || !campoClave) return;

    mostrarContrasena.addEventListener("change", function () {
        campoClave.type = this.checked ? "text" : "password";
    });
});