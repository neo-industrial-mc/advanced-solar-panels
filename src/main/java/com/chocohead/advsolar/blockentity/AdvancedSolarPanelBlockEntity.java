package com.chocohead.advsolar.blockentity;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class AdvancedSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
  public AdvancedSolarPanelBlockEntity(BlockPos p, BlockState s) {
    super(ASPBlockEntities.ADVANCED_SOLAR_PANEL.get(), p, s, 8, 1, 32000, 1);
  }
}
