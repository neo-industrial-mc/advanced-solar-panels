# Advanced Solar Panels (NeoForge 1.21.1, IC2R addon)

A parity port of SeNtiMeL/Chocohead's **Advanced Solar Panels 4.3.0** (1.12.2 IC2 addon) to
NeoForge 1.21.1 as an addon for **IndustrialCraft 2: Refactored** (the local fork at `../IC2R`).

## Content
- **Solar panels**: Advanced (8/1 EU/t day/night, tier 1), Hybrid (64/8, tier 2),
  Ultimate Hybrid (512/64, tier 3), Quantum (4096/2048, tier 5); each with internal storage
  and four charge slots. Binary day/night/blocked generation matching the 1.12 original
  (rain demotes day to night; no sky access stops generation).
- **Quantum Generator**: configurable infinite EU source (production/tier via GUI, redstone disables).
- **Molecular Transformer**: converts items using pure EU (no processing time), tier-14 sink.
  Recipes are a datapack recipe type `advanced_solar_panels:molecular_transformer`:
  `{"ingredient": {...}, "count": 1, "result": {"item": "...", "count": 1}, "energy": 50000}`.
- **Solar helmets**: Advanced/Hybrid/Ultimate electric helmets that generate EU while worn and
  charge worn armor, then inventory items, then themselves. Hybrid+ refill air underwater and are dyeable.
- 14 crafting components (sunnarium chain, iridium plates, cores), full recipe graph
  (hard-recipe defaults), 4 languages.

## Build / test
```
./gradlew build              # requires ../IC2R published to its local repo (./gradlew publish there)
./gradlew spotlessCheck      # verify formatting (also runs as part of check/build)
./gradlew spotlessApply      # format source and project text files
./gradlew runGameTestServer  # 11 gametests
./gradlew runClient
```

## Reference material
- `PORTING_SPEC.md` — behavioral spec extracted from the original.
- `IC2R_NOTES.md` — IC2R architecture map used for the port.
- `parity/` — original 4.3.0 jar, CFR-decompiled sources, and original assets.

## Known deviations from 4.3.0
- No config file: 1.12 config values are constants (defaults).
- Molecular Transformer uses a full-block collision shape and no animated beam TESR.
- No JEI category, helmet HUD modes, or double stone slab item.
- Molecular Transformer recipes moved from a cfg file to datapack JSON.
