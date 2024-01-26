package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Jackalope;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class JackalopeModel extends DefaultedEntityGeoModel<Jackalope> {

    public static final ResourceLocation TEXTURE = FnCConstants.resourceLocation("textures/entity/jackalope.png");
    public static final ResourceLocation SADDLED = FnCConstants.resourceLocation(
            "textures/entity/jackalope_saddle.png");

    @Override
    public ResourceLocation getTextureResource(Jackalope jackalope){
        return jackalope.isSaddled() ? SADDLED : TEXTURE;
    }

    public JackalopeModel(ResourceLocation assetSubpath, boolean turnsHead){
        super(assetSubpath, turnsHead);
    }
}