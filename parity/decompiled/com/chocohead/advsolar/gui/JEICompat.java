/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.recipe.IRecipeInput
 *  ic2.api.recipe.MachineRecipe
 *  ic2.core.block.ITeBlock
 *  ic2.core.init.Localization
 *  ic2.core.recipe.RecipeInputOreDict
 *  ic2.jeiIntegration.recipe.machine.DynamicCategory
 *  ic2.jeiIntegration.recipe.machine.IORecipeCategory
 *  ic2.jeiIntegration.recipe.machine.IORecipeWrapper
 *  ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator
 *  mezz.jei.api.IGuiHelper
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.IModRegistry
 *  mezz.jei.api.JEIPlugin
 *  mezz.jei.api.recipe.IRecipeCategory
 *  mezz.jei.api.recipe.IRecipeWrapper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.item.ItemStack
 */
package com.chocohead.advsolar.gui;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.IMolecularTransformerRecipeManager;
import com.chocohead.advsolar.gui.TransparentDynamicGUI;
import com.chocohead.advsolar.tiles.TEs;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.block.ITeBlock;
import ic2.core.init.Localization;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeWrapper;
import ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@JEIPlugin
public final class JEICompat
implements IModPlugin {
    public void register(IModRegistry registry) {
        registry.addRecipeClickArea(TransparentDynamicGUI.class, 23, 48, 10, 15, new String[]{TEs.molecular_transformer.getName()});
        this.addMachineRecipes(registry, (IORecipeCategory)new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()), (IRecipeWrapperGenerator)MolecularTransformerRecipeWrapper.RECIPE_WRAPPER);
    }

    private <T> void addMachineRecipes(IModRegistry registry, IORecipeCategory<T> category, IRecipeWrapperGenerator<T> wrappergen) {
        registry.addRecipeCategories(new IRecipeCategory[]{category});
        registry.addRecipes((Collection)wrappergen.getRecipeList(category), category.getUid());
        registry.addRecipeCatalyst((Object)category.getBlockStack(), new String[]{category.getUid()});
    }

    protected static class MolecularTransformerRecipeWrapper
    extends IORecipeWrapper {
        public static final IRecipeWrapperGenerator<IMolecularTransformerRecipeManager> RECIPE_WRAPPER = new IRecipeWrapperGenerator<IMolecularTransformerRecipeManager>(){

            public List<IRecipeWrapper> getRecipeList(IORecipeCategory<IMolecularTransformerRecipeManager> category) {
                ArrayList<IRecipeWrapper> recipes = new ArrayList<IRecipeWrapper>();
                for (MachineRecipe container : IMolecularTransformerRecipeManager.RECIPES.getRecipes()) {
                    recipes.add((IRecipeWrapper)new MolecularTransformerRecipeWrapper((MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack>)container, category));
                }
                return recipes;
            }
        };
        protected final String input;
        protected final String output;
        protected final String totalEU;

        MolecularTransformerRecipeWrapper(MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> container, IORecipeCategory<?> category) {
            super(((IMolecularTransformerRecipeManager.Input)container.getInput()).input, Collections.singletonList(container.getOutput()), category);
            String inputText;
            IRecipeInput input = ((IMolecularTransformerRecipeManager.Input)container.getInput()).input;
            if (!input.getInputs().isEmpty()) {
                inputText = ((ItemStack)input.getInputs().get(0)).func_82833_r();
            } else if (input instanceof RecipeInputOreDict) {
                inputText = ((RecipeInputOreDict)input).input;
            } else {
                AdvancedSolarPanels.log.warn("Unexpected empty recipe input: " + input + " (" + input.getClass() + ')');
                inputText = "Empty " + input.getClass().getSimpleName();
            }
            this.input = Localization.translate((String)"advanced_solar_panels.gui.input") + ' ' + inputText;
            this.output = Localization.translate((String)"advanced_solar_panels.gui.output") + ' ' + ((ItemStack)container.getOutput()).func_82833_r();
            this.totalEU = String.format("%s %,d %s", Localization.translate((String)"advanced_solar_panels.gui.energyPerOperation"), ((IMolecularTransformerRecipeManager.Input)container.getInput()).totalEU, Localization.translate((String)"ic2.generic.text.EU"));
        }

        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int space = 5;
            int x = 5;
            minecraft.field_71466_p.func_78279_b(this.input, 42, x, recipeWidth - 42, 0xFFFFFF);
            minecraft.field_71466_p.func_78279_b(this.output, 42, x += minecraft.field_71466_p.func_78267_b(this.input, recipeWidth - 42) + 5, recipeWidth - 42, 0xFFFFFF);
            minecraft.field_71466_p.func_78276_b(this.totalEU, 42, x += minecraft.field_71466_p.func_78267_b(this.output, recipeWidth - 42) + 5, 0xFFFFFF);
        }
    }

    protected static class MolecularTransformerCategory
    extends CustomHeightDynamicCategory<IMolecularTransformerRecipeManager> {
        public MolecularTransformerCategory(IGuiHelper guiHelper) {
            super(TEs.molecular_transformer, IMolecularTransformerRecipeManager.RECIPES, guiHelper, 63);
        }

        protected int getProcessSpeed(String name) {
            if ("progress".equals(name)) {
                return 50;
            }
            return super.getProcessSpeed(name);
        }
    }

    public static class CustomHeightDynamicCategory<T>
    extends DynamicCategory<T> {
        protected final int height;

        public CustomHeightDynamicCategory(ITeBlock block, T recipeManager, IGuiHelper guiHelper, int height) {
            super(block, recipeManager, guiHelper);
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }
    }
}

