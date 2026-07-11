package com.chocohead.advsolar.client;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.registry.ASPItems;
import ic2.core.gui.Gauge;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ASPClient {
  private ASPClient() {}

  public static void register(IEventBus bus) {
    bus.addListener(ASPClient::setup);
    bus.addListener(ASPClient::itemColors);
  }

  private static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(
        () -> {
          Gauge.GaugeStyle.addStyle(
              "energy_advanced_solar",
              () ->
                  new Gauge.GaugePropertyBuilder(
                          195, 0, 24, 14, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)
                      .withTexture(texture("advanced_solar_panel.png"))
                      .build());
          Gauge.GaugeStyle.addStyle(
              "progress_molecular_transformer",
              () ->
                  new Gauge.GaugePropertyBuilder(
                          221, 7, 10, 15, Gauge.GaugePropertyBuilder.GaugeOrientation.Down)
                      .withTexture(texture("molecular_transformer.png"))
                      .build());
        });
  }

  private static void itemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, layer) ->
            layer == 0
                ? stack
                    .getOrDefault(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF, false))
                    .rgb()
                : 0xFFFFFF,
        ASPItems.HYBRID_SOLAR_HELMET.get(),
        ASPItems.ULTIMATE_SOLAR_HELMET.get());
  }

  private static ResourceLocation texture(String name) {
    return ResourceLocation.fromNamespaceAndPath(
        AdvancedSolarPanels.MOD_ID, "textures/gui/" + name);
  }
}
