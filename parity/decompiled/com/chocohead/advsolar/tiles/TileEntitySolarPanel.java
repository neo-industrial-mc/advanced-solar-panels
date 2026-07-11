/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.energy.EnergyNet
 *  ic2.api.energy.event.EnergyTileLoadEvent
 *  ic2.api.energy.event.EnergyTileUnloadEvent
 *  ic2.api.energy.tile.IEnergyAcceptor
 *  ic2.api.energy.tile.IEnergySource
 *  ic2.api.energy.tile.IEnergyTile
 *  ic2.core.ContainerBase
 *  ic2.core.IHasGui
 *  ic2.core.block.ITeBlock
 *  ic2.core.block.TileEntityInventory
 *  ic2.core.block.invslot.InvSlot$Access
 *  ic2.core.gui.dynamic.DynamicContainer
 *  ic2.core.gui.dynamic.GuiParser
 *  ic2.core.gui.dynamic.GuiParser$GuiNode
 *  ic2.core.gui.dynamic.IGuiValueProvider
 *  ic2.core.init.Localization
 *  ic2.core.network.GuiSynced
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.util.ITooltipFlag
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
import com.chocohead.advsolar.slots.InvSlotMultiCharge;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntitySolarPanel
extends TileEntityInventory
implements IEnergySource,
IHasGui,
IGuiValueProvider {
    public final int maxStorage;
    protected final int dayPower;
    protected final int nightPower;
    protected final int tier;
    protected final InvSlotMultiCharge chargeSlots;
    private final double tierPower;
    @GuiSynced
    public int storage;
    @GuiSynced
    protected GenerationState active = GenerationState.NONE;
    protected int ticker;
    protected boolean canRain;
    protected boolean hasSky;
    private boolean addedToEnet;

    public TileEntitySolarPanel(SolarConfig config) {
        this(config.dayPower, config.nightPower, config.storage, config.tier);
    }

    public TileEntitySolarPanel(int dayPower, int nightPower, int storage, int tier) {
        this.dayPower = dayPower;
        this.nightPower = nightPower;
        this.maxStorage = storage;
        this.tier = tier;
        this.tierPower = EnergyNet.instance.getPowerFromTier(tier);
        this.chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.field_145850_b.field_72995_K) {
            this.addedToEnet = !MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent((IEnergyTile)this));
            this.canRain = this.field_145850_b.func_180494_b(this.field_174879_c).func_76738_d() || this.field_145850_b.func_180494_b(this.field_174879_c).func_76727_i() > 0.0f;
            this.hasSky = !this.field_145850_b.field_73011_w.func_177495_o();
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
        this.storage = nbt.func_74762_e("storage");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
        super.func_189515_b(nbt);
        nbt.func_74768_a("storage", this.storage);
        return nbt;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.ticker++ % this.tickRate() == 0) {
            this.checkTheSky();
        }
        switch (this.active) {
            case DAY: {
                this.tryGenerateEnergy(this.dayPower);
                break;
            }
            case NIGHT: {
                this.tryGenerateEnergy(this.nightPower);
                break;
            }
        }
        if (this.storage > 0) {
            this.storage = (int)((double)this.storage - this.chargeSlots.charge(this.storage));
        }
    }

    protected int tickRate() {
        return 128;
    }

    public void checkTheSky() {
        this.active = this.hasSky && this.field_145850_b.func_175710_j(this.field_174879_c.func_177984_a()) ? (this.field_145850_b.func_72935_r() && (!this.canRain || !this.field_145850_b.func_72896_J() && !this.field_145850_b.func_72911_I()) ? GenerationState.DAY : GenerationState.NIGHT) : GenerationState.NONE;
    }

    public void tryGenerateEnergy(int amount) {
        this.storage = this.storage + amount <= this.maxStorage ? (this.storage += amount) : this.maxStorage;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }

    public int getSourceTier() {
        return this.tier;
    }

    public double getOfferedEnergy() {
        return (double)this.storage < this.tierPower ? (double)this.storage : this.tierPower;
    }

    public void drawEnergy(double amount) {
        this.storage = (int)((double)this.storage - amount);
    }

    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate((String)"ic2.item.tooltip.PowerTier", (Object[])new Object[]{this.tier}));
    }

    public ContainerBase<? extends TileEntitySolarPanel> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create((IInventory)this, (EntityPlayer)player, (GuiParser.GuiNode)GuiParser.parse((ITeBlock)this.teBlock));
    }

    @SideOnly(value=Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return BackgroundlessDynamicGUI.create(this, player, GuiParser.parse((ITeBlock)this.teBlock));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public double getGuiValue(String name) {
        if ("progress".equals(name)) {
            return (double)this.storage / (double)this.maxStorage;
        }
        throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
    }

    public boolean getGuiState(String name) {
        if ("sunlight".equals(name)) {
            return this.active == GenerationState.DAY;
        }
        if ("moonlight".equals(name)) {
            return this.active == GenerationState.NIGHT;
        }
        return super.getGuiState(name);
    }

    public String getMaxOutput() {
        return String.format("%s %.0f %s", Localization.translate((String)"advanced_solar_panels.gui.maxOutput"), EnergyNet.instance.getPowerFromTier(this.tier), Localization.translate((String)"ic2.generic.text.EUt"));
    }

    public String getOutput() {
        switch (this.active) {
            case DAY: {
                return String.format("%s %d %s", Localization.translate((String)"advanced_solar_panels.gui.generating"), this.dayPower, Localization.translate((String)"ic2.generic.text.EUt"));
            }
            case NIGHT: {
                return String.format("%s %d %s", Localization.translate((String)"advanced_solar_panels.gui.generating"), this.nightPower, Localization.translate((String)"ic2.generic.text.EUt"));
            }
        }
        return String.format("%s 0 %s", Localization.translate((String)"advanced_solar_panels.gui.generating"), Localization.translate((String)"ic2.generic.text.EUt"));
    }

    public static final class SolarConfig {
        public final int dayPower;
        public final int nightPower;
        final int storage;
        final int tier;

        public SolarConfig(int dayPower, int nightPower, int storage, int tier) {
            this.dayPower = dayPower;
            this.nightPower = nightPower;
            this.storage = storage;
            this.tier = tier;
        }
    }

    public static enum GenerationState {
        NONE,
        NIGHT,
        DAY;

    }
}

