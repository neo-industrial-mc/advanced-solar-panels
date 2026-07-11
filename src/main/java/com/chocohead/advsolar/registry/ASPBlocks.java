package com.chocohead.advsolar.registry;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.blockentity.*;
import ic2.core.block.tileentity.Ic2TileEntityBlock;
import ic2.core.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ASPBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(Registries.BLOCK, AdvancedSolarPanels.MOD_ID);

  private static Block.Properties properties() {
    return Block.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(3, 15)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
  }

  private static DeferredHolder<Block, Ic2TileEntityBlock> machine(
      String name, Class<? extends ic2.core.block.tileentity.Ic2TileEntity> be, boolean active) {
    return BLOCKS.register(
        name,
        () ->
            Ic2TileEntityBlock.create(
                properties(),
                be,
                active,
                Ic2TileEntityBlock.DefaultDrop.Self,
                Util.horizontalFacings,
                true));
  }

  public static final DeferredHolder<Block, Ic2TileEntityBlock> ADVANCED_SOLAR_PANEL =
      machine("advanced_solar_panel", AdvancedSolarPanelBlockEntity.class, false);
  public static final DeferredHolder<Block, Ic2TileEntityBlock> HYBRID_SOLAR_PANEL =
      machine("hybrid_solar_panel", HybridSolarPanelBlockEntity.class, false);
  public static final DeferredHolder<Block, Ic2TileEntityBlock> ULTIMATE_SOLAR_PANEL =
      machine("ultimate_solar_panel", UltimateSolarPanelBlockEntity.class, false);
  public static final DeferredHolder<Block, Ic2TileEntityBlock> QUANTUM_SOLAR_PANEL =
      machine("quantum_solar_panel", QuantumSolarPanelBlockEntity.class, false);
  public static final DeferredHolder<Block, Ic2TileEntityBlock> QUANTUM_GENERATOR =
      machine("quantum_generator", QuantumGeneratorBlockEntity.class, true);
  public static final DeferredHolder<Block, Ic2TileEntityBlock> MOLECULAR_TRANSFORMER =
      BLOCKS.register(
          "molecular_transformer",
          () ->
              Ic2TileEntityBlock.create(
                  properties()
                      .noOcclusion()
                      .lightLevel(s -> s.getValue(Ic2TileEntityBlock.ACTIVE) ? 12 : 0),
                  MolecularTransformerBlockEntity.class,
                  true,
                  Ic2TileEntityBlock.DefaultDrop.Self,
                  Util.horizontalFacings,
                  true));

  private ASPBlocks() {}

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
