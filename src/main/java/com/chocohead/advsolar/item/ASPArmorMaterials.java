package com.chocohead.advsolar.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public final class ASPArmorMaterials {
  private ASPArmorMaterials() {}

  public static Holder<ArmorMaterial> create(String name, boolean dyeable) {
    Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
    defense.put(ArmorItem.Type.HELMET, 3);
    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("advanced_solar_panels", name);
    List<ArmorMaterial.Layer> layers =
        dyeable
            ? List.of(
                new ArmorMaterial.Layer(texture, "", true),
                new ArmorMaterial.Layer(texture, "_overlay", false))
            : List.of(new ArmorMaterial.Layer(texture));
    return Holder.direct(
        new ArmorMaterial(
            defense, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, Ingredient::of, layers, 2, 0));
  }
}
