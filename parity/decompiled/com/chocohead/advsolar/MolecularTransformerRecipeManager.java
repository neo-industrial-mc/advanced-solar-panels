/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.recipe.IRecipeInput
 *  ic2.api.recipe.MachineRecipe
 *  ic2.core.init.MainConfig
 *  ic2.core.recipe.MachineRecipeHelper
 *  ic2.core.util.StackUtil
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 */
package com.chocohead.advsolar;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.IMolecularTransformerRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.init.MainConfig;
import ic2.core.recipe.MachineRecipeHelper;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

class MolecularTransformerRecipeManager
extends MachineRecipeHelper<IMolecularTransformerRecipeManager.Input, ItemStack>
implements IMolecularTransformerRecipeManager {
    MolecularTransformerRecipeManager() {
    }

    static void showError(String message) {
        if (!MainConfig.ignoreInvalidRecipes) {
            throw new RuntimeException(message);
        }
        AdvancedSolarPanels.log.warn(message);
    }

    @Override
    public boolean addRecipe(IRecipeInput input, int totalEU, ItemStack output, boolean replace) {
        return this.addRecipe(new IMolecularTransformerRecipeManager.Input(input, totalEU), output, null, replace);
    }

    public boolean addRecipe(IMolecularTransformerRecipeManager.Input input, ItemStack output, NBTTagCompound metadata, boolean replace) {
        if (input == null) {
            MolecularTransformerRecipeManager.showError("Invalid recipe input: null");
            return false;
        }
        if (StackUtil.isEmpty((ItemStack)output)) {
            MolecularTransformerRecipeManager.showError("Invalid recipe output: " + StackUtil.toStringSafe((ItemStack)output));
            return false;
        }
        if (input.input.matches(output) && (metadata == null || !metadata.func_74764_b("ignoreSameInputOutput"))) {
            MolecularTransformerRecipeManager.showError("The output ItemStack " + StackUtil.toStringSafe((ItemStack)output) + " is the same as the recipe input " + input + ".");
            return false;
        }
        for (ItemStack is : input.input.getInputs()) {
            MachineRecipe recipe = this.getRecipe(is);
            if (recipe == null) continue;
            if (replace) {
                do {
                    this.recipes.remove(input);
                    this.removeCachedRecipes(input);
                } while ((recipe = this.getRecipe(is)) != null);
                continue;
            }
            return false;
        }
        MachineRecipe recipe = new MachineRecipe((Object)input, (Object)output.func_77946_l(), metadata);
        this.recipes.put(input, recipe);
        this.addToCache(recipe);
        return true;
    }

    protected IRecipeInput getForInput(IMolecularTransformerRecipeManager.Input input) {
        return input.input;
    }

    @Override
    public int getTotalEUNeeded(ItemStack input) {
        IMolecularTransformerRecipeManager.Input recipe = (IMolecularTransformerRecipeManager.Input)this.getRecipe(input).getInput();
        return recipe == null ? -1 : recipe.totalEU;
    }
}

