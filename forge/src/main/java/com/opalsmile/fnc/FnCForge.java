package com.opalsmile.fnc;

import com.opalsmile.fnc.client.FnCClient;
import com.opalsmile.fnc.entity.Boar;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.entity.Sabertooth;
import com.opalsmile.fnc.entity.goals.SabertoothFearEventListener;
import com.opalsmile.fnc.item.*;
import com.opalsmile.fnc.platform.FnCForgeConfigHelper;
import com.opalsmile.fnc.platform.FnCForgeNetworkHelper;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCRegistry;
import com.opalsmile.fnc.registries.FnCTriggers;
import com.opalsmile.fnc.util.FnCEventHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod(FnCConstants.MOD_ID)
public class FnCForge {

    private static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, FnCConstants.MOD_ID);
    public static final RegistryObject<? extends AntlerHeaddress> ANTLER_HEADDRESS = ITEM_REGISTRY.register("antler_headdress", () ->
            new ForgeAntlerHeaddress(new AntlerMaterial("antler"), ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<? extends SpearItem> SPEAR = ITEM_REGISTRY.register("spear", () ->
            new ForgeSpearItem(new Item.Properties().stacksTo(1).durability(200)));


    public static final RegistryObject<SpawnEggItem> JOCKEY_SPAWN_EGG =
            createSpawnEgg("jockey", FnCEntities.JOCKEY::get, 0xDBA5FF, 0x493835);

    public static final RegistryObject<SpawnEggItem> BOAR_SPAWN_EGG =
            createSpawnEgg("boar", FnCEntities.BOAR::get, 0x87784A, 0x44392E);

    public static final RegistryObject<SpawnEggItem> SABERTOOTH_SPAWN_EGG =
            createSpawnEgg("sabertooth", FnCEntities.SABERTOOTH::get, 0xC59125, 0x7E5C2D);

    public static final RegistryObject<SpawnEggItem> JACKALOPE_SPAWN_EGG =
            createSpawnEgg("jackalope", FnCEntities.JACKALOPE::get, 0xB3A98D, 0x9D9684);

    public FnCForge(){
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEM_REGISTRY.register(modBus);
        FnCRegistry.initialise();

        FnCForgeNetworkHelper.register();

        modBus.addListener(this::createEntityAttributes);
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::configLoad);
        modBus.addListener(this::configReload);
        modBus.addListener(this::setupClient);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FnCForgeConfigHelper.GENERAL_SPEC);
        MinecraftForge.EVENT_BUS.addListener(this::entityJoinLevel);

    }

    public void createEntityAttributes(final EntityAttributeCreationEvent event){
        event.put(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes().build());
        event.put(FnCEntities.JOCKEY.get(), Jockey.createAttributes().build());
        event.put(FnCEntities.BOAR.get(), Boar.createAttributes().build());
        event.put(FnCEntities.SABERTOOTH.get(), Sabertooth.createAttributes().build());
    }

    private static RegistryObject<SpawnEggItem> createSpawnEgg(String name, Supplier<EntityType<? extends Mob>> type, int backgroundColour, int highlightColour) {
        return ITEM_REGISTRY.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(type, backgroundColour, highlightColour, new Item.Properties()));
    }


    public void setupCommon(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            //Anything accessing a non threadsafe map needs to be enqueued on the main thread.
            FnCTriggers.register();
        });
    }

    public void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            FnCClient.registerItemProperties();
        });
    }

    public void configLoad(final ModConfigEvent.Loading event) {
        handleConfigUpdate(event.getConfig());
    }

    public void configReload(final ModConfigEvent.Reloading event) {
        handleConfigUpdate(event.getConfig());
    }

    public void handleConfigUpdate(final ModConfig config) {
        if (FnCConstants.MOD_ID.equals(config.getModId())){
            FnCForgeConfigHelper.update();
        }
    }

    public void entityJoinLevel(final EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;
        if (event.getEntity() instanceof ServerPlayer player) {
            FnCEventHandler.onPlayerJoinLevel(player, player.serverLevel());
        }
        else if(event.getEntity() instanceof PathfinderMob mob) {
            SabertoothFearEventListener.onEntitySpawn(mob);
        }
    }

}