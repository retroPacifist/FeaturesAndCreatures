package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.network.PacketAntlerKeypress;
import com.opalsmile.fnc.platform.services.FnCINetworkHelper;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

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


    public static void register() {
        int counter = 0;
        INSTANCE.messageBuilder(PacketAntlerKeypress.class, ++counter)
                .decoder(PacketAntlerKeypress::decode)
                .encoder(PacketAntlerKeypress::encode)
                .consumerMainThread(PacketAntlerKeypress::handle)
                .add();
        ;
    }


}
