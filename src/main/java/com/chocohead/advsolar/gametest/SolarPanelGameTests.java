package com.chocohead.advsolar.gametest;

import com.chocohead.advsolar.blockentity.AbstractSolarPanelBlockEntity;
import com.chocohead.advsolar.blockentity.AdvancedSolarPanelBlockEntity;
import com.chocohead.advsolar.blockentity.HybridSolarPanelBlockEntity;
import com.chocohead.advsolar.registry.ASPBlocks;
import ic2.api.item.ElectricItem;
import ic2.core.block.comp.Energy;
import ic2.core.block.wiring.tileentity.TileEntityElectricBatBox;
import ic2.core.ref.Ic2Blocks;
import ic2.core.ref.Ic2Items;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Tests that change the shared level's time/weather each run in their own batch — batches execute
 * sequentially, so they cannot race each other's daylight settings. State assertions run >= 2 ticks
 * after setDayTime because skyDarken (isDay) and sky light both update during the level tick, not
 * synchronously.
 */
@GameTestHolder("advanced_solar_panels")
@PrefixGameTestTemplate(false)
public final class SolarPanelGameTests {
  private static final String EMPTY = "gametest/empty3x3x3";
  private static final BlockPos PANEL_POS = new BlockPos(1, 1, 1);

  @GameTest(template = EMPTY, timeoutTicks = 60, batch = "solarDay")
  public static void solarPanelGeneratesDuringDay(GameTestHelper helper) {
    AdvancedSolarPanelBlockEntity panel =
        placePanel(
            helper, ASPBlocks.ADVANCED_SOLAR_PANEL.get(), AdvancedSolarPanelBlockEntity.class);
    setDaylight(helper.getLevel(), true);
    double[] start = new double[1];

    helper.runAtTickTime(
        2,
        () -> {
          panel.updateGenerationState();
          helper.assertValueEqual(
              panel.generationState,
              AbstractSolarPanelBlockEntity.GenerationState.DAY,
              "generation state");
          start[0] = energy(panel).getEnergy();
        });
    helper.runAtTickTime(
        12,
        () -> {
          AdvSolarGameTestAssertions.assertNear(
              helper,
              energy(panel).getEnergy() - start[0],
              80.0,
              8.0,
              "ten ticks of daylight generation");
          helper.succeed();
        });
  }

  @GameTest(template = EMPTY, timeoutTicks = 60, batch = "solarCovered")
  public static void solarPanelDoesNothingWhenCovered(GameTestHelper helper) {
    AdvancedSolarPanelBlockEntity panel =
        placePanel(
            helper, ASPBlocks.ADVANCED_SOLAR_PANEL.get(), AdvancedSolarPanelBlockEntity.class);
    helper.setBlock(PANEL_POS.above(), Blocks.STONE);
    setDaylight(helper.getLevel(), true);
    double[] start = new double[1];

    helper.runAtTickTime(
        2,
        () -> {
          panel.updateGenerationState();
          helper.assertValueEqual(
              panel.generationState,
              AbstractSolarPanelBlockEntity.GenerationState.NONE,
              "covered generation state");
          start[0] = energy(panel).getEnergy();
        });
    helper.runAtTickTime(
        12,
        () -> {
          AdvSolarGameTestAssertions.assertNear(
              helper, energy(panel).getEnergy() - start[0], 0.0, 0.0, "covered panel generation");
          helper.succeed();
        });
  }

  @GameTest(template = EMPTY, timeoutTicks = 80, batch = "solarNight")
  public static void solarPanelNightGeneration(GameTestHelper helper) {
    HybridSolarPanelBlockEntity panel =
        placePanel(helper, ASPBlocks.HYBRID_SOLAR_PANEL.get(), HybridSolarPanelBlockEntity.class);
    setDaylight(helper.getLevel(), false);
    double[] nightStart = new double[1];
    double[] nightGenerated = new double[1];
    double[] dayStart = new double[1];

    helper.runAtTickTime(
        2,
        () -> {
          panel.updateGenerationState();
          helper.assertValueEqual(
              panel.generationState,
              AbstractSolarPanelBlockEntity.GenerationState.NIGHT,
              "night generation state");
          nightStart[0] = energy(panel).getEnergy();
        });
    helper.runAtTickTime(
        12,
        () -> {
          nightGenerated[0] = energy(panel).getEnergy() - nightStart[0];
          AdvSolarGameTestAssertions.assertNear(
              helper, nightGenerated[0], 80.0, 8.0, "ten ticks of night generation");
          setDaylight(helper.getLevel(), true);
        });
    helper.runAtTickTime(
        14,
        () -> {
          panel.updateGenerationState();
          helper.assertValueEqual(
              panel.generationState,
              AbstractSolarPanelBlockEntity.GenerationState.DAY,
              "day generation state");
          dayStart[0] = energy(panel).getEnergy();
        });
    helper.runAtTickTime(
        24,
        () -> {
          double dayGenerated = energy(panel).getEnergy() - dayStart[0];
          AdvSolarGameTestAssertions.assertNear(
              helper, dayGenerated, 640.0, 64.0, "ten ticks of day generation");
          helper.assertTrue(
              dayGenerated >= nightGenerated[0] * 7.0,
              "hybrid daylight rate should be about eight times its night rate");
          helper.succeed();
        });
  }

  @GameTest(template = EMPTY, timeoutTicks = 100, batch = "solarSink")
  public static void solarPanelEmitsToSink(GameTestHelper helper) {
    AdvancedSolarPanelBlockEntity panel =
        placePanel(
            helper, ASPBlocks.ADVANCED_SOLAR_PANEL.get(), AdvancedSolarPanelBlockEntity.class);
    helper.setBlock(PANEL_POS.east(), Ic2Blocks.BATBOX);
    TileEntityElectricBatBox batbox =
        getBlockEntity(helper, PANEL_POS.east(), TileEntityElectricBatBox.class);
    energy(panel).addEnergy(512.0);
    double initial = energy(panel).getEnergy();

    helper.succeedWhen(
        () -> {
          helper.assertTrue(batbox.energy.getEnergy() > 0.0, "batbox should receive panel energy");
          helper.assertTrue(
              energy(panel).getEnergy() < initial, "panel storage should drain into the batbox");
        });
  }

  @GameTest(template = EMPTY, timeoutTicks = 40, batch = "solarCharge")
  public static void solarPanelChargesItemInSlot(GameTestHelper helper) {
    AdvancedSolarPanelBlockEntity panel =
        placePanel(
            helper, ASPBlocks.ADVANCED_SOLAR_PANEL.get(), AdvancedSolarPanelBlockEntity.class);
    ItemStack battery = new ItemStack(Ic2Items.RE_BATTERY);
    panel.charge.put(0, battery);
    energy(panel).addEnergy(1000.0);
    double initialStorage = energy(panel).getEnergy();

    helper.runAtTickTime(
        5,
        () -> {
          helper.assertTrue(
              ElectricItem.manager.getCharge(panel.charge.get(0)) > 0.0,
              "RE battery should gain charge");
          helper.assertTrue(
              energy(panel).getEnergy() < initialStorage, "charging should consume panel storage");
          helper.succeed();
        });
  }

  @GameTest(template = EMPTY)
  public static void solarPanelStoragePersistsNbt(GameTestHelper helper) {
    AdvancedSolarPanelBlockEntity panel =
        placePanel(
            helper, ASPBlocks.ADVANCED_SOLAR_PANEL.get(), AdvancedSolarPanelBlockEntity.class);
    energy(panel).addEnergy(12345.0);
    CompoundTag nbt = panel.saveWithoutMetadata(helper.getLevel().registryAccess());
    AdvancedSolarPanelBlockEntity restored =
        new AdvancedSolarPanelBlockEntity(
            BlockPos.ZERO, ASPBlocks.ADVANCED_SOLAR_PANEL.get().defaultBlockState());
    restored.loadWithComponents(nbt, helper.getLevel().registryAccess());
    AdvSolarGameTestAssertions.assertNear(
        helper, energy(restored).getEnergy(), 12345.0, 0.0, "restored panel storage");
    helper.succeed();
  }

  private static void setDaylight(ServerLevel level, boolean day) {
    level.setDayTime(day ? 6000L : 18000L);
    level.setWeatherParameters(100000, 0, false, false);
  }

  private static Energy energy(AbstractSolarPanelBlockEntity panel) {
    return panel.getComponent(Energy.class);
  }

  private static <T extends AbstractSolarPanelBlockEntity> T placePanel(
      GameTestHelper helper, net.minecraft.world.level.block.Block block, Class<T> type) {
    helper.setBlock(PANEL_POS, block);
    return getBlockEntity(helper, PANEL_POS, type);
  }

  private static <T extends BlockEntity> T getBlockEntity(
      GameTestHelper helper, BlockPos pos, Class<T> type) {
    BlockEntity blockEntity = helper.getBlockEntity(pos);
    helper.assertTrue(
        type.isInstance(blockEntity),
        "expected " + type.getSimpleName() + " at " + pos + ", got " + blockEntity);
    return type.cast(blockEntity);
  }
}
