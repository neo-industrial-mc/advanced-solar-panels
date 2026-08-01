package com.chocohead.advsolar.gametest;

import com.chocohead.advsolar.registry.ASPItems;
import ic2.api.item.ElectricItem;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudProvider;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("advanced_solar_panels")
@PrefixGameTestTemplate(false)
public final class SolarHelmetGameTests {
  private static final String EMPTY = "gametest/empty3x3x3";

  @GameTest(template = EMPTY)
  public static void hybridAndUltimateHelmetsRetainQuantumSuitBehavior(GameTestHelper helper) {
    for (ItemStack helmet :
        List.of(
            new ItemStack(ASPItems.HYBRID_SOLAR_HELMET.get()),
            new ItemStack(ASPItems.ULTIMATE_SOLAR_HELMET.get()))) {
      helper.assertTrue(
          helmet.getItem() instanceof ItemArmorQuantumSuit,
          "solar quantum helmet must remain a QuantumSuit helmet");
      helper.assertTrue(
          helmet.getItem() instanceof IHazmatLike,
          "solar quantum helmet must retain hazmat protection");
      helper.assertTrue(
          helmet.getItem() instanceof IItemHudProvider,
          "solar quantum helmet must retain the QuantumSuit HUD");
      ItemArmorQuantumSuit quantumHelmet = (ItemArmorQuantumSuit) helmet.getItem();
      helper.assertValueEqual(quantumHelmet.getTier(helmet), 4, "charge tier");
      AdvSolarGameTestAssertions.assertNear(
          helper, quantumHelmet.getMaxCharge(helmet), 10_000_000, 0, "charge capacity");
      AdvSolarGameTestAssertions.assertNear(
          helper, quantumHelmet.getTransferLimit(helmet), 10_000, 0, "transfer limit");
      helper.assertValueEqual(
          helmet.getRarity(),
          helmet.is(ASPItems.ULTIMATE_SOLAR_HELMET.get()) ? Rarity.EPIC : Rarity.RARE,
          "rarity");

      Player player = helper.makeMockPlayer(GameType.SURVIVAL);
      ElectricItem.manager.charge(helmet, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
      player.setItemSlot(EquipmentSlot.HEAD, helmet);
      player.addEffect(new MobEffectInstance(MobEffects.POISON, 100));

      helmet.getItem().inventoryTick(helmet, helper.getLevel(), player, 0, false);

      helper.assertFalse(
          player.hasEffect(MobEffects.POISON),
          "solar quantum helmet must retain QuantumSuit effect removal");
    }
    helper.succeed();
  }

  private SolarHelmetGameTests() {}
}
