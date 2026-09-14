document.addEventListener("DOMContentLoaded", function () {
    // 1. Buscador dinámico por descripción
    const buscar = document.getElementById("inputBuscarDescripcion");
    if (buscar) {
        buscar.addEventListener("input", () => {
            const filtro = buscar.value.toLowerCase();
            document.querySelectorAll("#tablaProductos tbody tr").forEach(tr => {
                const columna = tr.querySelector(".col-descripcion");
                tr.style.display = !columna || columna.textContent.toLowerCase().includes(filtro) ? "" : "none";
            });
        });
    }

    // 2. Lógica para el botón Editar Producto
    const form = document.getElementById("formProducto");
    const modalEl = document.getElementById("modalProducto");

    document.querySelectorAll(".btn-editar-producto").forEach(btn => {
        btn.addEventListener("click", () => {
            document.getElementById("modalLabel").textContent = "Editar Producto";
            document.getElementById("inputAccion").value = "actualizar";
            document.getElementById("inputIdProducto").value = btn.dataset.id;
            document.getElementById("descripcion").value = btn.dataset.descripcion;
            document.getElementById("precio").value = btn.dataset.precio;
            document.getElementById("stockActual").value = btn.dataset.stock;
            document.getElementById("idCatalogo").value = btn.dataset.categoria;
            document.getElementById("url").value = btn.dataset.url || "";
            
            bootstrap.Modal.getOrCreateInstance(modalEl).show();
        });
    });

    // 3. Resetear el modal al cerrarlo
    if (modalEl) {
        modalEl.addEventListener("hidden.bs.modal", () => {
            form.reset();
            document.getElementById("inputAccion").value = "crear";
            document.getElementById("inputIdProducto").value = "";
            document.getElementById("modalLabel").textContent = "Registrar Nuevo Producto";
        });
    }

    // 4. Confirmación global para formularios con onsubmit
    document.querySelectorAll('form[onsubmit]').forEach(f => {
        f.addEventListener("submit", e => {
            if (!confirm("¿Está seguro de realizar esta acción?")) {
                e.preventDefault();
            }
        });
    });
});