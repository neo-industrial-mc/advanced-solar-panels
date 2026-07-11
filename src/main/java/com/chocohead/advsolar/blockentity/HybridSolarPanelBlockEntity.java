package com.chocohead.advsolar.blockentity;
import com.chocohead.advsolar.registry.ASPBlockEntities; import net.minecraft.core.BlockPos; import net.minecraft.world.level.block.state.BlockState;
public final class HybridSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity { public HybridSolarPanelBlockEntity(BlockPos p, BlockState s) { super(ASPBlockEntities.HYBRID_SOLAR_PANEL.get(), p, s, 64, 8, 100000, 2); } }
