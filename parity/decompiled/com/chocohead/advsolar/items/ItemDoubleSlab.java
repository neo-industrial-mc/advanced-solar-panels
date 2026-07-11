/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.core.IC2
 *  ic2.core.init.BlocksItems
 *  ic2.core.ref.IItemModelProvider
 *  ic2.core.ref.ItemName
 *  ic2.core.util.StackUtil
 *  net.minecraft.block.Block
 *  net.minecraft.block.SoundType
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.chocohead.advsolar.items;

import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDoubleSlab
extends ItemBlock
implements IItemModelProvider {
    protected final String location;

    public ItemDoubleSlab() {
        super((Block)Blocks.field_150334_T);
        this.func_77637_a((CreativeTabs)IC2.tabIC2);
        this.location = ((ResourceLocation)Block.field_149771_c.func_177774_c((Object)Blocks.field_150334_T)).func_110623_a();
        BlocksItems.registerItem((Item)this, (ResourceLocation)new ResourceLocation("advanced_solar_panels", this.location));
    }

    @SideOnly(value=Side.CLIENT)
    public void registerModels(ItemName name) {
        ModelLoader.setCustomModelResourceLocation((Item)this, (int)0, (ModelResourceLocation)new ModelResourceLocation("advanced_solar_panels:" + this.location, null));
    }

    public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.func_180495_p(pos);
        Block block = state.func_177230_c();
        ItemStack stack = StackUtil.get((EntityPlayer)player, (EnumHand)hand);
        if (!block.func_176200_f((IBlockAccess)world, pos)) {
            pos = pos.func_177972_a(facing);
        }
        if (!StackUtil.isEmpty((ItemStack)stack) && player.func_175151_a(pos, facing, stack) && world.func_190527_a(block, pos, false, facing, (Entity)player)) {
            if (this.placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, block.func_176203_a(this.func_77647_b(stack.func_77960_j())))) {
                SoundType soundtype = this.field_150939_a.getSoundType(state, world, pos, (Entity)player);
                world.func_184133_a(player, pos, soundtype.func_185841_e(), SoundCategory.BLOCKS, (soundtype.func_185843_a() + 1.0f) / 2.0f, soundtype.func_185847_b() * 0.8f);
                StackUtil.consumeOrError((EntityPlayer)player, (EnumHand)hand, (int)1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    public void func_150895_a(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.func_194125_a(tab)) {
            items.add((Object)new ItemStack((Item)this));
        }
    }
}

