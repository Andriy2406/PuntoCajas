document.addEventListener("DOMContentLoaded", function () {

    const formularios = document.querySelectorAll(
        'form[action*="GestionarProduccionServlet"]'
    );

    formularios.forEach(function (formulario) {

        formulario.addEventListener("submit", function (evento) {

            const accion = formulario.querySelector(
                'input[name="accion"]'
            );

            if (!accion) {
                return;
            }

            if (accion.value === "cambiarEstado") {

                const estado = formulario.querySelector(
                    'select[name="estado"]'
                );

                if (estado && estado.value === "FINALIZADA") {

                    const confirmar = window.confirm(
                        "¿Está seguro de marcar esta orden como finalizada?"
                    );

                    if (!confirmar) {
                        evento.preventDefault();
                    }

                }

            }

        });

    });

});