package com.chocohead.advsolar.slot;

import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.world.item.ItemStack;

public final class InvSlotMultiCharge extends InvSlot implements IChargingSlot {
  private final int tier;

  public InvSlotMultiCharge(IInventorySlotHolder<?> base, int tier, int count) {
    super(base, "charge", Access.IO, count, InvSide.TOP);
    this.tier = tier;
  }

  @Override
  public boolean accepts(ItemStack stack) {
    return ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, tier, false, true) > 0;
  }

  @Override
  public double charge(double amount) {
    double charged = 0;
    for (ItemStack stack : this) {
      double accepted = ElectricItem.manager.charge(stack, amount, tier, false, false);
      amount -= accepted;
      charged += accepted;
      if (amount <= 0) break;
    }
    return charged;
  }
}
