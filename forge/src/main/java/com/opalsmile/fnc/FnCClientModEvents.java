package com.opalsmile.fnc;

import com.opalsmile.fnc.client.FnCClient;
import com.opalsmile.fnc.client.renderer.*;
import com.opalsmile.fnc.registries.FnCEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = FnCConstants.MOD_ID)
public class FnCClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
        event.registerEntityRenderer(FnCEntities.JOCKEY.get(), JockeyRenderer::new);
        event.registerEntityRenderer(FnCEntities.SPEAR.get(), SpearRenderer::new);
        event.registerEntityRenderer(FnCEntities.BOAR.get(), BoarRenderer::new);
        event.registerEntityRenderer(FnCEntities.SABERTOOTH.get(), SabertoothRenderer::new);
    }

    @SubscribeEvent
    public static void setupClient(final RegisterKeyMappingsEvent event) {
        event.register(FnCClient.ANTLER_KEYBIND);
    }
}
