package com.chocohead.advsolar.integration.jei;

import com.chocohead.advsolar.registry.ASPItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MolecularTransformerRecipeCategory
    implements IRecipeCategory<MolecularTransformerJeiRecipe> {
  private static final int WIDTH = 150;
  private static final int HEIGHT = 44;

  private final IDrawable icon;
  private final IDrawableAnimated arrow;

  public MolecularTransformerRecipeCategory(IGuiHelper guiHelper) {
    icon = guiHelper.createDrawableItemStack(new ItemStack(ASPItems.MOLECULAR_TRANSFORMER.get()));
    arrow = guiHelper.createAnimatedRecipeArrow(100);
  }

  @Override
  public @NotNull RecipeType<MolecularTransformerJeiRecipe> getRecipeType() {
    return ASPJeiPlugin.MOLECULAR_TRANSFORMER;
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.translatable("block.advanced_solar_panels.molecular_transformer");
  }

  @Override
  public int getWidth() {
    return WIDTH;
  }

  @Override
  public int getHeight() {
    return HEIGHT;
  }

  @Override
  public @NotNull IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(
      @NotNull IRecipeLayoutBuilder builder,
      @NotNull MolecularTransformerJeiRecipe recipe,
      @NotNull IFocusGroup focuses) {
    builder
        .addSlot(RecipeIngredientRole.INPUT, 12, 8)
        .addItemStacks(recipe.inputs())
        .setStandardSlotBackground();
    builder
        .addSlot(RecipeIngredientRole.OUTPUT, 122, 8)
        .addItemStack(recipe.output())
        .setOutputSlotBackground();
  }

  @Override
  public void draw(
      @NotNull MolecularTransformerJeiRecipe recipe,
      @NotNull IRecipeSlotsView recipeSlotsView,
      @NotNull GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    arrow.draw(guiGraphics, 63, 8);
    Component energy =
        Component.translatable("gui.advanced_solar_panels.energyPerOperation")
            .append(" ")
            .append(Integer.toString(recipe.energy()))
            .append(" EU");
    var font = Minecraft.getInstance().font;
    guiGraphics.drawString(font, energy, (WIDTH - font.width(energy)) / 2, 32, 0x404040, false);
  }

  @Override
  public ResourceLocation getRegistryName(MolecularTransformerJeiRecipe recipe) {
    return recipe.id();
  }
}
