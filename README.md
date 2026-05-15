# Deep Core Miner

Deep Core Miner ist ein eigenständiges, mobile-first Simulationsspiel für den Play Store. Das Spiel nutzt eine einfache PWA-Struktur und kann später über Trusted Web Activity oder WebView in ein Android-Projekt eingebettet werden.

## Entwicklungsplan

1. **Kernschleife:** Spieler bauen Erz ab, erzeugen Hitze, verkaufen Ressourcen und erfüllen Aufträge.
2. **Progression:** Bohrer, Crew und Scanner verbessern Ausbeute, Automatisierung und Verkaufspreise.
3. **Mine:** Tiefere Schichten schalten wertvollere Adern wie Silberbruch, Smaragdhöhle und Sternenobsidian frei.
4. **Mobile Design:** Große Touch-Ziele, Sticky-Aktionsleiste, Hochformat-Layout und Safe-Area-Unterstützung.
5. **Play-Store-Vorbereitung:** Manifest, Service Worker und maskierbares SVG-Icon bilden die Grundlage für eine installierbare App.
6. **Polish-Schicht:** Sanfte WebAudio-Sounds, Combo-Feedback, Floating Rewards, Button-Press-Effekte und reduzierte Animationen für empfindliche Nutzer.
7. **Nächste Ausbaustufe:** Speicherstände, Missionen, Daily Rewards, Android-Build-Pipeline, Store-Screenshots und Monetarisierungsbalance.

## Spielobjekte

- **Bohrmaschine:** Animierter Zentralbohrer im Canvas mit Hitzestatus, Lichtwellen, Funken und Floating-Rewards.
- **Erzadern:** Dynamisch gefärbte Schichten, Sternenhintergrund und pulsierende Adern, die je nach Tiefe wertvoller werden.
- **Crew:** Kleine animierte Arbeiter erscheinen nach dem Crew-Upgrade und bauen automatisch ab.
- **Scanner:** Animiertes Upgrade-Objekt für bessere Preise und Aderfunde.
- **Aufträge:** Fortschrittsbalken, Missions-KI und Belohnungsfeedback geben klare Kurzzeitziele.
- **Sounddesign:** Kurze, leise WebAudio-Töne für Mining, Verkauf, Upgrades, Kühlung und Fehler; jederzeit abschaltbar.

## Lokal starten

```bash
python3 -m http.server 4173
```

Öffne danach `http://localhost:4173` im Browser.
