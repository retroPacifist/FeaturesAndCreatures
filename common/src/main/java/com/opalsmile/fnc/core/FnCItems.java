package com.opalsmile.fnc.core;

import com.opalsmile.fnc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import opalsmile.fnc.reg.RegistrationProvider;
import opalsmile.fnc.reg.RegistryObject;

import java.util.function.Supplier;

public class FnCItems {

    public static final RegistrationProvider<Item> REGISTER = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID);

    public static final RegistryObject<Item> ANTLER = REGISTER.register("antler", createItem());
    public static final RegistryObject<Item> SABERTOOTH_FANG = REGISTER.register("sabertooth_fang", createItem());

    // Armor, Weapons, and Tools
//    public static final RegistryObject<? extends ArmorItem> ANTLER_HEADDRESS = REGISTER.register("antler_headdress", () -> PlatformItemHandler.INSTANCE.getAntlerHeaddressItem(FnCArmorMaterial.ANTLER, EquipmentSlot.HEAD, createProperties()));
//    public static final RegistryObject<? extends ArmorItem> LUNAR_HEADDRESS = REGISTER.register("lunar_headdress", () -> PlatformItemHandler.INSTANCE.getLunarHeaddressItem(FnCArmorMaterial.LUNAR, EquipmentSlot.HEAD, createProperties()));
//    public static final RegistryObject<Item> SPEAR = REGISTER.register("spear", () -> new SpearItem(createProperties()));
    //dawn spear goes here
//    public static final RegistryObject<Item> DOWSING_ROD = REGISTER.register("dowsing_rod", () -> new DowsingRodItem(createProperties()));
    //dawn dowser goes here
    //sunset dowser goes here
    //midnight dowser goes here

//    // Blocks
//    public static final RegistryObject<Item> DAWN_ORE = REGISTER.register("dawn_ore", createBlockItem(FeaturesCreaturesBlocks.DAWN_ORE));
//    public static final RegistryObject<Item> STONE_DAWN_ORE = REGISTER.register("stone_dawn_ore", createBlockItem(FeaturesCreaturesBlocks.STONE_DAWN_ORE));
//    public static final RegistryObject<Item> DEEPSLATE_DAWN_ORE = REGISTER.register("deepslate_dawn_ore", createBlockItem(FeaturesCreaturesBlocks.DEEPSLATE_DAWN_ORE));
    public static final RegistryObject<Item> DAWN_BLOCK = REGISTER.register("dawn_block", createBlockItem(FnCBlocks.DAWN_BLOCK));
//    public static final RegistryObject<Item> SUNSET_ORE = REGISTER.register("sunset_ore", createBlockItem(FeaturesCreaturesBlocks.SUNSET_ORE));
//    public static final RegistryObject<Item> STONE_SUNSET_ORE = REGISTER.register("stone_sunset_ore", createBlockItem(FeaturesCreaturesBlocks.STONE_SUNSET_ORE));
//    public static final RegistryObject<Item> DEEPSLATE_SUNSET_ORE = REGISTER.register("deepslate_sunset_ore", createBlockItem(FeaturesCreaturesBlocks.DEEPSLATE_SUNSET_ORE));
    public static final RegistryObject<Item> SUNSET_BLOCK = REGISTER.register("sunset_block", createBlockItem(FnCBlocks.SUNSET_BLOCK));
//    public static final RegistryObject<Item> MIDNIGHT_ORE = REGISTER.register("midnight_ore", createBlockItem(FeaturesCreaturesBlocks.MIDNIGHT_ORE));
//    public static final RegistryObject<Item> STONE_MIDNIGHT_ORE = REGISTER.register("stone_midnight_ore", createBlockItem(FeaturesCreaturesBlocks.STONE_MIDNIGHT_ORE));
//    public static final RegistryObject<Item> DEEPSLATE_MIDNIGHT_ORE = REGISTER.register("deepslate_midnight_ore", createBlockItem(FeaturesCreaturesBlocks.DEEPSLATE_MIDNIGHT_ORE));
    public static final RegistryObject<Item> MIDNIGHT_BLOCK = REGISTER.register("midnight_block", createBlockItem(FnCBlocks.MIDNIGHT_BLOCK));

    // Misc.
    public static final RegistryObject<Item> DAWN_CRYSTAL = REGISTER.register("dawn_crystal", createItem());
    public static final RegistryObject<Item> SUNSET_CRYSTAL = REGISTER.register("sunset_crystal", createItem());
    public static final RegistryObject<Item> MIDNIGHT_CRYSTAL = REGISTER.register("midnight_crystal", createItem());
    //tinted potion goes here

    // Hidden
    public static final RegistryObject<Item> MEGA_POTION = REGISTER.register("mega_potion", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BFS_ATTACK_ITEM = REGISTER.register("bfs_attack_item", () -> new Item(new Item.Properties()));

    private static Supplier<Item> createItem() {
        return () -> new Item(new Item.Properties());
    }

    private static Supplier<Item> createBlockItem(RegistryObject<Block> object) {
        return () -> new BlockItem(object.get(), new Item.Properties());
    }

    public static void init() {
    }
}
