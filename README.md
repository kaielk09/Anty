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
- `fabric/` — Fabric
- `forge/` — Forge
- `neoforge/` — NeoForge

## Kompatybilność wersji Minecraft

Docelowy zakres projektu to **Minecraft 1.21 i nowsze wydania, aż do najnowszego stabilnego**. Nie należy jednak zakładać, że obecny kod już obsługuje cały ten zakres.

Aktualna konfiguracja Gradle i metadane modułów nadal bazują na **Minecraft 1.21.1**. Nowsze wydania, w tym 26.x, wymagają osobnych konfiguracji loaderów, właściwej wersji Javy i ewentualnych zmian w API Minecrafta. Każda kombinacja wersji i loadera musi przejść osobną kompilację. Nie publikujemy jej jako wspieranej, dopóki test budowania nie przejdzie.

## Budowanie aktualnej konfiguracji

Wymagane: JDK 21 oraz Gradle.

```bash
gradle :fabric:build
gradle :forge:build
gradle :neoforge:build
```

Wynikowe pliki JAR powinny znaleźć się w `*/build/libs/`.

**Status:** źródła są w repozytorium. Kompilacja i test uruchomienia w Minecraft nie zostały jeszcze potwierdzone.