/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.item.IC2Items
 *  ic2.api.recipe.IRecipeInput
 *  ic2.api.recipe.IRecipeInputFactory
 *  ic2.api.recipe.Recipes
 *  ic2.core.block.ITeBlock
 *  ic2.core.init.Rezepte
 *  ic2.core.recipe.ArmorDyeingRecipe
 *  ic2.core.recipe.ArmorDyeingRecipe$RecipeInputClass
 *  ic2.core.recipe.ColourCarryingRecipe
 *  ic2.core.util.ConfigUtil
 *  ic2.core.util.StackUtil
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.util.NonNullList
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.oredict.OreDictionary
 */
package com.chocohead.advsolar;

import com.chocohead.advsolar.ASP_Items;
import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.Configs;
import com.chocohead.advsolar.IMolecularTransformerRecipeManager;
import com.chocohead.advsolar.MTRecipe;
import com.chocohead.advsolar.items.ItemArmourSolarHelmet;
import com.chocohead.advsolar.items.ItemCraftingThings;
import com.chocohead.advsolar.tiles.TEs;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.block.ITeBlock;
import ic2.core.init.Rezepte;
import ic2.core.recipe.ArmorDyeingRecipe;
import ic2.core.recipe.ColourCarryingRecipe;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import java.text.ParseException;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

final class Recipes {
    Recipes() {
    }

    static void addCraftingRecipes() {
        Recipes.addRecipe((IRecipe)new ArmorDyeingRecipe((IRecipeInput)new ArmorDyeingRecipe.RecipeInputClass(ItemArmourSolarHelmet.class){

            protected boolean matches(Item item) {
                return super.matches(item) && ((ItemArmourSolarHelmet)item).canBeDyed();
            }
        }));
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), "III", "IPI", "III", Character.valueOf('I'), "plateIron", Character.valueOf('P'), "ingotIridium");
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), "ACA", "CIC", "ACA", Character.valueOf('A'), IC2Items.getItem((String)"crafting", (String)"alloy"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"carbon_plate"), Character.valueOf('I'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE));
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE), "RSR", "LIL", "RDR", Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_PART), Character.valueOf('L'), new ItemStack(Items.field_151100_aR, 1, 4), Character.valueOf('I'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), Character.valueOf('D'), Items.field_151045_i);
        if (Configs.hardRecipes) {
            if (Configs.canCraftASP) {
                if (Configs.easyASPRecipe) {
                    Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), "PPP", "ASA", "CMC", Character.valueOf('P'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), Character.valueOf('A'), IC2Items.getItem((String)"crafting", (String)"alloy"), Character.valueOf('S'), IC2Items.getItem((String)"te", (String)"solar_generator"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('M'), IC2Items.getItem((String)"resource", (String)"advanced_machine"));
                } else {
                    Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), "PPP", "ASA", "CIC", Character.valueOf('P'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), Character.valueOf('A'), IC2Items.getItem((String)"crafting", (String)"alloy"), Character.valueOf('S'), IC2Items.getItem((String)"te", (String)"solar_generator"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('I'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE));
                }
            }
            if (Configs.canCraftHSP) {
                Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.hybrid_solar_panel), "PLP", "IAI", "CSC", Character.valueOf('P'), IC2Items.getItem((String)"crafting", (String)"carbon_plate"), Character.valueOf('L'), Blocks.field_150368_y, Character.valueOf('I'), IC2Items.getItem((String)"crafting", (String)"iridium"), Character.valueOf('A'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM));
            }
            if (Configs.canCraftUHSP) {
                Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel), " L ", "CSC", "ECE", Character.valueOf('L'), Blocks.field_150368_y, Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"coal_chunk"), Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), Character.valueOf('E'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY));
            }
        } else {
            if (Configs.canCraftASP) {
                if (Configs.easyASPRecipe) {
                    Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), "GGG", "ASA", "CMC", Character.valueOf('G'), IC2Items.getItem((String)"glass", (String)"reinforced"), Character.valueOf('A'), IC2Items.getItem((String)"crafting", (String)"alloy"), Character.valueOf('S'), IC2Items.getItem((String)"te", (String)"solar_generator"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('M'), IC2Items.getItem((String)"resource", (String)"advanced_machine"));
                } else {
                    Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), "GGG", "ASA", "CPC", Character.valueOf('G'), IC2Items.getItem((String)"glass", (String)"reinforced"), Character.valueOf('A'), IC2Items.getItem((String)"crafting", (String)"alloy"), Character.valueOf('S'), IC2Items.getItem((String)"te", (String)"solar_generator"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('P'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE));
                }
            }
            if (Configs.canCraftHSP) {
                Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.hybrid_solar_panel), "PLP", "IAI", "CSC", Character.valueOf('P'), IC2Items.getItem((String)"crafting", (String)"carbon_plate"), Character.valueOf('L'), Blocks.field_150368_y, Character.valueOf('I'), IC2Items.getItem((String)"crafting", (String)"iridium"), Character.valueOf('A'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM));
            }
            if (Configs.canCraftUHSP) {
                Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel), " L ", "CSC", "ECE", Character.valueOf('L'), Blocks.field_150368_y, Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"coal_chunk"), Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), Character.valueOf('E'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
            }
        }
        if (Configs.canCraftUHSP) {
            Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel), "SSS", "SCS", "SSS", Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.hybrid_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"));
            Recipes.addShapelessRecipe(StackUtil.setSize((ItemStack)AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.hybrid_solar_panel), (int)8), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel));
        }
        if (Configs.canCraftQSP) {
            Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.QUANTUM_CORE), "ANA", "NEN", "ANA", Character.valueOf('A'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), Character.valueOf('N'), Items.field_151156_bN, Character.valueOf('E'), Items.field_151061_bv);
            Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.quantum_solar_panel), "SSS", "SQS", "SSS", Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel), Character.valueOf('Q'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.QUANTUM_CORE));
        }
        if (Configs.canCraftASH) {
            Recipes.addShapedRecipe(new ItemStack(ASP_Items.ADVANCED_SOLAR_HELMET.getInstance()), " S ", "CNC", "GTG", Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.advanced_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('N'), IC2Items.getItem((String)"nano_helmet"), Character.valueOf('G'), IC2Items.getItem((String)"cable", (String)"type:gold,insulation:2"), Character.valueOf('T'), IC2Items.getItem((String)"te", (String)"lv_transformer"));
        }
        if (Configs.canCraftHSH) {
            Recipes.addShapedColourRecipe(new ItemStack(ASP_Items.HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG", Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.hybrid_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('Q'), IC2Items.getItem((String)"quantum_helmet"), Character.valueOf('G'), IC2Items.getItem((String)"cable", (String)"type:glass,insulation:0"), Character.valueOf('T'), IC2Items.getItem((String)"te", (String)"hv_transformer"));
        }
        if (Configs.canCraftUHSH) {
            Recipes.addShapedColourRecipe(new ItemStack(ASP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG", Character.valueOf('S'), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('Q'), IC2Items.getItem((String)"quantum_helmet"), Character.valueOf('G'), IC2Items.getItem((String)"cable", (String)"type:glass,insulation:0"), Character.valueOf('T'), IC2Items.getItem((String)"te", (String)"hv_transformer"));
            Recipes.addShapelessRecipe(new ItemStack(ASP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), ASP_Items.HYBRID_SOLAR_HELMET.getInstance(), AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel));
        }
        if (Configs.canCraftMT) {
            Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.MT_CORE), "PRP", "P P", "PRP", Character.valueOf('P'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), Character.valueOf('R'), IC2Items.getItem((String)"thick_neutron_reflector"));
            Recipes.addShapedRecipe(AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.molecular_transformer), "MTM", "CcC", "MTM", Character.valueOf('M'), IC2Items.getItem((String)"resource", (String)"advanced_machine"), Character.valueOf('T'), IC2Items.getItem((String)"te", (String)"ev_transformer"), Character.valueOf('C'), IC2Items.getItem((String)"crafting", (String)"advanced_circuit"), Character.valueOf('c'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.MT_CORE));
        }
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_URANIUM), " G ", "GUG", " G ", Character.valueOf('G'), Items.field_151114_aO, Character.valueOf('U'), "ingotUranium");
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), "GGG", "UDU", "GGG", Character.valueOf('G'), IC2Items.getItem((String)"glass", (String)"reinforced"), Character.valueOf('U'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_URANIUM), Character.valueOf('D'), Items.field_151114_aO);
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), "UUU", "USU", "UUU", Character.valueOf('U'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_URANIUM), Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM));
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), " S ", "SAS", " S ", Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), Character.valueOf('A'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_ALLOY), "III", "ISI", "III", Character.valueOf('I'), IC2Items.getItem((String)"crafting", (String)"iridium"), Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM));
        Recipes.addShapedRecipe(ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM), "SSS", "SSS", "SSS", Character.valueOf('S'), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_PART));
        if (Configs.canCraftDoubleSlabs) {
            Recipes.addShapelessRecipe(new ItemStack(ASP_Items.DOUBLE_STONE_SLAB.getInstance(), 1, 0), new ItemStack((Block)Blocks.field_150333_U, 1, 0), new ItemStack((Block)Blocks.field_150333_U, 1, 0));
        }
    }

    private static void addShapedRecipe(ItemStack output, Object ... inputs) {
        ic2.api.recipe.Recipes.advRecipes.addRecipe(output, inputs);
    }

    private static void addShapedColourRecipe(ItemStack output, Object ... inputs) {
        ColourCarryingRecipe.addAndRegister((ItemStack)output, (Object[])inputs);
    }

    private static void addShapelessRecipe(ItemStack output, Object ... inputs) {
        ic2.api.recipe.Recipes.advRecipes.addShapelessRecipe(output, inputs);
    }

    private static void addRecipe(IRecipe recipe) {
        ModContainer us = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().stream().filter(mod -> "ic2".equals(mod.getModId())).findFirst().ifPresent(arg_0 -> ((Loader)Loader.instance()).setActiveModContainer(arg_0));
        Rezepte.registerRecipe((IRecipe)recipe);
        Loader.instance().setActiveModContainer(us);
    }

    static void addMachineRecipes() {
        IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.addCompressorRecipe(input.forStack(IC2Items.getItem((String)"misc_resource", (String)"iridium_ore")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRIDIUM_INGOT));
        Recipes.addExtrudingRecipe(input.forStack(IC2Items.getItem((String)"crafting", (String)"iridium")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRIDIUM_INGOT));
        Recipes.addCompressorRecipe(input.forStack(IC2Items.getItem((String)"resource", (String)"uranium_ore")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.URANIUM_INGOT));
        Recipes.addCompressorRecipe(input.forStack(IC2Items.getItem((String)"crushed", (String)"uranium")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.URANIUM_INGOT));
        Recipes.addCompressorRecipe(input.forStack(IC2Items.getItem((String)"purified", (String)"uranium")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.URANIUM_INGOT));
        Recipes.addCompressorRecipe(input.forStack(IC2Items.getItem((String)"nuclear", (String)"uranium")), ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.URANIUM_INGOT));
    }

    private static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
        ic2.api.recipe.Recipes.compressor.addRecipe(input, null, false, new ItemStack[]{output});
    }

    private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
        ic2.api.recipe.Recipes.metalformerExtruding.addRecipe(input, null, false, new ItemStack[]{output});
    }

    static void addMolecularTransformerRecipes() {
        AdvancedSolarPanels.log.info("Loading Molecular Transformer recipes from file");
        int successes = 0;
        for (MTRecipe recipe : Configs.MTRecipes) {
            try {
                if (!Recipes.decodeLine(recipe.lineNumber, recipe.parts)) continue;
                ++successes;
            }
            catch (ParseException e) {
                AdvancedSolarPanels.log.warn("Skipping line " + recipe.lineNumber + " due to an error parsing", (Throwable)e);
            }
        }
        AdvancedSolarPanels.log.info("Load complete, successfully loaded {} out of {}.", (Object)successes, (Object)Configs.MTRecipes.length);
    }

    private static boolean decodeLine(int number, String[] parts) throws ParseException {
        int energy;
        IRecipeInput input = ConfigUtil.asRecipeInputWithAmount((String)parts[0].trim());
        if (input == null) {
            AdvancedSolarPanels.log.warn("Skipping line {} as the input ({}) cannot be resolved", (Object)number, (Object)parts[0].trim());
            return false;
        }
        ItemStack output = ConfigUtil.asStackWithAmount((String)parts[1].trim());
        if (output == null) {
            NonNullList potentialOptions;
            String attempt = parts[1].trim();
            if (attempt.startsWith("OreDict:") && !(potentialOptions = OreDictionary.getOres((String)attempt.substring(attempt.indexOf(58) + 1).trim())).isEmpty()) {
                output = (ItemStack)potentialOptions.get(0);
                AdvancedSolarPanels.log.debug("Continued on line {} as the output ({}) could be resolved to {}", (Object)number, (Object)attempt, (Object)output);
            }
            if (output == null) {
                AdvancedSolarPanels.log.warn("Skipping line {} as the output ({}) cannot be resolved", (Object)number, (Object)attempt);
                return false;
            }
        }
        try {
            energy = Integer.parseInt(parts[2].trim());
        }
        catch (NumberFormatException e) {
            AdvancedSolarPanels.log.warn("Skipping line {} as the energy ({}) cannot be resolved to a number", (Object)number, (Object)parts[2].trim());
            return false;
        }
        if (!IMolecularTransformerRecipeManager.RECIPES.addRecipe(input, energy, output, false)) {
            AdvancedSolarPanels.log.warn("Skipping line {} as the recipe is a duplicate", (Object)number);
            return false;
        }
        return true;
    }
}

