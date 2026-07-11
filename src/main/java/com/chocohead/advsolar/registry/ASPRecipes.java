package com.chocohead.advsolar.registry;

import com.chocohead.advsolar.AdvancedSolarPanels;
import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.v2.BasicMachineRecipeSerializer;
import ic2.core.recipe.v2.RecipeHolder;
import java.util.Collection;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ASPRecipes {
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
      DeferredRegister.create(Registries.RECIPE_TYPE, AdvancedSolarPanels.MOD_ID);
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(Registries.RECIPE_SERIALIZER, AdvancedSolarPanels.MOD_ID);

  @SuppressWarnings("unchecked")
  public static final DeferredHolder<
          RecipeType<?>, RecipeType<RecipeHolder<IRecipeInput, Collection<ItemStack>>>>
      MOLECULAR_TRANSFORMER =
          RECIPE_TYPES.register(
              "molecular_transformer",
              () ->
                  new RecipeType<>() {
                    public String toString() {
                      return AdvancedSolarPanels.MOD_ID + ":molecular_transformer";
                    }
                  });

  public static final DeferredHolder<
          RecipeSerializer<?>, RecipeSerializer<RecipeHolder<IRecipeInput, Collection<ItemStack>>>>
      MOLECULAR_TRANSFORMER_SERIALIZER =
          RECIPE_SERIALIZERS.register(
              "molecular_transformer",
              () ->
                  new BasicMachineRecipeSerializer(
                      MOLECULAR_TRANSFORMER.get(),
                      json -> {
                        CompoundTag tag = new CompoundTag();
                        tag.putInt("energy", json.get("energy").getAsInt());
                        tag.putInt("count", json.has("count") ? json.get("count").getAsInt() : 1);
                        return tag;
                      }));

  private ASPRecipes() {}

  public static void register(IEventBus modEventBus) {
    RECIPE_TYPES.register(modEventBus);
    RECIPE_SERIALIZERS.register(modEventBus);
  }
}
