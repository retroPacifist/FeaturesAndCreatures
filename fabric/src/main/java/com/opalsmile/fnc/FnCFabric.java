package com.opalsmile.fnc;

import com.opalsmile.fnc.entity.Boar;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.platform.FnCFabricConfigHelper;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCRegistry;
import com.opalsmile.fnc.registries.FnCTriggers;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;


public class FnCFabric implements ModInitializer {

    public static void registerEntityAttributes(){
        FabricDefaultAttributeRegistry.register(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.JOCKEY.get(), Jockey.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.BOAR.get(), Boar.createAttributes());
    }

    @Override
    public void onInitialize(){
        FnCRegistry.initialise();
        FnCFabric.registerEntityAttributes();
        FnCTriggers.register();
        MidnightConfig.init(FnCConstants.MOD_ID, FnCFabricConfigHelper.class);
    }
}
