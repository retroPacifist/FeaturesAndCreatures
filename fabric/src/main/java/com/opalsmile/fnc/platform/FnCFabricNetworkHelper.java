package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.services.FnCINetworkHelper;
import com.opalsmile.fnc.util.FnCAntlerHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FnCFabricNetworkHelper implements FnCINetworkHelper {

    public static final ResourceLocation ANTLER_PACKET = FnCConstants.resourceLocation("antler_headdress");


    @Override
    public void sendAntlerKeypress(boolean release){
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(release);
        ClientPlayNetworking.send(ANTLER_PACKET, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ANTLER_PACKET, (server, player,
                handler, buf, responseSender) -> FnCAntlerHandler.handlePacket(player, buf.readBoolean()));
    }

}
