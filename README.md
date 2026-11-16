# Purple Client

Purple Client to klientowy projekt moda do Minecraft Java Edition. Repozytorium nadal nazywa się **Anty**.

## Struktura

- `fabric/` — wydanie dla Fabric
- `forge/` — wydanie dla Forge
- `neoforge/` — wydanie dla NeoForge
- `strona/` — responsywna strona internetowa projektu

## Wersja docelowa

Projekt jest konfigurowany pod najnowsze stabilne wydanie Minecraft Java Edition **26.3** (stan na 16 listopada 2026).

- Minecraft: 26.3
- Java: 25
- Fabric Loader: 0.20.1
- Fabric API: 0.166.2+26.3
- Forge: 66.1.8
- NeoForge: 26.3.0.103

Każdy loader ma osobny plik JAR. Wydania będą udostępniane dopiero po poprawnej kompilacji i testach. Sama zmiana numeru wersji nie oznacza jeszcze potwierdzonej zgodności kodu.

## Strona

Otwórz `strona/index.html` w przeglądarce. Instrukcja publikacji znajduje się w `strona/README.md`.

## Budowanie

Wymagane JDK 25 oraz Gradle 9.6 lub zgodna wersja wymagana przez użyte pluginy.

```bash
gradle :fabric:build
gradle :forge:build
gradle :neoforge:build
```

Wyniki powinny znaleźć się w odpowiednich katalogach `*/build/libs/`.
