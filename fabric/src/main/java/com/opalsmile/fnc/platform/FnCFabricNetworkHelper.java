package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.services.FnCINetworkHelper;
import com.opalsmile.fnc.util.FnCAntlerHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FnCFabricNetworkHelper implements FnCINetworkHelper {

    public static final ResourceLocation ANTLER_PACKET = FnCConstants.resourceLocation("antler_headdress");
    public static final ResourceLocation JOCKEY_PACKET = FnCConstants.resourceLocation("jockey_status");

    @Override
    public void sendAntlerKeypress(boolean release){
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(release);
        ClientPlayNetworking.send(ANTLER_PACKET, buf);
    }

    @Override
    public void notifyPlayerOfJockey(ServerPlayer player, BlockPos jockeyPosition){
        ServerPlayNetworking.send(player, JOCKEY_PACKET, PacketByteBufs.create().writeBoolean(true).writeBlockPos(jockeyPosition));
    }

    @Override
    public void broadcastJockeySpawning(ServerLevel level, BlockPos position){
        for (ServerPlayer player : level.getPlayers(player -> true)) {
            ServerPlayNetworking.send(player, JOCKEY_PACKET, PacketByteBufs.create().writeBoolean(true).writeBlockPos(position));
        }
    }

    @Override
    public void notifyJockeyDeath(ServerLevel level) {
        for (ServerPlayer player : level.getPlayers(player -> true)) {
            ServerPlayNetworking.send(player, JOCKEY_PACKET, PacketByteBufs.create().writeBoolean(false));
        }
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ANTLER_PACKET, (server, player,
                handler, buf, responseSender) -> FnCAntlerHandler.handlePacket(player, buf.readBoolean()));
    }


}
