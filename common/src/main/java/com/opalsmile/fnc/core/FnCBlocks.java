package com.opalsmile.fnc.core;

import com.opalsmile.fnc.Constants;
import com.opalsmile.fnc.FnC;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import opalsmile.fnc.reg.BlockRegistryObject;
import opalsmile.fnc.reg.RegistrationProvider;

import java.util.function.Supplier;

public class FnCBlocks {
    public static final RegistrationProvider<Block> REGISTER = RegistrationProvider.get(Registries.BLOCK, Constants.MOD_ID);

//    public final BlockRegistryObject<Block> STONE_DAWN_ORE = createBlock("stone_dawn_ore", createOre(FeaturesCreaturesOre.Duration.DAWN));
//    public final BlockRegistryObject<Block> STONE_SUNSET_ORE = createBlock("stone_sunset_ore", createOre(FeaturesCreaturesOre.Duration.SUNSET));
//    public final BlockRegistryObject<Block> STONE_MIDNIGHT_ORE = createBlock("stone_midnight_ore", createOre(FeaturesCreaturesOre.Duration.MIDNIGHT));
//
//    public final BlockRegistryObject<Block> DAWN_ORE = createBlock("dawn_ore", createOre(FeaturesCreaturesOre.Duration.DAWN));
//    public final BlockRegistryObject<Block> SUNSET_ORE = createBlock("sunset_ore", createOre(FeaturesCreaturesOre.Duration.SUNSET));
//    public final BlockRegistryObject<Block> MIDNIGHT_ORE = createBlock("midnight_ore", createOre(FeaturesCreaturesOre.Duration.MIDNIGHT));
//
//    public final BlockRegistryObject<Block> DEEPSLATE_DAWN_ORE = createBlock("deepslate_dawn_ore", createOre(FeaturesCreaturesOre.Duration.DAWN));
//    public final BlockRegistryObject<Block> DEEPSLATE_SUNSET_ORE = createBlock("deepslate_sunset_ore", createOre(FeaturesCreaturesOre.Duration.SUNSET));
//    public final BlockRegistryObject<Block> DEEPSLATE_MIDNIGHT_ORE = createBlock("deepslate_midnight_ore", createOre(FeaturesCreaturesOre.Duration.MIDNIGHT));

    public static final BlockRegistryObject<Block> DAWN_BLOCK = createBlock("dawn_block", FnCBlocks::createBlock);
    public static final BlockRegistryObject<Block> SUNSET_BLOCK = createBlock("sunset_block", FnCBlocks::createBlock);
    public static final BlockRegistryObject<Block> MIDNIGHT_BLOCK = createBlock("midnight_block", FnCBlocks::createBlock);

//    private Supplier<Block> createOre(FeaturesCreaturesOre.Duration duration) {
//        return () -> new FeaturesCreaturesOre(duration, BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE));
//    }

    private static Block createBlock() {
        return new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK));
    }

    private static <B extends Block> BlockRegistryObject<B> createBlock(String id, Supplier<? extends B> block) {
        final var ro = REGISTER.<B>register(id, block);
        return BlockRegistryObject.wrap(ro);
    }

    public static void init() {
    }
}
