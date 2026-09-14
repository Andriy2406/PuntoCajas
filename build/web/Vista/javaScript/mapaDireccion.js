function inicializarMapaDireccion(opciones) {
    const cfg = Object.assign({
        mapaId: 'mapaDireccion',
        inputDireccion: 'direccion',
        inputBuscador: null,
        resultadosId: null,
        inputLat: 'latitud',
        inputLng: 'longitud',
        latInicial: 4.7110,
        lngInicial: -74.0721,
        zoomInicial: 13,
        soloLectura: false
    }, opciones || {});

    const contenedorMapa = document.getElementById(cfg.mapaId);
    if (!contenedorMapa || typeof L === 'undefined') return null;

    const inputLat = document.getElementById(cfg.inputLat);
    const inputLng = document.getElementById(cfg.inputLng);
    const inputDireccion = document.getElementById(cfg.inputDireccion);

    const latGuardada = inputLat && inputLat.value ? parseFloat(inputLat.value) : cfg.latInicial;
    const lngGuardada = inputLng && inputLng.value ? parseFloat(inputLng.value) : cfg.lngInicial;

    const mapa = L.map(cfg.mapaId).setView([latGuardada, lngGuardada], cfg.zoomInicial);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; colaboradores de OpenStreetMap'
    }).addTo(mapa);

    const marcador = L.marker([latGuardada, lngGuardada], { draggable: !cfg.soloLectura }).addTo(mapa);

    function fijarCoordenadas(lat, lng) {
        marcador.setLatLng([lat, lng]);
        if (inputLat) inputLat.value = lat.toFixed(7);
        if (inputLng) inputLng.value = lng.toFixed(7);
    }

    async function geocodificarInverso(lat, lng) {
        if (!inputDireccion) return;
        try {
            const resp = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`);
            const data = await resp.json();
            if (data && data.display_name) {
                inputDireccion.value = data.display_name;
            }
        } catch (e) {
            console.warn('No se pudo obtener la dirección desde el mapa:', e);
        }
    }

    if (!cfg.soloLectura) {
        mapa.on('click', function (e) {
            fijarCoordenadas(e.latlng.lat, e.latlng.lng);
            geocodificarInverso(e.latlng.lat, e.latlng.lng);
        });

        marcador.on('dragend', function () {
            const pos = marcador.getLatLng();
            fijarCoordenadas(pos.lat, pos.lng);
            geocodificarInverso(pos.lat, pos.lng);
        });
    }

    if (cfg.inputBuscador && cfg.resultadosId) {
        const buscador = document.getElementById(cfg.inputBuscador);
        const listaResultados = document.getElementById(cfg.resultadosId);
        let temporizador = null;

        buscador?.addEventListener('input', function () {
            clearTimeout(temporizador);
            let texto = buscador.value.trim();
            
            if (texto.length < 3) {
                listaResultados.style.display = 'none';
                listaResultados.innerHTML = '';
                return;
            }

            // Si el usuario escribe una dirección y no pone la ciudad, 
            // añadimos automáticamente "Bogota, Colombia" para que el motor la encuentre con éxito.
            let queryBusqueda = texto;
            if (!queryBusqueda.toLowerCase().includes('bogota') && !queryBusqueda.toLowerCase().includes('colombia')) {
                queryBusqueda += ', Bogota, Colombia';
            }

            temporizador = setTimeout(async () => {
                try {
                    const resp = await fetch(`https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&countrycodes=co&limit=6&q=${encodeURIComponent(queryBusqueda)}`);
                    const lugares = await resp.json();
                    listaResultados.innerHTML = '';
                    
                    if (!lugares.length) {
                        listaResultados.style.display = 'none';
                        return;
                    }

                    lugares.forEach(lugar => {
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.textContent = lugar.display_name;
                        btn.className = 'list-group-item list-group-item-action py-2 px-3 text-start small border-0 border-bottom';
                        btn.addEventListener('click', function () {
                            const lat = parseFloat(lugar.lat);
                            const lng = parseFloat(lugar.lon);
                            mapa.setView([lat, lng], 17);
                            fijarCoordenadas(lat, lng);
                            if (inputDireccion) inputDireccion.value = lugar.display_name;
                            listaResultados.style.display = 'none';
                            buscador.value = '';
                        });
                        listaResultados.appendChild(btn);
                    });
                    listaResultados.style.display = 'block';
                } catch (e) {
                    console.warn('Error buscando la dirección:', e);
                }
            }, 350);
        });

        document.addEventListener('click', function (e) {
            if (listaResultados && !listaResultados.contains(e.target) && e.target !== buscador) {
                listaResultados.style.display = 'none';
            }
        });
    }

    if (!inputLat?.value && inputDireccion?.value) {
        fetch(`https://nominatim.openstreetmap.org/search?format=json&countrycodes=co&limit=1&q=${encodeURIComponent(inputDireccion.value)}`)
            .then(r => r.json())
            .then(lugares => {
                if (lugares && lugares[0]) {
                    const lat = parseFloat(lugares[0].lat);
                    const lng = parseFloat(lugares[0].lon);
                    mapa.setView([lat, lng], 16);
                    fijarCoordenadas(lat, lng);
                }
            })
            .catch(() => {});
    }

    setTimeout(() => mapa.invalidateSize(), 250);

    return { mapa, marcador, fijarCoordenadas };
}