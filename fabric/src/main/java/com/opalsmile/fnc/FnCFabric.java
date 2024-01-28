package com.opalsmile.fnc;

import com.opalsmile.fnc.entity.Boar;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.entity.Sabertooth;
import com.opalsmile.fnc.item.*;
import com.opalsmile.fnc.platform.FnCFabricConfigHelper;
import com.opalsmile.fnc.platform.FnCFabricNetworkHelper;
import com.opalsmile.fnc.platform.FnCServices;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCRegistry;
import com.opalsmile.fnc.registries.FnCTriggers;
import com.opalsmile.fnc.util.FnCSavedData;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;


public class FnCFabric implements ModInitializer {

    public static final AntlerHeaddress ANTLER_HEADDRESS
            = Registry.register(BuiltInRegistries.ITEM, FnCConstants.resourceLocation("antler_headdress"),
            new FabricAntlerHeadress(new AntlerMaterial("antler"), ArmorItem.Type.HELMET, new Item.Properties()));

    public static final SpearItem SPEAR = Registry.register(BuiltInRegistries.ITEM, FnCConstants.resourceLocation("spear"),
            new FabricSpearItem(new Item.Properties().stacksTo(1).durability(200)));

    public static void registerEntityAttributes(){
        FabricDefaultAttributeRegistry.register(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.JOCKEY.get(), Jockey.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.BOAR.get(), Boar.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.SABERTOOTH.get(), Sabertooth.createAttributes());
    }

    @Override
    public void onInitialize(){
        FnCRegistry.initialise();
        FnCFabric.registerEntityAttributes();

        FnCTriggers.register();
        MidnightConfig.init(FnCConstants.MOD_ID, FnCFabricConfigHelper.class);

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ServerPlayer player)) return;
            FnCSavedData savedData = FnCSavedData.get(world.getServer());
            if (savedData.hasJockeySpawned() && savedData.getDimensionId().equals(world.dimension().location())) {
                FnCServices.NETWORK.notifyPlayerOfJockey(player, savedData.getSpawnPosition());
            }
        });

        FnCFabricNetworkHelper.register();
    }

    public static boolean handleEnchantability(boolean acceptable, ItemStack stack, Enchantment enchantment) {
        if (stack.is(FnCFabric.SPEAR)) {
            if (enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.FIRE_ASPECT) {
                return true;
            }
        }
        return acceptable;
    }
}
