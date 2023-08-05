package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.opalsmile.fnc.client.model.SpearModel;
import com.opalsmile.fnc.entity.Spear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpearRenderer extends GeoEntityRenderer<Spear> {
    public SpearRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new SpearModel());
    }

    @Override
    public RenderType getRenderType(Spear animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick){
        return RenderType.entityCutout(texture);
    }

    @Override
    public void preRender(PoseStack poseStack, Spear animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, red, green, blue, alpha);
        //Apply rotation here
        //Old code doesn't work here probably because BlockBench no longer inverts models on export
        //poseStack.mulPose(Axis...)
    }
}
