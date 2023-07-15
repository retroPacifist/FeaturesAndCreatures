package com.opalsmile.fnc.client;

import com.opalsmile.fnc.common.entity.jackalope.JackalopeRenderer;
import com.opalsmile.fnc.core.FnCEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class FnCEntityRenderers {
    public static <T extends Entity> void register(RegisterStrategy registerStrategy) {
        registerStrategy.register(FnCEntities.JACKALOPE.get(), JackalopeRenderer::new);
    }

    @FunctionalInterface
    public interface RegisterStrategy {
        <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
    }
}
