document.addEventListener("DOMContentLoaded", function () {
    // 1. Control de incremento y decremento de cantidad
    const input = document.getElementById("cantidadDetalle");
    document.querySelectorAll("[data-cantidad-delta]").forEach(b => {
        b.addEventListener("click", () => {
            const max = parseInt(input.max || "1", 10);
            const actual = parseInt(input.value || "1", 10);
            const delta = parseInt(b.dataset.cantidadDelta, 10);
            
            input.value = Math.min(max, Math.max(1, actual + delta));
        });
    });

    // 2. Funcionalidad asíncrona para agregar productos al carrito vía AJAX
    const btn = document.getElementById("btnAgregarCarrito");
    if (btn) {
        btn.addEventListener("click", async () => {
            const datos = new URLSearchParams({
                accion: "agregar",
                idProducto: btn.dataset.idProducto,
                cantidad: input.value
            });

            try {
                const response = await fetch(btn.closest("body").dataset.context || "CarritoAjaxController", {
                    method: "POST",
                    body: datos
                });
                
                const data = await response.json();
                
                const mensaje = document.getElementById("mensajeDetalle");
                if (mensaje) {
                    mensaje.textContent = data.error || "¡Agregado al carrito!";
                    mensaje.style.display = "block";
                    mensaje.className = "mt-3 fw-bold " + (data.error ? "text-danger" : "text-success");
                }
            } catch (error) {
                console.error("Error al agregar al carrito:", error);
            }
        });
    }
});