document.addEventListener("DOMContentLoaded", function () {
    const buscador = document.getElementById("buscadorProductos");
    const filtro = document.getElementById("filtroCategoria");

    function filtrarProductos() {
        const texto = (buscador?.value || "").toLowerCase().trim();
        const categoria = (filtro?.value || "").toLowerCase().trim();
        const tarjetas = document.querySelectorAll(".tarjeta-producto");
        let visibles = 0;

        tarjetas.forEach(function (tarjeta) {
            const nombre = (tarjeta.dataset.nombre || "").toLowerCase();
            const catTarjeta = (tarjeta.dataset.categoria || "").toLowerCase();

            const coincideTexto = !texto || nombre.includes(texto);
            const coincideCategoria = !categoria || catTarjeta === categoria;

            if (coincideTexto && coincideCategoria) {
                tarjeta.style.display = "";
                visibles++;
            } else {
                tarjeta.style.display = "none";
            }
        });

        const sinResultados = document.getElementById("sinResultados");
        if (sinResultados) {
            sinResultados.style.display = (visibles === 0) ? "block" : "none";
        }
    }

    buscador?.addEventListener("input", filtrarProductos);
    filtro?.addEventListener("change", filtrarProductos);
});

// Funciones globales accesibles desde los botones onclick del JSP
function cambiarCantidad(id, delta, stockMax) {
    let input = document.getElementById('cantidad_' + id);
    if (!input) return;
    let valor = parseInt(input.value) + delta;
    if (valor >= 1 && valor <= stockMax) {
        input.value = valor;
    }
}

function procesarCarrito(idProducto, accion) {
    let inputCantidad = document.getElementById('cantidad_' + idProducto);
    let cantidad = inputCantidad ? inputCantidad.value : 1;
    let url = window.contextPath + '/CarritoAjaxController';

    let datos = new URLSearchParams();
    datos.append('accion', accion);
    datos.append('idProducto', idProducto);
    if (accion === 'agregar') {
        datos.append('cantidad', cantidad);
    }

    fetch(url, {
        method: 'POST',
        body: datos
    })
    .then(response => response.json().then(data => ({ ok: response.ok, data })))
    .then(({ ok, data }) => {
        let mensajeDiv = document.getElementById('mensaje_' + idProducto);

        if (!ok || data.error) {
            if (mensajeDiv) {
                mensajeDiv.textContent = data.error || "No se pudo actualizar el carrito.";
                mensajeDiv.style.color = "#dc3545";
                mensajeDiv.style.display = 'block';
                setTimeout(() => mensajeDiv.style.display = 'none', 3500);
            }
            return;
        }

        if (mensajeDiv) {
            if (accion === 'agregar') {
                mensajeDiv.textContent = "¡Agregado al carrito!";
                mensajeDiv.style.color = "#198754";
            } else {
                mensajeDiv.textContent = "Se quitó del carrito";
                mensajeDiv.style.color = "#dc3545";
            }

            mensajeDiv.style.display = 'block';
            setTimeout(() => mensajeDiv.style.display = 'none', 3000);
        }

        // Badge global del carrito (en el menú superior)
        let badgeCarrito = document.getElementById('badge-carrito');
        if (badgeCarrito && data) {
            badgeCarrito.textContent = data.totalItems;
        }

        // Cantidad de ESTE producto específico que ya está en el carrito
        let badgeProducto = document.getElementById('enCarrito_' + idProducto);
        if (badgeProducto) {
            const cantidadProducto = data.cantidadProducto || 0;
            badgeProducto.textContent = cantidadProducto > 0
                ? ('En tu carrito: ' + cantidadProducto + ' unidad' + (cantidadProducto === 1 ? '' : 'es'))
                : '';
        }
    })
    .catch(error => console.error('Error en la petición AJAX:', error));
}