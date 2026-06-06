# PDF Werkzeugkasten

PDF Werkzeugkasten ist eine native Android-App in Kotlin mit Jetpack Compose und Material 3. Sie ist als mobile-first Alternative für häufige PDF-Aufgaben konzipiert: PDF komprimieren, zusammenführen, teilen, Bilder zu PDF, PDF drehen, schützen, entsperren, Vorschau, Verlauf, Werbung und Pro-Freischaltung.

## Status

Dieses Repository enthält ein vollständiges MVP-Projekt unter `app/`. Die frühere PWA-Spielstruktur bleibt unverändert im Repository, ist aber nicht Teil des Android-Builds.

## Kernfunktionen im MVP

- PDF-Auswahl über Storage Access Framework ohne breite Speicherberechtigungen.
- Bildauswahl über Android Photo Picker ohne Galerie-Permission.
- Lokale PDF-Verarbeitung über abstrahierte `PdfEngine`-Schnittstelle und PDFBox-Android.
- Bilder-zu-PDF über Android `PdfDocument`.
- Komprimieren, Zusammenführen, Seitenbereich extrahieren, Drehen, Passwort schützen und Passwort entfernen.
- Ergebnis teilen/öffnen/speichern über Android Share Sheet und FileProvider.
- Lokale Historie mit Room, ohne PDF-Inhalte zu speichern.
- Einstellungen mit DataStore.
- Google UMP Consent + Mobile Ads Initialisierung mit Test-App-ID.
- Google Play Billing Repository mit Restore-/Fehlerzuständen und lokalem Pro-Status.
- Onboarding, Home, Tool Flow, Processing, Result, History, Pro, Settings, Privacy Policy und Lizenzhinweise.
- Release-Konfiguration mit R8/ProGuard, `versionCode=1`, `versionName=1.0.0`, minSdk 26, targetSdk 35.

## Build-Anleitung

Voraussetzungen:

1. Android Studio oder Android SDK mit API 35.
2. JDK 17 für Android Gradle Plugin.
3. Google Play Console Produkt `lifetime_pro` für echte Käufe.
4. Eigene AdMob App-ID und Anzeigenblöcke vor Release eintragen.

Befehle:

```bash
gradle testDebugUnitTest
gradle assembleDebug
gradle bundleRelease
```

Für Release-Signing in Android Studio eine Keystore-Konfiguration hinterlegen oder Gradle Signing Configs über Umgebungsvariablen ergänzen. Das AAB entsteht unter `app/build/outputs/bundle/release/`.

## Libraries und Lizenzen

| Library | Zweck | Lizenzhinweis |
| --- | --- | --- |
| AndroidX Core, Activity, Lifecycle, Navigation, WorkManager, DataStore | App-Basis, Navigation, Hintergrundjobs, Einstellungen | Apache-2.0 |
| Jetpack Compose + Material 3 | UI | Apache-2.0 |
| Room | Lokale Historie | Apache-2.0 |
| Coil | Bildanzeige/Preview-Erweiterung | Apache-2.0 |
| PDFBox-Android (`com.tom-roush:pdfbox-android`) | PDF-Manipulation | Apache-2.0, keine AGPL-Pflicht |
| Google Mobile Ads SDK | Werbung | Google SDK Terms |
| Google UMP SDK | DSGVO-/Consent-Flow | Google SDK Terms |
| Google Play Billing | In-App-Käufe | Google Play Developer Terms |

AGPL-Bibliotheken wie iText AGPL werden bewusst nicht genutzt. Die PDF-Engine ist abstrahiert, damit bei Bedarf eine andere permissive oder kommerziell lizenzierte Engine eingesetzt werden kann.

## Play Store Listing

**Kurzbeschreibung**
PDFs komprimieren, zusammenführen, teilen und Bilder in PDFs umwandeln – schnell, einfach und lokal auf deinem Gerät.

**Lange Beschreibung**
PDF Werkzeugkasten ist deine einfache All-in-One-App für tägliche PDF-Aufgaben. Komprimiere große PDF-Dateien, führe mehrere PDFs zusammen, teile Seitenbereiche auf, wandle Bilder in PDFs um, exportiere PDF-Seiten als Bilder und schütze Dokumente mit Passwort.

Die App ist für schnelle Workflows gebaut: Datei auswählen, Option wählen, Ergebnis speichern oder teilen. Viele Vorgänge laufen lokal auf deinem Gerät, ohne dass deine Dokumente auf Server hochgeladen werden.

Funktionen:
• PDF komprimieren
• Mehrere PDFs zusammenführen
• PDF in einzelne Seiten oder Bereiche teilen
• Bilder zu PDF konvertieren
• PDF-Seiten als Bilder exportieren
• Seiten drehen
• PDF mit Passwort schützen
• Passwort entfernen, wenn du das Passwort kennst
• PDF-Vorschau
• Verlauf deiner letzten Vorgänge
• Teilen direkt aus anderen Apps

PDF Werkzeugkasten Pro entfernt Werbung und hebt Limits für Power-User auf.

**Keywords**
PDF komprimieren, PDF zusammenführen, Bilder zu PDF, PDF teilen, PDF konverter, PDF bearbeiten, PDF drehen, PDF schützen, PDF scanner, PDF tools

**Kategorie**: Produktivität oder Tools
**Content Rating**: Für alle Altersgruppen geeignet

## Screenshot-Plan

1. Home Screen mit allen Tools.
2. PDF komprimieren mit Vorher/Nachher-Größe.
3. PDFs zusammenführen mit Reihenfolge.
4. Bilder zu PDF Flow.
5. Ergebnis-Screen mit Speichern/Teilen.
6. Pro Screen.
7. Datenschutz-Hinweis „Dateien bleiben lokal“.

## Privacy Policy Entwurf

PDF Werkzeugkasten verarbeitet ausgewählte PDF- und Bilddateien lokal auf deinem Gerät. Wir betreiben im MVP keine eigenen Server für Dokumentverarbeitung und laden keine PDF-Inhalte ohne deine ausdrückliche Aktion hoch.

Gespeicherte Daten: Einstellungen, Pro-Status und Verlauf-Metadaten wie Werkzeugtyp, Dateiname, Datum, Eingabe-/Ausgabegröße und optional eine Ergebnis-URI. Originaldokumente werden nicht ungefragt kopiert.

Werbung: Google AdMob kann Gerätekennungen einschließlich Advertising ID, IP-Adresse und Nutzungsdaten verarbeiten. Für EU-Nutzer wird ein Consent-Flow über Google UMP angeboten. Ohne Zustimmung werden, soweit verfügbar, nicht-personalisierte Anzeigen genutzt.

In-App-Käufe: Google Play Billing verarbeitet Kaufstatus und Transaktionsdaten. Die App speichert den letzten bekannten Pro-Status lokal, damit Pro-Funktionen offline erhalten bleiben.

Crashlogs: Firebase Crashlytics ist im MVP nicht aktiviert. Falls später aktiviert, werden keine PDF-Inhalte in Crashlogs protokolliert.

Datenlöschung: Verlauf kann in der App gelöscht werden. App-Daten können über Android-Systemeinstellungen entfernt werden. Kontakt: support@example.com.

## Google Play Data Safety Vorbereitung

- Werbung/Ad-ID offenlegen, weil AdMob und `AD_ID` genutzt werden.
- Käufe offenlegen, weil Google Play Billing genutzt wird.
- App-Aktivität/Diagnosedaten nur offenlegen, wenn Crashlytics später aktiviert wird.
- Dokumentinhalte werden nicht an eigene Server gesendet.
- Nutzer kann Verlauf lokal löschen.

## Testplan

### Unit Tests
- Seitenbereich-Parser: gültige Bereiche, Duplikate, ungültige Seiten.
- Dateinamen-Generator: sichere Namen und Datumsnamen.
- Pro-Limit-Logik: Free/Pro-Grenzen.
- Kompressionsoptionen: Qualitätsstufen.

### Manuelle Tests
- PDF aus Dateimanager teilen und Tool-Auswahl prüfen.
- Einzelne PDF komprimieren und Ergebnis teilen.
- Mehrere PDFs zusammenführen.
- Bilder per Photo Picker auswählen und PDF erstellen.
- Seitenbereich `1-3, 5` extrahieren.
- Passwortgeschützte PDF mit falschem/richtigem Passwort öffnen.
- Keine Internetverbindung: App und Pro-Cache funktionieren, Ads/Billing zeigen Fehler ohne Absturz.
- Dark Mode und Rotation prüfen.

## Bekannte Einschränkungen

- PDF-Komprimierung ist im MVP konservativ und entfernt Metadaten; aggressive Bild-Rekompression in bestehenden PDFs ist als nächste Engine-Optimierung geplant.
- Drag-and-drop Sortierung ist als UX-Ziel definiert; MVP nutzt Auswahlreihenfolge und Entfernen/Neu-Auswahl.
- Billing enthält Repository- und Fehlerzustände; echte Produktdetails müssen nach Play-Console-Konfiguration ergänzt werden.
- Banner-/Interstitial-Platzierung nutzt SDK-Initialisierung; echte Anzeigenblöcke müssen vor Release eingesetzt werden.
- Firebase Crashlytics ist bewusst nicht aktiviert, um Datenschutz und Data-Safety-Angaben einfach zu halten.

## Nächste Verbesserungen nach MVP

- Echtes Thumbnail-Grid mit PdfRenderer und Zoom-Vorschau.
- Drag-and-drop Reordering für Merge und Bilder-zu-PDF.
- WorkManager-Fortschritt für sehr große Jobs und Wiederaufnahme nach App-Neustart.
- Batch-Komprimierung und Batch-Bilder-zu-PDF für Pro.
- Mehrsprachigkeit DE/EN und professionelle Store-Screenshots.
- Optional OCR/Cloud-Funktionen nur mit separater Zustimmung und transparenter Datenschutzerklärung.
