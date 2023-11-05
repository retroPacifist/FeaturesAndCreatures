package com.opalsmile.fnc.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FnCEventHandler {

    public static void onPlayerJoinLevel(ServerPlayer player, ServerLevel level) {
        FnCSavedData savedData = FnCSavedData.get(level.getServer());
        if (savedData.hasJockeySpawned()) {
            
        }
    }
}
