package com.opalsmile.fnc.util;

import com.opalsmile.fnc.platform.FnCServices;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FnCEventHandler {

    public static void onPlayerJoinLevel(ServerPlayer player, ServerLevel level) {
        JockeySavedData savedData = JockeySavedData.get(level.getServer());
        if (savedData.hasJockeySpawned() && player.level().dimension().location().equals(savedData.getDimensionId())) {
            FnCServices.NETWORK.notifyPlayerOfJockey(player, savedData.getSpawnPosition());
        }
    }
}
