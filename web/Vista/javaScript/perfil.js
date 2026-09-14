document.addEventListener("DOMContentLoaded", function () {
    if (typeof inicializarMapaDireccion === "function") {
        inicializarMapaDireccion({
            mapaId: 'mapaDireccion',
            inputDireccion: 'direccion',
            inputBuscador: 'buscadorDireccion',
            resultadosId: 'resultadosBusquedaDireccion',
            inputLat: 'latitud',
            inputLng: 'longitud',
            latInicial: 4.7110,
            lngInicial: -74.0721,
            zoomInicial: 13,
            soloLectura: false
        });
    }
});