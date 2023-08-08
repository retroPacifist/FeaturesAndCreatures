package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Jackalope;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class JackalopeModel extends DefaultedEntityGeoModel<Jackalope> {

    public static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/jackalope.png");
    public static final ResourceLocation SADDLED = FnCConstants.resourceLocation(
            "textures/entity/jackalope_saddle.png");

    @Override
    public ResourceLocation getTextureResource(Jackalope object){
        return object.isSaddled() ? SADDLED : TEXTURE;
    }

    public JackalopeModel(ResourceLocation assetSubpath){
        super(assetSubpath, true);
    }

    @Override
    public void setCustomAnimations(Jackalope animatable, long instanceId, AnimationState<Jackalope> animationState){
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        if(animatable.isBaby()) {
            head.setScaleX(2);
            head.setScaleY(2);
            head.setScaleZ(2);
        }
    }

}