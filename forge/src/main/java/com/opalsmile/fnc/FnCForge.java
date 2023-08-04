package com.opalsmile.fnc;

import com.opalsmile.fnc.common.entity.Jackalope;
import com.opalsmile.fnc.common.entity.Jockey;
import com.opalsmile.fnc.core.FnCEntities;
import com.opalsmile.fnc.core.FnCRegistry;
import com.opalsmile.fnc.core.FnCTriggers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FnCConstants.MOD_ID)
public class FnCForge {

    public FnCForge(){
        FnCRegistry.initialise();
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::createEntityAttributes);
        modBus.addListener(this::setupCommon);
    }

    public void createEntityAttributes(final EntityAttributeCreationEvent event){
        event.put(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes().build());
        event.put(FnCEntities.JOCKEY.get(), Jockey.createAttributes().build());
    }

    public void setupCommon(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            //Anything accessing a non threadsafe map needs to be enqueued on the main thread.
            FnCTriggers.register();
        });
    }
}