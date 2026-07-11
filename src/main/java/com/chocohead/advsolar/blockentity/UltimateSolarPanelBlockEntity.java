package com.chocohead.advsolar.blockentity;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class UltimateSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
  public UltimateSolarPanelBlockEntity(BlockPos p, BlockState s) {
    super(ASPBlockEntities.ULTIMATE_SOLAR_PANEL.get(), p, s, 512, 64, 1000000, 3);
  }
}
