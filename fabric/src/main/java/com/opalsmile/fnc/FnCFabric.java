package com.opalsmile.fnc;

import com.opalsmile.fnc.common.entity.jackalope.Jackalope;
import com.opalsmile.fnc.core.FnCRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import static com.opalsmile.fnc.core.FnCEntities.JACKALOPE;

public class FnCFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FnCRegistry.loadClasses();
        registerEntityAttributes();
        Constants.LOG.info("Hello Fabric world!");
        FnC.init();
    }

    public static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(JACKALOPE.get(), Jackalope.createAttributes());
    }
}
