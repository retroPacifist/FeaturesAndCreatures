package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.client.model.SabertoothModel;
import com.opalsmile.fnc.entity.Sabertooth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SabertoothRenderer extends GeoEntityRenderer<Sabertooth> {

    public SabertoothRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new SabertoothModel(FnCConstants.resourceLocation("sabertooth")));
    }
}
