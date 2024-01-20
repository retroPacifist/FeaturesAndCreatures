package com.opalsmile.fnc;

import com.opalsmile.fnc.client.FnCClient;
import com.opalsmile.fnc.client.renderer.*;
import com.opalsmile.fnc.platform.FnCFabricNetworkHelper;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.item.ItemProperties;

public class FnCFabricClient implements ClientModInitializer {

    public static boolean keybindPressed;

    @Override
    public void onInitializeClient(){
        EntityRendererRegistry.register(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
        EntityRendererRegistry.register(FnCEntities.JOCKEY.get(), JockeyRenderer::new);
        EntityRendererRegistry.register(FnCEntities.SPEAR.get(), SpearRenderer::new);
        EntityRendererRegistry.register(FnCEntities.BOAR.get(), BoarRenderer::new);
        EntityRendererRegistry.register(FnCEntities.SABERTOOTH.get(), SabertoothRenderer::new);

        KeyBindingHelper.registerKeyBinding(FnCClient.ANTLER_KEYBIND);

        FnCClient.registerItemProperties();

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            FnCClient.handleClientTick();
        });

        ClientPlayNetworking.registerGlobalReceiver(
                FnCFabricNetworkHelper.JOCKEY_PACKET, ((client, handler, buf, responseSender) -> {
            boolean sameLevel = buf.readBoolean();
            FnCClient.setSameJockeyLevel(sameLevel);
            if (sameLevel) FnCClient.setJockeyPosition(buf.readBlockPos());
        }));
    }



}
