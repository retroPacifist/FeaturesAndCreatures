package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.client.model.JackalopeModel;
import com.opalsmile.fnc.entity.Jackalope;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JackalopeRenderer extends GeoEntityRenderer<Jackalope> {

    public JackalopeRenderer(EntityRendererProvider.Context dispatcher){
        super(dispatcher, new JackalopeModel(FnCConstants.resourceLocation("jackalope"), true));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, Jackalope animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay){
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick,
                packedLight, packedOverlay);
        if (!isReRender) {
            if (animatable.isBaby()) {
                model.getBone("head").ifPresent(bone -> bone.updateScale(2, 2, 2));
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            shadowRadius = 0.8f;
        }
    }

}
