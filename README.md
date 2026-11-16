# Purple Client

**Purple Client** to mod do Minecraft Java Edition. Nazwa repozytorium GitHub to nadal `Anty`.

## Co jest w projekcie

- GUI moda otwierane domyślnie klawiszem **Right Shift**.
- Osobna kategoria **Purple Client** w `Options → Controls → Key Binds`.
- Skrót można zmienić w standardowych ustawieniach sterowania Minecrafta.
- Przycisk **Ustawienia** otwiera ekran ustawień wewnątrz gry.
- Ciemny interfejs z fioletowym akcentem.

## Loadery

Projekt ma osobne moduły:
- `fabric/` — Fabric
- `forge/` — Forge
- `neoforge/` — NeoForge

## Wersja bazowa

Aktualny szkielet jest przygotowany dla **Minecraft 1.21.1** i Javy 21. Nie oznacza to jeszcze obsługi wszystkich wydań od 1.21 do najnowszego. Każdą kolejną wersję trzeba dopasować do API danego loadera i sprawdzić kompilacją.

## Budowanie

W katalogu głównym projektu, przy zainstalowanym Gradle i JDK 21:

```bash
gradle :fabric:build
gradle :forge:build
gradle :neoforge:build
```

Pliki JAR powinny pojawić się w odpowiednich katalogach `*/build/libs/`.

**Uwaga:** pliki źródłowe zostały dodane do repozytorium, ale kompilacja nie została jeszcze uruchomiona w tym środowisku. Nie traktuj tych JAR-ów jako przetestowanych wydań.
