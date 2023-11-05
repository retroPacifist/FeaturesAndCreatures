package com.opalsmile.fnc;

import com.mojang.blaze3d.platform.InputConstants;
import com.opalsmile.fnc.client.FnCClient;
import com.opalsmile.fnc.client.renderer.*;
import com.opalsmile.fnc.entity.Sabertooth;
import com.opalsmile.fnc.registries.FnCEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

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

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (keybindPressed && !FnCClient.ANTLER_KEYBIND.isDown()) {
                keybindPressed = false;
                FnCClient.onKeyPress(InputConstants.RELEASE);
            }
            if (FnCClient.ANTLER_KEYBIND.isDown()) {
                FnCClient.ANTLER_KEYBIND.consumeClick();
                keybindPressed = true;
                FnCClient.onKeyPress(InputConstants.PRESS);
            }
        });
    }


}
