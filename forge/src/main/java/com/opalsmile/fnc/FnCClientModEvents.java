package com.opalsmile.fnc;

import com.opalsmile.fnc.client.renderer.JackalopeRenderer;
import com.opalsmile.fnc.client.renderer.JockeyRenderer;
import com.opalsmile.fnc.core.FnCEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = FnCConstants.MOD_ID)
public class FnCClientModEvents {


    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers renderer) {
        renderer.registerEntityRenderer(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
        renderer.registerEntityRenderer(FnCEntities.JOCKEY.get(), JockeyRenderer::new);
    }
}
