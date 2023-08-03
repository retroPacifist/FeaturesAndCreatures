package com.opalsmile.fnc;

import com.opalsmile.fnc.common.entity.Jackalope;
import com.opalsmile.fnc.common.entity.Jockey;
import com.opalsmile.fnc.core.FnCEntities;
import com.opalsmile.fnc.core.FnCRegistry;
import com.opalsmile.fnc.core.FnCTriggers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;


public class FnCFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FnCRegistry.initialise();
        FnCFabric.registerEntityAttributes();
        FnCTriggers.register();
    }

    public static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(FnCEntities.JACKALOPE.get(), Jackalope.createAttributes());
        FabricDefaultAttributeRegistry.register(FnCEntities.JOCKEY.get(), Jockey.createAttributes());
    }
}
