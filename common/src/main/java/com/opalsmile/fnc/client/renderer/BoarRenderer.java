package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.client.model.BoarModel;
import com.opalsmile.fnc.entity.Boar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoarRenderer extends GeoEntityRenderer<Boar> {

    //TODO RenderLayer for saddle as well as angry when Jackalope and boar share a parent class

    public BoarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoarModel(FnCConstants.resourceLocation("boar")));
    }
}
