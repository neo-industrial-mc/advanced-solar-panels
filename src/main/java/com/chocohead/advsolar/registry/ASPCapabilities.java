package com.chocohead.advsolar.registry;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public final class ASPCapabilities {
  private ASPCapabilities() {}

  public static void register(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(
        Capabilities.ItemHandler.BLOCK,
        ASPBlockEntities.MOLECULAR_TRANSFORMER.get(),
        (transformer, side) -> side == null ? null : new SidedInvWrapper(transformer, side));
  }
}
