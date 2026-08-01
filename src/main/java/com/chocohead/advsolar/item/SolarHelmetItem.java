package com.chocohead.advsolar.item;

import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class SolarHelmetItem extends ItemArmorElectric {
  private final int energyPerDamage;
  private final double absorption;
  private final SolarHelmetGenerator generator;

  public SolarHelmetItem(
      Holder<ArmorMaterial> material,
      Properties properties,
      double capacity,
      double transfer,
      int tier,
      int dayPower,
      int nightPower,
      int energyPerDamage,
      double absorption,
      boolean airRefill) {
    super(material, EquipmentSlot.HEAD, properties, capacity, transfer, tier);
    this.energyPerDamage = energyPerDamage;
    this.absorption = absorption;
    this.generator = new SolarHelmetGenerator(dayPower, nightPower, tier, airRefill);
  }

  @Override
  public int getEnergyPerDamage() {
    return energyPerDamage;
  }

  @Override
  public double getDamageAbsorptionRatio() {
    return absorption;
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
}
