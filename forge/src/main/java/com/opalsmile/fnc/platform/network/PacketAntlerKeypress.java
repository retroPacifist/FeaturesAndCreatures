package com.opalsmile.fnc.platform.network;

import com.opalsmile.fnc.util.AntlerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketAntlerKeypress {

    private final boolean release;

    public PacketAntlerKeypress(boolean release) {
        this.release = release;
    }

    public static PacketAntlerKeypress decode(FriendlyByteBuf buf) {
        return new PacketAntlerKeypress(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.release);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.getSender() == null) return;
            AntlerHandler.handlePacket(context.getSender(), this.release);
        });
        context.setPacketHandled(true);
    }
}
