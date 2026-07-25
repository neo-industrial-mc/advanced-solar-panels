package com.chocohead.advsolar.gametest;

import ic2.api.item.ElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import ic2.core.item.armor.jetpack.JetpackHandler;
import ic2.core.ref.Ic2Items;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("advanced_solar_panels")
@PrefixGameTestTemplate(false)
public final class JetpackAttachmentGameTests {
  private static final String EMPTY = "gametest/empty3x3x3";
  private static final ResourceLocation ATTACHMENT_RECIPE_ID =
      ResourceLocation.fromNamespaceAndPath("ic2", "shaped/jetpack_attachment");
  private static final double ELECTRIC_JETPACK_CAPACITY = 30000.0;

  @GameTest(template = EMPTY)
  public static void attachmentRecipeAddsJetpackAndTransfersCharge(GameTestHelper helper) {
    ItemStack chestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
    ItemStack jetpack =
        ElectricItemManager.getCharged(Ic2Items.JETPACK_ELECTRIC, Double.POSITIVE_INFINITY);
    CraftingInput input =
        CraftingInput.of(
            3, 1, List.of(chestplate, jetpack, new ItemStack(Ic2Items.JETPACK_ATTACHMENT_PLATE)));
    CraftingRecipe recipe = getAttachmentRecipe(helper);

    helper.assertTrue(recipe.matches(input, helper.getLevel()), "attachment recipe should match");
    ItemStack result = recipe.assemble(input, helper.getLevel().registryAccess());

    helper.assertValueEqual(
        result.getItem(), Items.DIAMOND_CHESTPLATE, "attachment recipe output item");
    helper.assertTrue(
        JetpackHandler.hasJetpackAttached(result), "crafted chestplate should have a jetpack");
    helper.assertFalse(
        JetpackHandler.hasJetpackAttached(chestplate),
        "recipe matching and assembly must not modify the input chestplate");
    AdvSolarGameTestAssertions.assertNear(
        helper,
        ElectricItem.manager.getCharge(result),
        ELECTRIC_JETPACK_CAPACITY,
        0.0,
        "attached jetpack charge");

    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void attachmentRecipeRejectsInvalidInputs(GameTestHelper helper) {
    CraftingRecipe recipe = getAttachmentRecipe(helper);
    ItemStack jetpack =
        ElectricItemManager.getCharged(Ic2Items.JETPACK_ELECTRIC, Double.POSITIVE_INFINITY);

    CraftingInput missingPlate =
        CraftingInput.of(2, 1, List.of(new ItemStack(Items.DIAMOND_CHESTPLATE), jetpack.copy()));
    helper.assertFalse(
        recipe.matches(missingPlate, helper.getLevel()),
        "attachment recipe must require an attachment plate");

    CraftingInput nonChestArmor =
        CraftingInput.of(
            3,
            1,
            List.of(
                new ItemStack(Items.DIAMOND_LEGGINGS),
                jetpack.copy(),
                new ItemStack(Ic2Items.JETPACK_ATTACHMENT_PLATE)));
    helper.assertFalse(
        recipe.matches(nonChestArmor, helper.getLevel()),
        "attachment recipe must reject non-chest armor");

    ItemStack attachedChestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
    JetpackHandler.setJetpackAttached(attachedChestplate, true);
    CraftingInput alreadyAttached =
        CraftingInput.of(
            3,
            1,
            List.of(attachedChestplate, jetpack, new ItemStack(Ic2Items.JETPACK_ATTACHMENT_PLATE)));
    helper.assertFalse(
        recipe.matches(alreadyAttached, helper.getLevel()),
        "attachment recipe must reject armor that already has a jetpack");

    helper.succeed();
  }

  private static CraftingRecipe getAttachmentRecipe(GameTestHelper helper) {
    RecipeHolder<CraftingRecipe> holder =
        helper.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
            .filter(recipe -> recipe.id().equals(ATTACHMENT_RECIPE_ID))
            .findFirst()
            .orElseThrow(() -> new AssertionError("missing " + ATTACHMENT_RECIPE_ID));
    helper.assertTrue(
        holder.value() instanceof JetpackAttachmentRecipe,
        ATTACHMENT_RECIPE_ID + " should use the jetpack attachment recipe");
    return holder.value();
  }
}
