package com.chocohead.advsolar.registry;

import com.chocohead.advsolar.AdvancedSolarPanels;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import com.chocohead.advsolar.blockentity.*;

public final class ASPBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AdvancedSolarPanels.MOD_ID);

    private static <T extends net.minecraft.world.level.block.entity.BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> block) {
        return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, block.get()).build(null));
    }
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedSolarPanelBlockEntity>> ADVANCED_SOLAR_PANEL = register("advanced_solar_panel", AdvancedSolarPanelBlockEntity::new, ASPBlocks.ADVANCED_SOLAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HybridSolarPanelBlockEntity>> HYBRID_SOLAR_PANEL = register("hybrid_solar_panel", HybridSolarPanelBlockEntity::new, ASPBlocks.HYBRID_SOLAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UltimateSolarPanelBlockEntity>> ULTIMATE_SOLAR_PANEL = register("ultimate_solar_panel", UltimateSolarPanelBlockEntity::new, ASPBlocks.ULTIMATE_SOLAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuantumSolarPanelBlockEntity>> QUANTUM_SOLAR_PANEL = register("quantum_solar_panel", QuantumSolarPanelBlockEntity::new, ASPBlocks.QUANTUM_SOLAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuantumGeneratorBlockEntity>> QUANTUM_GENERATOR = register("quantum_generator", QuantumGeneratorBlockEntity::new, ASPBlocks.QUANTUM_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MolecularTransformerBlockEntity>> MOLECULAR_TRANSFORMER = register("molecular_transformer", MolecularTransformerBlockEntity::new, ASPBlocks.MOLECULAR_TRANSFORMER);

    private ASPBlockEntities() { }
    public static void register(IEventBus modEventBus) { BLOCK_ENTITY_TYPES.register(modEventBus); }
}
