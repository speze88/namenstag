# Namenstag – Benutzerhandbuch

## Was ist die Namenstag-App?

Die Namenstag-App zeigt dir die katholischen Namenstage des Tages an und gleicht sie automatisch mit deiner Kontaktliste ab. So verpasst du nie wieder den Namenstag eines Freundes oder Familienmitglieds.

---

## Installation

1. Öffne das Projekt in Android Studio
2. Verbinde ein Android-Gerät (ab Android 8.0) oder starte einen Emulator
3. Klicke auf **Run** (grüner Pfeil) oder drücke `Shift+F10`
4. Die App wird auf dem Gerät installiert und gestartet

---

## Die drei Hauptbereiche

Die App hat drei Bereiche, die über die untere Navigationsleiste erreichbar sind:

### 1. Heute

Der Startbildschirm zeigt alle Namenstage des heutigen Tages. Jede Karte enthält:

- **Name** des Heiligen
- **Patronat** (falls vorhanden)
- **Kurzbeschreibung**

Tippe auf eine Karte, um die vollständige Beschreibung des Heiligen zu sehen.

Oben rechts findest du ein **Lupen-Symbol** für die Namenssuche (siehe unten).

### 2. Kalender

Der Kalender ermöglicht es dir, Namenstage für beliebige Tage nachzuschlagen.

- **Monat wechseln:** Nutze die Pfeile links und rechts neben dem Monatsnamen
- **Tag auswählen:** Tippe auf einen Tag im Kalender
- Der heutige Tag ist farblich hervorgehoben
- Unter dem Kalender erscheinen die Namenstage des ausgewählten Tages

### 3. Kontakte

Hier siehst du, welche deiner Kontakte einen Namenstag haben. Beim ersten Öffnen wird die Berechtigung zum Lesen der Kontakte abgefragt.

Jeder Eintrag zeigt:
- **Name** des Kontakts
- **Zugeordneter Heiliger** mit Datum
- **Art der Zuordnung** (bei Alias- oder phonetischer Zuordnung)

Tippe auf einen Namenstag, um Details zum Heiligen zu sehen.

---

## Namenssuche

Über das **Lupen-Symbol** im Heute-Bildschirm gelangst du zur Namenssuche.

- Gib einen Namen (oder Teile davon) in das Suchfeld ein
- Die Ergebnisse erscheinen sofort während der Eingabe
- Jedes Ergebnis zeigt den **Namen**, das **Datum** des Namenstags und eine Kurzbeschreibung
- Tippe auf ein Ergebnis, um die vollständige Beschreibung des Heiligen zu sehen

**Beispiele:**
- Suche nach "Ben" findet "Benjamin" (31. März)
- Suche nach "Mar" findet "Maria", "Martin", "Markus", "Margarete" usw.

---

## Detailansicht eines Heiligen

Die Detailseite zeigt dir alle Informationen zu einem Heiligen:

- **Name**
- **Lebensdaten** (Geburts- und Sterbejahr, falls bekannt)
- **Historische Beschreibung** (2–4 Sätze)
- **Patronat** (wofür der Heilige Schutzpatron ist)
- **Namenstage** (alle zugehörigen Daten im Jahr)

Über den Zurück-Pfeil oben links gelangst du zur vorherigen Ansicht.

---

## Namens-Zuordnung

Die App verwendet drei Stufen, um Kontaktnamen mit Heiligennamen abzugleichen:

| Stufe | Beispiel | Erklärung |
|-------|----------|-----------|
| **Exakt** | Martin → Martin | Direkter Treffer (Groß-/Kleinschreibung wird ignoriert) |
| **Alias** | Hans → Johannes | Bekannte Kurzformen und Varianten werden erkannt |
| **Phonetisch** | Catarina → Katharina | Ähnlich klingende Namen werden über die Kölner Phonetik gefunden |

### Beispiele für erkannte Aliase

| Kontaktname | Wird zugeordnet zu |
|-------------|-------------------|
| Hans, Hannes, Jan | Johannes |
| Sepp, Peppi | Josef |
| Lisi, Lisa, Elsa | Elisabeth |
| Kathi, Katja | Katharina |
| Steffi | Stefan |
| Toni | Antonius |
| Niki, Klaus | Nikolaus |
| Michi | Michael |
| Hilde | Hildegard |
| Gitti | Brigitte |
| Vroni | Veronika |

---

## Tägliche Benachrichtigungen

Die App prüft jeden Morgen um **7:00 Uhr** automatisch, ob einer deiner Kontakte Namenstag hat. Falls ja, erhältst du eine Benachrichtigung mit:

- Name des Kontakts
- Name des zugehörigen Heiligen
- Kurzbeschreibung

### Voraussetzungen

- **Benachrichtigungs-Berechtigung:** Ab Android 13 muss die Berechtigung für Benachrichtigungen erteilt werden. Die App fragt beim ersten Start danach.
- **Kontakte-Berechtigung:** Muss erteilt sein, damit die App deine Kontakte abgleichen kann.
- **Akku-Optimierung:** Die Prüfung läuft batterieschonend. Bei aktiviertem Energiesparmodus kann sich die Benachrichtigung verzögern.

### Benachrichtigungen verwalten

Du kannst die Benachrichtigungen in den Android-Systemeinstellungen ein- und ausschalten:

1. Öffne **Einstellungen → Apps → Namenstag → Benachrichtigungen**
2. Aktiviere oder deaktiviere den Kanal **Tägliche Namenstage**

---

## Berechtigungen

| Berechtigung | Zweck |
|-------------|-------|
| **Kontakte lesen** | Abgleich der Vornamen mit der Namenstag-Datenbank |
| **Benachrichtigungen** | Tägliche Erinnerung an Namenstage deiner Kontakte |

Beide Berechtigungen sind optional. Ohne Kontakte-Berechtigung funktionieren der Heute- und Kalender-Bereich weiterhin – nur der Kontakt-Abgleich und die täglichen Benachrichtigungen sind dann nicht verfügbar.

---

## Datenbank

Die App enthält knapp **200 Heilige** mit historischen Beschreibungen und über **55 Namens-Aliase**. Alle Daten werden lokal auf dem Gerät gespeichert – es ist keine Internetverbindung nötig.

---

## Tipps

- **Namenstag heute prüfen:** Öffne einfach die App – der Heute-Bildschirm zeigt sofort die aktuellen Namenstage
- **Namenstag nachschlagen:** Nutze die Suche (Lupen-Symbol) oder wechsle zum Kalender
- **Name nicht in der Datenbank?** Versuche Kurzformen oder Varianten des Namens – die Suche findet auch Teilübereinstimmungen
- **Kontakt nicht gefunden?** Der Vorname muss in der Kontakte-App als separates Feld eingetragen sein (nicht nur als Anzeigename)
