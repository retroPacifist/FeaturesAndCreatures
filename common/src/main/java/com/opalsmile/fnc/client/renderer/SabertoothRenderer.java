package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.client.model.SabertoothModel;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Sabertooth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SabertoothRenderer extends GeoEntityRenderer<Sabertooth> {

    public SabertoothRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new SabertoothModel(FnCConstants.resourceLocation("sabertooth"), true));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, Sabertooth animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay){
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick,
                packedLight, packedOverlay);
        if (!isReRender) {
            if (animatable.isBaby()) {
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            shadowRadius = 0.8f;
        }
    }
}
