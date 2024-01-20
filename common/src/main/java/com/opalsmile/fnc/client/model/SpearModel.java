package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Spear;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpearModel extends GeoModel<Spear> {

    //GeoModel is used here instead of Defaulted because it's using non-standard locations.
    private static final ResourceLocation MODEL = FnCConstants.resourceLocation("geo/entity/spear.geo.json");
    private static final ResourceLocation ANIMATION = FnCConstants.resourceLocation("animations/empty.animation.json");

    private static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/spear.png");

    @Override
    public ResourceLocation getModelResource(Spear animatable){
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Spear animatable){
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Spear animatable){
        return ANIMATION;
    }
}
