package com.chocohead.advsolar.integration.jei;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record MolecularTransformerJeiRecipe(
    ResourceLocation id, List<ItemStack> inputs, ItemStack output, int energy) {
  public static MolecularTransformerJeiRecipe from(
      ResourceLocation id, MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe) {
    int count = recipe.getMetaData().getInt("count");
    List<ItemStack> inputs =
        recipe.getInput().getInputs().stream()
            .map(
                stack -> {
                  ItemStack copy = stack.copy();
                  copy.setCount(count);
                  return copy;
                })
            .toList();
    ItemStack output = recipe.getOutput().iterator().next().copy();
    int energy = recipe.getMetaData().getInt("energy");
    return new MolecularTransformerJeiRecipe(id, inputs, output, energy);
  }
}
