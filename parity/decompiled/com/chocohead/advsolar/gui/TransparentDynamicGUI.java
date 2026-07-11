/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.core.ContainerBase
 *  ic2.core.gui.GuiElement
 *  ic2.core.gui.Image
 *  ic2.core.gui.dynamic.DynamicContainer
 *  ic2.core.gui.dynamic.DynamicGui
 *  ic2.core.gui.dynamic.GuiParser$GuiNode
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 */
package com.chocohead.advsolar.gui;

import com.chocohead.advsolar.gui.BackgroundlessDynamicGUI;
import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.Image;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class TransparentDynamicGUI<T extends ContainerBase<? extends IInventory>>
extends BackgroundlessDynamicGUI<T> {
    public static <T extends IInventory> DynamicGui<ContainerBase<T>> create(T base, EntityPlayer player, GuiParser.GuiNode guiNode) {
        return new TransparentDynamicGUI<DynamicContainer>(player, DynamicContainer.create(base, (EntityPlayer)player, (GuiParser.GuiNode)guiNode), guiNode);
    }

    protected TransparentDynamicGUI(EntityPlayer player, T container, GuiParser.GuiNode guiNode) {
        super(player, container, guiNode);
    }

    @Override
    protected void drawElement(GuiElement<?> element, int mouseX, int mouseY) {
        boolean image = element instanceof Image;
        if (image) {
            GlStateManager.func_179147_l();
            GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }
        super.drawElement(element, mouseX, mouseY);
        if (image) {
            GlStateManager.func_179084_k();
        }
    }
}

