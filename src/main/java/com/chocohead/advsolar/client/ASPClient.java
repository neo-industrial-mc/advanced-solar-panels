package com.chocohead.advsolar.client;

import com.chocohead.advsolar.registry.ASPItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class ASPClient {
  private ASPClient() {}

  public static void register(IEventBus bus) {
    bus.addListener(ASPClient::itemColors);
    bus.addListener(ASPClient::clientExtensions);
  }

  private static void itemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, layer) -> layer == 0 ? helmetColor(stack) : 0xFFFFFFFF,
        ASPItems.HYBRID_SOLAR_HELMET.get(),
        ASPItems.ULTIMATE_SOLAR_HELMET.get());
  }

  private static void clientExtensions(RegisterClientExtensionsEvent event) {
    event.registerItem(
        new IClientItemExtensions() {
          @Override
          public int getDefaultDyeColor(ItemStack stack) {
            return helmetColor(stack);
          }
        },
        ASPItems.HYBRID_SOLAR_HELMET.get(),
        ASPItems.ULTIMATE_SOLAR_HELMET.get());
  }

  private static int helmetColor(ItemStack stack) {
    return DyedItemColor.getOrDefault(stack, 0xFFFFFFFF);
  }
}
