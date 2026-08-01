package com.chocohead.advsolar.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

final class SolarHelmetGenerator {
  private final int dayPower;
  private final int nightPower;
  private final int tier;
  private final boolean airRefill;

  private enum State {
    NONE,
    NIGHT,
    DAY
  }

  SolarHelmetGenerator(int dayPower, int nightPower, int tier, boolean airRefill) {
    this.dayPower = dayPower;
    this.nightPower = nightPower;
    this.tier = tier;
    this.airRefill = airRefill;
  }

  void inventoryTick(ItemStack stack, Level level, Entity entity) {
    if (level.isClientSide
        || !(entity instanceof Player player)
        || player.getItemBySlot(EquipmentSlot.HEAD) != stack) return;
    State state = computeState(level, player.blockPosition());
    if (airRefill && player.getAirSupply() < 100 && ElectricItem.manager.use(stack, 1000, player))
      player.setAirSupply(player.getAirSupply() + 200);
    double output = state == State.DAY ? dayPower : state == State.NIGHT ? nightPower : 0;
    if (output <= 0) return;
    for (EquipmentSlot armorSlot :
        new EquipmentSlot[] {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST}) {
      output = charge(player.getItemBySlot(armorSlot), output);
      if (output <= 0) return;
    }
    for (ItemStack candidate : player.getInventory().offhand) {
      output = charge(candidate, output);
      if (output <= 0) return;
    }
    for (ItemStack candidate : player.getInventory().items) {
      if (candidate != stack) output = charge(candidate, output);
      if (output <= 0) return;
    }
    ElectricItem.manager.charge(stack, output, Integer.MAX_VALUE, true, false);
  }

  private double charge(ItemStack target, double amount) {
    return target.getItem() instanceof IElectricItem
        ? amount - ElectricItem.manager.charge(target, amount, tier, false, false)
        : amount;
  }

  private static State computeState(Level level, BlockPos pos) {
    if (!level.dimensionType().hasSkyLight() || !level.canSeeSky(pos.above())) return State.NONE;
    boolean canRain =
        level.getBiome(pos).value().getPrecipitationAt(pos)
            != net.minecraft.world.level.biome.Biome.Precipitation.NONE;
    return level.isDay() && (!canRain || (!level.isRaining() && !level.isThundering()))
        ? State.DAY
        : State.NIGHT;
  }
}
