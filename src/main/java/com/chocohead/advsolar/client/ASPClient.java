package com.chocohead.advsolar.client;

import com.chocohead.advsolar.registry.ASPItems;
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
        (stack, layer) -> layer == 0 ? DyedItemColor.getOrDefault(stack, 0xFFFFFFFF) : 0xFFFFFFFF,
        ASPItems.HYBRID_SOLAR_HELMET.get(),
        ASPItems.ULTIMATE_SOLAR_HELMET.get());
  }
}
