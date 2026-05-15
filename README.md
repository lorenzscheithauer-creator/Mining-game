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

## Testen

Führe diese schnellen Checks aus, bevor du eine Änderung mergst:

```bash
node --check src/game.js
node --check service-worker.js
python3 -m http.server 4173
curl -I http://localhost:4173/index.html
curl -I http://localhost:4173/styles.css
curl -I http://localhost:4173/src/game.js
curl -I http://localhost:4173/manifest.webmanifest
```

Manuell im Browser prüfen:

1. `http://localhost:4173` öffnen.
2. Mehrfach auf **Abbauen** oder direkt auf die Mine tippen.
3. Prüfen, ob Erz, Tiefe, Combo, Partikel, Floating-Text und Sound reagieren.
4. **Erz verkaufen**, **Bohrer kühlen** und alle Upgrade-Buttons testen.
5. Auf einem schmalen/mobile Viewport prüfen, ob die untere Aktionsleiste sticky bleibt und Buttons gut bedienbar sind.

## Mergekonflikte lösen

Aktuell enthält das Repository keine Konfliktmarker. Wenn Git trotzdem einen Mergekonflikt meldet, gehe so vor:

```bash
git status --short
rg -n "(<){7}|(=){7}|(>){7}" .
```

Dann jede betroffene Datei öffnen und die Konfliktbereiche bereinigen:

```text
[Startmarker HEAD]
Version aus deinem Branch
[Trennmarker]
Version aus dem anderen Branch
[Endmarker branch-name]
```

Entscheide pro Block, welche Version bleiben soll, oder kombiniere beide Änderungen. Danach alle Konfliktmarker entfernen und erneut prüfen:

```bash
rg -n "(<){7}|(=){7}|(>){7}" .
node --check src/game.js
node --check service-worker.js
git add <datei>
git commit
```

Wenn der Konflikt nur in generierten oder Cache-Dateien steckt, lieber die echte Quelle bearbeiten und keine kaputten Marker committen.
