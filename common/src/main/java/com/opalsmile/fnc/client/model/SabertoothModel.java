package com.opalsmile.fnc.client.model;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.entity.Sabertooth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SabertoothModel extends DefaultedEntityGeoModel<Sabertooth> {

    private static final ResourceLocation SADDLED = FnCConstants.resourceLocation("textures/entity/sabertooth_saddled.png");

    public SabertoothModel(ResourceLocation assetSubpath){
        super(assetSubpath);
    }

    @Override
    public ResourceLocation getTextureResource(Sabertooth sabertooth){
        return sabertooth.getControllingPassenger() instanceof Jockey ? SADDLED : super.getTextureResource(sabertooth);
    }
}
