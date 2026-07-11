/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.CaseFormat
 *  ic2.api.item.ElectricItem
 *  ic2.api.item.HudMode
 *  ic2.api.item.IElectricItem
 *  ic2.api.item.IItemHudProvider
 *  ic2.api.item.IMetalArmor
 *  ic2.core.IC2
 *  ic2.core.init.BlocksItems
 *  ic2.core.init.Localization
 *  ic2.core.item.ElectricItemManager
 *  ic2.core.ref.IItemModelProvider
 *  ic2.core.ref.ItemName
 *  ic2.core.util.StackUtil
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.EnumRarity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.ISpecialArmor
 *  net.minecraftforge.common.ISpecialArmor$ArmorProperties
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.chocohead.advsolar.items;

import com.chocohead.advsolar.tiles.TileEntityAdvancedSolar;
import com.chocohead.advsolar.tiles.TileEntityHybridSolar;
import com.chocohead.advsolar.tiles.TileEntitySolarPanel;
import com.chocohead.advsolar.tiles.TileEntityUltimateHybridSolar;
import com.google.common.base.CaseFormat;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudProvider;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ElectricItemManager;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import java.util.Locale;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmourSolarHelmet
extends ItemArmor
implements IItemModelProvider,
IElectricItem,
IMetalArmor,
ISpecialArmor,
IItemHudProvider {
    protected static final int DEFAULT_COLOUR = -1;
    protected final SolarHelmetTypes type;
    public static boolean chargeWholeInventory = false;
    protected TileEntitySolarPanel.GenerationState state;
    protected int ticker;

    public ItemArmourSolarHelmet(SolarHelmetTypes type) {
        super(ItemArmor.ArmorMaterial.DIAMOND, -1, EntityEquipmentSlot.HEAD);
        ((ItemArmourSolarHelmet)BlocksItems.registerItem((Item)this, (ResourceLocation)new ResourceLocation("advanced_solar_panels", type.getName()))).func_77655_b(type.getLocalisedName());
        this.func_77637_a((CreativeTabs)IC2.tabIC2);
        this.func_77656_e(27);
        this.type = type;
    }

    public String func_77658_a() {
        return "advanced_solar_panels." + super.func_77658_a().substring(5);
    }

    public String func_77667_c(ItemStack stack) {
        return this.func_77658_a();
    }

    public String func_77653_i(ItemStack stack) {
        return Localization.translate((String)this.func_77667_c(stack));
    }

    public int getMetadata(ItemStack stack) {
        return 0;
    }

    @SideOnly(value=Side.CLIENT)
    public void registerModels(ItemName name) {
        ModelLoader.setCustomModelResourceLocation((Item)this, (int)0, (ModelResourceLocation)new ModelResourceLocation("advanced_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.type.getName()), null));
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "advanced_solar_panels:textures/armour/" + this.type.getName() + (type != null ? "Overlay" : "") + ".png";
    }

    public boolean canBeDyed() {
        return this.type != SolarHelmetTypes.ADVANCED;
    }

    public void func_82813_b(ItemStack stack, int colour) {
        this.getDisplayNbt(stack, true).func_74768_a("colour", colour);
    }

    public boolean func_82816_b_(ItemStack stack) {
        return this.func_82814_b(stack) != -1;
    }

    public int func_82814_b(ItemStack stack) {
        NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        if (nbt == null || !nbt.func_150297_b("colour", 3)) {
            return -1;
        }
        return nbt.func_74762_e("colour");
    }

    public void func_82815_c(ItemStack stack) {
        NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        if (nbt == null || !nbt.func_150297_b("colour", 3)) {
            return;
        }
        nbt.func_82580_o("colour");
        if (nbt.func_82582_d()) {
            stack.func_77978_p().func_82580_o("display");
        }
    }

    protected NBTTagCompound getDisplayNbt(ItemStack stack, boolean create) {
        NBTTagCompound out;
        NBTTagCompound nbt = stack.func_77978_p();
        if (nbt == null) {
            if (!create) {
                return null;
            }
            nbt = new NBTTagCompound();
            stack.func_77982_d(nbt);
        }
        if (!nbt.func_150297_b("display", 10)) {
            if (!create) {
                return null;
            }
            out = new NBTTagCompound();
            nbt.func_74782_a("display", (NBTBase)out);
        } else {
            out = nbt.func_74775_l("display");
        }
        return out;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        int output;
        if (this.HUDstuff(world.field_72995_K, player, stack)) {
            return;
        }
        if (this.ticker++ % this.tickRate() == 0) {
            this.checkTheSky(world, player.func_180425_c());
        }
        if (this.type != SolarHelmetTypes.ADVANCED) {
            int airLevel = player.func_70086_ai();
            if (ElectricItem.manager.canUse(stack, 1000.0) && airLevel < 100) {
                player.func_70050_g(airLevel + 200);
                ElectricItem.manager.use(stack, 1000.0, (EntityLivingBase)player);
            }
        }
        switch (this.state) {
            case DAY: {
                output = this.type.dayEU;
                break;
            }
            case NIGHT: {
                output = this.type.nightEU;
                break;
            }
            default: {
                return;
            }
        }
        for (ItemStack playerStack : player.field_71071_by.field_70460_b.subList(0, player.field_71071_by.field_70460_b.size() - 1)) {
            if (StackUtil.isEmpty((ItemStack)playerStack) || !(playerStack.func_77973_b() instanceof IElectricItem) || (output = (int)((double)output - ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false))) > 0) continue;
            return;
        }
        if (chargeWholeInventory) {
            for (ItemStack playerStack : player.field_71071_by.field_184439_c) {
                if (StackUtil.isEmpty((ItemStack)playerStack) || !(playerStack.func_77973_b() instanceof IElectricItem) || (output = (int)((double)output - ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false))) > 0) continue;
                return;
            }
            for (ItemStack playerStack : player.field_71071_by.field_70462_a) {
                if (StackUtil.isEmpty((ItemStack)playerStack) || !(playerStack.func_77973_b() instanceof IElectricItem) || (output = (int)((double)output - ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false))) > 0) continue;
                return;
            }
        }
        ElectricItem.manager.charge(stack, (double)output, Integer.MAX_VALUE, true, false);
    }

    protected boolean HUDstuff(boolean isRemote, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData((ItemStack)stack);
        byte toggleTimer = nbt.func_74771_c("toggleTimer");
        if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
            byte hubmode = nbt.func_74771_c("hudMode");
            toggleTimer = 10;
            hubmode = hubmode == HudMode.getMaxMode() ? (byte)0 : (byte)(hubmode + 1);
            if (!isRemote) {
                nbt.func_74774_a("hudMode", hubmode);
                IC2.platform.messagePlayer(player, Localization.translate((String)HudMode.getFromID((int)hubmode).getTranslationKey()), new Object[0]);
            }
        }
        if (!isRemote && toggleTimer > 0) {
            toggleTimer = (byte)(toggleTimer - 1);
            nbt.func_74774_a("toggleTimer", toggleTimer);
        }
        return isRemote;
    }

    protected int tickRate() {
        return 128;
    }

    public void checkTheSky(World world, BlockPos pos) {
        this.state = !world.field_73011_w.func_177495_o() && world.func_175710_j(pos) ? (world.func_72935_r() && (!world.func_180494_b(pos).func_76738_d() && !(world.func_180494_b(pos).func_76727_i() > 0.0f) || !world.func_72896_J() && !world.func_72911_I()) ? TileEntitySolarPanel.GenerationState.DAY : TileEntitySolarPanel.GenerationState.NIGHT) : TileEntitySolarPanel.GenerationState.NONE;
    }

    public void func_150895_a(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.func_194125_a(tab)) {
            ElectricItemManager.addChargeVariants((Item)this, items);
        }
    }

    public EnumRarity func_77613_e(ItemStack stack) {
        return this.type.rarity;
    }

    public boolean isMetalArmor(ItemStack stack, EntityPlayer player) {
        return true;
    }

    public int func_77619_b() {
        return 0;
    }

    public boolean func_82789_a(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armour, DamageSource source, double damage, int slot) {
        if (source.func_76363_c()) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        }
        return new ISpecialArmor.ArmorProperties(0, 0.15 * this.type.damageAbsorptionRatio, (int)(25.0 * ElectricItem.manager.getCharge(armour) / (double)this.type.energyPerDamage));
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armour, int slot) {
        if (ElectricItem.manager.getCharge(armour) >= (double)this.type.energyPerDamage) {
            return (int)Math.round(3.0 * this.type.damageAbsorptionRatio);
        }
        return 0;
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, (double)(damage * this.type.energyPerDamage), Integer.MAX_VALUE, true, false, false);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public int getTier(ItemStack stack) {
        return this.type.tier;
    }

    public double getMaxCharge(ItemStack stack) {
        return this.type.maxCharge;
    }

    public double getTransferLimit(ItemStack stack) {
        return this.type.transferLimit;
    }

    public boolean doesProvideHUD(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0;
    }

    public HudMode getHudMode(ItemStack stack) {
        return HudMode.getFromID((int)StackUtil.getOrCreateNbtData((ItemStack)stack).func_74771_c("hudMode"));
    }

    public static final class SolarHelmetTypes
    extends Enum<SolarHelmetTypes> {
        public static final /* enum */ SolarHelmetTypes ADVANCED = new SolarHelmetTypes(EnumRarity.UNCOMMON, TileEntityAdvancedSolar.settings.dayPower, TileEntityAdvancedSolar.settings.nightPower, 3, 1000000.0, 3000.0, 800, 0.9);
        public static final /* enum */ SolarHelmetTypes HYBRID = new SolarHelmetTypes(EnumRarity.RARE, TileEntityHybridSolar.settings.dayPower, TileEntityHybridSolar.settings.nightPower, 4, 1.0E7, 10000.0, 2000, 1.0);
        public static final /* enum */ SolarHelmetTypes ULTIMATE = new SolarHelmetTypes(EnumRarity.EPIC, TileEntityUltimateHybridSolar.settings.dayPower, TileEntityUltimateHybridSolar.settings.nightPower, 4, 1.0E7, 10000.0, 2000, 1.0);
        public final double maxCharge;
        public final double transferLimit;
        public final double damageAbsorptionRatio;
        public final int dayEU;
        public final int nightEU;
        public final int tier;
        public final int energyPerDamage;
        public final EnumRarity rarity;
        private final String name = this.name().toLowerCase(Locale.ENGLISH);
        private static final /* synthetic */ SolarHelmetTypes[] $VALUES;

        public static SolarHelmetTypes[] values() {
            return (SolarHelmetTypes[])$VALUES.clone();
        }

        public static SolarHelmetTypes valueOf(String name) {
            return Enum.valueOf(SolarHelmetTypes.class, name);
        }

        private SolarHelmetTypes(EnumRarity rarity, int dayEU, int nightEU, int tier, double maxCharge, double transferLimit, int energyPerDamage, double damageAbsorptionRatio) {
            this.rarity = rarity;
            this.dayEU = dayEU;
            this.nightEU = nightEU;
            this.tier = tier;
            this.maxCharge = maxCharge;
            this.transferLimit = transferLimit;
            this.energyPerDamage = energyPerDamage;
            this.damageAbsorptionRatio = damageAbsorptionRatio;
            assert (damageAbsorptionRatio > 0.0);
        }

        public String getName() {
            return this.name + "SolarHelmet";
        }

        protected String getLocalisedName() {
            return "solar_helmets." + this.name;
        }

        static {
            $VALUES = new SolarHelmetTypes[]{ADVANCED, HYBRID, ULTIMATE};
        }
    }
}

