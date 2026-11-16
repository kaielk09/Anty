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

YouTube Music Holo nie wymaga logowania ani klucza API. Wyszukiwanie otwiera wyniki w YouTube Music, a przyciski odtwarzania, pauzy i zmiany utworu wysyłają systemowe klawisze multimedialne. Działanie sterowania zależy od systemu operacyjnego i aktywnej aplikacji lub przeglądarki obsługującej multimedia.

Mod nie odczytuje aktualnego tytułu utworu i nie steruje bezpośrednio kolejką YouTube Music — do tego potrzebna byłaby autoryzowana integracja. Tryby Stick i Fly pozostają ustawieniami interfejsu; renderowanie HUD/tekstu w świecie wymaga osobnej implementacji.
