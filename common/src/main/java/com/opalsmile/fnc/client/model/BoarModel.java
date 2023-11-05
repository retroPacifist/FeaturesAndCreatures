package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Boar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BoarModel extends DefaultedEntityGeoModel<Boar> {
    public BoarModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    public static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/boar.png");
    public static final ResourceLocation SADDLED = FnCConstants.resourceLocation("textures/entity/boar_saddle.png");
    public static final ResourceLocation ANGRY_TEXTURE = FnCConstants.resourceLocation("textures/entity/boar_angry.png");
    public static final ResourceLocation ANGRY_SADDLED = FnCConstants.resourceLocation("textures/entity/boar_saddle_angry.png");

    @Override
    public ResourceLocation getTextureResource(Boar boar){
        boolean isSaddled = boar.isSaddled();
        if (boar.isAngry()) {
            return isSaddled ? ANGRY_SADDLED : ANGRY_TEXTURE;
        }
        return isSaddled ? SADDLED : TEXTURE;
    }
}
