/**
 * Mapa de rutas, cálculo de tiempos aproximados y selección interactiva de paradas.
 */
document.addEventListener("DOMContentLoaded", function () {
    const contenedor = document.getElementById("mapaRutas");
    if (!contenedor || typeof L === "undefined") return;

    const paradas = (window.paradasConductor || []).filter(p => p.lat != null && p.lng != null);

    if (paradas.length === 0) {
        contenedor.innerHTML = '<div class="d-flex align-items-center justify-content-center h-100 text-secondary text-center p-4">'
            + 'Ninguno de tus pedidos asignados tiene una ubicación guardada en el mapa todavía.<br>'
            + 'Pídele al cliente que ubique su dirección en "Mi perfil".</div>';
        return;
    }

    // Inicializar mapa centrado en la primera parada
    const mapa = L.map("mapaRutas").setView([paradas[0].lat, paradas[0].lng], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; colaboradores de OpenStreetMap'
    }).addTo(mapa);

    const marcadores = {};
    const bounds = [];

    paradas.forEach((parada, i) => {
        const marcador = L.marker([parada.lat, parada.lng]).addTo(mapa);
        marcador.bindPopup(`<strong>Parada ${i + 1} · Pedido #${parada.id}</strong><br>${parada.direccion || ''}`);
        
        marcadores[parada.id] = marcador;
        bounds.push([parada.lat, parada.lng]);
    });

    mapa.fitBounds(bounds, { padding: [30, 30] });

    // Si hay paradas, consultamos el servicio OSRM para la ruta óptima, tiempos y distancias
    if (paradas.length >= 1) {
        const coords = paradas.map(p => `${p.lng},${p.lat}`).join(';');
        const url = `https://router.project-osrm.org/route/v1/driving/${coords}?overview=full&geometries=geojson`;

        fetch(url)
            .then(r => r.json())
            .then(data => {
                if (data && data.routes && data.routes[0]) {
                    const ruta = data.routes[0];
                    
                    // Dibujar la línea de ruta en el mapa
                    const linea = L.geoJSON(ruta.geometry, {
                        style: { color: '#4b2e83', weight: 5, opacity: 0.8 }
                    }).addTo(mapa);
                    mapa.fitBounds(linea.getBounds(), { padding: [30, 30] });

                    // Mostrar resumen global de tiempo y distancia
                    const totalMinutos = Math.round(ruta.duration / 60);
                    const totalKm = (ruta.distance / 1000).toFixed(1);

                    document.getElementById("tiempoTotalRuta").textContent = `${totalMinutos} minutos aprox.`;
                    document.getElementById("distanciaTotalRuta").textContent = `${totalKm} km`;
                    document.getElementById("resumenRutaContainer").classList.remove("d-none");

                    // Si hay tramos (legs), asignar tiempos estimados individuales a cada parada
                    if (ruta.legs && ruta.legs.length > 0) {
                        let acumuladoMinutos = 0;
                        ruta.legs.forEach((leg, index) => {
                            if (paradas[index + 1]) {
                                const idParadaSiguiente = paradas[index + 1].id;
                                acumuladoMinutos += Math.round(leg.duration / 60);
                                const kmTramo = (leg.distance / 1000).toFixed(1);

                                const badgeTramo = document.getElementById(`info-tramo-${idParadaSiguiente}`);
                                if (badgeTramo) {
                                    badgeTramo.querySelector(".tiempo-tramo").textContent = `${acumuladoMinutos} min`;
                                    badgeTramo.querySelector(".distancia-tramo").textContent = `${kmTramo} km`;
                                    badgeTramo.classList.remove("d-none");
                                }
                            }
                        });
                    }
                }
            })
            .catch(e => console.warn('No se pudo calcular la ruta sugerida:', e));
    }

    // Funcionalidad interactiva: Al hacer clic en una tarjeta de la lista, el mapa se centra en esa dirección
    document.querySelectorAll(".card-parada").forEach(card => {
        card.addEventListener("click", function () {
            const lat = parseFloat(this.getAttribute("data-lat"));
            const lng = parseFloat(this.getAttribute("data-lng"));
            const id = this.getAttribute("data-id");

            if (!isNaN(lat) && !isNaN(lng)) {
                mapa.setView([lat, lng], 16, { animate: true });
                if (marcadores[id]) {
                    marcadores[id].openPopup();
                }
                
                // Resaltar visualmente la tarjeta seleccionada
                document.querySelectorAll(".card-parada").forEach(c => c.classList.remove("border-primary"));
                this.classList.add("border-primary");
            }
        });
    });

    setTimeout(() => mapa.invalidateSize(), 250);
});