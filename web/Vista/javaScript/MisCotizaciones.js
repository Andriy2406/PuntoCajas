document.addEventListener("DOMContentLoaded", function () {
    configurarBusqueda();
    configurarFiltroEstado();
    configurarLimpiarFiltros();
    configurarBotonesDetalles();
    abrirCotizacionSeleccionada();
    actualizarContador();
});

/* =========================================================
   BUSCADOR
========================================================= */
function configurarBusqueda() {
    const buscador = document.getElementById("buscarMisCotizaciones");
    if (!buscador) return;

    buscador.addEventListener("input", function () {
        filtrarCotizaciones();
    });
}

/* =========================================================
   FILTRO POR ESTADO
========================================================= */
function configurarFiltroEstado() {
    const filtro = document.getElementById("filtroMisCotizaciones");
    if (!filtro) return;

    filtro.addEventListener("change", function () {
        filtrarCotizaciones();
    });
}

/* =========================================================
   BOTÓN LIMPIAR FILTROS
========================================================= */
function configurarLimpiarFiltros() {
    const boton = document.getElementById("limpiarFiltrosMisCotizaciones");
    if (!boton) return;

    boton.addEventListener("click", function () {
        const buscador = document.getElementById("buscarMisCotizaciones");
        const filtro = document.getElementById("filtroMisCotizaciones");

        if (buscador) buscador.value = "";
        if (filtro) filtro.value = "";

        filtrarCotizaciones();
    });
}

/* =========================================================
   FILTRAR COTIZACIONES
========================================================= */
function filtrarCotizaciones() {
    const buscador = document.getElementById("buscarMisCotizaciones");
    const filtro = document.getElementById("filtroMisCotizaciones");

    const texto = buscador ? buscador.value.toLowerCase().trim() : "";
    const estado = filtro ? filtro.value : "";

    const tarjetas = document.querySelectorAll("#listaMisCotizaciones .filaCotizacionCliente");
    let visibles = 0;

    tarjetas.forEach(function (tarjeta) {
        const contenido = tarjeta.textContent.toLowerCase();
        const estadoTarjeta = tarjeta.dataset.estado || "";

        const coincideTexto = contenido.includes(texto);
        const coincideEstado = (estado === "" || estadoTarjeta === estado);

        const visible = coincideTexto && coincideEstado;

        tarjeta.style.display = visible ? "" : "none";

        if (visible) {
            visibles++;
        }
    });

    actualizarContador(visibles);

    const mensaje = document.getElementById("sinResultadosMisCotizaciones");
    if (mensaje) {
        mensaje.style.display = (visibles === 0) ? "block" : "none";
    }
}

/* =========================================================
   CONTADOR
========================================================= */
function actualizarContador(cantidad) {
    const contador = document.getElementById("contadorMisCotizaciones");
    if (!contador) return;

    if (cantidad === undefined) {
        const tarjetas = document.querySelectorAll("#listaMisCotizaciones .filaCotizacionCliente");
        cantidad = 0;
        tarjetas.forEach(function (tarjeta) {
            if (tarjeta.style.display !== "none") {
                cantidad++;
            }
        });
    }

    contador.textContent = cantidad;
}

/* =========================================================
   BOTONES VER / OCULTAR DETALLES
========================================================= */
function configurarBotonesDetalles() {
    const botones = document.querySelectorAll(".btn-ver-detalles");

    botones.forEach(function (boton) {
        const objetivoSelector = boton.getAttribute("data-bs-target");
        if (!objetivoSelector) return;

        const objetivo = document.querySelector(objetivoSelector);
        if (!objetivo) return;

        actualizarTextoBoton(boton, objetivo.classList.contains("show"));

        objetivo.addEventListener("shown.bs.collapse", function () {
            actualizarTextoBoton(boton, true);
        });

        objetivo.addEventListener("hidden.bs.collapse", function () {
            actualizarTextoBoton(boton, false);
        });
    });
}

/* =========================================================
   TEXTO DEL BOTÓN
========================================================= */
function actualizarTextoBoton(boton, abierto) {
    if (abierto) {
        boton.textContent = "Ocultar detalles ▲";
    } else {
        boton.textContent = "Ver detalles ▼";
    }
    boton.setAttribute("aria-expanded", abierto ? "true" : "false");
}

/* =========================================================
   ABRIR COTIZACIÓN SELECCIONADA
========================================================= */
function abrirCotizacionSeleccionada() {
    const id = document.body.dataset.cotizacionSeleccionada;
    if (!id) return;

    const elemento = document.getElementById("cotizacion-" + id);
    if (!elemento) return;

    const detalle = document.getElementById("detalleCotizacion-" + id);
    const boton = elemento.querySelector(".btn-ver-detalles");

    if (detalle) {
        const instancia = bootstrap.Collapse.getOrCreateInstance(detalle, {
            toggle: false
        });
        instancia.show();
    }

    setTimeout(function () {
        elemento.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    }, 250);

    if (boton) {
        setTimeout(function () {
            actualizarTextoBoton(boton, true);
        }, 300);
    }
}