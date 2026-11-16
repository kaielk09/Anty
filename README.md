# Purple Client

**Purple Client** to mod klientowy do Minecraft Java Edition. Nazwa repozytorium pozostaje **Anty**.

## Funkcje interfejsu

- Menu otwierane domyślnie klawiszem **Right Shift**.
- Ponowne naciśnięcie tego samego klawisza zamyka menu.
- Skrót można zmienić w **Opcje → Sterowanie → Klawisze**.
- Ekran ustawień dostępny z poziomu menu.
- Ciemny, fioletowy interfejs z boczną nawigacją i kartami informacyjnymi.
- Menu nie zatrzymuje gry w tle.

## Loadery

Osobne moduły znajdują się w katalogach:
- fabric/ — Fabric
- forge/ — Forge
- neoforge/ — NeoForge

## Aktualna wersja

Pierwszym przygotowanym celem jest **Minecraft Java 1.21.0**:
- Fabric Loader 0.15.11 + Fabric API 0.102.0+1.21
- Forge 51.0.33
- NeoForge 21.0.167
- Java 21

Każdy loader ma osobny moduł i osobny plik JAR. Nie oznacza to jeszcze obsługi 1.21.1–1.21.11 — te wersje będą dodawane i sprawdzane osobno.

## Budowanie

Wymagane: JDK 21 oraz Gradle.

Uruchom:
    gradle :fabric:build
    gradle :forge:build
    gradle :neoforge:build

Wynikowe pliki JAR powinny znaleźć się w katalogach */build/libs/.

**Status:** konfiguracje źródłowe zostały przygotowane dla Minecraft 1.21.0. Kompilacja i test uruchomienia w grze nie zostały jeszcze potwierdzone.
