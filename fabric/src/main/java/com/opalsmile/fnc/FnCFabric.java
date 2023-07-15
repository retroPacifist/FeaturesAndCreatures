package com.opalsmile.fnc;

import com.opalsmile.fnc.core.FnCRegistry;
import net.fabricmc.api.ModInitializer;

public class FnCFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        FnCRegistry.loadClasses();
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        FnC.init();
    }
}
