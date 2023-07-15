package com.opalsmile.fnc;

import com.opalsmile.fnc.common.entity.jackalope.Jackalope;
import com.opalsmile.fnc.common.entity.jackalope.JackalopeRenderer;
import com.opalsmile.fnc.core.FnCEntities;
import com.opalsmile.fnc.core.FnCRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.opalsmile.fnc.core.FnCEntities.JACKALOPE;

@Mod(Constants.MOD_ID)
public class FnCForge {

    public FnCForge() {
        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        FnCRegistry.loadClasses();
        FnC.init();
        modBus.addListener(this::createEntityAttributes);
        modBus.addListener(this::registerRenderers);
    }

    public void registerRenderers(EntityRenderersEvent.RegisterRenderers renderer) {
        renderer.registerEntityRenderer(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
    }

    public void createEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(JACKALOPE.get(), Jackalope.createAttributes().build());
    }
}