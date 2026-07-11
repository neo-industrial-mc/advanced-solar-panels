package com.chocohead.advsolar.gametest;

import com.chocohead.advsolar.blockentity.MolecularTransformerBlockEntity;
import com.chocohead.advsolar.blockentity.QuantumGeneratorBlockEntity;
import com.chocohead.advsolar.registry.ASPBlocks;
import ic2.core.block.comp.Energy;
import ic2.core.block.wiring.tileentity.TileEntityElectricMFE;
import ic2.core.ref.Ic2Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("advanced_solar_panels")
@PrefixGameTestTemplate(false)
public final class MachineGameTests {
    private static final String EMPTY = "gametest/empty3x3x3";
    private static final BlockPos MACHINE_POS = new BlockPos(1, 1, 1);

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public static void molecularTransformerProcessesRecipe(GameTestHelper helper) {
        MolecularTransformerBlockEntity transformer = placeTransformer(helper);
        transformer.input.put(new ItemStack(Items.SAND));

        helper.runAtTickTime(2, () -> {
            helper.assertTrue(transformer.input.isEmpty(), "recipe input should be consumed when processing starts");
            helper.assertTrue(transformer.getActive(), "transformer should be active while processing");
            transformer.getComponent(Energy.class).addEnergy(49999.0);
        });
        helper.runAtTickTime(6, () -> {
            helper.assertTrue(transformer.output.get().isEmpty(), "one EU short of the recipe cost must not produce output");
            helper.assertTrue(transformer.getActive(), "transformer should stay active while short of energy");
            transformer.getComponent(Energy.class).addEnergy(1.0);
        });
        helper.runAtTickTime(10, () -> {
            helper.assertValueEqual(transformer.output.get().getItem(), Items.GRAVEL, "recipe output");
            helper.assertValueEqual(transformer.output.get().getCount(), 1, "recipe output count");
            helper.assertTrue(!transformer.getActive(), "transformer should stop after completing the recipe");
            helper.succeed();
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 100)
    public static void molecularTransformerStopsWithoutEnergy(GameTestHelper helper) {
        MolecularTransformerBlockEntity transformer = placeTransformer(helper);
        transformer.input.put(new ItemStack(Items.SAND));

        helper.runAtTickTime(2, () -> helper.assertTrue(transformer.getActive(), "transformer should start the recipe"));
        helper.runAtTickTime(45, () -> {
            helper.assertTrue(!transformer.getActive(), "transformer should become inactive after forty unpowered ticks");
            transformer.getComponent(Energy.class).addEnergy(50000.0);
        });
        helper.runAtTickTime(47, () -> {
            helper.assertValueEqual(transformer.output.get().getItem(), Items.GRAVEL, "resumed recipe output");
            helper.assertValueEqual(transformer.output.get().getCount(), 1, "resumed recipe output count");
            helper.succeed();
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 80)
    public static void quantumGeneratorEmitsConfiguredPower(GameTestHelper helper) {
        QuantumGeneratorBlockEntity generator = placeGeneratorAndMfe(helper);
        TileEntityElectricMFE mfe = getBlockEntity(helper, MACHINE_POS.east(), TileEntityElectricMFE.class);

        helper.runAtTickTime(10, () -> {
            helper.assertTrue(generator.getActive(), "unpowered quantum generator should be active");
            helper.assertTrue(mfe.energy.getEnergy() >= 512.0, "MFE should receive at least one configured 512 EU packet");
            double start = mfe.energy.getEnergy();
            helper.runAtTickTime(20, () -> {
                AdvSolarGameTestAssertions.assertNear(helper, mfe.energy.getEnergy() - start, 5120.0, 512.0,
                        "ten ticks of configured quantum generator output");
                helper.succeed();
            });
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public static void quantumGeneratorRespectsRedstone(GameTestHelper helper) {
        QuantumGeneratorBlockEntity generator = placeGeneratorAndMfe(helper);
        TileEntityElectricMFE mfe = getBlockEntity(helper, MACHINE_POS.east(), TileEntityElectricMFE.class);
        helper.setBlock(MACHINE_POS.west(), Blocks.REDSTONE_BLOCK);

        helper.runAtTickTime(20, () -> {
            helper.assertTrue(!generator.getActive(), "powered quantum generator should be inactive");
            AdvSolarGameTestAssertions.assertNear(helper, mfe.energy.getEnergy(), 0.0, 0.0, "MFE energy while generator is redstone-disabled");
            helper.succeed();
        });
    }

    private static MolecularTransformerBlockEntity placeTransformer(GameTestHelper helper) {
        helper.setBlock(MACHINE_POS, ASPBlocks.MOLECULAR_TRANSFORMER.get());
        return getBlockEntity(helper, MACHINE_POS, MolecularTransformerBlockEntity.class);
    }

    private static QuantumGeneratorBlockEntity placeGeneratorAndMfe(GameTestHelper helper) {
        helper.setBlock(MACHINE_POS, ASPBlocks.QUANTUM_GENERATOR.get());
        helper.setBlock(MACHINE_POS.east(), Ic2Blocks.MFE);
        return getBlockEntity(helper, MACHINE_POS, QuantumGeneratorBlockEntity.class);
    }

    private static <T extends BlockEntity> T getBlockEntity(GameTestHelper helper, BlockPos pos, Class<T> type) {
        BlockEntity blockEntity = helper.getBlockEntity(pos);
        helper.assertTrue(type.isInstance(blockEntity), "expected " + type.getSimpleName() + " at " + pos + ", got " + blockEntity);
        return type.cast(blockEntity);
    }
}
