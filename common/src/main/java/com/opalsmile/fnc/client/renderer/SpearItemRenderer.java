package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.item.SpearItem;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpearItemRenderer extends GeoItemRenderer<SpearItem> {
    public SpearItemRenderer(){
        super(new DefaultedGeoModel<>(FnCConstants.resourceLocation("spear")) {
            @Override
            protected String subtype() {
                return "entity";
            }
        });
    }
}
