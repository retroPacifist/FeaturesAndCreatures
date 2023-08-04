package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.platform.services.FnCIPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class FnCForgePlatformHelper implements FnCIPlatformHelper {

    @Override
    public String getPlatformName(){

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId){

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment(){

        return !FMLLoader.isProduction();
    }
}