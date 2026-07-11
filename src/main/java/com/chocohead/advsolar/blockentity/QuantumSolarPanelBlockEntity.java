package com.chocohead.advsolar.blockentity;
import com.chocohead.advsolar.registry.ASPBlockEntities; import net.minecraft.core.BlockPos; import net.minecraft.world.level.block.state.BlockState;
public final class QuantumSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity { public QuantumSolarPanelBlockEntity(BlockPos p, BlockState s) { super(ASPBlockEntities.QUANTUM_SOLAR_PANEL.get(), p, s, 4096, 2048, 10000000, 5); } }
