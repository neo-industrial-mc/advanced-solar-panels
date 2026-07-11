# Advanced Solar Panels → NeoForge 1.21.1 (IC2R addon) — Porting Spec

Source of truth: `parity/decompiled/` (CFR decompilation of ASP 4.3.0 for 1.12.2 / IC2 experimental)
and `parity/original-assets/` (extracted jar assets). Reference implementation for platform patterns:
the IC2R repo at `../IC2R` (NeoForge 1.21.1, mod id `ic2`).

Mod id: `advanced_solar_panels`. Display name: "Advanced Solar Panels". Depends on `ic2`.

## Content inventory

### Blocks (all: hardness 3.0, resistance 15.0, pickaxe, drops self, horizontal facing, metal material)
| Block | Rarity | Notes |
|---|---|---|
| `advanced_solar_panel` | Uncommon | solar generator |
| `hybrid_solar_panel` | Rare | solar generator |
| `ultimate_solar_panel` | Epic | solar generator ("Ultimate Hybrid Solar Panel") |
| `quantum_solar_panel` | Epic | solar generator |
| `quantum_generator` | Epic | configurable creative EU source; has active state |
| `molecular_transformer` | Rare | EU-driven item converter; non-full-cube shape, glows (light 12) when active |

### Solar panel mechanics (`TileEntitySolarPanel`)
Per-tier config (day EU/t, night EU/t, internal storage EU, source tier):
- Advanced: 8 / 1 / 32,000 / tier 1
- Hybrid: 64 / 8 / 100,000 / tier 2
- Ultimate: 512 / 64 / 1,000,000 / tier 3
- Quantum: 4096 / 2048 / 10,000,000 / tier 5

Behavior (server tick):
- Every 128 ticks re-evaluate `GenerationState` (NONE/DAY/NIGHT):
  - Needs sky access: dimension has sky AND `canSeeSky(pos.above())`.
  - DAY when it's daytime AND (biome can't rain OR no rain/thunder happening).
  - NIGHT otherwise (i.e. night time, or raining during the day). NIGHT still generates (moonlight power).
  - NONE when no sky access → generates nothing.
  - Biome rain capability (`canRain`) and dimension sky are cached on load.
- Each tick adds day/night power to internal `storage`, clamped at max.
- 4 charge slots (top side access): charges electric items at panel tier, drains `storage`.
- EU source on all sides: offers `min(storage, EnergyNet.getPowerFromTier(tier))`, `drawEnergy` subtracts.
- GUI: shows storage / max storage, "Max Output: <tierPower> EU/t", "Generating: <current> EU/t",
  sun or moon icon by state, 4 charge slots. (Texture: `textures/gui/advancedsolarpanel.png`, shared by all 4 panels.)
- Tooltip: power tier line.
- NBT: `storage` int.

### Quantum Generator (`TileEntityQuantumGenerator`)
- Infinite EU source (admin/creative utility): `drawEnergy` is a no-op, offers `production` EU/t while active.
- Defaults: production 512, tier 3. NBT-persisted (`production`, `tier` ints).
- Active = redstone level 0 (redstone signal disables it). Set active on placement.
- If production > power-for-tier it sends multiple packets per tick (IMultiEnergySource in 1.12;
  in IC2R adapt to whatever the energy net supports — the observable behavior: a suitable sink
  network receives `production` EU/t total).
- GUI buttons adjust production by ±1/±10/±100 (±5/±50/±500 with shift) clamped ≥ 0, and set tier 1–6
  (tier 6 displays "MAX"; tier > 5 means unlimited-tier packets).

### Molecular Transformer (`TileEntityMolecularAssembler`)
- EU sink, tier 14 (accepts any voltage), accepts energy from all sides.
- Recipe: 1 input item(+count) → 1 output item(+count) at a total EU cost. No time component:
  progress is purely energy delivered. Finishes the instant `energyUsed >= totalEU`.
- Flow: when idle and input slot has a matching recipe and output fits → consume input, become active,
  store `currentRecipe`. Demands `totalEU - energyUsed`. Each tick consumes buffered energy.
  On completion pushes output to output slot, resets. Leftover injected EU beyond the demand is refunded/carried.
- Goes inactive after 40 consecutive ticks with zero energy received.
- Comparator output: progress lerped 0→15.
- Light level 12 when active; custom smaller hitbox; fancy TESR (electron beams) — the TESR/model
  port is optional-nice-to-have; a static baked model is acceptable, keep the light + active state.
- GUI: input slot, output slot, vertical progress gauge, text lines Input/Output/Energy/EU in/Progress%
  (values: current recipe input name, output name, total EU needed, EU received last tick, percent).
- NBT: `energyUsed` double + `recipe` (the consumed input stack) — re-resolves recipe on load.
- **Recipes become a JSON datapack recipe type** `advanced_solar_panels:molecular_transformer`
  `{ "ingredient": {...}, "count": n, "result": {...}, "energy": <int EU> }` (replaces the 1.12 cfg file).

Default MT recipes to port (from Configs.fillDefault, adapted to 1.21.1/IC2R where items exist; skip
ones referencing mods/items that don't exist, keep vanilla+IC2R ones):
- wither skeleton skull → nether star, 250,000,000 EU
- iron ingot → IC2R iridium (ore/ingot as IC2R has it), 9,000,000 EU
- netherrack → 2 gunpowder, 70,000 EU
- sand → gravel, 50,000 EU ; dirt → clay(ball? original: clay block) 50,000 EU
- charcoal → coal, 60,000 EU
- glowstone dust → sunnarium part, 1,000,000 EU
- glowstone block → sunnarium, 9,000,000 EU
- yellow wool → glowstone, 500,000 ; blue wool → lapis block, 500,000 ; red wool → redstone block, 500,000
- coal → industrial diamond (IC2R), 9,000,000 ; industrial diamond → diamond, 1,000,000 (if IC2R has industrial diamond, else coal→diamond 9M)
- Skip OreDict lines for metals IC2R lacks (sapphire/ruby/titanium/etc.); include tin→silver, silver→gold,
  copper→nickel etc. only if IC2R provides those materials/tags.

### Items
`crafting` multi-item → 14 separate items (flat names, textures in `parity/original-assets/.../textures/items/`):
sunnarium, sunnarium_part, sunnarium_alloy, irradiant_uranium, enriched_sunnarium,
enriched_sunnarium_alloy, irradiant_glass_pane, iridium_iron_plate, reinforced_iridium_iron_plate,
irradiant_reinforced_plate, iridium_ingot, uranium_ingot, mt_core, quantum_core.
- Tag `c:ingots/uranium` on uranium_ingot, `c:ingots/iridium` on iridium_ingot.
- The 1.12 `double_stone_slab` item is dropped (obsolete in 1.21).

### Solar helmets (`ItemArmourSolarHelmet`) — 3 electric helmets
| | Advanced | Hybrid | Ultimate |
|---|---|---|---|
| generates day/night (EU/t) | 8 / 1 | 64 / 8 | 512 / 64 |
| charge tier | 3 | 4 | 4 |
| capacity (EU) | 1,000,000 | 10,000,000 | 10,000,000 |
| transfer limit | 3,000 | 10,000 | 10,000 |
| EU per damage point | 800 | 2,000 | 2,000 |
| absorption ratio | 0.9 | 1.0 | 1.0 |
| rarity | Uncommon | Rare | Epic |
| dyeable | no | yes | yes |

Behavior while worn (head slot), mirroring panel sky logic (state re-checked every 128 ticks at player pos):
- Generates day/night EU; charges other worn armor pieces first, then (config `chargeWholeInventory`,
  default true) offhand + main inventory electric items, remainder charges the helmet itself.
- Hybrid/Ultimate: when player air < 100 and helmet has ≥1000 EU, +200 air for 1000 EU.
- Electric armor: damage absorbed using stored EU (like IC2R nano/quantum armor — follow IC2R's
  electric armor implementation), 15% * ratio absorption share, no enchanting/repair.
- Not a fuel/energy provider; charge variants in creative tab if IC2R does that for electric items.

### Recipes (JSON, hard-recipe defaults; map 1.12 IC2 items to IC2R 1.21.1 equivalents — verify exact
registry names in IC2R; skip a recipe only if an ingredient truly has no IC2R equivalent, and note it)
- iridium_iron_plate: 8x iron plate around 1 iridium (ore-dict `ingotIridium` → tag/IC2R iridium)
- reinforced_iridium_iron_plate: alloy/carbon-plate ring around iridium_iron_plate ("ACA/CIC/ACA")
- irradiant_reinforced_plate: "RSR/LIL/RDR" R=redstone S=sunnarium_part L=lapis I=reinforced_iridium_iron_plate D=diamond
- irradiant_uranium: 4 glowstone dust around uranium ingot (+shape " G /GUG/ G ")
- irradiant_glass_pane: "GGG/UDU/GGG" G=reinforced glass U=irradiant_uranium D=glowstone dust
- sunnarium: 9 sunnarium_part (3x3); enriched_sunnarium: 8 irradiant_uranium around sunnarium
- sunnarium_alloy: 8 iridium around sunnarium; enriched_sunnarium_alloy: " S/SAS/ S" S=enriched_sunnarium A=sunnarium_alloy
- advanced_solar_panel: "PPP/ASA/CIC" P=irradiant_glass_pane A=advanced alloy S=IC2 solar panel C=advanced circuit I=irradiant_reinforced_plate
- hybrid_solar_panel: "PLP/IAI/CSC" P=carbon plate L=lapis block I=iridium A=advanced_solar_panel C=adv circuit S=enriched_sunnarium
- ultimate_solar_panel: " L /CSC/ECE" L=lapis block C=coal chunk S=advanced_solar_panel(!NOTE: original uses advanced panel here + separate 8-hybrid recipe) E=enriched_sunnarium_alloy; plus "SSS/SCS/SSS" S=hybrid_solar_panel C=adv circuit; plus shapeless ultimate→8 hybrid
- quantum_core: "ANA/NEN/ANA" A=enriched_sunnarium_alloy N=nether star E=ender eye
- quantum_solar_panel: 8 ultimate around quantum_core
- mt_core: "PRP/P P/PRP" P=irradiant_glass_pane R=thick neutron reflector
- molecular_transformer: "MTM/CcC/MTM" M=advanced machine casing T=EV transformer C=adv circuit c=mt_core
- advanced_solar_helmet: " S /CNC/GTG" S=advanced_solar_panel C=adv circuit N=nano helmet G=2xIns gold cable T=LV transformer
- hybrid_solar_helmet: " S /CQC/GTG" S=hybrid panel Q=quantum helmet G=glass fibre cable T=HV transformer (colour-carrying)
- ultimate helmet: same with ultimate panel; plus shapeless hybrid_helmet + ultimate_panel → ultimate helmet
- Machine recipes: compressor: iridium shard/ore→iridium_ingot, uranium (ore/crushed/purified/nuclear)→uranium_ingot — adapt to what IC2R's compressor recipe JSON supports.
- Armor dyeing for hybrid/ultimate helmets (vanilla dyeing if possible).

### Lang
Port `en_US.lang` (+ru_RU, zh_CN, zh_TW) to JSON lang files with modern keys
(`block.advanced_solar_panels.*`, `item.advanced_solar_panels.*`, `gui.advanced_solar_panels.*`).
"Ultimate Hybrid Solar Panel" is the display name of `ultimate_solar_panel`.
Note: original en_US has a bug (enriched_sunnarium_alloy shows "Sunnarium Alloy") — fix to "Enriched Sunnarium Alloy".

### Textures
All original textures are in `parity/original-assets/assets/advanced_solar_panels/textures/`
(blocks have spaces in filenames — rename to snake_case). Panels: top/side/bottom per panel.
Quantum generator: single texture + active variant. GUI textures reusable. Armor worn-textures in `armour/`.

## Gametests (the port must include NeoForge gametests)
1. Solar panel with open sky at noon reaches DAY state and accumulates storage; covered panel generates nothing.
2. Solar panel at night generates night power (hybrid+: nonzero; verify rate ratio).
3. Solar panel emits EU into an adjacent IC2R sink/storage (e.g. batbox equivalent) — storage drains.
4. Solar panel charges an electric item placed in a charge slot.
5. Molecular Transformer: given a recipe input + powered by a quantum generator (or injected energy),
   consumes exactly `energy` EU and produces the output; inactive after 40 unpowered ticks mid-recipe.
6. Molecular Transformer respects full output slot (doesn't start/void).
7. Quantum generator: emits configured production to adjacent sink; redstone signal stops emission.
8. NBT round-trip: panel storage persists (optional if covered by other tests).

## Non-goals for v1
- JEI integration, MT TESR beam animation, HUD modes on helmets, config file (use defaults as constants
  or a simple NeoForge config), double slab item, guide/manual pages.
