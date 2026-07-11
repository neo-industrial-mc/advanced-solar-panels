package com.chocohead.advsolar.blockentity;

import com.chocohead.advsolar.slot.InvSlotMultiCharge;
import ic2.api.energy.EnergyNet;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Energy;
import ic2.core.block.tileentity.TileEntityBase;
import ic2.core.block.tileentity.TileEntityInventory;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.GuiSynced;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractSolarPanelBlockEntity extends TileEntityBase implements IHasGui, IGuiValueProvider {
    public enum GenerationState { NONE, NIGHT, DAY }

    public final InvSlotMultiCharge charge;
    protected final Energy energy;
    public final int maxStorage;
    protected final int dayPower;
    protected final int nightPower;
    protected final int tier;
    @GuiSynced public int storage;
    @GuiSynced public GenerationState generationState = GenerationState.NONE;
    private int ticker;
    private boolean canRain;
    private boolean hasSkyLight;

    protected AbstractSolarPanelBlockEntity(BlockEntityType<? extends TileEntityInventory> type, BlockPos pos, BlockState state,
                                             int dayPower, int nightPower, int maxStorage, int tier) {
        super(type, pos, state);
        this.dayPower = dayPower;
        this.nightPower = nightPower;
        this.maxStorage = maxStorage;
        this.tier = tier;
        this.charge = new InvSlotMultiCharge(this, tier, 4);
        this.energy = addComponent(Energy.asBasicSource(this, maxStorage, tier).addManagedSlot(charge));
    }

    @Override protected void onLoaded() {
        super.onLoaded();
        hasSkyLight = level.dimensionType().hasSkyLight();
        canRain = level.getBiome(worldPosition).value().getPrecipitationAt(worldPosition) != net.minecraft.world.level.biome.Biome.Precipitation.NONE;
        updateGenerationState();
    }

    @Override protected void updateEntityServer() {
        super.updateEntityServer();
        if (++ticker % 128 == 0) updateGenerationState();
        int generated = generationState == GenerationState.DAY ? dayPower : generationState == GenerationState.NIGHT ? nightPower : 0;
        if (generated > 0) energy.addEnergy(generated);
        storage = (int) energy.getEnergy();
    }

    public void updateGenerationState() {
        if (!hasSkyLight || !level.canSeeSky(worldPosition.above())) {
            generationState = GenerationState.NONE;
        } else if (level.isDay() && (!canRain || (!level.isRaining() && !level.isThundering()))) {
            generationState = GenerationState.DAY;
        } else {
            generationState = GenerationState.NIGHT;
        }
    }

    @Override public double getGuiValue(String name) { return "progress".equals(name) ? energy.getFillRatio() : 0; }
    @Override public boolean getGuiState(String name) {
        return switch (name) { case "sunlight" -> generationState == GenerationState.DAY; case "moonlight" -> generationState == GenerationState.NIGHT; default -> false; };
    }
    public String getMaxOutput() { return "Max Output: " + (int) EnergyNet.instance.getPowerFromTier(tier) + " EU/t"; }
    public String getOutput() { return "Generating: " + (generationState == GenerationState.DAY ? dayPower : generationState == GenerationState.NIGHT ? nightPower : 0) + " EU/t"; }
    @Override public ContainerBase<?> createServerScreenHandler(int id, Player player) { return DynamicContainer.create(id, player.getInventory(), this); }
    @Override public ContainerBase<?> createClientScreenHandler(int id, Inventory inv, GrowingBuffer data) { return DynamicContainer.create(id, inv, this); }
}
