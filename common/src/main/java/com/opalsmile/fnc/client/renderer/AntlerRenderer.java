package com.opalsmile.fnc.client.renderer;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.item.AntlerHeaddress;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AntlerRenderer extends GeoArmorRenderer<AntlerHeaddress> {
    public AntlerRenderer(){
        super(new DefaultedItemGeoModel<>(FnCConstants.resourceLocation("armor/antler_headdress")));
    }
}
