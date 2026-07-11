package com.chocohead.advsolar.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class SolarHelmetItem extends ItemArmorElectric {
    private final int dayPower, nightPower, energyPerDamage;
    private final double absorption;
    private final boolean airRefill;
    private int ticker;
    private State state = State.NONE;
    private enum State { NONE, NIGHT, DAY }

    public SolarHelmetItem(Holder<ArmorMaterial> material, Properties properties, double capacity, double transfer,
                           int tier, int dayPower, int nightPower, int energyPerDamage, double absorption, boolean airRefill) {
        super(material, EquipmentSlot.HEAD, properties, capacity, transfer, tier);
        this.dayPower = dayPower; this.nightPower = nightPower; this.energyPerDamage = energyPerDamage;
        this.absorption = absorption; this.airRefill = airRefill;
    }
    @Override public int getEnergyPerDamage() { return energyPerDamage; }
    @Override public double getDamageAbsorptionRatio() { return absorption; }
    @Override public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level.isClientSide || !(entity instanceof Player player) || player.getItemBySlot(EquipmentSlot.HEAD) != stack) return;
        if (++ticker % 128 == 0) updateState(level, player.blockPosition());
        if (airRefill && player.getAirSupply() < 100 && ElectricItem.manager.use(stack, 1000, player)) player.setAirSupply(player.getAirSupply() + 200);
        double output = state == State.DAY ? dayPower : state == State.NIGHT ? nightPower : 0;
        if (output <= 0) return;
        for (EquipmentSlot armorSlot : new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST}) {
            output = charge(player.getItemBySlot(armorSlot), output);
            if (output <= 0) return;
        }
        for (ItemStack candidate : player.getInventory().offhand) { output = charge(candidate, output); if (output <= 0) return; }
        for (ItemStack candidate : player.getInventory().items) { if (candidate != stack) output = charge(candidate, output); if (output <= 0) return; }
        ElectricItem.manager.charge(stack, output, Integer.MAX_VALUE, true, false);
    }
    private double charge(ItemStack target, double amount) {
        return target.getItem() instanceof IElectricItem ? amount - ElectricItem.manager.charge(target, amount, tier, false, false) : amount;
    }
    private void updateState(Level level, BlockPos pos) {
        if (!level.dimensionType().hasSkyLight() || !level.canSeeSky(pos.above())) { state = State.NONE; return; }
        boolean canRain = level.getBiome(pos).value().getPrecipitationAt(pos) != net.minecraft.world.level.biome.Biome.Precipitation.NONE;
        state = level.isDay() && (!canRain || (!level.isRaining() && !level.isThundering())) ? State.DAY : State.NIGHT;
    }
}
