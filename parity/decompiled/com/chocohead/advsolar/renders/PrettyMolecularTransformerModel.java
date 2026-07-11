/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 */
package com.chocohead.advsolar.renders;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class PrettyMolecularTransformerModel
extends ModelBase {
    ModelRenderer coreBottom;
    ModelRenderer coreWorkZone;
    ModelRenderer coreTopElectr;
    ModelRenderer coreTopPlate;
    ModelRenderer firstElTop;
    ModelRenderer firstElBottom;
    ModelRenderer secondElTop;
    ModelRenderer secondElBottom;
    ModelRenderer thirdElTop;
    ModelRenderer thirdElBottom;

    public PrettyMolecularTransformerModel() {
        this.field_78090_t = 128;
        this.field_78089_u = 64;
        this.coreBottom = new ModelRenderer((ModelBase)this, 0, 0);
        this.coreBottom.func_78789_a(-5.0f, 4.0f, -5.0f, 10, 3, 10);
        this.coreBottom.func_78793_a(0.0f, 16.0f, 0.0f);
        this.coreBottom.func_78787_b(128, 64);
        this.coreBottom.field_78809_i = true;
        this.setRotation(this.coreBottom, 0.0f, 0.0f, 0.0f);
        this.coreWorkZone = new ModelRenderer((ModelBase)this, 0, 44);
        this.coreWorkZone.func_78789_a(-3.0f, -4.0f, -3.0f, 6, 9, 6);
        this.coreWorkZone.func_78793_a(0.0f, 16.0f, 0.0f);
        this.coreWorkZone.func_78787_b(128, 64);
        this.coreWorkZone.field_78809_i = true;
        this.setRotation(this.coreWorkZone, 0.0f, 0.0f, 0.0f);
        this.coreTopElectr = new ModelRenderer((ModelBase)this, 25, 44);
        this.coreTopElectr.func_78789_a(-2.0f, -8.0f, -1.466667f, 3, 2, 3);
        this.coreTopElectr.func_78793_a(0.0f, 16.0f, 0.0f);
        this.coreTopElectr.func_78787_b(128, 64);
        this.coreTopElectr.field_78809_i = true;
        this.setRotation(this.coreTopElectr, 0.0f, 0.0f, 0.0f);
        this.coreTopPlate = new ModelRenderer((ModelBase)this, 0, 30);
        this.coreTopPlate.func_78789_a(-5.0f, -7.0f, -4.5f, 9, 3, 9);
        this.coreTopPlate.func_78793_a(0.0f, 16.0f, 0.0f);
        this.coreTopPlate.func_78787_b(128, 64);
        this.coreTopPlate.field_78809_i = true;
        this.setRotation(this.coreTopPlate, 0.0f, 0.0f, 0.0f);
        this.firstElTop = new ModelRenderer((ModelBase)this, 20, 16);
        this.firstElTop.func_78789_a(3.0f, -8.0f, -5.0f, 4, 3, 10);
        this.firstElTop.func_78793_a(0.0f, 16.0f, 0.0f);
        this.firstElTop.func_78787_b(128, 64);
        this.firstElTop.field_78809_i = true;
        this.setRotation(this.firstElTop, 0.0f, 0.0f, 0.0f);
        this.firstElBottom = new ModelRenderer((ModelBase)this, 49, 16);
        this.firstElBottom.func_78789_a(4.0f, 3.0f, -3.0f, 3, 5, 6);
        this.firstElBottom.func_78793_a(0.0f, 16.0f, 0.0f);
        this.firstElBottom.func_78787_b(128, 64);
        this.firstElBottom.field_78809_i = true;
        this.setRotation(this.firstElBottom, 0.0f, 0.0f, 0.0f);
        this.secondElTop = new ModelRenderer((ModelBase)this, 20, 16);
        this.secondElTop.func_78789_a(3.0f, -8.0f, -5.0f, 4, 3, 10);
        this.secondElTop.func_78793_a(0.0f, 16.0f, 0.0f);
        this.secondElTop.func_78787_b(128, 64);
        this.secondElTop.field_78809_i = true;
        this.setRotation(this.secondElTop, 0.0f, -2.094395f, 0.0f);
        this.secondElBottom = new ModelRenderer((ModelBase)this, 49, 16);
        this.secondElBottom.func_78789_a(4.0f, 3.0f, -3.0f, 3, 5, 6);
        this.secondElBottom.func_78793_a(0.0f, 16.0f, 0.0f);
        this.secondElBottom.func_78787_b(128, 64);
        this.secondElBottom.field_78809_i = true;
        this.setRotation(this.secondElBottom, 0.0f, -2.094395f, 0.0f);
        this.thirdElTop = new ModelRenderer((ModelBase)this, 20, 16);
        this.thirdElTop.func_78789_a(3.0f, -8.0f, -5.0f, 4, 3, 10);
        this.thirdElTop.func_78793_a(0.0f, 16.0f, 0.0f);
        this.thirdElTop.func_78787_b(128, 64);
        this.thirdElTop.field_78809_i = true;
        this.setRotation(this.thirdElTop, 0.0f, 2.094395f, 0.0f);
        this.thirdElBottom = new ModelRenderer((ModelBase)this, 49, 16);
        this.thirdElBottom.func_78789_a(4.0f, 3.0f, -3.0f, 3, 5, 6);
        this.thirdElBottom.func_78793_a(0.0f, 16.0f, 0.0f);
        this.thirdElBottom.func_78787_b(128, 64);
        this.thirdElBottom.field_78809_i = true;
        this.setRotation(this.thirdElBottom, 0.0f, 2.094395f, 0.0f);
    }

    public void func_78088_a(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.coreBottom.func_78785_a(scale);
        this.firstElTop.func_78785_a(scale);
        this.firstElBottom.func_78785_a(scale);
        this.secondElTop.func_78785_a(scale);
        this.secondElBottom.func_78785_a(scale);
        this.thirdElTop.func_78785_a(scale);
        this.thirdElBottom.func_78785_a(scale);
        this.coreTopElectr.func_78785_a(scale);
        this.coreTopPlate.func_78785_a(scale);
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.coreWorkZone.func_78785_a(scale);
        GlStateManager.func_179121_F();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }
}

