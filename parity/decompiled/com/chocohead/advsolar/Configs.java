/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Configuration
 *  org.apache.commons.io.IOUtils
 */
package com.chocohead.advsolar;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.MTRecipe;
import com.chocohead.advsolar.MolecularTransformerRecipeManager;
import com.chocohead.advsolar.items.ItemArmourSolarHelmet;
import com.chocohead.advsolar.renders.PrettyMolecularTransformerTESR;
import com.chocohead.advsolar.tiles.TileEntityAdvancedSolar;
import com.chocohead.advsolar.tiles.TileEntityHybridSolar;
import com.chocohead.advsolar.tiles.TileEntityQuantumGenerator;
import com.chocohead.advsolar.tiles.TileEntityQuantumSolar;
import com.chocohead.advsolar.tiles.TileEntitySolarPanel;
import com.chocohead.advsolar.tiles.TileEntityUltimateHybridSolar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.IOUtils;

final class Configs {
    private static final String GENERAL = "general";
    private static final String SOLARS = "solars";
    private static final String QUANTUM_GENERATOR = "quantum generator";
    private static final String CRAFTING = "recipes settings";
    static boolean hardRecipes;
    static boolean easyASPRecipe;
    static boolean canCraftDoubleSlabs;
    static boolean canCraftMT;
    static boolean canCraftASP;
    static boolean canCraftHSP;
    static boolean canCraftUHSP;
    static boolean canCraftQSP;
    static boolean canCraftASH;
    static boolean canCraftHSH;
    static boolean canCraftUHSH;
    private static final String NEW_LINE;
    private static final String CONFIG_VERSION = "2.0";
    static MTRecipe[] MTRecipes;

    Configs() {
    }

    static void loadConfig(File config, boolean client) {
        AdvancedSolarPanels.log.info("Loading ASP Config from " + config.getAbsolutePath());
        Configs.loadNormalConfig(config, client);
        try {
            Configs.loadMolecularTransformerConfig(config.getParentFile(), config.getName());
        }
        catch (ParseException e) {
            MolecularTransformerRecipeManager.showError("Error reading Molecular Transformer recipes file:" + NEW_LINE + e.toString());
        }
    }

    private static void loadNormalConfig(File configFile, boolean client) {
        Configuration config = new Configuration(configFile);
        try {
            config.load();
            TileEntityAdvancedSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get(SOLARS, "AdvancedSPGenDay", 8).getInt(8), config.get(SOLARS, "AdvancedSPGenNight", 1).getInt(1), config.get(SOLARS, "AdvancedSPStorage", 32000).getInt(32000), config.get(SOLARS, "AdvancedSPTier", 1).getInt(1));
            TileEntityHybridSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get(SOLARS, "HybrydSPGenDay", 64).getInt(64), config.get(SOLARS, "HybrydSPGenNight", 8).getInt(8), config.get(SOLARS, "HybrydSPStorage", 100000).getInt(100000), config.get(SOLARS, "HybrydSPTier", 2).getInt(2));
            TileEntityUltimateHybridSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get(SOLARS, "UltimateHSPGenDay", 512).getInt(512), config.get(SOLARS, "UltimateHSPGenNight", 64).getInt(64), config.get(SOLARS, "UltimateHSPStorage", 1000000).getInt(1000000), config.get(SOLARS, "UltimateHSPTier", 3).getInt(3));
            TileEntityQuantumSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get(SOLARS, "QuantumSPGenDay", 4096).getInt(4096), config.get(SOLARS, "QuantumSPGenNight", 2048).getInt(2048), config.get(SOLARS, "QuantumSPStorage", 10000000).getInt(10000000), config.get(SOLARS, "QuantumSPTier", 5).getInt(5));
            TileEntityQuantumGenerator.settings = new TileEntityQuantumGenerator.QuantumGeneratorConfig(config.get(QUANTUM_GENERATOR, "quantumGeneratorDefaultProduction", 512).getInt(512), config.get(QUANTUM_GENERATOR, "quantumGeneratorDefaultTier", 3).getInt(3));
            if (client) {
                PrettyMolecularTransformerTESR.drawActiveCore = config.get(GENERAL, "Draw Molecular Transformer active animation", false).getBoolean(false);
            }
            ItemArmourSolarHelmet.chargeWholeInventory = config.get(GENERAL, "Should solar helmets charge armour only?", true).getBoolean(true);
            hardRecipes = config.get(CRAFTING, "Enable hard recipes", true).getBoolean(true);
            easyASPRecipe = config.get(CRAFTING, "Enable simple Advanced Solar Panel recipe", false).getBoolean(false);
            canCraftDoubleSlabs = !config.get(CRAFTING, "Disable Double-Slab recipe", false).getBoolean(false);
            canCraftMT = !config.get(CRAFTING, "Disable Molecular Transformer recipe", false).getBoolean(false);
            canCraftASP = !config.get(CRAFTING, "Disable Advanced Solar Panel recipe", false).getBoolean(false);
            canCraftASH = !config.get(CRAFTING, "Disable Advanced Solar Helmet recipe", false).getBoolean(false);
            canCraftHSP = !config.get(CRAFTING, "Disable Hybrid Solar Panel recipe", false).getBoolean(false);
            canCraftHSH = !config.get(CRAFTING, "Disable Hybrid Solar Helmet recipe", false).getBoolean(false);
            canCraftUHSP = !config.get(CRAFTING, "Disable Ultimate Solar Panel recipe", false).getBoolean(false);
            canCraftUHSH = !config.get(CRAFTING, "Disable Ultimate Solar Helmet recipe", false).getBoolean(false);
            canCraftQSP = !config.get(CRAFTING, "Disable QuantumSolarPanel recipe", false).getBoolean(false);
        }
        catch (Exception e) {
            AdvancedSolarPanels.log.fatal("Fatal error reading config file.", (Throwable)e);
            throw new RuntimeException(e);
        }
        finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void loadMolecularTransformerConfig(File configFolder, String configFile) throws ParseException {
        int fileExtensionMarker = configFile.lastIndexOf(46);
        File config = new File(configFolder, configFile.substring(0, fileExtensionMarker) + "_MTRecipes" + configFile.substring(fileExtensionMarker));
        AdvancedSolarPanels.log.info("Loading MT Recipes from " + config.getAbsolutePath());
        if (!config.exists()) {
            Configs.fillDefault(config);
        }
        FileInputStream stream = null;
        BufferedReader reader = null;
        ArrayList<MTRecipe> recipes = new ArrayList<MTRecipe>(20);
        try {
            String line;
            stream = new FileInputStream(config);
            reader = new BufferedReader(new InputStreamReader(stream));
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                ++lineNumber;
                if ((line = line.trim()).startsWith("#") || line.isEmpty()) continue;
                if (line.startsWith("version=")) {
                    String version = line.substring(line.indexOf(61) + 1);
                    if (CONFIG_VERSION.equals(version)) continue;
                    throw new ParseException("Advanced Solars expected a file version of 2.0, but the config is " + version, line.indexOf(61) + 1);
                }
                MTRecipe recipe = new MTRecipe(lineNumber, line);
                if (recipe.isValid()) {
                    recipes.add(recipe);
                    continue;
                }
                AdvancedSolarPanels.log.warn("Skipping line {} as it is has the wrong format (expected length 3, found {})", (Object)lineNumber, (Object)recipe.parts.length);
            }
        }
        catch (IOException e) {
            try {
                AdvancedSolarPanels.log.fatal("RIP MT Config!", (Throwable)e);
                throw new RuntimeException("Fatal error reading Molecular Transformer recipe file", e);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly((InputStream)stream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Reader)reader);
        IOUtils.closeQuietly((InputStream)stream);
        MTRecipes = recipes.toArray(new MTRecipe[recipes.size()]);
    }

    private static void fillDefault(File config) {
        FileOutputStream stream = null;
        BufferedWriter writer = null;
        try {
            config.createNewFile();
            stream = new FileOutputStream(config);
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            Configs.write(writer, "##################################################################################################");
            Configs.write(writer, "#                        AdvancedSolarPanels Molecular Transformer Recipes                       #");
            Configs.write(writer, "##################################################################################################");
            Configs.write(writer, "# Format of recipe: \"inputItem*stackSize;outputItem*outputStackSize;energy\"                      #");
            Configs.write(writer, "# InputItem (and outputItem) format:                                                             #");
            Configs.write(writer, "# \"OreDict:forgeOreDictName\" or \"minecraft:item_name@meta\" or \"modID:item_name@meta\"             #");
            Configs.write(writer, "# New line = new recipe.                                                                         #");
            Configs.write(writer, "# Add \"#\" before line to skip parsing line/recipe                                                #");
            Configs.write(writer, "##################################################################################################");
            writer.write("version=2.0" + NEW_LINE);
            Configs.write(writer, "##################################################################################################");
            Configs.write(writer, "minecraft:skull@1; minecraft:nether_star; 250000000");
            Configs.write(writer, "minecraft:iron_ingot@*; ic2:misc_resource#iridium_ore; 9000000");
            Configs.write(writer, "minecraft:netherrack@*; minecraft:gunpowder*2; 70000");
            Configs.write(writer, "minecraft:sand@*; minecraft:gravel; 50000");
            Configs.write(writer, "minecraft:dirt@*; minecraft:clay; 50000");
            Configs.write(writer, "minecraft:coal@1; minecraft:coal@0; 60000");
            Configs.write(writer, "minecraft:glowstone_dust@*; advanced_solar_panels:crafting@1; 1000000");
            Configs.write(writer, "minecraft:glowstone@*; advanced_solar_panels:crafting@0; 9000000");
            Configs.write(writer, "minecraft:wool@4; minecraft:glowstone; 500000");
            Configs.write(writer, "minecraft:wool@11; minecraft:lapis_block; 500000");
            Configs.write(writer, "minecraft:wool@14; minecraft:redstone_block; 500000");
            Configs.write(writer, "minecraft:dye@4; OreDict:gemSapphire; 5000000");
            Configs.write(writer, "minecraft:redstone@*; OreDict:gemRuby; 5000000");
            Configs.write(writer, "minecraft:coal@0; ic2:crafting#industrial_diamond; 9000000");
            Configs.write(writer, "ic2:crafting#industrial_diamond; minecraft:diamond; 1000000");
            Configs.write(writer, "OreDict:dustTitanium; OreDict:dustChrome; 500000");
            Configs.write(writer, "OreDict:ingotTitanium; OreDict:ingotChrome; 500000");
            Configs.write(writer, "OreDict:gemNetherQuartz; OreDict:gemCertusQuartz; 500000");
            Configs.write(writer, "OreDict:ingotCopper; OreDict:ingotNickel; 300000");
            Configs.write(writer, "OreDict:ingotTin; OreDict:ingotSilver; 500000");
            Configs.write(writer, "OreDict:ingotSilver; OreDict:ingotGold; 500000");
            Configs.write(writer, "OreDict:ingotGold; OreDict:ingotPlatinum; 9000000");
        }
        catch (IOException e) {
            try {
                AdvancedSolarPanels.log.fatal("RIP MT Config!", (Throwable)e);
                throw new RuntimeException("Fatal error writing Molecular Transformer recipe file", e);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(writer);
                IOUtils.closeQuietly(stream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Writer)writer);
        IOUtils.closeQuietly((OutputStream)stream);
    }

    private static void write(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    static {
        NEW_LINE = System.getProperty("line.separator");
    }
}

