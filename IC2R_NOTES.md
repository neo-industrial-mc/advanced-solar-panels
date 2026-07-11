# IC2R (NeoForge 1.21.1) architecture notes — for the ASP addon port

IC2R repo: `../IC2R` (absolute: `/Users/flame/Desktop/Experiments/mc/IC2R`). Mod id `ic2`,
maven `me.halfcooler.ic2r.core:ic2r:21.1.0`, ALREADY published to `../IC2R/repo` (file maven repo;
re-publish with `./gradlew publish` if IC2R changes). Built jar also at `../IC2R/build/libs/ic2-neoforge-21.1.0.jar`.
The jar ships BOTH `ic2.api.*` and `ic2.core.*` — the addon may extend internal `ic2.core` classes
(pin version 21.1.0).

## Build
- Plugin: ModDevGradle `net.neoforged.moddev` 2.0.141; Java 21; NeoForge 21.1.234; MC 1.21.1;
  Parchment 2024.11.17/1.21.1. Runs: client / server / gameTestServer / data, each with
  `systemProperty 'neoforge.enabledGameTestNamespaces', mod_id`.

## Energy (EU)
- API ifaces in `ic2.api.energy.tile`: `IEnergySource { getOfferedEnergy, drawEnergy, getSourceTier }`,
  `IEnergySink { getDemandedEnergy, getSinkTier, injectEnergy(Direction,double,double) }`,
  `IEnergyEmitter.emitsEnergyTo`, `IEnergyAcceptor`. `EnergyNet.instance.getPowerFromTier(int)`.
- IC2R machines DON'T implement these directly; they use the component
  `ic2.core.block.comp.Energy`: `Energy.asBasicSource(Ic2TileEntity parent, double capacity, int tier)`,
  `Energy.asBasicSink(...)`, `.addManagedSlot(InvSlotCharge/InvSlotDischarge)`. Methods:
  `addEnergy, useEnergy, getEnergy, getFreeEnergy, setCapacity, setSourceTier/SinkTier, getComparatorValue`.
  The component registers/unregisters delegates with the EnergyNet on load/unload.
- Public addon-facing prefabs also exist: `ic2.api.energy.prefab.BasicSource/BasicSink/BasicSinkSource`.

## Solar generator chain (reference impl)
- `ic2.core.block.generator.tileentity.TileEntitySolarGenerator` extends `TileEntityBaseGenerator`
  (ctor `super(type, pos, state, production, tier, maxStorage)`), which extends `TileEntityBase`/`Ic2TileEntity`.
- Sky logic: public static `TileEntitySolarGenerator.getSkyLight(Level, BlockPos)` — 0 if no dimension
  skylight; sun brightness from sun angle; rain/thunder reduce unless SANDY biome; scaled by sky light /15.
  Re-checked every 128 ticks. `@GuiSynced boolean sunlight`. `delayActiveUpdate()`.
- `TileEntityBaseGenerator` supplies: `Energy energy` source component, `InvSlotCharge chargeSlot`
  (managed), `updateEntityServer()` calling `gainFuel()/gainEnergy()/needsFuel()`, active state handling.

## Blocks / registration
- Machine block: `ic2.core.block.tileentity.Ic2TileEntityBlock.create(Properties, teClass, canActive,
  DefaultDrop, Set<Direction> facings, allowWrenchRotating)` — adds `facing` + `active` blockstate props,
  placement orientation, ticker, wrench support (`IWrenchAble`). `DefaultDrop.{Self,None,Generator,Machine,AdvMachine}`.
  `Util.horizontalFacings`.
- IC2R registers via internal `EnvProxy` facade with `DeferredRegister`s under modid `ic2` — the addon
  CANNOT reuse that; use standard NeoForge `DeferredRegister` under `advanced_solar_panels`.
- Ref-class pattern: `ic2.core.ref.Ic2Blocks` / `Ic2Items` / `Ic2BlockEntities` / `Ic2ScreenHandlers` /
  `Ic2RecipeTypes` / `Ic2RecipeSerializers` — static final fields + register helpers.

## GUI
- `ic2.core.ContainerBase<T>` / `ContainerFullInv<T>`, `SlotInvSlot`; screens per machine, OR the
  dynamic system: `ic2.core.gui.dynamic.DynamicContainer.create(...)` + `DynamicGui` parses XML from
  `assets/<ns>/guidef/<name>.xml` (slots, slotgrid, gauges, energygauge, buttons, playerInventory).
  BE implements `ic2.core.IHasGui` (`createServerScreenHandler`, `createClientScreenHandler`) and
  `IGuiValueProvider.getGuiValue(String)`; fields annotated `@ic2.core.network.GuiSynced` auto-sync.
  Original ASP guidef XMLs are in `parity/original-assets/.../guidef/` and use the same format.

## Items
- Electric item base: `ic2.core.item.BaseElectricItem(Properties, maxCharge, transferLimit, tier)`;
  manager `ic2.api.item.ElectricItem.manager` (`charge/discharge/use/getCharge`).
- Electric armor: `ic2.core.item.armor.ItemArmorElectric(Holder<ArmorMaterial>, EquipmentSlot,
  Properties, maxCharge, transferLimit, tier)`; abstract `getEnergyPerDamage()`,
  `getDamageAbsorptionRatio()`; see `ItemArmorBatpack` (canProvideEnergy pattern) and
  `ItemArmorNanoSuit` (per-tick effects / ElectricItem.manager use).
- Charging slot: `ic2.core.block.invslot.InvSlotCharge implements IChargingSlot`; `InvSlotDischarge`.

## Recipes (v2 JSON datapack)
- `RecipeType<RecipeHolder<I,O>>` (`ic2.core.recipe.v2.RecipeHolder` — record wrapping `MachineRecipe`,
  inert vanilla Recipe). Serializers: `BasicMachineRecipeSerializer`, `WeightedMachineRecipeSerializer`,
  `IntegerOutputRecipeSerializer` (+ optional JsonObject→CompoundTag metadata fn for extras like energy).
- JSON at `data/ic2/recipe/<machine>/*.json`, e.g. `{"type":"ic2:macerator","ingredient":{...},"result":[...]}`.
- Consumed via `ic2.api.recipe.Recipes.<machine>` (`IGetter<IBasicMachineRecipeManager>`) and
  `InvSlotProcessableGeneric(this, "input", 1, Recipes.macerator)` in `TileEntityStandardMachine`.
- **Matter Fabricator** (`TileEntityMatter`, `IntegerOutputRecipeSerializer`) is the closest analog
  for the Molecular Transformer (single input, energy-driven).
- `TileEntityStandardMachine<RI,RO,I>` (extends `TileEntityElectricMachine`) provides: Energy sink,
  `dischargeSlot`, `outputSlot`, `upgradeSlot` (`InvSlotUpgrade`, `IUpgradableBlock.getUpgradableProperties()`),
  progress sync `guiProgress`, overclocker handling via `setOverclockRates()`.

## Gametests
- `ic2.core.gametest.*` (~45 classes): `@GameTestHolder("ic2")` + `@PrefixGameTestTemplate(false)` on class,
  `@GameTest(template = "gametest/empty3x3x3", timeoutTicks = N)` on static methods taking `GameTestHelper`.
- Templates under `data/ic2/structure/gametest/…`. Helpers `Ic2GameTestUtil`, `Ic2GameTestAssertions`.
- `GeneratorGameTests.solarGeneratorChargesBatboxInDaylight` is the model for solar tests
  (set time/weather, `updateSunVisibility()`, assert adjacent BatBox energy gain).
- Wrench rotation re-registers energy delegates (`updateStateForEnergyNet`); see
  `stirlingGeneratorTransmitsAfterWrenchRotation`.

## Addon skeleton recommendation
- (a) Solar panels: extend `TileEntityBaseGenerator`, override `gainEnergy()` (day/night amounts,
  reuse `TileEntitySolarGenerator.getSkyLight`), `needsFuel/gainFuel` → false. Block via
  `Ic2TileEntityBlock.create(...)`. 4 charge slots: custom `InvSlot`+`IChargingSlot` like ASP's
  InvSlotMultiCharge, added as managed slots on the Energy component.
- (b) Molecular Transformer: extend `TileEntityStandardMachine` (or a custom sibling given MT's
  pure-energy progress), own RecipeType+serializer with `energy` field, own recipe manager.
- (c) Helmets: extend `ItemArmorElectric` with `inventoryTick` charging.
- Dev-runtime note: for the addon's runs to load IC2R as a mod with ModDevGradle, the maven dep may
  need to be on `additionalRuntimeClasspath`/declared as a run mod — verify what MDG 2.0.141 needs.
