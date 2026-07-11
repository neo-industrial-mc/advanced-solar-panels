package com.chocohead.advsolar.blockentity;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IMultiEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Redstone;
import ic2.core.block.tileentity.TileEntityBase;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.GuiSynced;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public final class QuantumGeneratorBlockEntity extends TileEntityBase
    implements IMultiEnergySource, IHasGui, INetworkClientTileEntityEventListener {
  @GuiSynced public int production = 512;
  @GuiSynced private int tier = 3;
  private final Redstone redstone;

  public QuantumGeneratorBlockEntity(BlockPos pos, BlockState state) {
    super(ASPBlockEntities.QUANTUM_GENERATOR.get(), pos, state);
    redstone = addComponent(new Redstone(this));
    redstone.subscribe(value -> setActiveState(value == 0, false));
  }

  @Override
  protected void onLoaded() {
    super.onLoaded();
    if (!level.isClientSide) {
      EnergyNet.instance.addBlockEntityTile(this);
      setActiveState(!redstone.hasRedstoneInput(), false);
    }
  }

  @Override
  protected void onUnloaded() {
    if (!level.isClientSide) EnergyNet.instance.removeTile(this);
    super.onUnloaded();
  }

  @Override
  public double getOfferedEnergy() {
    return getActive()
        ? (sendMultipleEnergyPackets()
            ? (double) production / getMultipleEnergyPacketAmount()
            : production)
        : 0;
  }

  @Override
  public void drawEnergy(double amount) {}

  @Override
  public int getSourceTier() {
    return tier;
  }

  @Override
  public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction side) {
    return true;
  }

  @Override
  public boolean sendMultipleEnergyPackets() {
    return production > EnergyNet.instance.getPowerFromTier(tier);
  }

  @Override
  public int getMultipleEnergyPacketAmount() {
    return Math.max(1, (int) Math.ceil(production / EnergyNet.instance.getPowerFromTier(tier)));
  }

  public String getTier() {
    return tier > 5 ? "MAX" : Integer.toString(tier);
  }

  @Override
  public void onNetworkEvent(Player player, int event) {
    int[] changes = {-100, -10, -1, 1, 10, 100};
    int[] shifted = {-500, -50, -5, 5, 50, 500};
    if (event >= 0 && event < 6) production = Math.max(0, production + changes[event]);
    else if (event >= 10 && event < 16) production = Math.max(0, production + shifted[event - 10]);
    else if (event >= 20 && event < 26) tier = event - 19;
    setChanged();
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    production = tag.contains("production") ? tag.getInt("production") : 512;
    tier = tag.contains("tier") ? tag.getInt("tier") : 3;
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putInt("production", production);
    tag.putInt("tier", tier);
  }

  @Override
  public ContainerBase<?> createServerScreenHandler(int id, Player player) {
    return DynamicContainer.create(id, player.getInventory(), this);
  }

  @Override
  public ContainerBase<?> createClientScreenHandler(int id, Inventory inv, GrowingBuffer data) {
    return DynamicContainer.create(id, inv, this);
  }
}
