package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.opalsmile.fnc.client.model.SpearModel;
import com.opalsmile.fnc.entity.Spear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) - 90.0F));
        poseStack.translate(0.0f, -1.8f, 0.0f);
    }
}
