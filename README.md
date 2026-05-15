# Deep Core Miner

Deep Core Miner ist ein eigenständiges, mobile-first Simulationsspiel für den Play Store. Das Spiel nutzt eine einfache PWA-Struktur und kann später über Trusted Web Activity oder WebView in ein Android-Projekt eingebettet werden.

## Entwicklungsplan

1. **Kernschleife:** Spieler bauen Erz ab, erzeugen Hitze, verkaufen Ressourcen und erfüllen Aufträge.
2. **Progression:** Bohrer, Crew und Scanner verbessern Ausbeute, Automatisierung und Verkaufspreise.
3. **Mine:** Tiefere Schichten schalten wertvollere Adern wie Silberbruch, Smaragdhöhle und Sternenobsidian frei.
4. **Mobile Design:** Große Touch-Ziele, Sticky-Aktionsleiste, Hochformat-Layout und Safe-Area-Unterstützung.
5. **Play-Store-Vorbereitung:** Manifest, Service Worker und maskierbares SVG-Icon bilden die Grundlage für eine installierbare App.
6. **Nächste Ausbaustufe:** Speicherstände, Soundeffekte, Missionen, Daily Rewards, Android-Build-Pipeline und Store-Grafiken.

## Spielobjekte

- **Bohrmaschine:** Animierter Zentralbohrer im Canvas mit Hitzestatus und Partikeleffekten.
- **Erzadern:** Dynamisch gefärbte Schichten, die je nach Tiefe wertvoller werden.
- **Crew:** Kleine animierte Arbeiter erscheinen nach dem Crew-Upgrade und bauen automatisch ab.
- **Scanner:** Upgrade-Objekt für bessere Preise und Aderfunde.
- **Aufträge:** Fortschrittsbalken und Belohnungen geben klare Kurzzeitziele.

## Lokal starten

```bash
python3 -m http.server 4173
```

Öffne danach `http://localhost:4173` im Browser.
