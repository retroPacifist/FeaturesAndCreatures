package com.opalsmile.fnc;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FnCConstants {

    public static final String MOD_ID = "fnc";
    public static final String MOD_NAME = "Features and Creatures";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation resourceLocation(final String path){
        return new ResourceLocation(MOD_ID, path);
    }
}