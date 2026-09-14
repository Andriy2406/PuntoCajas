document.addEventListener("DOMContentLoaded", function () {

    const botonContinuar = document.querySelector(
        ".pago-mercado-pago .btn-punto-primario"
    );

    if (!botonContinuar) {
        return;
    }

    botonContinuar.addEventListener("click", function () {

        if (botonContinuar.dataset.procesando === "true") {
            return;
        }

        botonContinuar.dataset.procesando = "true";

        botonContinuar.classList.add("disabled");
        botonContinuar.setAttribute("aria-disabled", "true");

        botonContinuar.textContent =
            "Conectando con Mercado Pago...";

    });

});

