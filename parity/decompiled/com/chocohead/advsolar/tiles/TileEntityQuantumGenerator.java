/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.energy.EnergyNet
 *  ic2.api.energy.event.EnergyTileLoadEvent
 *  ic2.api.energy.event.EnergyTileUnloadEvent
 *  ic2.api.energy.tile.IEnergyAcceptor
 *  ic2.api.energy.tile.IEnergyTile
 *  ic2.api.energy.tile.IMultiEnergySource
 *  ic2.api.network.INetworkClientTileEntityEventListener
 *  ic2.core.ContainerBase
 *  ic2.core.IHasGui
 *  ic2.core.block.ITeBlock
 *  ic2.core.block.TileEntityBlock
 *  ic2.core.block.TileEntityInventory
 *  ic2.core.block.comp.Redstone
 *  ic2.core.block.comp.Redstone$IRedstoneChangeHandler
 *  ic2.core.block.comp.TileEntityComponent
 *  ic2.core.gui.dynamic.DynamicContainer
 *  ic2.core.gui.dynamic.GuiParser
 *  ic2.core.gui.dynamic.GuiParser$GuiNode
 *  ic2.core.init.Localization
 *  ic2.core.network.GuiSynced
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.util.ITooltipFlag
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.chocohead.advsolar.tiles;

import com.chocohead.advsolar.gui.BackgroundlessDynamicGUI;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMultiEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Redstone;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityQuantumGenerator
extends TileEntityInventory
implements IMultiEnergySource,
IHasGui,
INetworkClientTileEntityEventListener {
    public static QuantumGeneratorConfig settings;
    @GuiSynced
    public int production;
    @GuiSynced
    public int tier;
    protected Redstone redstone;
    private boolean addedToEnet;

    public TileEntityQuantumGenerator() {
        this.production = TileEntityQuantumGenerator.settings.production;
        this.tier = TileEntityQuantumGenerator.settings.tier;
        this.redstone = (Redstone)this.addComponent((TileEntityComponent)new Redstone((TileEntityBlock)this));
        this.redstone.subscribe(new Redstone.IRedstoneChangeHandler(){

            public void onRedstoneChange(int newLevel) {
                TileEntityQuantumGenerator.this.setActive(newLevel <= 0);
            }
        });
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.field_145850_b.field_72995_K) {
            this.addedToEnet = !MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent((IEnergyTile)this));
        }
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (this.addedToEnet) {
            this.addedToEnet = MinecraftForge.EVENT_BUS.post((Event)new EnergyTileUnloadEvent((IEnergyTile)this));
        }
    }

    public void func_145839_a(NBTTagCompound nbt) {
        super.func_145839_a(nbt);
        this.production = nbt.func_74762_e("production");
        this.tier = nbt.func_74762_e("tier");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
        super.func_189515_b(nbt);
        nbt.func_74768_a("production", this.production);
        nbt.func_74768_a("tier", this.tier);
        return nbt;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!this.field_145850_b.field_72995_K) {
            this.setActive(true);
        }
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }

    public int getSourceTier() {
        return this.tier;
    }

    public double getOfferedEnergy() {
        return this.getActive() ? (this.sendMultipleEnergyPackets() ? (double)this.production / (double)this.getMultipleEnergyPacketAmount() : (double)this.production) : 0.0;
    }

    public void drawEnergy(double amount) {
    }

    public boolean sendMultipleEnergyPackets() {
        return (double)this.production - EnergyNet.instance.getPowerFromTier(this.tier) > 0.0;
    }

    public int getMultipleEnergyPacketAmount() {
        return (int)Math.ceil((double)this.production / EnergyNet.instance.getPowerFromTier(this.tier));
    }

    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate((String)"ic2.item.tooltip.PowerTier", (Object[])new Object[]{"Variable"}));
    }

    public ContainerBase<TileEntityQuantumGenerator> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create((IInventory)this, (EntityPlayer)player, (GuiParser.GuiNode)GuiParser.parse((ITeBlock)this.teBlock));
    }

    @SideOnly(value=Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return BackgroundlessDynamicGUI.create(this, player, GuiParser.parse((ITeBlock)this.teBlock));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        switch (event / 10) {
            case 0: {
                switch (event % 10) {
                    case 0: {
                        this.changeProduction(-100);
                        break;
                    }
                    case 1: {
                        this.changeProduction(-10);
                        break;
                    }
                    case 2: {
                        this.changeProduction(-1);
                        break;
                    }
                    case 3: {
                        this.changeProduction(1);
                        break;
                    }
                    case 4: {
                        this.changeProduction(10);
                        break;
                    }
                    case 5: {
                        this.changeProduction(100);
                    }
                }
                break;
            }
            case 1: {
                switch (event % 10) {
                    case 0: {
                        this.changeProduction(-500);
                        break;
                    }
                    case 1: {
                        this.changeProduction(-50);
                        break;
                    }
                    case 2: {
                        this.changeProduction(-5);
                        break;
                    }
                    case 3: {
                        this.changeProduction(5);
                        break;
                    }
                    case 4: {
                        this.changeProduction(50);
                        break;
                    }
                    case 5: {
                        this.changeProduction(500);
                    }
                }
                break;
            }
            case 2: {
                this.tier = event % 10 + 1;
            }
        }
    }

    protected void changeProduction(int value) {
        this.production += value;
        if (this.production < 0) {
            this.production = 0;
        }
    }

    public String getTier() {
        return this.tier > 5 ? Localization.translate((String)"advanced_solar_panels.gui.max") : Integer.toString(this.tier);
    }

    public static final class QuantumGeneratorConfig {
        final int production;
        final int tier;

        public QuantumGeneratorConfig(int production, int tier) {
            this.production = production;
            this.tier = tier;
        }
    }
}

