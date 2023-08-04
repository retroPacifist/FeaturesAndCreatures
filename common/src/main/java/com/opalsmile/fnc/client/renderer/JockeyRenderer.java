package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.client.model.JockeyModel;
import com.opalsmile.fnc.common.entity.Jockey;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JockeyRenderer extends GeoEntityRenderer<Jockey> {
    public JockeyRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new JockeyModel(FnCConstants.resourceLocation("jockey")));
    }
}
