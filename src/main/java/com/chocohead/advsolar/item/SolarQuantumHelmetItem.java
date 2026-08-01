package com.chocohead.advsolar.item;

import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class SolarQuantumHelmetItem extends ItemArmorQuantumSuit {
  private final SolarHelmetGenerator generator;
  private final int energyPerDamage;
  private final double transferLimit;

  public SolarQuantumHelmetItem(
      Holder<ArmorMaterial> material,
      Properties properties,
      double transferLimit,
      int dayPower,
      int nightPower,
      int energyPerDamage) {
    super(material, EquipmentSlot.HEAD, properties);
    this.generator = new SolarHelmetGenerator(dayPower, nightPower, 4, false);
    this.energyPerDamage = energyPerDamage;
    this.transferLimit = transferLimit;
  }

  public static Item.Properties properties(Rarity rarity) {
    return new QuantumProperties(rarity);
  }

  @Override
  public int getEnergyPerDamage() {
    return energyPerDamage;
  }

  @Override
  public double getTransferLimit(ItemStack stack) {
    return transferLimit;
  }

  @Override
  public void inventoryTick(
      @NotNull ItemStack stack,
      @NotNull Level level,
      @NotNull Entity entity,
      int slot,
      boolean selected) {
    super.inventoryTick(stack, level, entity, slot, selected);
    generator.inventoryTick(stack, level, entity);
  }

  private static final class QuantumProperties extends Item.Properties {
    private final Rarity rarity;

    private QuantumProperties(Rarity rarity) {
      this.rarity = rarity;
      super.rarity(rarity);
    }

    @Override
    public Item.Properties rarity(Rarity ignored) {
      return super.rarity(rarity);
    }
  }
}
