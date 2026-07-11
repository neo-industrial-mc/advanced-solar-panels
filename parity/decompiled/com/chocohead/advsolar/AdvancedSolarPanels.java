/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.event.TeBlockFinalCallEvent
 *  ic2.core.block.BlockTileEntity
 *  ic2.core.block.ITeBlock
 *  ic2.core.block.TeBlockRegistry
 *  ic2.core.util.ReflectionUtil
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.color.IItemColor
 *  net.minecraft.client.renderer.color.ItemColors
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.event.ColorHandlerEvent$Item
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  net.minecraftforge.oredict.OreDictionary
 *  org.apache.logging.log4j.Logger
 */
package com.chocohead.advsolar;

import com.chocohead.advsolar.ASP_Items;
import com.chocohead.advsolar.Configs;
import com.chocohead.advsolar.Recipes;
import com.chocohead.advsolar.gui.ProgressBars;
import com.chocohead.advsolar.items.ItemCraftingThings;
import com.chocohead.advsolar.renders.PrettyMolecularTransformerTESR;
import com.chocohead.advsolar.tiles.TEs;
import com.chocohead.advsolar.tiles.TileEntityMolecularAssembler;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid="advanced_solar_panels", name="Advanced Solar Panels", dependencies="required-after:ic2@[2.8.74,);", version="4.3.0", acceptedMinecraftVersions="[1.12,1.12.2]")
public final class AdvancedSolarPanels {
    public static final String MODID = "advanced_solar_panels";
    public static Logger log;
    public static BlockTileEntity machines;

    @SubscribeEvent
    public static void register(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(TEs.class, (ResourceLocation)TEs.IDENTITY);
        TeBlockRegistry.setDefaultMaterial((ResourceLocation)TEs.IDENTITY, (Material)Material.field_151573_f);
    }

    @Mod.EventHandler
    public void load(FMLPreInitializationEvent event) {
        log = event.getModLog();
        Configs.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        machines = TeBlockRegistry.get((ResourceLocation)TEs.IDENTITY);
        if (event.getSide().isClient()) {
            AdvancedSolarPanels.setupRenderingGuf();
        }
        ASP_Items.buildItems(event.getSide());
        OreDictionary.registerOre((String)"ingotUranium", (ItemStack)ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.URANIUM_INGOT));
        OreDictionary.registerOre((String)"ingotIridium", (ItemStack)ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRIDIUM_INGOT));
        OreDictionary.registerOre((String)"craftingSolarPanelHV", (ItemStack)machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel));
        OreDictionary.registerOre((String)"craftingSunnariumPart", (ItemStack)ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM_PART));
        OreDictionary.registerOre((String)"craftingSunnarium", (ItemStack)ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.SUNNARIUM));
        OreDictionary.registerOre((String)"craftingMTCore", (ItemStack)ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.MT_CORE));
        OreDictionary.registerOre((String)"craftingMolecularTransformer", (ItemStack)machines.getItemStack((ITeBlock)TEs.molecular_transformer));
    }

    @SideOnly(value=Side.CLIENT)
    private static void setupRenderingGuf() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMolecularAssembler.class, (TileEntitySpecialRenderer)new PrettyMolecularTransformerTESR());
        ForgeHooksClient.registerTESRItemStack((Item)machines.getItem(), (int)TEs.molecular_transformer.getId(), TEs.molecular_transformer.getTeClass());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Recipes.addCraftingRecipes();
        Recipes.addMachineRecipes();
        Recipes.addMolecularTransformerRecipes();
        TEs.buildDummies();
        ProgressBars.addStyles();
        TileEntityMolecularAssembler.MolecularOutput.registerNetwork();
    }

    @SubscribeEvent
    @SideOnly(value=Side.CLIENT)
    public static void doColourThings(ColorHandlerEvent.Item event) {
        ItemColors colours = event.getItemColors();
        IItemColor armourColouring = (IItemColor)((Map)ReflectionUtil.getFieldValue((Field)ReflectionUtil.getField(ItemColors.class, Map.class), (Object)colours)).get(Items.field_151021_T.delegate);
        colours.func_186730_a(armourColouring, new Item[]{ASP_Items.HYBRID_SOLAR_HELMET.getInstance(), ASP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()});
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}

