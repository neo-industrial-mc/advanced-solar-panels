/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.energy.tile.IChargingSlot
 *  ic2.api.item.ElectricItem
 *  ic2.core.block.TileEntityInventory
 *  ic2.core.block.invslot.InvSlot
 *  ic2.core.block.invslot.InvSlot$Access
 *  ic2.core.block.invslot.InvSlot$InvSide
 *  net.minecraft.item.ItemStack
 */
package com.chocohead.advsolar.slots;

import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import java.util.Iterator;
import net.minecraft.item.ItemStack;

public class InvSlotMultiCharge
extends InvSlot
implements IChargingSlot {
    public final int tier;

    public InvSlotMultiCharge(TileEntityInventory base, int tier, int slotNumbers, InvSlot.Access access) {
        super(base, "charge", access, slotNumbers, InvSlot.InvSide.TOP);
        this.tier = tier;
    }

    public boolean accepts(ItemStack stack) {
        return ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, this.tier, false, true) > 0.0;
    }

    public double charge(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        double charged = 0.0;
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = (ItemStack)iterator.next();
            if (stack == null) continue;
            double energyIn = ElectricItem.manager.charge(stack, amount, this.tier, false, false);
            amount -= energyIn;
            charged += energyIn;
            if (!(amount <= 0.0)) continue;
            break;
        }
        return charged;
    }
}

