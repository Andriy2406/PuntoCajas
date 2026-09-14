document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("inputBuscarDireccion");
    input?.addEventListener("input", () => {
        const filtro = input.value.toLowerCase();
        document.querySelectorAll("#tablaVentas tbody tr").forEach(tr => {
            if (tr.classList.contains("fila-seguimiento")) return;
            const c = tr.querySelector(".col-direccion");
            const visible = !c || c.textContent.toLowerCase().includes(filtro);
            tr.style.display = visible ? "" : "none";
            const siguiente = tr.nextElementSibling;
            if (siguiente && siguiente.classList.contains("fila-seguimiento")) {
                siguiente.style.display = visible ? "" : "none";
            }
        });
    });

    const modal = document.getElementById("modalPedido");
    const form = document.getElementById("formPedido");
    let mapaPedido = null;

    document.querySelectorAll(".btn-editar-pedido").forEach(b => {
        b.addEventListener("click", () => {
            document.getElementById("modalLabel").textContent = "Seguimiento del pedido #" + b.dataset.id;
            document.getElementById("inputAccion").value = "actualizar";
            document.getElementById("inputIdPedido").value = b.dataset.id;
            document.getElementById("fecha").value = b.dataset.fecha;
            document.getElementById("direccionEnvio").value = b.dataset.direccion;
            document.getElementById("total").value = b.dataset.total;
            document.getElementById("estadoPedido").value = b.dataset.estado;
            document.getElementById("idConductor").value = b.dataset.conductor && b.dataset.conductor !== "null" ? b.dataset.conductor : "";

            const lat = parseFloat(b.dataset.lat);
            const lng = parseFloat(b.dataset.lng);
            document.getElementById("latitud").value = !isNaN(lat) ? lat : "";
            document.getElementById("longitud").value = !isNaN(lng) ? lng : "";

            bootstrap.Modal.getOrCreateInstance(modal).show();
        });
    });

    // El mapa de Leaflet solo se puede inicializar cuando el modal ya es
    // visible (necesita medir el contenedor), por eso se crea en "shown".
    modal?.addEventListener("shown.bs.modal", () => {
        if (typeof inicializarMapaDireccion !== "function") return;
        if (mapaPedido) {
            setTimeout(() => mapaPedido.mapa.invalidateSize(), 150);
            return;
        }
        mapaPedido = inicializarMapaDireccion({
            mapaId: 'mapaPedido',
            inputDireccion: 'direccionEnvio',
            inputLat: 'latitud',
            inputLng: 'longitud'
        });
    });

    modal?.addEventListener("hidden.bs.modal", () => {
        form.reset();
        document.getElementById("inputIdPedido").value = "";
    });
});
