package com.opalsmile.fnc;

import com.opalsmile.fnc.client.FnCClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = FnCConstants.MOD_ID)
public class FnCClientForgeEvents {

    @SubscribeEvent
    public static void keypress(final InputEvent.Key event) {
        if (event.getKey() == FnCClient.ANTLER_KEYBIND.getKey().getValue()) {
            FnCClient.onKeyPress(event.getAction());
        }
    }
}
