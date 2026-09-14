document.addEventListener("DOMContentLoaded", function() {
    // 1. Filtrado dinámico por la barra de búsqueda
    const inputBuscar = document.getElementById("inputBuscarCatalogo");
    if (inputBuscar) {
        inputBuscar.addEventListener("keyup", function() {
            let filtro = inputBuscar.value.toLowerCase();
            let filas = document.querySelectorAll("#tablaCatalogos tbody tr");
            filas.forEach(fila => {
                let colNombre = fila.querySelector(".col-nombre");
                if (colNombre) {
                    let texto = colNombre.textContent.toLowerCase();
                    fila.style.display = texto.includes(filtro) ? "" : "none";
                }
            });
        });
    }

    // 2. Control de datos para el Modal (Crear vs Editar)
    const modalCatalogo = document.getElementById("modalCatalogo");
    if (modalCatalogo) {
        modalCatalogo.addEventListener("show.bs.modal", function(event) {
            let button = event.relatedTarget; // Botón que abrió el modal
            let inputAccion = document.getElementById("inputAccion");
            let inputId = document.getElementById("inputIdCatalogo");
            let inputNombre = document.getElementById("nombreCatalogo");
            let modalLabel = document.getElementById("modalLabel");

            // Validar si se presionó el botón de Editar
            if (button && button.classList.contains("btn-editar-catalogo")) {
                modalLabel.textContent = "Editar Catálogo";
                inputAccion.value = "actualizar";
                inputId.value = button.getAttribute("data-id");
                inputNombre.value = button.getAttribute("data-nombre");
            } 
            // Validar si se presionó el botón de Nuevo Catálogo
            else if (button && button.id === "btnNuevoCatalogo") {
                modalLabel.textContent = "Nuevo catálogo";
                inputAccion.value = "crear";
                inputId.value = "";
                inputNombre.value = "";
            }
        });
    }
});