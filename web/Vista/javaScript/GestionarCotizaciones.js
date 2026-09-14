document.addEventListener("DOMContentLoaded", function () {
    configurarBusqueda();
    configurarFiltroEstado();
    configurarCalculoComercial();
});

function configurarBusqueda() {
    const buscador = document.getElementById("buscarCotizacion");
    if (buscador) buscador.addEventListener("input", filtrarCotizaciones);
}

function configurarFiltroEstado() {
    const filtro = document.getElementById("filtroEstado");
    if (filtro) filtro.addEventListener("change", filtrarCotizaciones);
}

function filtrarCotizaciones() {
    const buscador = document.getElementById("buscarCotizacion");
    const filtro = document.getElementById("filtroEstado");
    const texto = buscador ? buscador.value.toLowerCase().trim() : "";
    const estado = filtro ? filtro.value : "";

    const filas = document.querySelectorAll("#tablaCotizaciones tbody .filaCotizacion");
    let visibles = 0;

    filas.forEach(function (fila) {
        const contenido = fila.textContent.toLowerCase();
        const estadoFila = fila.dataset.estado || "";
        const visible = contenido.includes(texto) && (estado === "" || estadoFila === estado);
        fila.style.display = visible ? "" : "none";
        if (visible) visibles++;
    });

    const mensaje = document.getElementById("sinResultados");
    if (mensaje) mensaje.style.display = visibles === 0 ? "block" : "none";
}

const IVA = 0.19;
const ANTICIPO = 0.50;

function obtenerNumero(valor) {
    if (valor === null || valor === undefined || valor === "") return 0;
    const numero = Number.parseFloat(String(valor).replace(",", "."));
    return Number.isFinite(numero) ? numero : 0;
}

function formatearMoneda(valor) {
    return new Intl.NumberFormat("es-CO", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(valor);
}

function calcularComercial(id) {
    const cantidadEl = document.getElementById("cantidad" + id);
    const unitarioEl = document.getElementById("valorUnitario" + id);
    const subtotalEl = document.getElementById("subtotal" + id);
    const ivaEl = document.getElementById("iva" + id);
    const totalEl = document.getElementById("total" + id);
    const anticipoEl = document.getElementById("anticipoCalculo" + id);

    if (!cantidadEl || !unitarioEl || !subtotalEl || !ivaEl || !totalEl || !anticipoEl) return;

    const cantidad = obtenerNumero(cantidadEl.value);
    const unitario = obtenerNumero(unitarioEl.value);
    const subtotal = cantidad * unitario;
    const iva = subtotal * IVA;
    const total = subtotal + iva;
    const anticipo = total * ANTICIPO;

    subtotalEl.value = formatearMoneda(subtotal);
    ivaEl.value = formatearMoneda(iva);
    totalEl.value = formatearMoneda(total);
    anticipoEl.value = formatearMoneda(anticipo);
}

function configurarCalculoComercial() {
    document.querySelectorAll("[id^='valorUnitario']").forEach(function (input) {
        const id = input.id.replace("valorUnitario", "");
        input.addEventListener("input", function () {
            calcularComercial(id);
        });
        calcularComercial(id);
    });

    document.querySelectorAll("[id^='cotizar']").forEach(function (modal) {
        modal.addEventListener("shown.bs.modal", function () {
            calcularComercial(modal.id.replace("cotizar", ""));
        });
    });
}

window.calcularComercial = calcularComercial;
