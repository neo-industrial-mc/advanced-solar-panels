/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.trove.map.TObjectIntMap
 *  gnu.trove.map.hash.TObjectIntHashMap
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package com.chocohead.advsolar.renders;

import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.renders.PrettyMolecularTransformerModel;
import com.chocohead.advsolar.tiles.TileEntityMolecularAssembler;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class PrettyMolecularTransformerTESR
extends TileEntitySpecialRenderer<TileEntityMolecularAssembler> {
    private static final ResourceLocation transfTextloc = new ResourceLocation("advanced_solar_panels", "textures/models/textureMolecularTransformer.png");
    private static final ResourceLocation plazmaTextloc = new ResourceLocation("advanced_solar_panels", "textures/models/plazma.png");
    private static final ResourceLocation particlesTextloc = new ResourceLocation("advanced_solar_panels", "textures/models/particles.png");
    public static final PrettyMolecularTransformerModel model = new PrettyMolecularTransformerModel();
    private static final TObjectIntMap<List<Serializable>> textureSizeCache = new TObjectIntHashMap();
    private static final IResourceManager resources = Minecraft.func_71410_x().func_110442_L();
    public static boolean drawActiveCore = false;
    public int ticker;

    public static int getTextureSize(String s, int dv) {
        if (textureSizeCache.containsKey(Arrays.asList(s, dv))) {
            return textureSizeCache.get(Arrays.asList(s, dv));
        }
        try {
            InputStream inputstream = resources.func_110536_a(new ResourceLocation("advanced_solar_panels", s)).func_110527_b();
            if (inputstream == null) {
                throw new FileNotFoundException("Image not found: " + s);
            }
            int size = ImageIO.read(inputstream).getWidth() / dv;
            textureSizeCache.put(Arrays.asList(s, dv), size);
            return size;
        }
        catch (Exception e) {
            AdvancedSolarPanels.log.error("Error getting size of texture " + s + " (" + dv + ')', (Throwable)e);
            return 16;
        }
    }

    public void renderCore(TileEntity te, double x, double y, double z) {
        ++this.ticker;
        if (this.ticker > 160) {
            this.ticker = 0;
        }
        int plazmaSize = PrettyMolecularTransformerTESR.getTextureSize(plazmaTextloc.func_110623_a(), 64);
        int particleSize = PrettyMolecularTransformerTESR.getTextureSize(particlesTextloc.func_110623_a(), 32);
        float rotationX = ActiveRenderInfo.func_178808_b();
        float rotationXZ = ActiveRenderInfo.func_178809_c();
        float rotationZ = ActiveRenderInfo.func_178803_d();
        float rotationYZ = ActiveRenderInfo.func_178805_e();
        float rotationXY = ActiveRenderInfo.func_178807_f();
        float scaleCore = 0.35f;
        float posX = (float)x + 0.5f;
        float posY = (float)y + 0.5f;
        float posZ = (float)z + 0.5f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder buffer = tessellator.func_178180_c();
        Color colour = new Color(0xC0FFFF);
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)1);
        this.func_147499_a(plazmaTextloc);
        int phase = this.ticker % 16;
        float quadPlazmaSize = plazmaSize * 4;
        float plasmaEdge = (float)plazmaSize - 0.01f;
        float xBottom = ((float)(phase % 4 * plazmaSize) + 0.0f) / quadPlazmaSize;
        float xTop = ((float)(phase % 4 * plazmaSize) + plasmaEdge) / quadPlazmaSize;
        float yBottom = ((float)(phase / 4 * plazmaSize) + 0.0f) / quadPlazmaSize;
        float yTop = ((float)(phase / 4 * plazmaSize) + plasmaEdge) / quadPlazmaSize;
        buffer.func_181668_a(7, DefaultVertexFormats.field_176600_a);
        GL11.glColor4f((float)((float)colour.getRed() / 255.0f), (float)((float)colour.getGreen() / 255.0f), (float)((float)colour.getBlue() / 255.0f), (float)1.0f);
        buffer.func_181662_b((double)(posX - rotationX * scaleCore - rotationYZ * scaleCore), (double)(posY - rotationXZ * scaleCore), (double)(posZ - rotationZ * scaleCore - rotationXY * scaleCore)).func_187315_a((double)xTop, (double)yTop).func_181675_d();
        buffer.func_181662_b((double)(posX - rotationX * scaleCore + rotationYZ * scaleCore), (double)(posY + rotationXZ * scaleCore), (double)(posZ - rotationZ * scaleCore + rotationXY * scaleCore)).func_187315_a((double)xTop, (double)yBottom).func_181675_d();
        buffer.func_181662_b((double)(posX + rotationX * scaleCore + rotationYZ * scaleCore), (double)(posY + rotationXZ * scaleCore), (double)(posZ + rotationZ * scaleCore + rotationXY * scaleCore)).func_187315_a((double)xBottom, (double)yBottom).func_181675_d();
        buffer.func_181662_b((double)(posX + rotationX * scaleCore - rotationYZ * scaleCore), (double)(posY - rotationXZ * scaleCore), (double)(posZ + rotationZ * scaleCore - rotationXY * scaleCore)).func_187315_a((double)xBottom, (double)yTop).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)3042);
        GL11.glDepthMask((boolean)true);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)1);
        this.func_147499_a(particlesTextloc);
        float octParticleSize = particleSize * 8;
        plasmaEdge = (float)particleSize - 0.01f;
        xBottom = ((float)((phase += 24) % 8 * particleSize) + 0.0f) / octParticleSize;
        xTop = ((float)(phase % 8 * particleSize) + plasmaEdge) / octParticleSize;
        yBottom = ((float)(phase / 8 * particleSize) + 0.0f) / octParticleSize;
        yTop = ((float)(phase / 8 * particleSize) + plasmaEdge) / octParticleSize;
        scaleCore = 0.4f + MathHelper.func_76126_a((float)((float)this.ticker / 10.0f)) * 0.1f;
        buffer.func_181668_a(7, DefaultVertexFormats.field_176600_a);
        GlStateManager.func_179140_f();
        buffer.func_181662_b((double)(posX - rotationX * scaleCore - rotationYZ * scaleCore), (double)(posY - rotationXZ * scaleCore), (double)(posZ - rotationZ * scaleCore - rotationXY * scaleCore)).func_187315_a((double)xTop, (double)yTop).func_181669_b(255, 255, 255, 255).func_181675_d();
        buffer.func_181662_b((double)(posX - rotationX * scaleCore + rotationYZ * scaleCore), (double)(posY + rotationXZ * scaleCore), (double)(posZ - rotationZ * scaleCore + rotationXY * scaleCore)).func_187315_a((double)xTop, (double)yBottom).func_181669_b(255, 255, 255, 255).func_181675_d();
        buffer.func_181662_b((double)(posX + rotationX * scaleCore + rotationYZ * scaleCore), (double)(posY + rotationXZ * scaleCore), (double)(posZ + rotationZ * scaleCore + rotationXY * scaleCore)).func_187315_a((double)xBottom, (double)yBottom).func_181669_b(255, 255, 255, 255).func_181675_d();
        buffer.func_181662_b((double)(posX + rotationX * scaleCore - rotationYZ * scaleCore), (double)(posY - rotationXZ * scaleCore), (double)(posZ + rotationZ * scaleCore - rotationXY * scaleCore)).func_187315_a((double)xBottom, (double)yTop).func_181669_b(255, 255, 255, 255).func_181675_d();
        GlStateManager.func_179145_e();
        tessellator.func_78381_a();
        GL11.glDisable((int)3042);
        GL11.glDepthMask((boolean)true);
        GL11.glPopMatrix();
    }

    protected int getTileLighting(TileEntity tile, int lightValue) {
        if (tile == null || !tile.func_145830_o()) {
            int blockLight = EnumSkyBlock.BLOCK.field_77198_c;
            if (blockLight < lightValue) {
                blockLight = lightValue;
            }
            return EnumSkyBlock.SKY.field_77198_c << 20 | blockLight << 4;
        }
        return tile.func_145831_w().func_175626_b(tile.func_174877_v(), lightValue);
    }

    public void render(TileEntityMolecularAssembler tileTransformer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)((float)x + 0.5f), (float)((float)y + 1.5f), (float)((float)z + 0.5f));
        if (destroyStage >= 0) {
            this.func_147499_a(field_178460_a[destroyStage]);
            GlStateManager.func_179128_n((int)5890);
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a((float)4.0f, (float)4.0f, (float)1.0f);
            GlStateManager.func_179109_b((float)0.0625f, (float)0.0625f, (float)0.0625f);
            GlStateManager.func_179128_n((int)5888);
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        this.func_147499_a(transfTextloc);
        model.func_78088_a(null, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.func_179121_F();
        if (destroyStage >= 0) {
            GlStateManager.func_179128_n((int)5890);
            GlStateManager.func_179121_F();
            GlStateManager.func_179128_n((int)5888);
        }
        GlStateManager.func_179121_F();
        if (tileTransformer != null && drawActiveCore && tileTransformer.getActive()) {
            GL11.glPushMatrix();
            GlStateManager.func_179123_a();
            this.renderCore((TileEntity)tileTransformer, x, y, z);
            GlStateManager.func_179099_b();
            GL11.glPopMatrix();
        }
    }
}

