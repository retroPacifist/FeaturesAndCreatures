package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.common.entity.Jockey;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class JockeyModel extends GeoModel<Jockey> {

    public static final ResourceLocation MODEL = FnCConstants.resourceLocation("geo/jockey.geo.json");
    public static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/jockey.png");
    public static final ResourceLocation ANIMATION = FnCConstants.resourceLocation("animations/jockey.animation.json");

    @Override
    public ResourceLocation getModelResource(Jockey animatable){
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Jockey animatable){
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Jockey animatable){
        return ANIMATION;
    }
}
