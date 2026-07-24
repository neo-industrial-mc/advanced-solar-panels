package com.chocohead.advsolar.client;

import com.chocohead.advsolar.registry.ASPItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ASPClient {
  private ASPClient() {}

  public static void register(IEventBus bus) {
    bus.addListener(ASPClient::itemColors);
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
}
