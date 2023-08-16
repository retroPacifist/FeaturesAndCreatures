package com.opalsmile.fnc;

import com.opalsmile.fnc.client.renderer.BoarRenderer;
import com.opalsmile.fnc.client.renderer.JackalopeRenderer;
import com.opalsmile.fnc.client.renderer.JockeyRenderer;
import com.opalsmile.fnc.client.renderer.SpearRenderer;
import com.opalsmile.fnc.registries.FnCEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class FnCFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(){
        EntityRendererRegistry.register(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
        EntityRendererRegistry.register(FnCEntities.JOCKEY.get(), JockeyRenderer::new);
        EntityRendererRegistry.register(FnCEntities.SPEAR.get(), SpearRenderer::new);
        EntityRendererRegistry.register(FnCEntities.BOAR.get(), BoarRenderer::new);
    }
}
