/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.core.block.state.IIdProvider
 *  ic2.core.init.BlocksItems
 *  ic2.core.item.ItemMulti
 *  ic2.core.ref.ItemName
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.item.Item
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.chocohead.advsolar.items;

import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import java.util.Locale;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingThings
extends ItemMulti<CraftingTypes> {
    protected static final String NAME = "crafting";

    public ItemCraftingThings() {
        super(null, CraftingTypes.class);
        ((ItemCraftingThings)BlocksItems.registerItem((Item)this, (ResourceLocation)new ResourceLocation("advanced_solar_panels", NAME))).func_77655_b(NAME);
    }

    @SideOnly(value=Side.CLIENT)
    protected void registerModel(int meta, ItemName name, String extraName) {
        ModelLoader.setCustomModelResourceLocation((Item)this, (int)meta, (ModelResourceLocation)new ModelResourceLocation("advanced_solar_panels:crafting/" + CraftingTypes.getFromID(meta).getName(), null));
    }

    public String func_77658_a() {
        return "advanced_solar_panels." + super.func_77658_a().substring(4);
    }

    public static enum CraftingTypes implements IIdProvider
    {
        SUNNARIUM(0),
        SUNNARIUM_PART(1),
        SUNNARIUM_ALLOY(2),
        IRRADIANT_URANIUM(3),
        ENRICHED_SUNNARIUM(4),
        ENRICHED_SUNNARIUM_ALLOY(5),
        IRRADIANT_GLASS_PANE(6),
        IRIDIUM_IRON_PLATE(7),
        REINFORCED_IRIDIUM_IRON_PLATE(8),
        IRRADIANT_REINFORCED_PLATE(9),
        IRIDIUM_INGOT(10),
        URANIUM_INGOT(11),
        MT_CORE(12),
        QUANTUM_CORE(13);

        private final String name = this.name().toLowerCase(Locale.ENGLISH);
        private final int ID;
        private static final CraftingTypes[] VALUES;

        private CraftingTypes(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }

        public static CraftingTypes getFromID(int ID) {
            return VALUES[ID % VALUES.length];
        }

        static {
            VALUES = CraftingTypes.values();
        }
    }
}

