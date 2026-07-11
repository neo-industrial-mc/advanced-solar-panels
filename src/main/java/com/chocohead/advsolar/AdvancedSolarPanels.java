package com.chocohead.advsolar;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import com.chocohead.advsolar.registry.ASPBlocks;
import com.chocohead.advsolar.registry.ASPItems;
import com.chocohead.advsolar.registry.ASPMenus;
import com.chocohead.advsolar.registry.ASPRecipes;
import ic2.api.energy.EnergyNet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
    if (FMLEnvironment.dist == Dist.CLIENT)
      com.chocohead.advsolar.client.ASPClient.register(modEventBus);
  }

  // Compile-time linkage check for the required IC2R API dependency.
  @SuppressWarnings("unused")
  private static double ic2PacketSize(int tier) {
    return EnergyNet.instance.getPowerFromTier(tier);
  }
}
