package com.chocohead.advsolar.blockentity;

import com.chocohead.advsolar.registry.ASPBlockEntities;
import com.chocohead.advsolar.registry.ASPRecipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.tileentity.TileEntityBase;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.GuiSynced;
import ic2.core.recipe.v2.RecipeHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public final class MolecularTransformerBlockEntity extends TileEntityBase implements IHasGui, IGuiValueProvider {
    public final InvSlot input = new InvSlot(this, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
    public final InvSlotOutput output = new InvSlotOutput(this, "output", 1);
    private final Energy energy = addComponent(Energy.asBasicSink(this, 0, 14));
    @GuiSynced public double energyUsed;
    @GuiSynced public double lastEnergyGiven;
    @GuiSynced private int totalEnergy;
    @GuiSynced private ItemStack recipeInput = ItemStack.EMPTY;
    @GuiSynced private ItemStack recipeOutput = ItemStack.EMPTY;
    private MachineRecipe<IRecipeInput, Collection<ItemStack>> currentRecipe;
    private int idleTicks;

    public MolecularTransformerBlockEntity(BlockPos pos, BlockState state) { super(ASPBlockEntities.MOLECULAR_TRANSFORMER.get(), pos, state); comparator.setUpdate(() -> totalEnergy <= 0 ? 0 : Math.min(15, (int)(energyUsed * 15 / totalEnergy))); }

    @Override protected void updateEntityServer() {
        super.updateEntityServer();
        if (currentRecipe == null) tryStart();
        if (currentRecipe == null) { lastEnergyGiven = 0; return; }
        double received = energy.getEnergy();
        lastEnergyGiven = received;
        if (received > 0) { energyUsed += received; energy.useEnergy(received); idleTicks = 0; setActiveState(true, false); }
        else if (++idleTicks >= 40) setActiveState(false, false);
        if (energyUsed >= totalEnergy) finish();
    }

    private void tryStart() {
        ItemStack stack = input.get();
        if (stack.isEmpty() || level.getServer() == null) return;
        for (var vanillaHolder : level.getServer().getRecipeManager().getAllRecipesFor(ASPRecipes.MOLECULAR_TRANSFORMER.get())) {
            RecipeHolder<IRecipeInput, Collection<ItemStack>> holder = vanillaHolder.value();
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = holder.recipe();
            int count = recipe.getMetaData().getInt("count");
            ItemStack result = recipe.getOutput().iterator().next();
            if (recipe.getInput().matches(stack) && stack.getCount() >= count && output.canAdd(result)) {
                currentRecipe = recipe; totalEnergy = recipe.getMetaData().getInt("energy"); energyUsed = 0;
                recipeInput = stack.copyWithCount(count); recipeOutput = result.copy();
                input.put(stack.copyWithCount(stack.getCount() - count)); energy.setCapacity(totalEnergy); setActiveState(true, false); setChanged(); return;
            }
        }
    }
    private void finish() { output.add(recipeOutput.copy()); currentRecipe = null; recipeInput = ItemStack.EMPTY; recipeOutput = ItemStack.EMPTY; totalEnergy = 0; energyUsed = 0; energy.setCapacity(0); idleTicks = 0; setActiveState(false, false); setChanged(); }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) { super.loadAdditional(tag, registries); energyUsed = tag.getDouble("energyUsed"); recipeInput = ItemStack.parseOptional(registries, tag.getCompound("recipe")); }
    @Override public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) { super.saveAdditional(tag, registries); tag.putDouble("energyUsed", energyUsed); if (!recipeInput.isEmpty()) tag.put("recipe", recipeInput.save(registries)); }
    @Override protected void onLoaded() { super.onLoaded(); if (!recipeInput.isEmpty() && level.getServer() != null) { double savedEnergy = energyUsed; ItemStack saved = recipeInput.copy(); input.put(saved.copy()); tryStart(); input.put(ItemStack.EMPTY); energyUsed = Math.min(savedEnergy, totalEnergy); } }
    @Override public double getGuiValue(String name) { return "progress".equals(name) && totalEnergy > 0 ? energyUsed / totalEnergy : 0; }
    public String getInput() { return recipeInput.isEmpty() ? "-" : recipeInput.getHoverName().getString(); }
    public String getOutput() { return recipeOutput.isEmpty() ? "-" : recipeOutput.getHoverName().getString(); }
    public String getEnergyNeeded() { return Integer.toString(totalEnergy); }
    public String getEU() { return Integer.toString((int)lastEnergyGiven); }
    public String getPercent() { return totalEnergy <= 0 ? "0%" : String.format("%.1f%%", energyUsed * 100 / totalEnergy); }
    @Override public ContainerBase<?> createServerScreenHandler(int id, Player player) { return DynamicContainer.create(id, player.getInventory(), this); }
    @Override public ContainerBase<?> createClientScreenHandler(int id, Inventory inv, GrowingBuffer data) { return DynamicContainer.create(id, inv, this); }
}
