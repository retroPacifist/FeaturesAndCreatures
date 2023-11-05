package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Jockey;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JockeyRenderer extends GeoEntityRenderer<Jockey> {
    public JockeyRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new DefaultedEntityGeoModel<>(FnCConstants.resourceLocation("jockey")));
    }
}
