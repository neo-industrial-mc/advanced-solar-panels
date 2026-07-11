/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.api.energy.event.EnergyTileLoadEvent
 *  ic2.api.energy.event.EnergyTileUnloadEvent
 *  ic2.api.energy.tile.IEnergyEmitter
 *  ic2.api.energy.tile.IEnergySink
 *  ic2.api.energy.tile.IEnergyTile
 *  ic2.api.network.IGrowingBuffer
 *  ic2.api.network.INetworkCustomEncoder
 *  ic2.api.recipe.IMachineRecipeManager
 *  ic2.api.recipe.MachineRecipeResult
 *  ic2.core.ContainerBase
 *  ic2.core.IHasGui
 *  ic2.core.block.ITeBlock
 *  ic2.core.block.TileEntityInventory
 *  ic2.core.block.invslot.InvSlotOutput
 *  ic2.core.block.invslot.InvSlotProcessable
 *  ic2.core.gui.dynamic.DynamicContainer
 *  ic2.core.gui.dynamic.GuiParser
 *  ic2.core.gui.dynamic.GuiParser$GuiNode
 *  ic2.core.gui.dynamic.IGuiValueProvider
 *  ic2.core.init.Localization
 *  ic2.core.network.DataEncoder
 *  ic2.core.network.DataEncoder$EncodedType
 *  ic2.core.network.GuiSynced
 *  ic2.core.util.StackUtil
 *  ic2.core.util.Tuple$T2
 *  ic2.core.util.Util
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.util.ITooltipFlag
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.chocohead.advsolar.tiles;

import com.chocohead.advsolar.IMolecularTransformerRecipeManager;
import com.chocohead.advsolar.gui.TransparentDynamicGUI;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.network.IGrowingBuffer;
import ic2.api.network.INetworkCustomEncoder;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.MachineRecipeResult;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.DataEncoder;
import ic2.core.network.GuiSynced;
import ic2.core.util.StackUtil;
import ic2.core.util.Tuple;
import ic2.core.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMolecularAssembler
extends TileEntityInventory
implements IEnergySink,
IHasGui,
IGuiValueProvider {
    protected static final List<AxisAlignedBB> AABBs = Arrays.asList(new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75), new AxisAlignedBB(0.05, 0.0, 0.2, 0.6, 1.0, 0.8));
    protected static final byte MAX_TIME_WAIT = 40;
    public final InvSlotProcessable<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> inputSlot = new InvSlotProcessable<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack>((TileEntityInventory)this, "input", 1, (IMachineRecipeManager)IMolecularTransformerRecipeManager.RECIPES){

        protected ItemStack getInput(ItemStack stack) {
            return stack;
        }

        protected void setInput(ItemStack input) {
            this.put(input);
        }
    };
    public final InvSlotOutput outputSlot = new InvSlotOutput((TileEntityInventory)this, "output", 1);
    @GuiSynced
    protected Tuple.T2<ItemStack, MolecularOutput> currentRecipe;
    private boolean addedToEnet;
    protected double energyIn;
    protected double energyGiven;
    @GuiSynced
    protected double lastEnergyGiven;
    @GuiSynced
    protected double energyUsed;
    protected byte wait;

    public TileEntityMolecularAssembler() {
        this.comparator.setUpdate(() -> this.currentRecipe == null ? 0 : (int)Util.lerp((double)0.0, (double)15.0, (double)(this.energyUsed / (double)((MolecularOutput)this.currentRecipe.b).totalEU)));
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.field_145850_b.field_72995_K) {
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent((IEnergyTile)this));
            this.addedToEnet = true;
        }
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (this.addedToEnet) {
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileUnloadEvent((IEnergyTile)this));
            this.addedToEnet = false;
        }
    }

    public void func_145839_a(NBTTagCompound nbt) {
        MachineRecipeResult output;
        ItemStack input;
        super.func_145839_a(nbt);
        this.energyUsed = nbt.func_74769_h("energyUsed");
        if (nbt.func_74764_b("recipe") && (input = new ItemStack(nbt.func_74775_l("recipe"))) != null && (output = IMolecularTransformerRecipeManager.RECIPES.apply(input, false)) != null) {
            this.currentRecipe = new Tuple.T2((Object)input, (Object)new MolecularOutput((MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack>)output));
        }
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
        super.func_189515_b(nbt);
        nbt.func_74780_a("energyUsed", this.energyUsed);
        if (this.currentRecipe != null) {
            nbt.func_74782_a("recipe", (NBTBase)((ItemStack)this.currentRecipe.a).func_77955_b(new NBTTagCompound()));
        }
        return nbt;
    }

    public List<String> getNetworkedFields() {
        List out = super.getNetworkedFields();
        out.add("energyUsed");
        out.add("energyIn");
        return out;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean nextActive = this.getActive();
        boolean updateInv = false;
        nextActive = this.currentRecipe == null ? (!this.inputSlot.isEmpty() ? (updateInv = this.canWork()) : false) : true;
        this.lastEnergyGiven = this.energyGiven;
        this.energyGiven = 0.0;
        if (nextActive) {
            if (this.energyIn <= 0.0) {
                this.energyIn = 0.0;
                byte by = this.wait;
                this.wait = (byte)(by + 1);
                if (by >= 40) {
                    nextActive = false;
                }
            } else {
                this.wait = 0;
                double energyLeft = this.getDemandedEnergy();
                if (energyLeft > this.energyIn) {
                    this.energyUsed += this.energyIn;
                    this.energyIn = 0.0;
                } else {
                    this.energyIn -= energyLeft;
                    this.outputSlot.add(((MolecularOutput)this.currentRecipe.b).output);
                    this.currentRecipe = null;
                    this.energyUsed = 0.0;
                    updateInv = true;
                }
            }
        }
        if (this.getActive() != nextActive) {
            this.setActive(nextActive);
            this.func_145831_w().func_180500_c(EnumSkyBlock.BLOCK, this.field_174879_c);
        }
        if (updateInv) {
            this.func_70296_d();
        }
    }

    protected boolean canWork() {
        MachineRecipeResult result = this.inputSlot.process();
        if (result != null && this.outputSlot.canAdd(StackUtil.copy((ItemStack)((ItemStack)result.getOutput())))) {
            this.currentRecipe = new Tuple.T2((Object)this.inputSlot.get().func_77946_l(), (Object)new MolecularOutput((MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack>)result));
            this.inputSlot.consume(result);
            return true;
        }
        return false;
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return AABBs;
    }

    @SideOnly(value=Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean canRenderBreaking() {
        return true;
    }

    protected int getLightValue() {
        return this.getActive() ? 12 : 0;
    }

    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("active")) {
            this.func_145831_w().func_180500_c(EnumSkyBlock.BLOCK, this.field_174879_c);
        }
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return true;
    }

    public int getSinkTier() {
        return 14;
    }

    public double getDemandedEnergy() {
        return this.currentRecipe == null ? (this.energyGiven = 0.0) : (double)((MolecularOutput)this.currentRecipe.b).totalEU - this.energyUsed;
    }

    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        this.energyGiven += amount;
        double wanted = this.getDemandedEnergy();
        if (wanted == 0.0) {
            return amount;
        }
        if (wanted >= amount) {
            this.energyIn += amount;
            return 0.0;
        }
        double in = amount - wanted;
        this.energyIn += in;
        return amount - in;
    }

    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate((String)"ic2.item.tooltip.PowerTier", (Object[])new Object[]{this.getSinkTier()}));
    }

    public ContainerBase<TileEntityMolecularAssembler> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create((IInventory)this, (EntityPlayer)player, (GuiParser.GuiNode)GuiParser.parse((ITeBlock)this.teBlock));
    }

    @SideOnly(value=Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return TransparentDynamicGUI.create(this, player, GuiParser.parse((ITeBlock)this.teBlock));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public double getGuiValue(String name) {
        if ("progress".equals(name)) {
            return this.currentRecipe == null ? 0.0 : this.energyUsed / (double)((MolecularOutput)this.currentRecipe.b).totalEU;
        }
        throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
    }

    @SideOnly(value=Side.CLIENT)
    public String getInput() {
        return this.currentRecipe == null ? "" : ((ItemStack)this.currentRecipe.a).func_82833_r();
    }

    @SideOnly(value=Side.CLIENT)
    public String getOutput() {
        return this.currentRecipe == null ? "" : ((MolecularOutput)this.currentRecipe.b).output.func_82833_r();
    }

    @SideOnly(value=Side.CLIENT)
    public String getEnergyNeeded() {
        return this.currentRecipe == null ? "" : String.format("%,d %s", ((MolecularOutput)this.currentRecipe.b).totalEU, Localization.translate((String)"ic2.generic.text.EU"));
    }

    @SideOnly(value=Side.CLIENT)
    public String getEU() {
        return this.currentRecipe == null ? "" : String.format("%,.0f %s", this.lastEnergyGiven, Localization.translate((String)"ic2.generic.text.EUt"));
    }

    @SideOnly(value=Side.CLIENT)
    public String getPercent() {
        return this.currentRecipe == null ? "" : String.format("%,.0f%%", this.energyUsed * 100.0 / (double)((MolecularOutput)this.currentRecipe.b).totalEU);
    }

    public static final class MolecularOutput
    implements INetworkCustomEncoder {
        public final ItemStack output;
        public final int totalEU;

        public MolecularOutput(MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> result) {
            this((ItemStack)result.getOutput(), ((IMolecularTransformerRecipeManager.Input)result.getRecipe().getInput()).totalEU);
        }

        private MolecularOutput(ItemStack output, int totalEU) {
            this.output = output;
            this.totalEU = totalEU;
        }

        public static void registerNetwork() {
            DataEncoder.addNetworkEncoder(MolecularOutput.class, (INetworkCustomEncoder)new MolecularOutput(null, 0));
        }

        public boolean isThreadSafe() {
            return true;
        }

        public void encode(IGrowingBuffer buffer, Object instance) throws IOException {
            MolecularOutput mo = (MolecularOutput)instance;
            DataEncoder.encode((IGrowingBuffer)buffer, (Object)mo.output, (boolean)false);
            DataEncoder.encode((IGrowingBuffer)buffer, (Object)mo.totalEU, (boolean)false);
        }

        public Object decode(IGrowingBuffer buffer) throws IOException {
            return new MolecularOutput((ItemStack)DataEncoder.decode((IGrowingBuffer)buffer, (DataEncoder.EncodedType)DataEncoder.EncodedType.ItemStack), (Integer)DataEncoder.decode((IGrowingBuffer)buffer, (DataEncoder.EncodedType)DataEncoder.EncodedType.Integer));
        }
    }
}

