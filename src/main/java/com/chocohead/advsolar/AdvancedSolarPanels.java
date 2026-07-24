package com.chocohead.advsolar;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import com.chocohead.advsolar.registry.ASPBlocks;
import com.chocohead.advsolar.registry.ASPItems;
import com.chocohead.advsolar.registry.ASPMenus;
import com.chocohead.advsolar.registry.ASPRecipes;
import ic2.api.energy.EnergyNet;
import ic2.core.gui.Gauge;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AdvancedSolarPanels.MOD_ID)
public final class AdvancedSolarPanels {
  public static final String MOD_ID = "advanced_solar_panels";

  public AdvancedSolarPanels(IEventBus modEventBus) {
    ASPBlocks.register(modEventBus);
    ASPItems.register(modEventBus);
    ASPBlockEntities.register(modEventBus);
    ASPMenus.register(modEventBus);
    ASPRecipes.register(modEventBus);
    modEventBus.addListener(AdvancedSolarPanels::setup);
    if (FMLEnvironment.dist == Dist.CLIENT)
      com.chocohead.advsolar.client.ASPClient.register(modEventBus);
  }

  private static void setup(FMLCommonSetupEvent event) {
    // GuiParser resolves gauge styles on the server too, so these must be registered
    // on both dists.
    event.enqueueWork(
        () -> {
          Gauge.GaugeStyle.addStyle(
              "energy_advanced_solar",
              () ->
                  new Gauge.GaugePropertyBuilder(
                          195, 0, 24, 14, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)
                      .withTexture(guiTexture("advanced_solar_panel.png"))
                      .build());
          Gauge.GaugeStyle.addStyle(
              "progress_molecular_transformer",
              () ->
                  new Gauge.GaugePropertyBuilder(
                          221, 7, 10, 15, Gauge.GaugePropertyBuilder.GaugeOrientation.Down)
                      .withTexture(guiTexture("molecular_transformer.png"))
                      .build());
        });
  }

  private static ResourceLocation guiTexture(String name) {
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/" + name);
  }

  // Compile-time linkage check for the required IC2R API dependency.
  @SuppressWarnings("unused")
  private static double ic2PacketSize(int tier) {
    return EnergyNet.instance.getPowerFromTier(tier);
  }
}
