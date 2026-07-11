package com.chocohead.advsolar.registry;

import com.chocohead.advsolar.AdvancedSolarPanels;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import ic2.core.item.ItemBlockIc2;
import net.minecraft.world.item.Rarity;
import com.chocohead.advsolar.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ASPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, AdvancedSolarPanels.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdvancedSolarPanels.MOD_ID);

    private static DeferredHolder<Item, Item> simple(String name) { return ITEMS.register(name, () -> new Item(new Item.Properties())); }
    public static final DeferredHolder<Item, Item> SUNNARIUM = simple("sunnarium"), SUNNARIUM_PART = simple("sunnarium_part"), SUNNARIUM_ALLOY = simple("sunnarium_alloy"), IRRADIANT_URANIUM = simple("irradiant_uranium"), ENRICHED_SUNNARIUM = simple("enriched_sunnarium"), ENRICHED_SUNNARIUM_ALLOY = simple("enriched_sunnarium_alloy"), IRRADIANT_GLASS_PANE = simple("irradiant_glass_pane"), IRIDIUM_IRON_PLATE = simple("iridium_iron_plate"), REINFORCED_IRIDIUM_IRON_PLATE = simple("reinforced_iridium_iron_plate"), IRRADIANT_REINFORCED_PLATE = simple("irradiant_reinforced_plate"), IRIDIUM_INGOT = simple("iridium_ingot"), URANIUM_INGOT = simple("uranium_ingot"), MT_CORE = simple("mt_core"), QUANTUM_CORE = simple("quantum_core");
    private static DeferredHolder<Item, Item> block(String name, java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> block, Rarity rarity) { return ITEMS.register(name, () -> new ItemBlockIc2(block.get(), new Item.Properties().rarity(rarity))); }
    public static final DeferredHolder<Item, Item> ADVANCED_SOLAR_PANEL = block("advanced_solar_panel", ASPBlocks.ADVANCED_SOLAR_PANEL, Rarity.UNCOMMON), HYBRID_SOLAR_PANEL = block("hybrid_solar_panel", ASPBlocks.HYBRID_SOLAR_PANEL, Rarity.RARE), ULTIMATE_SOLAR_PANEL = block("ultimate_solar_panel", ASPBlocks.ULTIMATE_SOLAR_PANEL, Rarity.EPIC), QUANTUM_SOLAR_PANEL = block("quantum_solar_panel", ASPBlocks.QUANTUM_SOLAR_PANEL, Rarity.EPIC), QUANTUM_GENERATOR = block("quantum_generator", ASPBlocks.QUANTUM_GENERATOR, Rarity.EPIC), MOLECULAR_TRANSFORMER = block("molecular_transformer", ASPBlocks.MOLECULAR_TRANSFORMER, Rarity.RARE);
    public static final DeferredHolder<Item, SolarHelmetItem> ADVANCED_SOLAR_HELMET = ITEMS.register("advanced_solar_helmet", () -> new SolarHelmetItem(ASPArmorMaterials.create("advanced_solar", false), new Item.Properties().rarity(Rarity.UNCOMMON), 1_000_000, 3_000, 3, 8, 1, 800, .9, false));
    public static final DeferredHolder<Item, SolarHelmetItem> HYBRID_SOLAR_HELMET = ITEMS.register("hybrid_solar_helmet", () -> new SolarHelmetItem(ASPArmorMaterials.create("hybrid_solar", true), new Item.Properties().rarity(Rarity.RARE), 10_000_000, 10_000, 4, 64, 8, 2000, 1, true));
    public static final DeferredHolder<Item, SolarHelmetItem> ULTIMATE_SOLAR_HELMET = ITEMS.register("ultimate_solar_helmet", () -> new SolarHelmetItem(ASPArmorMaterials.create("ultimate_solar", true), new Item.Properties().rarity(Rarity.EPIC), 10_000_000, 10_000, 4, 512, 64, 2000, 1, true));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.advanced_solar_panels")).icon(() -> ADVANCED_SOLAR_PANEL.get().getDefaultInstance()).displayItems((params, out) -> ITEMS.getEntries().forEach(e -> out.accept(e.get()))).build());

    private ASPItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
