package com.opalsmile.fnc.platform.services;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface FnCINetworkHelper {

    void sendAntlerKeypress(boolean release);

    void notifyPlayerOfJockey(ServerPlayer player, BlockPos jockeyPosition);

    void broadcastJockeySpawning(ServerLevel level, BlockPos position);

    void notifyJockeyDeath(ServerLevel level);
}
