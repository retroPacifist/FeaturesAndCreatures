package com.opalsmile.fnc.common.entity.jackalope;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.Constants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JackalopeRenderer extends GeoEntityRenderer<Jackalope> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/jackalope.png");
    public static final ResourceLocation SADDLED = new ResourceLocation(Constants.MOD_ID, "textures/entity/jackalope_saddle.png");


    public JackalopeRenderer(EntityRendererProvider.Context dispatcher) {
        super(dispatcher, new JackalopeModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Jackalope instance) {
        return instance.isSaddled() ? SADDLED : TEXTURE;
    }

    @Override
    public void render(Jackalope entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
            shadowRadius = 0.5f;
        } else {
            shadowRadius = 0.8f;
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public boolean shouldRender(Jackalope jackalope, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
