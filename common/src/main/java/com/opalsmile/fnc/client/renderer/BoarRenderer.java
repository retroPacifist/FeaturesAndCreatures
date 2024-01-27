package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.client.model.BoarModel;
import com.opalsmile.fnc.entity.Boar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoarRenderer extends GeoEntityRenderer<Boar> {

    public BoarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoarModel(FnCConstants.resourceLocation("boar"), true));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, Boar animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay){
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick,
                packedLight, packedOverlay);
        if (!isReRender) {
            if (animatable.isBaby()) {
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            shadowRadius = 0.5f;
        }
    }
}
