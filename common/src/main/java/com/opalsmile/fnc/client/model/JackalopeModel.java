package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.common.entity.Jackalope;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class JackalopeModel extends GeoModel<Jackalope> {

    public static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/jackalope.png");
    public static final ResourceLocation SADDLED = FnCConstants.resourceLocation(
            "textures/entity/jackalope_saddle.png");

    public static final ResourceLocation MODEL = FnCConstants.resourceLocation("geo/jackalope.geo.json");
    public static final ResourceLocation ANIMATION = FnCConstants.resourceLocation(
            "animations/jackalope.animation.json");

    @Override
    public ResourceLocation getModelResource(Jackalope object){
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Jackalope object){
        return object.isSaddled() ? SADDLED : TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Jackalope animatable){
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Jackalope animatable, long instanceId, AnimationState<Jackalope> animationState){
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        head.setRotX(animatable.xRotO * ((float) Math.PI / 180F));
        head.setRotY(animatable.yHeadRotO * ((float) Math.PI / 180F));
        if(animatable.isBaby()) {
            head.setScaleX(2);
            head.setScaleY(2);
            head.setScaleZ(2);
        }
    }

}