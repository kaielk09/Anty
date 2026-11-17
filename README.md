# Purple Client

Purple Client to klientowy projekt moda do Minecraft Java Edition. Repozytorium nadal nazywa się **Anty**.

## Struktura

- `fabric/` — wydanie dla Fabric
- `forge/` — wydanie dla Forge
- `neoforge/` — wydanie dla NeoForge
- Brak wymaganej strony internetowej — projekt jest modem publikowanym jako pliki JAR na Modrinth.

## Wersja docelowa

Projekt jest konfigurowany pod najnowsze stabilne wydanie Minecraft Java Edition **26.3** (stan na 16 listopada 2026).

- Minecraft: 26.3
- Java: 25
- Fabric Loader: 0.20.1
- Fabric API: 0.166.2+26.3
- Forge: 66.1.8
- NeoForge: 26.3.0.103

Każdy loader ma osobny plik JAR. Wydania będą udostępniane dopiero po poprawnej kompilacji i testach. Sama zmiana numeru wersji nie oznacza jeszcze potwierdzonej zgodności kodu.

## Budowanie

Wymagane JDK 25 oraz Gradle 9.6 lub zgodna wersja wymagana przez użyte pluginy.

```bash
gradle :fabric:build
gradle :forge:build
gradle :neoforge:build
```

Wyniki powinny znaleźć się w odpowiednich katalogach `*/build/libs/`.

## YouTube Music Holo

W wydaniu Fabric odtwarzacz YouTube Music jest osadzony bezpośrednio w oknie Minecrafta przez bibliotekę MCEF Modern (Chromium Embedded Framework). Do uruchomienia wymagany jest również mod MCEF Modern w wersji co najmniej 0.3.3. Po wejściu w moduł YouTube Music Holo wybierz **Otwórz YouTube Music w grze**. Logowanie odbywa się na stronie Google/YouTube wyświetlanej w osadzonym Chromium; Purple Client nie prosi o hasło i go nie zapisuje.

MCEF Modern obsługuje obecnie Minecraft 26.3 na Fabric. Dlatego osadzony odtwarzacz jest na razie dostępny wyłącznie w pliku Fabric. Wersje Forge i NeoForge pokazują informację o tym ograniczeniu i nie otwierają zewnętrznej przeglądarki.

MCEF Modern pobiera komponenty Chromium przy pierwszym uruchomieniu. Wymaga połączenia z internetem i dodatkowego miejsca na dysku. Google może ograniczać logowanie w osadzonych przeglądarkach — jeśli strona logowania odmówi działania, nie należy wpisywać hasła w żadnym innym oknie ani udostępniać go modowi.

Integracja nie odczytuje jeszcze metadanych aktualnego utworu do HUD ani nie udostępnia osobnych przycisków Stick/Fly. Te funkcje wymagają kolejnego etapu integracji i testów.
