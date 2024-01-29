package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.network.PacketAntlerKeypress;
import com.opalsmile.fnc.platform.network.PacketJockeyInformation;
import com.opalsmile.fnc.platform.services.FnCINetworkHelper;
import com.opalsmile.fnc.util.JockeySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

public class FnCForgeNetworkHelper implements FnCINetworkHelper {

    private static final SimpleChannel INSTANCE = ChannelBuilder.named(FnCConstants.resourceLocation("packets"))
            .networkProtocolVersion(1)
            .clientAcceptedVersions((status, version) -> version == 1)
            .serverAcceptedVersions((status, version) -> version == 1)
            .simpleChannel();

    @Override
    public void sendAntlerKeypress(boolean release){
        INSTANCE.send(new PacketAntlerKeypress(release), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void notifyPlayerOfJockey(ServerPlayer player, BlockPos jockeyPosition){
        JockeySavedData savedData = JockeySavedData.get(player.server);
        INSTANCE.send(new PacketJockeyInformation(savedData.hasJockeySpawned() && savedData.getDimensionId().equals(player.level().dimension().location()),
                        jockeyPosition),
                PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void broadcastJockeySpawning(ServerLevel level, BlockPos position){
        PacketJockeyInformation packet = new PacketJockeyInformation(true, position);
        INSTANCE.send(packet, PacketDistributor.DIMENSION.with(level.dimension()));
    }

    @Override
    public void notifyJockeyDeath(ServerLevel level){
        PacketJockeyInformation packet = new PacketJockeyInformation(false, null);
        INSTANCE.send(packet, PacketDistributor.DIMENSION.with(level.dimension()));
    }

    public static void register() {
        int counter = 0;
        INSTANCE.messageBuilder(PacketAntlerKeypress.class, ++counter, NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketAntlerKeypress::decode)
                .encoder(PacketAntlerKeypress::encode)
                .consumerMainThread(PacketAntlerKeypress::handle)
                .add();
        INSTANCE.messageBuilder(PacketJockeyInformation.class, ++counter, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketJockeyInformation::decode)
                .encoder(PacketJockeyInformation::encode)
                .consumerMainThread(PacketJockeyInformation::handle)
                .add();
    }


}
