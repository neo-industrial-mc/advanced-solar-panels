/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.core.block.ITeBlock
 *  ic2.core.block.TileEntityBlock
 *  ic2.core.ref.TeBlock$DefaultDrop
 *  ic2.core.ref.TeBlock$HarvestTool
 *  ic2.core.util.Util
 *  net.minecraft.item.EnumRarity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.fml.common.registry.GameRegistry
 */
package com.chocohead.advsolar.tiles;

import com.chocohead.advsolar.tiles.TileEntityAdvancedSolar;
import com.chocohead.advsolar.tiles.TileEntityHybridSolar;
import com.chocohead.advsolar.tiles.TileEntityMolecularAssembler;
import com.chocohead.advsolar.tiles.TileEntityQuantumGenerator;
import com.chocohead.advsolar.tiles.TileEntityQuantumSolar;
import com.chocohead.advsolar.tiles.TileEntityUltimateHybridSolar;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import java.util.Set;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum TEs implements ITeBlock
{
    molecular_transformer(TileEntityMolecularAssembler.class, 0, EnumRarity.RARE),
    quantum_generator(TileEntityQuantumGenerator.class, 1, EnumRarity.EPIC),
    advanced_solar_panel(TileEntityAdvancedSolar.class, 2),
    hybrid_solar_panel(TileEntityHybridSolar.class, 3, EnumRarity.RARE),
    ultimate_solar_panel(TileEntityUltimateHybridSolar.class, 4, EnumRarity.EPIC),
    quantum_solar_panel(TileEntityQuantumSolar.class, 5, EnumRarity.EPIC);

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;
    private static final TEs[] VALUES;
    public static final ResourceLocation IDENTITY;

    private TEs(Class<? extends TileEntityBlock> teClass, int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);
    }

    private TEs(Class<? extends TileEntityBlock> teClass, int itemMeta, EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;
        GameRegistry.registerTileEntity(teClass, (String)("advanced_solar_panels:" + this.getName()));
    }

    public boolean hasItem() {
        return true;
    }

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.itemMeta;
    }

    public ResourceLocation getIdentifier() {
        return IDENTITY;
    }

    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    public boolean hasActive() {
        return this == quantum_generator;
    }

    public float getHardness() {
        return 3.0f;
    }

    public float getExplosionResistance() {
        return 15.0f;
    }

    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Pickaxe;
    }

    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Self;
    }

    public boolean allowWrenchRotating() {
        return false;
    }

    public Set<EnumFacing> getSupportedFacings() {
        return Util.horizontalFacings;
    }

    public EnumRarity getRarity() {
        return this.rarity;
    }

    public static void buildDummies() {
        ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !"advanced_solar_panels".equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (TEs block : VALUES) {
            if (block.teClass == null) continue;
            try {
                block.dummyTe = block.teClass.newInstance();
            }
            catch (Exception e) {
                if (!Util.inDev()) continue;
                e.printStackTrace();
            }
        }
    }

    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    static {
        VALUES = TEs.values();
        IDENTITY = new ResourceLocation("advanced_solar_panels", "machines");
    }
}

