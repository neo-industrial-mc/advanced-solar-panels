package com.chocohead.advsolar.integration.jei;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.registry.ASPItems;
import com.chocohead.advsolar.registry.ASPRecipes;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public final class ASPJeiPlugin implements IModPlugin {
  public static final RecipeType<MolecularTransformerJeiRecipe> MOLECULAR_TRANSFORMER =
      RecipeType.create(
          AdvancedSolarPanels.MOD_ID, "molecular_transformer", MolecularTransformerJeiRecipe.class);

  private static final ResourceLocation PLUGIN_ID =
      ResourceLocation.fromNamespaceAndPath(AdvancedSolarPanels.MOD_ID, "jei_plugin");

  @Override
  public @NotNull ResourceLocation getPluginUid() {
    return PLUGIN_ID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
        new MolecularTransformerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
        new ItemStack(ASPItems.MOLECULAR_TRANSFORMER.get()), MOLECULAR_TRANSFORMER);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    var level = Minecraft.getInstance().level;
    if (level == null) return;

    List<MolecularTransformerJeiRecipe> recipes =
        level.getRecipeManager().getAllRecipesFor(ASPRecipes.MOLECULAR_TRANSFORMER.get()).stream()
            .map(holder -> MolecularTransformerJeiRecipe.from(holder.id(), holder.value().recipe()))
            .toList();
    registration.addRecipes(MOLECULAR_TRANSFORMER, recipes);
  }
}
