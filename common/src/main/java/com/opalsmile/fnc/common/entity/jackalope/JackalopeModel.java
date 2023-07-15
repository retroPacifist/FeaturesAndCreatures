package com.opalsmile.fnc.common.entity.jackalope;

import com.opalsmile.fnc.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class JackalopeModel extends GeoModel<Jackalope> {

    @Override
    public ResourceLocation getModelResource(Jackalope object) {
        return new ResourceLocation(Constants.MOD_ID, "geo/jackalope.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Jackalope object) {
        return object.isSaddled() ? JackalopeRenderer.SADDLED : JackalopeRenderer.TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Jackalope animatable) {
        return new ResourceLocation(Constants.MOD_ID, "animations/jackalope.animation.json");
    }

    @Override
    public void setCustomAnimations(Jackalope animatable, long instanceId, AnimationState<Jackalope> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        head.setRotX(animatable.xRotO * ((float) Math.PI / 180F));
        head.setRotY(animatable.yHeadRotO * ((float) Math.PI / 180F));
        if (animatable.isBaby()) {
            head.setScaleX(2);
            head.setScaleY(2);
            head.setScaleZ(2);
        }
    }

}