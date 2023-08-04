package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.platform.services.FnCIPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FnCFabricPlatformHelper implements FnCIPlatformHelper {

    @Override
    public String getPlatformName(){
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId){

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment(){

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
