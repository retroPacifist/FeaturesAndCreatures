package com.opalsmile.fnc;

import com.opalsmile.fnc.entity.Boar;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.platform.FnCForgeConfigHelper;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCRegistry;
import com.opalsmile.fnc.registries.FnCTriggers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FnCConstants.MOD_ID)
public class FnCForge {

    public FnCForge(){
        FnCRegistry.initialise();
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::createEntityAttributes);
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::configLoad);
        modBus.addListener(this::configReload);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FnCForgeConfigHelper.GENERAL_SPEC);
    }

    public void createEntityAttributes(final EntityAttributeCreationEvent event){
        event.put(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes().build());
        event.put(FnCEntities.JOCKEY.get(), Jockey.createAttributes().build());
        event.put(FnCEntities.BOAR.get(), Boar.createAttributes().build());
    }

    public void setupCommon(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            //Anything accessing a non threadsafe map needs to be enqueued on the main thread.
            FnCTriggers.register();
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
}